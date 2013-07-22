/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import java.util.Collection;

import se.mah.elis.adaptor.building.api.exceptions.StaticEntityException;

/**
 * The RoomCollection interface describes a collection or a set of rooms within
 * a building.
 * 
 * @author "Johan Holmberg, Malmö University"
 */
public interface RoomCollection extends BuildingPartition, Collection<Room> {
}
