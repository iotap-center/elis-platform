/**
 * 
 */
package se.mah.elis.adaptor.building.api.plan;

import java.util.Collection;

import se.mah.elis.exceptions.StaticEntityException;

/**
 * The RoomCollection interface describes a collection or a set of rooms within
 * a building.
 * 
 * @author "Johan Holmberg, Malmö University"
 * @since 1.0
 */
public interface RoomCollection extends BuildingPartition, Collection<Room> {
}
