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
package es.jafs.jaiberdroid.utils;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.util.Log;
import es.jafs.jaiberdroid.JaiberdroidException;
import es.jafs.jaiberdroid.JaiberdroidInstance;

/**
 * Loads the configuration of Jaiberdroid from current application context.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
public class ContextLoader {
	/** Name of field that stores Jaiberdroid Entities list. */
	private static final String CFG_ENTITIES = "jaiberdroid_entities";
	/** Name of field that stores Jaiberdroid database name. */
	private static final String CFG_DB_NAME = "jaiberdroid_database";
	/** Name of field that stores Jaiberdroid database version. */
	private static final String CFG_DB_VERSION = "jaiberdroid_version";
	/** Name of field that stores Jaiberdroid debug mode. */
	private static final String CFG_DEBUG = "jaiberdroid_debug";

	/** Type of data for arrays. */
	private static final String DATA_ARRAY = "array";
	/** Type of data for strings. */
	private static final String DATA_STRING = "string";

	/** Current instance. */
	private static ContextLoader instance;

	/** Application context. */
	private Context context;
	/** Database name. */
	private String database;
	/** Database version. */
	private int version;
	/** Indicates if Jaiberdroid is in debug mode. */
	private boolean debug = false;
	/** Array of loaded entities. */
	private String[] entities;


	/**
	 * Private constructor for avoid external instances.
	 */
	private ContextLoader() {
	}


	/**
	 * Creates an instance of Jaiberdroid.
	 * @param  context  Application context.
	 * @throws JaiberdroidException 
	 */
	public static void loadContext(final Context context) throws JaiberdroidException {
		getContext().context = context;
		getContext().load();
	}


	/**
	 * Gets the instance of ContextLoader.
	 * @return The current ContextLoader instance.
	 */
	public static ContextLoader getContext() {
		if (null == instance) {
			instance = new ContextLoader();
		}

		return instance;
	}

	/**
	 * Load the system config.
	 * @throws JaiberdroidException 
	 */
	private void load() throws JaiberdroidException {
		loadDebug();
		loadEntities();
		loadDatabase();
	}


	/**
	 * Loads the database.
	 * @throws JaiberdroidException 
	 */
	private void loadDatabase() throws JaiberdroidException {
		loadVersion();
		loadName();
	}


	/**
	 * Load the system entities to be used.
	 * @throws JaiberdroidException 
	 */
	private void loadEntities() throws JaiberdroidException {
		try {
			final int id = context.getResources().getIdentifier(CFG_ENTITIES, DATA_ARRAY,
																context.getPackageName());
			entities = context.getResources().getStringArray(id);
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_ENTITIES + " doesn't exits");
		}
	}


	/**
	 * Loads the database's version.
	 * @throws JaiberdroidException 
	 */
	private void loadVersion() throws JaiberdroidException {
		try {
			version = Integer.parseInt(context.getResources().getString(context.getResources().getIdentifier(
																				CFG_DB_VERSION,
																				DATA_STRING,
																				context.getPackageName())));
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_DB_VERSION + " doesn't exits");
		} catch (final NumberFormatException e) {
			throw new JaiberdroidException("Invalid database version number: " + e.getMessage());
		}
	}


	/**
	 * Loads the database's name.
	 * @throws JaiberdroidException 
	 */
	private void loadName() throws JaiberdroidException {
		try {
			database = context.getResources().getString(context.getResources().getIdentifier(
																				CFG_DB_NAME,
																				DATA_STRING,
																				context.getPackageName()));
			if (null == database && TextUtils.isEmpty(database)) {
				throw new JaiberdroidException("Database name can't be empty");
			}
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_DB_NAME + " doesn't exits");
		}
	}


	/**
	 * Loads the debug mode.
	 */
	private void loadDebug() {
		try {
			final String debugRes = context.getResources().getString(context.getResources().getIdentifier(
																				CFG_DEBUG,
																				DATA_STRING,
																				context.getPackageName()));
			if (null != debugRes && !TextUtils.isEmpty(debugRes)) {
				debug = (Boolean.TRUE.toString().equalsIgnoreCase(debugRes));
			}
		} catch (final NotFoundException e) {
			Log.i(JaiberdroidInstance.LOG_TAG, "Debug configuration parameter not found. Debug mode is off");
		}
	}


	/**
	 * Retuns a boolean value that indicates if Jaiberdroid is in debug mode.
	 * @return Boolean value that indicates if Jaiberdroid is in debug mode.
	 */
	public boolean isDebug() {
		return debug;
	}


	/**
	 * Returns a string with database name.
	 * @return String with database name.
	 */
	public String getDatabase() {
		return database;
	}


	/**
	 * Returns an integer value with database versión.
	 * @return Integer value with database versión.
	 */
	public int getVersion() {
		return version;
	}


	/**
	 * Returns an strings array with loaded entities names.
	 * @return Strings array with loaded entities names.
	 * */
	public String[] getEntities() {
		return entities;
	}
}
