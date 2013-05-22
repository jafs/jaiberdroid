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

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Class that execute and control the querys.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 * @todo    This class must receive only one query method, and analyzes whats method call.
 */
final class QueryManager extends SQLiteOpenHelper {
	/** Log tag for SQL queries. */
	private static final String SQL_TAG = "sqlop";

	/** Instance of Entity Manager. */
	private EntityManager entityManager;


	/**
	 * Default constructor of the class.
	 * @param  context        Application context.
	 * @param  entityManager  Entity manager for persistence.
	 * @param  version        Database version.
	 * @param  name           Name of database.
	 * @throws JaiberdroidException 
	 */
	QueryManager(final Context context, final EntityManager entityManager, final int version,
				final String name) throws JaiberdroidException {
		super(context, name, null, version);
		this.entityManager = entityManager;
	}


	/**
	 * Called when the database is created for the first time.
	 * @param  database  The database
	 */
	@Override
	public void onCreate(final SQLiteDatabase database) {
		if (!executeUpdates(entityManager.getCreateQueries(), false, database)) {
			Log.e(JaiberdroidInstance.LOG_TAG, "Problem creating database.");
		}
	}


	/**
	 * Called when the database needs to be upgraded. This method executes within a transaction. If an
	 * exception is thrown, all changes will automatically be rolled back.
	 * @param  database    The database.
	 * @param  oldVersion  Old version id.
	 * @param  newVersion  New version id.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
		// The false value in if executeUpdates call, is because this method creates automatically a
		// transaction.
		if (!executeUpdates(entityManager.getDropQueries(), false, database)) {
			Log.e(JaiberdroidInstance.LOG_TAG, "Problem upgrading database.");
		}
		onCreate(database);
	}


	/**
	 * Executes an update with received query.
	 * @param  query  Query to execute.
	 * @return Number of rows affected. -1 if there an error.
	 * @throws JaiberdroidException 
	 */
	long executeUpdate(final Query query) throws JaiberdroidException {
		long rows = -1;

		try {
			final SQLiteDatabase database = getWritableDatabase();

			if (query.isTransactional()) {
				database.beginTransaction();
			}

			debugQuery(query);

			switch (query.getType()) {
				// Inserts a value into the database.
				case INSERT:
					// Returns the row id of inserted data.
					rows = (int) database.insert(query.getEntity().getTableName(), null, query.getValues());
					if (-1 != rows) {
						JaiberdroidReflection.executeSetMethod(JaiberdroidReflection.SET_ID, query.getObject(),
															int.class, (int) rows);
						rows = 1; // Affected 1 row.
					}
					break;

				// Updates existing values into database.
				case UPDATE:
					rows = database.update(query.getEntity().getTableName(), query.getValues(),
											query.getCondition(), query.getArgsArray());
					break;

				// Delete values of database.
				case DELETE:
					rows = database.delete(query.getEntity().getTableName(), query.getCondition(),
										query.getArgsArray());
					break;

				default:
					Log.e(JaiberdroidInstance.LOG_TAG, "Only Insert, Update, Delete are supported");
			}

			if (database.inTransaction()) {
				if (rows != -1) {
					database.setTransactionSuccessful();
				}

				database.endTransaction();
			}
		} catch (final SQLException e) {
			Log.e(JaiberdroidInstance.LOG_TAG, "When executing update: " + e.getMessage(), e);
			throw new JaiberdroidException("Executing SQL" + e.getMessage());
		}

		return rows;
	}


	/**
	 * Prints a debug trace for a query.
	 * @param  query  Query to print in debug.
	 */
	private static void debugQuery(final Query query) {
		if (JaiberdroidInstance.isDebug() && null != query) {
			final StringBuilder message = new StringBuilder();

			message.append(query.getType().name());
			message.append(" over ");
			message.append(query.getEntity().getTableName());
			message.append(" |");
			if (null != query.getValues() && query.getValues().size() > 0) {
				message.append(" values [");
				message.append(query.getValues());
				message.append("]");
			}
			if (!TextUtils.isEmpty(query.getCondition())) {
				message.append(" condition [");
				message.append(query.getCondition());
				message.append("]");
			}
			if (null != query.getArgs()) {
				message.append(" variables [ ");
				for (int i = 0; i < query.getArgs().size(); ++i) {
					message.append(query.getArgsArray());
					message.append(" ");
				}
				message.append("]");
			}

			Log.d(SQL_TAG, message.toString());
		}
	}


	/**
	 * Execute a query in database.
	 * @param  query        String with query to execute.
	 * @param  database     Database into execute queries.
	 * @return Object with results. Can be a List of String array or a single object.
	 */
	List<String[]> executeSql(final String query) throws SQLException {
		final List<String[]> result = new ArrayList<String[]>();

		try {
			// TODO analyze the query
			final SQLiteDatabase database = getWritableDatabase();

			if (JaiberdroidInstance.isDebug()) {
				Log.d(SQL_TAG, query);
			}
			final Cursor cursor = database.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				String[] row;

				do {
					row = new String[cursor.getColumnCount()];
					for (int j = 0; j < cursor.getColumnCount(); ++j) {
						row[j] = cursor.getString(j);
					}
					result.add(row);
				} while (cursor.moveToNext());
			}
		} catch (final SQLException e) {
			Log.e(JaiberdroidInstance.LOG_TAG, "Executing sql: " + e.getMessage(), e);
			throw e;
		}

