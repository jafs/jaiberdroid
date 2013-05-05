package es.jafs.jaiberdroid.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that prints attributes in classes that inherited from it.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
public class ToString {
	/** Values and variables separator. */ 
	private static final String SEPARATOR = "->";
	/** List of attributes ignored. */
	private List<String> ignored = new ArrayList<String>();


	/**
	 * Avoid extern instances for the class.
	 */
	protected ToString() {
	}


	/**
	 * Add attributes that will not printed.
	 * @param  params  List of attributes.
	 */
	protected void avoidToString(final Object... params) {
		for (final Object current : params) {
			ignored.add(current.getClass().toString());
		}
	}


	/**
	 * Gets a string with attributes and its values.
	 * @return String with content of the object.
	 */
	@Override
	public String toString() {
		final StringBuilder objBuilder = new StringBuilder();

		// String start.
		objBuilder.append(getClass().getSimpleName());
		objBuilder.append(" {");

		try {
			// Check all the classes until find current SqliteToString class.
			Class<?> objClase = getClass();
			Field[] arCampos;

			while (null != objClase && !ToString.class.equals(objClase)) {
				arCampos = objClase.getDeclaredFields();
				if (null != arCampos) {
					// Check fields of the object.
					for (Field objCampo : arCampos) {
						if (isPrintable(objCampo)) {
							objCampo.setAccessible(true);
							objBuilder.append(objCampo.getName());
							objBuilder.append(SEPARATOR);
							objBuilder.append(objCampo.get(this));
							objBuilder.append("; ");
						}
					}
				}

				// Next father class.
				objClase = objClase.getSuperclass();
			}
		} catch (final IllegalArgumentException e) {
		} catch (final IllegalAccessException e) {
		}

		// End of string. If there are extra data delete the last "; ".
		if (objBuilder.length() > getClass().getSimpleName().length() + 2) {
			objBuilder.setLength(objBuilder.length() - 2);
		}
		objBuilder.append('}');

		return objBuilder.toString();
	}


	/**
	 * Returns a boolean value that indicates if current attribute is printable.
	 * @param  field  Field to check.
	 * @return Boolean value that indicates if current attribute is printable.
	 */
	private boolean isPrintable(final Field field) {
		final String type = field.getType().getName();

		return !ignored.contains(type) 
				&& (!Modifier.isFinal(field.getModifiers())
				|| !type.equals(String.class.getName()));
	}
}
