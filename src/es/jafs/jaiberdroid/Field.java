package es.jafs.jaiberdroid;






/**
 * Class that represents a field of a table.<br />
 * Stores the name, type, if is primary key and if is auto increment field. Provides
 * methods for get SQL strings in querys.
 * 
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
@SuppressWarnings("rawtypes")
class Field {
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
	/** Class of field. */
	private Class fieldClass = null;


	/**
	 * Level package constructor for table keys.
	 * @param  name        Name of the field.
	 * @param  fieldClass  Class type of the field
	 */
	public Field(final String name, final Class fieldClass) {
		this.name = name;
		this.type = FieldTypes.INTEGER;
		this.primary = true;
		this.unique = false;
		this.isnull = false;
		this.fieldClass = fieldClass;
	}


	/**
	 * Default constructor with only two params.
	 * @param  name  Name of the field.
	 * @param  type  Field's data type.
	 * @param  fieldClass  Class type of the field
	 */
	public Field(final String name, final FieldTypes type, final Class fieldClass) {
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
	 * @param  auto     The field is auto numeric.
	 * @param  unique   The field has unique constraint.
	 * @param  fieldClass  Class type of the field
	 */
	public Field(final String name, final FieldTypes type, final boolean isnull, final boolean unique, final Class fieldClass) {
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
	public final String getName() {
		return name;
	}


	/**
	 * Gets the type of the field.
	 * @return Enumeration with the type of the field.
	 */
	public final FieldTypes getType() {
		return type;
	}


	/**
	 * Gets if the field is primary key.
	 * @return Boolean that is true if field is primary key.
	 */
	public final boolean isPrimary() {
		return primary;
	}


	/**
	 * Gets if the field is unique.
	 * @return Boolean that is true if field is unique.
	 */
	public final boolean isUnique() {
		return unique;
	}


	/**
	 * Gets if the field is null.
	 * @return Boolean that is true if field is null.
	 */
	public final boolean isNull() {
		return isnull;
	}


	/**
	 * Gets the class of the field.
	 * @return Class of the field.
	 */
	public final Class getFieldClass() {
		return fieldClass;
	}


	/**
	 * Sets the class of the field.
	 * @param  fieldClass  Class of the field.
	 */
	public final void setFieldClass(final Class fieldClass) {
		this.fieldClass = fieldClass;
	}


	/**
	 * Gets a string with the object's content.
	 * @return String with the object's content.
	 */
	@Override
	public String toString() {
		final StringBuilder objSql = new StringBuilder();

		objSql.append(Field.class.getName());
		objSql.append("{name->");
		objSql.append(name);
		objSql.append("; type->");
		objSql.append(type.name());
		objSql.append("; primary->");
		objSql.append(primary);
		objSql.append("; isnull->");
		objSql.append(isnull);
		objSql.append("; unique->");
		objSql.append(unique);
		objSql.append('}');

		return objSql.toString();
	}
}