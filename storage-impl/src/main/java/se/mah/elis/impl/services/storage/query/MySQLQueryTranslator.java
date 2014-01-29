package se.mah.elis.impl.services.storage.query;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import se.mah.elis.impl.services.storage.exceptions.YouAreBreakingTheInternetException;
import se.mah.elis.impl.services.storage.exceptions.YouAreDoingItWrongException;
import se.mah.elis.services.storage.exceptions.StorageException;
import se.mah.elis.services.storage.query.ChainingPredicate;
import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.QueryTranslator;
import se.mah.elis.services.users.UserIdentifier;

/**
 * The MySQLQueryTranslator translates Query objects to runnable MySQL queries.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 2.0
 */
public class MySQLQueryTranslator implements QueryTranslator {

	private Class what;
	private Predicate where;
	private int start, limit;
	private boolean oldestFirst;
	
	/*
	 * This is part of a MySQL string escaping mechanism. It was graciousy
	 * borrowed from
	 * http://stackoverflow.com/questions/881194/how-to-escape-special-character-in-mysql.
	 */
	private static final HashMap<String,String> sqlTokens;
	private static Pattern sqlTokenPattern;

	static {     
		String[][] mysqlMagicChars = new String[][] {
		  // In string   In regex   In SQL
			{"\u0000",   "\\x00",   "\\\\0"},
			{"'",        "'",       "\\\\'"},
			{"\"",       "\"",      "\\\\\""},
			{"\b",       "\\x08",   "\\\\b"},
			{"\n",       "\n",      "\\\\n"},
			{"\r",       "\\r",     "\\\\r"},
			{"\t",       "\\t",     "\\\\t"},
			{"\u001A",   "\\x1A",    "\\\\Z"},
			{"\\",       "\\\\",    "\\\\\\\\"}
		};
		
		sqlTokens = new HashMap<String,String>();
	    String patternStr = "";
	    for (String[] srr : mysqlMagicChars)
	    {
	        sqlTokens.put(srr[0], srr[2]);
	        patternStr += (patternStr.isEmpty() ? "" : "|") + srr[1];            
	    }
	    sqlTokenPattern = Pattern.compile('(' + patternStr + ')');
	}
	
