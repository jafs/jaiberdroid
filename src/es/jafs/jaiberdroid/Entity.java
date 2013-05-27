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

import java.util.Map;

import es.jafs.jaiberdroid.utils.ToString;

/**
 * Interface that represents a Table interface.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
final class Entity extends ToString {
	/** Table name. */
	private String tableName;
	/** Set of fields. */
	private final FieldSet fields = new FieldSet();
	/** Check if table is loaded. */
	private boolean loaded = false;
	/** Clases referenced. */
	@SuppressWarnings("rawtypes")
	private Class referenced; 


	/**
	 * Gets a boolean value that indicates if table is loaded.
	 * @return Boolean value that indicates if table is loaded.
	 */
	boolean isLoaded() {
		return loaded;
	}


	/**
	 * Sets a boolean value that indicates if table is loaded.
	 * @param  loaded  Boolean value that indicates if table is loaded.
	 */
	void setLoaded(final boolean loaded) {
		this.loaded = loaded;
	}


	/**
	 * Gets the name of the table.
	 * @return String with name of the table.
	 */
	String getTableName() {
		return tableName;
	}


	/**
	 * Sets the name of the table.
	 * @param  tableName  String with name of the table.
	 */
	void setTableName(final String tableName) {
		this.tableName = tableName;
	}


	/**
	 * Appends a field to field factory.
	 * @param  field  Data of field to insert.
	 */
	void appendField(final Field field) throws JaiberdroidException {
		try {
			fields.append(field);
		} catch (final JaiberdroidException e) {
			final StringBuilder error = new StringBuilder("In entity ");
			error.append(tableName);
			error.append(": ");
			error.append(e.getMessage());

			throw new JaiberdroidException(error.toString());
		}
	}


	/**
	 * Gets a field factory.
	 * @return Object with field factory.
	 */
	FieldSet getFields() {
		return fields;
	}


	/**
	 * Returns a value that indicates if current entity has a valid primary key.
	 * @return Value boolean that indicates if entity has valid primary key.
	 */
	boolean hasKey() {
		return fields.hasKey();
	}


	/**
	 * Returns a boolean value that indicates if the entity has index fields.
	 * @return Boolean value that indicates if the entity has index fields.
	 */
	boolean hasIndexes() {
		final Map<String, Field> fieldsMap = fields.getFields();
		boolean index = false;

		if (null != fieldsMap) {
			for (final Field field : fieldsMap.values()) {
				if (field.isIndex()) {
					index = true;
					break;
				}
			}
		}

		return index;
	}


	/**
	 * Gets the class linked with this entity.
	 * @return Class linked with this entity.
	 */
	@SuppressWarnings("rawtypes")
	Class getReferenced() {
		return referenced;
	}


	/**
	 * Sets the class linked with this entity.
	 * @param  referenced  Class linked with this entity.
	 */
	@SuppressWarnings("rawtypes")
	void setReferenced(final Class referenced) {
		this.referenced = referenced;
	}
}
