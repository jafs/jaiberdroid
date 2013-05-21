package es.jafs.jaiberdroid.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to specify the mapped tables.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	/**
	 * The name of the table (default the entity name).
	 * @return String with the table name.
	 */
	public String name() default "";
}
