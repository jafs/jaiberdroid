package es.jafs.jaiberdroid;

import java.util.HashMap;
import java.util.Map;

import android.provider.BaseColumns;
import android.text.TextUtils;
import es.jafs.jaiberdroid.utils.ToString;



/**
 * Class that implements a fields controller.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
class FieldSet extends ToString implements BaseColumns {
	/** Map with a set of fields. */
	private final Map<String, Field> fields = new HashMap<String, Field>();


	/**
	 * Appends a field to field factory.
	 * @param  field  Data of field to insert.
	 * @throws IllegalArgumentException 
	 * @see    Field
	 */
	public void append(final Field field) throws JaiberdroidException {
		if (null == field) {
			throw new JaiberdroidException("Null field");
		}

		if (null == field.getName() || TextUtils.isEmpty(field.getName())) {
			throw new JaiberdroidException("Empty or null field's name");
		}

		if (_ID.equals(field.getName()) && (!field.getType().equals(FieldTypes.INTEGER)
			|| !field.isPrimary())) {
			throw new JaiberdroidException("The field _id must be of type int and primary key");
		}

		if (field.isPrimary() && !_ID.equals(field.getName())) {
			throw new JaiberdroidException("Only the field _id can be primary key.");
		}

		fields.put(field.getName(), field);
	}


	/**
	 * Deletes a existing field.
	 * @param  field  Field to detele.
	 */
	public void remove(final Field field) {
		if (null != field) {
			remove(field.getName());
		}
	}


	/**
	 * Deletes a existing field.
	 * @param  name  String with the name of the field.
	 */
	public void remove(final String name) {
		if (null != name && fields.containsKey(name)) {
			fields.remove(name);
		}
	}


	/**
	 * Returns a boolean value that indicates is there are fields.
	 * @return Boolean value that indicates is there are fields.
	 */
	public final boolean isEmpty() {
		return fields.isEmpty();
	}


	/**
	 * Get te fields of the factory without the key.
	 * @return Map with fields without key.
	 */
	public final Map<String, Field> getFields() {
		return fields;
	}


	/**
	 * Gets if the field set has valid primary key.
	 * @return Value boolean that indicates if field set has valid primary key.
	 */
	public boolean hasKey() {
		final Field key = fields.get(_ID);
		return (null != key && key.isPrimary());
	}


	/**
	 * Gets an array of fields of current field set.
	 * @return Array with the names of the fields.
	 */
	public String[] getFieldsArray() {
		final String[] array = new String[fields.size()];

		int i = 0;
		for (final String field : fields.keySet()) {
			array[i] = field;
			++i;
		}

		return array;
	}


	/**
	 * Gets a field class of attribute received.
	 * @param  name  Name of field to search.
	 * @return Class of field or null is not find.
	 */
	@SuppressWarnings("rawtypes")
	public Class getFieldClass(final String name) {
		return fields.get(name).getFieldClass();
	}
}
