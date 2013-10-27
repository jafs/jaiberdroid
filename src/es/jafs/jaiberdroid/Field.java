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

import es.jafs.jaiberdroid.utils.ToString;

/**
 * Class that represents a field of a table.<br />
 * Stores the name, type, if is primary key and if is auto increment field. Provides
 * methods for get SQL strings in querys.
 * 
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
@SuppressWarnings("rawtypes")
final class Field extends ToString {
	/** Name of the field. */
	private String name = "";
	/** Field's data type. */
	private FieldTypes type = FieldTypes.NULL;
	/** The field is primary key or not. */
	private boolean primary = false;
	/** The field is unique. */
	private boolean unique = false;
	/** The field is null. */
	private boolean isnull = true;
	/** The field has default value. */
	private String defaultValue = null;
	/** The current field must be indexed. */
	private boolean index = false;
	/** Ascendent order when current column is indexed. */
	private boolean ascOrder = true;
	/** Class of field. */
	private Class fieldClass = null;


	/**
	 * Level package constructor for table keys.
	 * @param  name        Name of the field.
	 * @param  fieldClass  Class type of the field
	 */
	Field(final String name, final Class fieldClass) {
		this.name = name;
		this.type = FieldTypes.INTEGER;
		this.primary = true;
		this.unique = false;
		this.isnull = false;
		this.fieldClass = fieldClass;
	}


	/**
	 * Default constructor with three params.
	 * @param  name  Name of the field.
	 * @param  type  Field's data type.
	 * @param  fieldClass  Class type of the field
	 */
	Field(final String name, final FieldTypes type, final Class fieldClass) {
		this.name = name;
		this.fieldClass = fieldClass;

		if (null != type) {
			this.type = type;
		}
	}


	/**
	 * Constructor with default params and autoincrement.
	 * @param  name     Name of the field.
	 * @param  type     Field's data type.
	 * @param  isnull   Field is null or not.
	 * @param  unique   The field has unique constraint.
	 * @param  fieldClass  Class type of the field
	 */
	Field(final String name, final FieldTypes type, final boolean isnull, final boolean unique, final Class fieldClass) {
		this.name = name;
		this.unique = unique;
		this.isnull = isnull;
		this.fieldClass = fieldClass;

		if (null != type) {
			this.type = type;
		}
	}


	/**
	 * Gets the name of the field.
	 * @return String with the name of the field.
	 */
	final String getName() {
		return name;
	}


	/**
	 * Gets the type of the field.
	 * @return Enumeration with the type of the field.
	 */
	final FieldTypes getType() {
		return type;
	}


	/**
	 * Gets if the field is primary key.
	 * @return Boolean that is true if field is primary key.
	 */
	final boolean isPrimary() {
		return primary;
	}


	/**
	 * Gets if the field is unique.
	 * @return Boolean that is true if field is unique.
	 */
	final boolean isUnique() {
		return unique;
	}


	/**
	 * Gets if the field is null.
	 * @return Boolean that is true if field is null.
	 */
	final boolean isNull() {
		return isnull;
	}


	/**
	 * Gets the class of the field.
	 * @return Class of the field.
	 */
	final Class getFieldClass() {
		return fieldClass;
	}


	/**
	 * Sets the class of the field.
	 * @param  fieldClass  Class of the field.
	 */
	final void setFieldClass(final Class fieldClass) {
		this.fieldClass = fieldClass;
	}


	/**
	 * Gets a string with default value of field.
	 * @return String with default value of field.
	 */
	final String getDefaultValue() {
		return defaultValue;
	}


	/**
	 * Sets a string with default value of field.
	 * @param  defaultValue  String with default value of field.
	 */
	final void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}


	/**
	 * Gets a boolean value that indicates if current field must be indexed.
	 * @return Boolean value that indicates if current field must be indexed.
	 */
	final boolean isIndex() {
		return index;
	}


	/**
	 * Sets a boolean value that indicates if current field must be indexed.
	 * @param  index  Boolean value that indicates if current field must be indexed.
	 */
	final void setIndex(final boolean index) {
		this.index = index;
	}


	/**
	 * Gets a boolean value that indicates if index must be ordered ascending.
	 * @return Boolean value that indicates if index must be ordered ascending.
	 */
	final boolean isAscOrder() {
		return ascOrder;
	}


	/**
	 * Sets a boolean value that indicates if index must be ordered ascending.
	 * @param ascOrder Boolean value that indicates if index must be ordered ascending.
	 */
	final void setAscOrder(final boolean ascOrder) {
		this.ascOrder = ascOrder;
	}
}