	/**
	 * Creates an instance of this class.
	 * 
	 * @since 2.0
	 */
	public MySQLQueryTranslator() {
		what = null;
		where = null;
		start = limit = -1;
		oldestFirst = true;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#what(Class) what(Class)}.
	 * 
	 * @param c The class of the data to look for.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	@Override
	public QueryTranslator what(Class dataType) {
		what = dataType;
		
		return this;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#where(Predicate) where(Predicate)}.
	 * 
	 * @param start The number of objects in the full list that will be
	 * 		ignored in the result from the storage engine.
	 * @param size The size of the returned list.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	@Override
	public QueryTranslator where(Predicate predicate) {
		where = predicate;
		where.setTranslator(this);
		
		return this;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#limit(int, int) where(int, int)}.
	 * 
	 * @param p The predicate to match against.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	@Override
	public QueryTranslator limit(int start, int limit) {
		this.start = start;
		this.limit = limit;

		return this;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#order(boolean) order(boolean)}.
	 * 
	 * @param oldestFirst True if the oldest data objects should be returned
	 * 		first (i.e. list[0] is the oldest object), otherwise false.
	 * @return A reference back to the query translator object.
	 * @since 2.0
	 */
	@Override
	public QueryTranslator order(boolean oldestFirst) {
		this.oldestFirst = oldestFirst;

		return this;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#or(Predicate, Predicate) or(Predicate, Predicate)}.
	 * 
	 * @param left The left-hand predicate.
	 * @param right The right-hand predicate.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String or(Predicate left, Predicate right) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		try {
			return encapsulate(left) + " OR " + encapsulate(right);
		} catch (StorageException e) {
			throw new YouAreDoingItWrongException();
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#and(Predicate, Predicate) and(Predicate, Predicate)}.
	 * 
	 * @param left The left-hand predicate.
	 * @param right The right-hand predicate.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String and(Predicate left, Predicate right) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		try {
			return encapsulate(left) + " AND " + encapsulate(right);
		} catch (StorageException e) {
			throw new YouAreDoingItWrongException();
		}
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#eq(String, Object) eq(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String eq(String field, Object criterion) {
		return quoteField(field) + " = " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#neq(String, Object) neq(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String neq(String field, Object criterion) {
		return quoteField(field) + " <> " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#like(String, Object) like(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String like(String field, Object criterion) {
		if (!(criterion instanceof Number || criterion instanceof String)) {
			throw new YouAreDoingItWrongException();
		}
		
		return quoteField(field) + " LIKE '%" + criterion + "%'";
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#lt(String, Object) lt(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String lt(String field, Object criterion) {
		if (!(criterion instanceof Number || criterion instanceof DateTime)) {
			throw new YouAreDoingItWrongException();
		}
		
		return quoteField(field) + " < " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#lte(String, Object) lte(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String lte(String field, Object criterion) {
		if (!(criterion instanceof Number || criterion instanceof DateTime)) {
			throw new YouAreDoingItWrongException();
		}
		
		return quoteField(field) + " <= " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#gt(String, Object) gt(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String gt(String field, Object criterion) {
		if (!(criterion instanceof Number || criterion instanceof DateTime)) {
			throw new YouAreDoingItWrongException();
		}
		
		return quoteField(field) + " > " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#gte(String, Object) gte(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String gte(String field, Object criterion) {
		if (!(criterion instanceof Number || criterion instanceof DateTime)) {
			throw new YouAreDoingItWrongException();
		}
		
		return quoteField(field) + " >= " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#user(UserIdentifier) user(UserIdentifier)}.
	 * 
	 * @param user The user to match.
	 * @return The string representing the predicate.
	 * @since 2.0
	 */
	@Override
	public String user(UserIdentifier user) {
		String compiled = "";
		Properties props = user.getProperties();
		int i = 0;
		
		for (Entry<Object, Object> e : props.entrySet()) {
			if (!isEmpty(e.getValue())) {
				if (++i > 1) {
					compiled += " AND ";
				}
				compiled += quoteField((String) e.getKey()) + " = " +
					process(e.getValue());
			}
		}
		if (i > 1) {
			compiled = "(" + compiled + ")";
		}
		
		return compiled;
	}

	/**
	 * Checks whether an object is empty or not. An object is considered empty
	 * if the object is null or if the object is an empty string.
	 * 
	 * @param o The object to evaluate.
	 * @return True if the object is considered empty, otherwise false;
	 * @since 2.0
	 */
	private boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		} else if (o instanceof String) {
			return ((String) o).length() < 1;
		}
		
		return false;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#compile() compile()}.
	 * 
	 * @return A string representation of the query, suitable for MySQL
	 * 		execution.
	 * @since 2.0
	 */
	@Override
	public String compile() {
		String compiled = "SELECT * FROM ";
		
		if (what != null) {
			compiled += quoteField(mysqlifyName(what.getName()));
		}
		
		if (where != null) {
			try {
				compiled += " WHERE " + where.compile();
			} catch (StorageException e) {
			}
		}
		
		if (start > -1 && limit > -1) {
			compiled += " LIMIT " + start + ", " + limit;
		}
		
		if (oldestFirst) {
			compiled += " ASC;";
		} else {
			compiled += " DESC;";
		}
		
		return compiled;
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#eq(String, Object) eq(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @throws StorageException 
	 * @since 2.0
	 */
	public String compileDeleteQuery() throws StorageException {
		if (what == null || where == null) {
			throw new YouAreBreakingTheInternetException();
		}
		
		String compiled = "DELETE FROM " + quoteField(mysqlifyName(what.getName())) +
						  " WHERE " + where.compile() + ";";
		
		return compiled;
	}

	/**
	 * Processes a criterion, making it suitable for SQL queries. In short, it
	 * analyzed said criterion, escapes it and quotes it if it's a string,
	 * returns the string value of any numeral and converts a boolean into a 1
	 * or a 0.
	 * 
	 * @param criterion The criterion to analyze.
	 * @return A String The MySQLified criterion.
	 * @since 2.0
	 */
	private String process(Object criterion) {
		String processed = null;
		
		if (criterion instanceof Number) {
			processed = criterion.toString();
		} else if (criterion instanceof Boolean) {
			processed = ((Boolean) criterion).toString();
		} else if (criterion instanceof DateTime) {
			processed = "" + ((DateTime) criterion).getMillis();
		} else if (criterion == null) {
			processed = "NULL";
		} else {
			processed = "'" + escape(criterion.toString()) + "'";
		}
		
		return processed;
	}
	
	/**
	 * Escapes a string prior to insertion in a MySQL query.
	 * 
	 * @param criterion The criterion string to escape.
	 * @return An escaped string.
	 * @since 2.0
	 */
	private String escape(String criterion) {
		Matcher matcher = sqlTokenPattern.matcher(criterion);
		StringBuffer sb = new StringBuffer();
		
		while (matcher.find()) {
			matcher.appendReplacement(sb, null);
		}
		matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * Quotes a field name prior to MySQL usage.
	 * 
	 * @param field The field name to quote.
	 * @return A quoted string.
	 * @since 2.0
	 */
	private String quoteField(String field) {
		return "`" + field + "`";
	}

	/**
	 * Make a class name possible to use as a table name. In practice, this
	 * means that we're replacing all periods with hyphens.
	 * 
	 * @param name The class name to convert to a table name.
	 * @return A decent table name.
	 * @since 2.0
	 */
	private String mysqlifyName(String name) {
		return name.replace('.', '-');
	}

	/**
	 * Make a class name possible to use as a table name. In practice, this
	 * means that we're replacing all periods with hyphens.
	 * 
	 * @param name The table name to convert to a class name.
	 * @return A canonical class name.
	 * @since 2.0
	 */
	private String demysqlifyName(String name) {
		return name.replace('-', '.');
	}
	
	/**
	 * Checks if a predicate is a chaining predicate. If it is, the compiled
	 * predicate gets encapsulated in a pair of parentheses.
	 * 
	 * @param predicate The predicate to potentially encapsulate.
	 * @return A string. 
	 * @throws StorageException If the predicates couldn't be compiled.
	 * @since 2.0
	 */
	private String encapsulate(Predicate predicate) throws StorageException {
		String encapsulated = predicate.compile();
		
		if (predicate instanceof ChainingPredicate) {
			encapsulated = "(" + encapsulated + ")";
		}
		
		return encapsulated;
	}
}