		return result;
	}


	/**
	 * Execute a list of queries in database.
	 * @param  database     Database into execute queries.
	 * @param  queries      List of String with queries to execute.
	 * @param  transaction  Boolean value that sets if the queries are executed in transacction. 
	 */
	private boolean executeUpdates(final List<String> queries, final boolean transaction,
									final SQLiteDatabase database) {
		boolean ok = false;

		if (null != queries) {
			try {
				if (transaction) {
					if (JaiberdroidInstance.isDebug()) {
						Log.d(SQL_TAG, "BEGIN");
					}
					database.beginTransaction();
				}
	
				try {
					for (String query : queries) {
						if (JaiberdroidInstance.isDebug()) {
							Log.d(SQL_TAG, query);
						}
						database.execSQL(query);
					}
	
					if (transaction && database.inTransaction()) {
						if (JaiberdroidInstance.isDebug()) {
							Log.d(SQL_TAG, "COMMIT");
						}
						database.setTransactionSuccessful();
					}
				} catch (final SQLException e) {
					Log.e(JaiberdroidInstance.LOG_TAG, "When executing SQL: " + e.getMessage(), e);
				}
	
				if (transaction && database.inTransaction()) {
					if (JaiberdroidInstance.isDebug()) {
						Log.d(SQL_TAG, "END");
					}
					database.endTransaction();
				}

				ok = true;
			} catch (final SQLException e) {
				Log.e(JaiberdroidInstance.LOG_TAG, "Problem in update: " + e.getMessage(), e);
			}
		}

		return ok;
	}


	/**
	 * Executes a query that returns data of an entity.
	 * @param  query  Query to execute.
	 * @return List of results or null is there an error.
	 * @throws JaiberdroidException 
	 */
	List<Object> executeQueryEntity(final Query query) throws JaiberdroidException {
		List<Object> results = null;

		// Checks if query is SELECT type.
		if (Query.Type.SELECT.equals(query.getType())) {
			try {
				final SQLiteDatabase database = getWritableDatabase();

				final Cursor cursor = database.query(query.getEntity().getTableName(), query.getFields(),
													query.getCondition(), query.getArgsArray(), null, null,
													null);

				if (cursor.moveToFirst()) {
					results = new ArrayList<Object>();

					do {
						results.add(getObject(cursor, query.getEntity()));
					} while (cursor.moveToNext());
				}
				cursor.close();
			} catch (final SQLException e) {
				Log.e(JaiberdroidInstance.LOG_TAG, "When executing a query: " + e.getMessage(), e);
			}
		}

		return results;
	}


	/**
	 * Executes a query that returns data of an entity.
	 * @param  entity  Entity with table to count.
	 * @return List of results or null is there an error.
	 * @throws JaiberdroidException 
	 */
	long executeCountQuery(final Entity entity) throws JaiberdroidException {
		long count = 0;

		try {
			final SQLiteDatabase database = getWritableDatabase();

			Cursor mCount= database.rawQuery(JaiberdroidSql.getCountSql(entity.getTableName()), null);
			if (mCount.moveToFirst());
			count= mCount.getLong(0);
			mCount.close();
		} catch (final SQLException e) {
			Log.e(JaiberdroidInstance.LOG_TAG, "When executing a query: " + e.getMessage(), e);
		}

		return count;
	}


	/**
	 * Extracts object data from a cursor and an entity.
	 * @param  cursor  Cursor with results of a query.
	 * @param  entity  Entity to extract.
	 * @return Object of type of entity class.
	 * @throws JaiberdroidException When a problem occurs.
	 */
	@SuppressWarnings("rawtypes")
	private Object getObject(final Cursor cursor, final Entity entity) throws JaiberdroidException {
		Object result = null;

		if (null != cursor && cursor.getCount() > 0) {
			try {
				result = entity.getReferenced().newInstance();
				Class type;
				String name;
				int pos;

				for (String column : cursor.getColumnNames()) {
					type = entity.getFields().getFieldClass(column);
					name = JaiberdroidReflection.getMethodSet(column);
					pos = cursor.getColumnIndex(column);

					if (int.class.equals(type) || Integer.class.equals(type)) {
						JaiberdroidReflection.executeSetMethod(name, result, type, cursor.getInt(pos));
					} else if (long.class.equals(type) || Long.class.equals(type)) {
						JaiberdroidReflection.executeSetMethod(name, result, type, cursor.getLong(pos));
					} else if (String.class.equals(type)) {
						JaiberdroidReflection.executeSetMethod(name, result, type, cursor.getString(pos));
					} else if (float.class.getName().equals(type) || Float.class.getName().equals(type)) { 
						JaiberdroidReflection.executeSetMethod(name, result, type, cursor.getFloat(pos));
					} else if (double.class.getName().equals(type) || Double.class.getName().equals(type)) {
						JaiberdroidReflection.executeSetMethod(name, result, type, cursor.getDouble(pos));
					}
				}
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final InstantiationException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
