package se.mah.elis.impl.services.storage.result;

import java.util.ArrayList;
import java.util.Arrays;

import se.mah.elis.services.storage.result.ResultSet;

public class ResultSetImpl implements ResultSet {

	private int pointer;
	private Class objectType;
	private Object[] rows;
	
	public ResultSetImpl(Class objectType, Object[] results) {
		pointer = -1;
		this.objectType = objectType;
		if (results != null) {
			rows = results;
		} else {
			rows = new Object[0];
		}
	}

	@Override
	public int size() {
		return rows.length;
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return rows[index];
	}

	@Override
	public Object[] getArray() {
		return rows;
	}

	@Override
	public void reset() {
		pointer = -1;
	}

	@Override
	public boolean hasNext() {
		return (pointer + 2) < rows.length;
	}

	@Override
	public Object next() {
		return rows[++pointer];
	}

	@Override
	public Object first() {
		if (rows.length > 0) {
			return rows[0];
		}
		
		return null;
	}

	@Override
	public Object last() {
		if (rows.length > 0) {
			return rows[rows.length - 1];
		}
		
		return null;
	}

	@Override
	public Class getObjectType() {
		return objectType;
	}

}
