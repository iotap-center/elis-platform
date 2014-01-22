package se.mah.elis.impl.services.storage.query;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.mah.elis.services.storage.query.Predicate;
import se.mah.elis.services.storage.query.QueryTranslator;

/**
 * The MySQLQueryTranslator translates Query objects to runnable MySQL queries.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
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
			{"\u001A",   "\\xA",    "\\\\Z"},
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
	 * @since 1.1
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
	 * @since 1.1
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
	 * @since 1.1
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
	 * @since 1.1
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
	 * @since 1.1
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
	 * @param right The right-hand predicate.
	 * @param left The left-hand predicate.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String or(Predicate right, Predicate left) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		return left.compile() + " OR " + right.compile();
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#and(Predicate, Predicate) and(Predicate, Predicate)}.
	 * 
	 * @param right The right-hand predicate.
	 * @param left The left-hand predicate.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String and(Predicate right, Predicate left) {
		right.setTranslator(this);
		left.setTranslator(this);
		
		return "(" + left.compile() + " AND " + right.compile() + ")";
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#eq(String, Object) eq(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String eq(String field, Object criterion) {
		return field + " = " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#neq(String, Object) neq(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String neq(String field, Object criterion) {
		return field + " <> " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#like(String, Object) like(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String like(String field, Object criterion) {
		return field + " LIKE \"%" + criterion + "%\"";
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#lt(String, Object) lt(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String lt(String field, Object criterion) {
		return field + " < " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#lte(String, Object) lte(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String lte(String field, Object criterion) {
		return field + " <= " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#gt(String, Object) gt(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String gt(String field, Object criterion) {
		return field + " > " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#gte(String, Object) gte(String, Object)}.
	 * 
	 * @param field The field to match.
	 * @param Object The criterion to match against.
	 * @return The string representing the predicate.
	 * @since 1.1
	 */
	@Override
	public String gte(String field, Object criterion) {
		return field + " >= " + process(criterion);
	}

	/**
	 * Implementation of
	 * {@link se.mah.elis.services.storage.query.QueryTranslator#compile() compile()}.
	 * 
	 * @return A string representation of the query, suitable for MySQL
	 * 		execution.
	 * @since 1.1
	 */
	@Override
	public String compile() {
		String compiled = "SELECT * FROM " + what.getSimpleName();
		
		if (where != null) {
			compiled += " WHERE " + where.compile();
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
	 * @since 1.1
	 */
	public String compileDeleteQuery() {
		String compiled = "DELETE FROM `" + what.getSimpleName();
		
		if (where != null) {
			compiled += "` WHERE " + where.compile();
		}
		
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
	 * @since 1.1
	 */
	private String process(Object criterion) {
		String processed = null;
		
		if (criterion instanceof Number) {
			processed = criterion.toString();
		} else if (criterion instanceof Boolean) {
			if ((Boolean) criterion) {
				processed = "1";
			} else {
				processed = "0";
			}
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
	 * @since 1.1
	 */
	private String escape(String criterion) {
		Matcher matcher = null;
		StringBuffer sb = new StringBuffer();
		
		while (matcher.find()) {
			matcher.appendReplacement(sb, null);
		}
		matcher.appendTail(sb);
		
		return sb.toString();
	}
}
