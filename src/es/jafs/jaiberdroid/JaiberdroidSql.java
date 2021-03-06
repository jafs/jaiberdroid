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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Class that constructs SQL queries from a little data.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
final class JaiberdroidSql implements BaseColumns {
	/** Start of count function. */
	private static final String SQL_COUNT_INI = "COUNT";
	/** Start of create table command. */
	private static final String CREATE_TABLE = "CREATE TABLE ";
	/** Start of drop table command. */
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
	/** Select command. */
	private static final String SQL_SELECT = "SELECT ";
	/** From parameter. */
	private static final String SQL_FROM = " FROM ";
	/** Start of count function. */
	private static final char SQL_FUNCTION_INI = '(';
	/** End of count function. */
	private static final char SQL_FUNCTION_END = ')';
	/** Start of create index command. */
	private static final String CREATE_INDEX = "CREATE INDEX ";
	/** Prefix before table name in index. */
	private static final String INDEX_ON = " ON ";
	/** Index of type ascending. */
	private static final String INDEX_ASC = "ASC";
	/** Index of type descending. */
	private static final String INDEX_DESC = "DESC";
	/** Prefix of indexes names. */
	private static final String INDEX_PREFIX = "index";
	/** Separator in index name. */
	private static final char INDEX_SEPARATOR = '_';

	/** String with PRIMARY KEY constraint name. */
	private static final String PRIMARY_KEY = "PRIMARY KEY";
	/** String with AUTOINCREMENT constraint name. */
	private static final String AUTOINCREMENT = "AUTOINCREMENT";
	/** String with UNIQUE constraint name. */
	private static final String UNIQUE = "UNIQUE";
	/** String with DEFAULT constraint name. */
	private static final String DEFAULT = "DEFAULT";
	/** String with NOT NULL constraint name. */
	private static final String NOT_NULL = "NOT NULL";


	/**
	 * Private constructor for avoid external instances.
	 */
	private JaiberdroidSql() {
	}


	/**
	 * Gets the create table SQL sentence.
	 * @return String with the create table SQL sentence.
	 */
	public static String getCreateSql(final Entity entity) {
		final StringBuilder objSql = new StringBuilder();

		objSql.append(CREATE_TABLE);
		objSql.append(entity.getTableName());
		objSql.append(' ');
		objSql.append(SQL_FUNCTION_INI);
		objSql.append(getFieldsCreateSql(entity.getFields()));
		objSql.append(SQL_FUNCTION_END);

		return objSql.toString();
	}


	/**
	 * Gets a list of string with Create Index queries for current entity.
	 * @param  entity  Entity to analyze.
	 * @return List of string with Create Index queries.
	 */
	public static List<String> getCreateIndex(final Entity entity) {
		final List<String> queries = new ArrayList<String>();
		final Map<String, Field> fields = entity.getFields().getFields();
		final StringBuilder objSql = new StringBuilder();

		// Make Create Index sentences for al indexes.
		for (Field field : fields.values()) {
			if (field.isIndex()) {
				objSql.setLength(0);
				objSql.append(CREATE_INDEX);
				objSql.append(INDEX_PREFIX);
				objSql.append(INDEX_SEPARATOR);
				objSql.append(entity.getTableName());
				objSql.append(INDEX_SEPARATOR);
				objSql.append(field.getName());
				objSql.append(INDEX_ON);
				objSql.append(entity.getTableName());
				objSql.append(SQL_FUNCTION_INI);
				objSql.append(field.getName());
				objSql.append(' ');
				if (field.isAscOrder()) {
					objSql.append(INDEX_ASC);
				} else {
					objSql.append(INDEX_DESC);
				}
				objSql.append(SQL_FUNCTION_END);

				queries.add(objSql.toString());
			}
		}

		return queries;
	}


	/**
	 * Gets the drop table SQL query.
	 * @return String with the drop table SQL query.
	 */
	public static String getDropSql(final String table) {
		final StringBuilder objSql = new StringBuilder();

		objSql.append(DROP_TABLE);
		objSql.append(table);

		return objSql.toString();
	}


	/**
	 * Gets the number of elements.
	 * @return String with general SELECT query.
	 */
	public static String getCountSql(final String table) {
		final StringBuilder objSql = new StringBuilder();

		objSql.append(SQL_SELECT);
		objSql.append(SQL_COUNT_INI);
		objSql.append(SQL_FUNCTION_INI);
		objSql.append(_ID);
		objSql.append(SQL_FUNCTION_END);
		objSql.append(SQL_FROM);
		objSql.append(table);

		return objSql.toString();
	}


	/**
	 * Gets the create table SQL for this field.
	 * @return String with the create table SQL.
	 */
	private static String getCreateFieldSql(final Field field) {
		final StringBuilder objSql = new StringBuilder();

		if (null != field && null != field.getName() && !TextUtils.isEmpty(field.getName())) {
			objSql.append(field.getName());
			objSql.append(' ');
			objSql.append(field.getType().getDataName());

			if (field.isPrimary()) {
				objSql.append(' ');
				objSql.append(PRIMARY_KEY);
				objSql.append(' ');
				objSql.append(AUTOINCREMENT);
			} else {
				if (!field.isNull()) {
					objSql.append(' ');
					objSql.append(NOT_NULL);
				}
				if (field.isUnique()) {
					objSql.append(' ');
					objSql.append(UNIQUE);
				}
				if (!TextUtils.isEmpty(field.getDefaultValue())) {
					objSql.append(' ');
					objSql.append(DEFAULT);
					objSql.append('(');

					switch (field.getType()) {
						case INTEGER:
						case REAL:
							objSql.append(field.getDefaultValue());
							break;

						default:
							objSql.append('\'');
							objSql.append(field.getDefaultValue());
							objSql.append('\'');
							break;
					}

					objSql.append(')');
				}
			}
		}

		return objSql.toString();
	}


	/**
	 * Gets a string with SQL of fields.
	 * @return String with SQL of fields.
	 */
	private static String getFieldsCreateSql(final FieldSet set) {
		final StringBuilder objSql = new StringBuilder();

		if (null != set) {
			for (Field field : set.getFields().values()) {
				objSql.append(getCreateFieldSql(field));
				objSql.append(',');
			}
	
			objSql.setLength(objSql.length() - 1);
		}

		return objSql.toString();
	}


	/**
	 * Get a string with the names of declared fields.
	 * @return String with the names of declared fields.
	 */
	public static String getFieldsNamesSql(final FieldSet set) {
		final StringBuilder objSql = new StringBuilder();

		for (Field field : set.getFields().values()) {
			objSql.append(field.getName());
			objSql.append(',');
		}

		objSql.setLength(objSql.length() - 1);

		return objSql.toString();
	}
}
