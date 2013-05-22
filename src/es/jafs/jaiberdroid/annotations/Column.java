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

	/**
	 * Default value for current column (defaults "").
	 * @return String that indicates default value for current column.
	 */
	public String defaultValue() default "";

	/**
	 * Current column must be indexed (defaults false).
	 * @return Boolean value that indicates if current column is an index.
	 */
	public boolean index() default false;

	/**
	 * Ascendent order when current column is indexed (defaults true).
	 * @return Boolean value that indicates order in index.
	 */
	public boolean ascOrder() default true;
}
