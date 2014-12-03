/**
 * 
 */
package se.mah.elis.adaptor.structure.api;

import java.util.Iterator;

/**
 * The Apartment interface describes a logical apartment, which is a collection
 * of rooms.
 * 
 * An apartment holds two lists: a list of rooms, and a list of room
 * collections. A room can belong to several room collections, but should
 * always be added to the apartment itself as well.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface Apartment extends BuildingPartition {
	
	/**
	 * Adds a room to this apartment.
	 * 
	 * @param room The room to be added.
	 * @since 1.0
	 */
	void addRoom(Room room);
	
	/**
	 * Adds a number of rooms to this apartment.
	 * 
	 * @param rooms The rooms to be added.
	 * @since 1.0
	 */
	void addRooms(Room[] rooms);
	
	/**
	 * Adds all rooms of a collection to this apartment.
	 * 
	 * @param rooms The room collection to be added.
	 * @since 1.0
	 */
	void addRooms(RoomCollection rooms);
	
	/**
	 * Removes a room from this apartment.
	 * 
	 * @param room The room to be removed.
	 * @since 1.0
	 */
	void removeRoom(Room room);
	
	/**
	 * Removes all rooms from this apartment.
	 * 
	 * @since 1.0
	 */
	void flushRooms();
	
	/**
	 * Checks whether a room is part of this apartment or not. It checks the
	 * internal list of rooms, as well as all room collections.
	 * 
	 * @param room The room to check for.
	 * @return True if the room is part of this apartment, otherwise false.
	 * @since 1.0
	 */
	boolean containsRoom(Room room);
	
	/**
	 * Counts the number of unique rooms in this apartment. All rooms in the
	 * internal list, as well as in the room collections, are counted.
	 * 
	 * @return The number of rooms.
	 * @since 1.0
	 */
	int getNumberOfRooms();
	
	/**
	 * Returns an Iterator object for the internal list of rooms.
	 * 
	 * @return The room iterator.
	 * @since 1.0
	 */
	Iterator<Room> getRoomIterator();
	
	/**
	 * Adds a collection of rooms to this apartment.
	 * 
	 * @param collection The collection to be added.
	 * @since 1.0
	 */
	void addCollection(RoomCollection collection);
	
	/**
	 * Removes a collection of rooms from this apartment.
	 * 
	 * @param collection
	 * @since 1.0
	 */
	void removeCollection(RoomCollection collection);
	
	/**
	 * Removes all room collections from this apartment.
	 * 
	 * @since 1.0
	 */
	void flushCollections();
	
	/**
	 * Counts the number of room collections associated with this apartment.
	 * 
	 * @return The number of collections.
	 * @since 1.0
	 */
	int getNumberOfCollections();
	
	/**
	 *  Returns an Iterator object for the internal list of room collections.
	 * 
	 * @return The room collection iterator.
	 * @since 1.0
	 */
	Iterator<RoomCollection> getCollectionIterator();
}
