package es.jafs.jaiberdroid;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import android.text.TextUtils;
import es.jafs.jaiberdroid.annotations.Column;
import es.jafs.jaiberdroid.annotations.Table;

/**
 * Class used like an interface with Java Reflection system.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
public class JaiberdroidReflection {
	/** Prefix of SET type methods. */
	public static final String SET_PREFIX = "set";
	/** Prefix of GET type methods. */
	public static final String GET_PREFIX = "get";
	/** Name of get id method. */
	public static final String GET_ID = GET_PREFIX + JaiberdroidSql._ID;
	/** Name of set id method. */
	public static final String SET_ID = SET_PREFIX + JaiberdroidSql._ID;


	/**
	 * Executes a method of type get.
	 * @param  name    Method name.
	 * @param  object  Object that contains the method.
	 * @return String with value of execution of get method.
	 * @throws JaiberdroidException 
	 */
	public static String executeGetMethod(final String name, final Object object) throws JaiberdroidException {
		return execute(name, object, null, null, true).toString();
	}


	/**
	 * Executes a method of type set.
	 * @param name    Method name.
	 * @param object  Object that contains the method.
	 * @param type    Type of data to set.
	 * @param value   Value to set.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	public static void executeSetMethod(final String name, final Object object, final Class type, final Object value) throws JaiberdroidException {
		execute(name, object, type, value, false);
	}


	/**
	 * Executes a method with received parameters.
	 * @param name      Method name.
	 * @param object    Object that contains the method.
	 * @param type      Type of data to set, when method isn't read only.
	 * @param value     Value to set, when method isn't read only.
	 * @param readOnly  If true method is read only (get), if false method is write mode (set).
	 * @return Value with result of method execution.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	private static Object execute(final String name, final Object object, final Class type, final Object value, final boolean readOnly) throws JaiberdroidException {
		Object result = null;

		try {
			if (readOnly) { // Methods of type GET.
				result = object.getClass().getMethod(name, new Class[0]).invoke(object, new Object[0]);
			} else { // Methods of type SET.
				object.getClass().getMethod(name, type).invoke(object, value);
			}
		} catch (final IllegalArgumentException e) {
			throw new JaiberdroidException("Illegal argument: " + e.getMessage());
		} catch (final SecurityException e) {
			throw new JaiberdroidException("Security Exception: " + e.getMessage());
		} catch (final IllegalAccessException e) {
			throw new JaiberdroidException("Illegal access: " + e.getMessage());
		} catch (final InvocationTargetException e) {
			throw new JaiberdroidException("Invocation exception: " + e.getMessage());
		} catch (final NoSuchMethodException e) {
			throw new JaiberdroidException("No such method: " + e.getMessage());
		}

		return result;
	}


	/**
	 * Load an analyze an entity.
	 * @param  type  Class to add.
	 * @return Entity if loaded successful.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	public static Entity getEntity(final Class type) throws JaiberdroidException {
		// Load table data.
		final Entity entity = loadTable(type);
		if (null == entity) {
			throw new JaiberdroidException("Class " + type.getName() + " has no annotation Table");
		}

		// Load columns data.
		loadColumns(type, entity);
		entity.setLoaded(true);

		return entity;
	}


	/**
	 * Loads the columns of database in the object.
	 * @param  entity  Entity to analize.
	 */
	@SuppressWarnings("rawtypes")
	private static Entity loadTable(final Class type) {
		Entity entity = null;

		// Load the annotations of entity.
		final Annotation[] annotations = type.getDeclaredAnnotations();
		String name;
		for (Annotation note : annotations) {
			if (note instanceof Table) {
				name = ((Table) note).name();
				if (null == name || TextUtils.isEmpty(name)) {
					name = type.getSimpleName();
				}

				entity = new Entity();
				entity.setTableName(name);
				entity.setReferenced(type);
				break;
			}
		}

		return entity;
	}


	/**
	 * Loads the columns of database in the object.
	 * @param  entity  Entity to analize.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	private static void loadColumns(final Class type, final Entity entity) throws JaiberdroidException {
		final java.lang.reflect.Field[] attributes = type.getDeclaredFields();
		Annotation[] annotations;

		// Load the annotations of attributes.
		for (java.lang.reflect.Field current : attributes) {
			annotations = current.getDeclaredAnnotations();
			for (Annotation note : annotations) {
				if (note instanceof Column) {
					entity.appendField(getColumn(current, (Column) note));
				}
			}
		}

		if (!entity.hasKey()) {
			throw new JaiberdroidException("Class " + type.getName() + " has no primary key _id");
		}
	}


	/**
	 * Process a field with its annotation.
	 * @param attribute   Attribute with annotation.
	 * @param annotation  Annotation of the attribute.
	 */
	private static Field getColumn(final java.lang.reflect.Field attribute, final Column annotation)
								throws JaiberdroidException {
		FieldTypes type = FieldTypes.NULL;
		String name = null;
		String typeName = attribute.getType().getName();

		// Load the name.
		name = attribute.getName();

		if (annotation.primary()) {
			if (int.class.getName().equals(typeName)) {
				return new Field(name, int.class);
			} else {
				throw new JaiberdroidException("Primary key must be of int type");
			}
		} else {
			// Load the attribute type.
			if (String.class.getName().equals(typeName)) {
				type = FieldTypes.TEXT;
			} else if (int.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)
					|| long.class.getName().equals(typeName) || Long.class.getName().equals(typeName)) {
				type = FieldTypes.INTEGER;
			} else if (float.class.getName().equals(typeName) || double.class.getName().equals(typeName)
					|| Float.class.getName().equals(typeName) || Double.class.getName().equals(typeName)) {
				type = FieldTypes.REAL;
			} else {
				throw new JaiberdroidException("Invalid data type: " + attribute.getType().getName());
			}

			// Check if the attribute is null and is primitive type.
			if (annotation.nullable() && isPrimitive(attribute.getType())) {
				throw new JaiberdroidException("Primitive fields must be not null: " + attribute.getName());
			}

			return new Field(name, type, annotation.nullable(), annotation.unique(), attribute.getType());
		}
	}


	/**
	 * Checks if a type of data is primitive.
	 * @param  type  Type of data to check.
	 * @return Boolean value that is true when data type is primitive.
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isPrimitive(final Class type) {
		boolean primitive = false;

		primitive = int.class.getName().equals(type.getName())
					|| long.class.getName().equals(type.getName())
					|| double.class.getName().equals(type.getName())
					|| float.class.getName().equals(type.getName());
		
		return primitive;
	}


	/**
	 * Gets a method name for received name and prefix.
	 * @param  prefix  Prefix of the method.
	 * @param  name    Name of the attribute.
	 * @return String with the name of the method.
	 */
	public static String getMethodName(final String prefix, final String name) {
		final StringBuilder methodName = new StringBuilder();

		methodName.append(prefix);
		methodName.append(name.substring(0, 1).toUpperCase(Locale.getDefault()));
		methodName.append(name.substring(1));

		return methodName.toString();
	}
}
