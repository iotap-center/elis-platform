/**
 * 
 */
package se.mah.elis.adaptor.structure.api;

import java.util.Collection;

/**
 * The RoomCollection interface describes a collection or a set of rooms within
 * a building.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface RoomCollection extends BuildingPartition, Collection<Room> {
}
