package se.mah.elis.data;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>OrderedProperties is an extension of Properties, where the entries are
 * ordered chronologically, meaning that if an element A was added before an
 * element B, then A is guaranteed to always come before B.</p>
 * 
 * <p>This class is thread safe.</p>
 * 
 * <p>Based on a snipplet from {@link stackoverflow.com/questions/1312383/pulling-values-from-a-java-properties-file-in-ordeSalmi﻿r?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0CC8QFjAA&url=http%3A%2F%2Fstackoverflow.com%2Fquestions%2F1312383%2Fpulling-values-from-a-java-properties-file-in-order&ei=rfzoUpHBBuiRywOgvYHQDQ&usg=AFQjCNEnqRWCR44MGKvrdjzBHB23y2TycQ&sig2=zwAZssJnP-Adytn7FD3iEg&bvm=bv.60157871,d.bGQ/}</p>
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.1
 */
public class OrderedProperties extends Properties {
	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	/**
	 * <p>Overrides {@link java.util.Properties#propertyNames() propertyNames()}.</p>
	 * 
	 * <p>The elements will be ordered in the same order that they were added
	 * to the collection.</p> 
	 * 
	 * @return An enumeration of all the keys in this property list, including
	 * 		the keys in the default property list.
	 * @since 1.1
	 */
	@Override
	public Enumeration<?> propertyNames() {
		return Collections.enumeration(keys);
	}

	/**
	 * <p>Overrides {@link java.util.Hashtable#elements() elements()}.</p>
	 * 
	 * <p>The elements will be ordered in the same order that they were added
	 * to the collection.</p> 
	 * 
	 * @return An enumeration of the values.
	 * @since 1.1
	 */
	@Override
	public synchronized Enumeration<Object> elements() {
		return Collections.enumeration(keys);
	}

	/**
	 * Overrides {@link java.util.Hashtable#keys() keys()}.
	 * 
	 * @return An enumeration of the keys.
	 * @since 1.1
	 */
	@Override
	public Enumeration<Object> keys() {
		return Collections.enumeration(keys);
	}

	/**
	 * Associates the specified value with the specified key in this map
	 * (optional operation). If the map previously contained a mapping for the
	 * key, the old value is replaced by the specified value. (A map m is said
	 * to contain a mapping for a key k if and only if m.containsKey(k) would
	 * return true.)
	 * 
	 * @param key The key 
	 * @param value
	 * @return The previous value associated with key, or null if there was no
	 * 		mapping for key. (A null return can also indicate that the map
	 * 		previously associated null with key, if the implementation supports
	 * 		null values.)
	 * @since 1.1
	 */
	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	/**
	 * Overrides {@link java.util.Hashtable#remove(Object) remove(Object)}.
	 * 
	 * @param The key that needs to be removed
	 * @returned The value to which the key had been mapped, or null if the key
	 * 		did not have a mapping
	 * @since 1.1
	 */
	@Override
	public synchronized Object remove(Object key) {
		keys.remove(key);
		return super.remove(key);
	}

	/**
	 * Overrides {@link java.util.Hashtable#clear() clear()}.
	 * 
	 * @since 1.1
	 */
	@Override
	public synchronized void clear() {
		keys.clear();
		super.clear();
	}
	
	/**
	 * Overrides {@link java.util.Hashtable#entrySet() entrySet()}.
	 * 
	 * @return Returns an ordered set of this object's entries.
	 * @since 1.1
	 */
	@Override
	public Set<Map.Entry<Object, Object>> entrySet() {
		 Set<Map.Entry<Object, Object>> entrySet =
				 Collections.synchronizedSet(new LinkedHashSet());
		 Object value;
		 
		  for (Object key: keys) {
			  value = this.get(key);
			  entrySet.add(new EntryImpl<Object, Object>(key, value));
		  }
		 
		 return entrySet;
	}
	
	private class EntryImpl<K, V> implements Map.Entry<K, V> {
		
		private K key;
		private V value;

		public EntryImpl(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			
			return old;
		}

		
	}
}
