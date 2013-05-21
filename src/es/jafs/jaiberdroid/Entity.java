package es.jafs.jaiberdroid;

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
	 * Deletes a existing field.
	 * @param  field  Field to detele.ç
	 * @deprecated  This method will be deleted in 1.0 version.
	 * @todo        Delete in 1.0 version.
	 */
	void removeField(final Field field) {
		fields.remove(field);
	}


	/**
	 * Deletes a existing field.
	 * @param  name  String with the name of the field.
	 * @deprecated  This method will be deleted in 1.0 version.
	 * @todo        Delete in 1.0 version.
	 */
	void removeField(final String name) {
		fields.remove(name);
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
