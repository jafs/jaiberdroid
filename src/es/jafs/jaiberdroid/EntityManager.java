/*
 * Copyright (C) 2013 JAFS.es
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.jafs.jaiberdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity loader for the Jaiberdroid system.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
final class EntityManager {
	/** Map of entities. */
	private final Map<String, Entity> entities = new HashMap<String, Entity>();


	/**
	 * Only for Jaiberdroid package can be instanced.
	 */
	EntityManager() {
	}


	/**
	 * Adds a class to manager.
	 * @param  type  Type of class to add.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	void add(final Class type) throws JaiberdroidException {
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
	Entity getEntity(final Class type) {
		return entities.get(type.getName());
	}


	/**
	 * Gets the number of entities loaded.
	 * @return     Numer of entities loaded.
	 * @deprecated In version 1.0 will be deleted.
	 * @todo       Delete in 1.0 version.
	 */
	int countEntities() {
		return entities.size();
	}


	/**
	 * Gets all create table queries string.
	 * @return List with all create table queries.
	 */
	List<String> getCreateQueries() {
		final List<String> queriesList = new ArrayList<String>();

		for (final Entity entity : entities.values()) {
			queriesList.add(JaiberdroidSql.getCreateSql(entity));

			// Puts Create Index queries into list.
			if (entity.hasIndexes()) {
				queriesList.addAll(JaiberdroidSql.getCreateIndex(entity));
			}
		}

		return queriesList;
	}


	/**
	 * Gets all create table queries string.
	 * @return List with all create table queries.
	 */
	List<String> getDropQueries() {
		final List<String> queriesList = new ArrayList<String>();

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
