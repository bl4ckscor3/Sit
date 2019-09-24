package bl4ckscor3.mod.sit;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Use this class to manage sit entities correctly
 */
public class SitUtil
{
	/**
	 * <dimension type id, <position, entity>>
	 * This map only gets populated on server side.
	 */
	private static final Map<Integer,Map<BlockPos,EntitySit>> OCCUPIED = new HashMap<>();

	/**
	 * Adds a sit entity to the map that keeps track of them. This does not spawn the entity itself.
	 * @param world The world to add the entity in
	 * @param pos The position at which to add the entity
	 * @param entity The entity to add
	 * @return true if the entity was added, false otherwhise. This is always false on the client.
	 */
	public static boolean addSitEntity(World world, BlockPos pos, EntitySit entity)
	{
		if(!world.isRemote)
		{
			int id = getDimensionTypeId(world);

			if(!OCCUPIED.containsKey(id))
				OCCUPIED.put(id, new HashMap<>());

			OCCUPIED.get(id).put(pos, entity);
			return true;
		}

		return false;
	}

	/**
	 * Removes a sit entity from the map that keeps track of them. This does not remove the entity itself.
	 * @param world The world to remove the entity from
	 * @param pos The position to remove the entity from
	 * @return true if the entity was removed, false otherwhise. This is always false on the client.
	 */
	public static boolean removeSitEntity(World world, BlockPos pos)
	{
		if(!world.isRemote)
		{
			int id = getDimensionTypeId(world);

			if(OCCUPIED.containsKey(id))
			{
				OCCUPIED.get(id).remove(pos);
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the sit entity that is situated at the given position in the given world
	 * @param world The world to get the entity from
	 * @param pos The position to get the entity from
	 * @return The entity at the given position in the given world, null if there is none. This is always null on the client.
	 */
	public static EntitySit getSitEntity(World world, BlockPos pos)
	{
		if(!world.isRemote)
		{
			int id = getDimensionTypeId(world);

			if(OCCUPIED.containsKey(id))
				return OCCUPIED.get(id).get(pos);
		}

		return null;
	}

	/**
	 * Checks whether there is a player sitting at the given block position in the given world
	 * @param world The world to check in
	 * @param pos The position to check at
	 * @return true if a player is sitting at the given position in the given world, false otherwhise. This is always false on the client.
	 */
	public static boolean isOccupied(World world, BlockPos pos)
	{
		int id = getDimensionTypeId(world);

		return SitUtil.OCCUPIED.containsKey(id) && SitUtil.OCCUPIED.get(id).containsKey(pos);
	}

	private static int getDimensionTypeId(World world)
	{
		return world.provider.getDimension();
	}
}
