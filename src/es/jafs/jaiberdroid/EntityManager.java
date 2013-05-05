package es.jafs.jaiberdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Entity loader for the Jaiberdroid system.
 * @author  Jose Antonio Fuentes Santiago
 * @versino 0.5
 */
final class EntityManager {
	/** Map of entities. */
	private final Map<String, Entity> entities = new HashMap<String, Entity>();


	/**
	 * Adds a class to manager.
	 * @param  type  Type of class to add.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	public void add(final Class type) throws JaiberdroidException {
		if (!entities.containsKey(type.getName())) {
			final Entity entity = JaiberdroidReflection.getEntity(type);
			if (null != entity) {
				entities.put(type.getName(), entity);
			} else {
				throw new JaiberdroidException("Entity " + type.getName() + " not loaded");
			}
		}
	}


	/**
	 * Gets an entity by its type.
	 * @param  type  Type of entity.
	 * @return The entity if found or null.
	 */
	@SuppressWarnings("rawtypes")
	public Entity getEntity(final Class type) {
		return entities.get(type.getName());
	}


	/**
	 * Gets the number of entities loaded.
	 * @return Numer of entities loaded.
	 */
	public int countEntities() {
		return entities.size();
	}


	/**
	 * Gets all create table queries string.
	 * @return List with all create table queries.
	 */
	public List<String> getCreateQueries() {
		List<String> queriesList = new ArrayList<String>();

		for (final Entity entity : entities.values()) {
			queriesList.add(JaiberdroidSql.getCreateSql(entity));
		}

		return queriesList;
	}


	/**
	 * Gets all create table queries string.
	 * @return List with all create table queries.
	 */
	public List<String> getDropQueries() {
		List<String> queriesList = new ArrayList<String>();

		for (final Entity entity : entities.values()) {
			queriesList.add(JaiberdroidSql.getDropSql(entity.getTableName()));
		}

		return queriesList;
	}


	/**
	 * Clear the entities stored.
	 */
	void clear() {
		entities.clear();
	}
}
