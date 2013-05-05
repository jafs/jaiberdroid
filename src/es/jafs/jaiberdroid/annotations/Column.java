package es.jafs.jaiberdroid.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Is used to specify the mapped column for a persistent property or field.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	/**
	 * Whether the column is a primary key (defaults false).
	 * @return Boolean value that indicates if the field is primary key.
	 */
	public boolean primary() default false;

	/**
	 * Whether the column is a unique key (defaults false).
	 * @return Boolean value that indicates if the field has unique key.
	 */
	public boolean unique() default false;

	/**
	 * The field can be contains null values (defaults true).
	 * @return Boolean value that indicates if the field can store null values.
	 */
	public boolean nullable() default true;
}
