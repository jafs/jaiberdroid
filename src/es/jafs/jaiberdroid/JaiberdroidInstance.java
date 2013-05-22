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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import es.jafs.jaiberdroid.utils.ContextLoader;

/**
 * <p>Jaiberdroid main class. This must be instanciated to initialize the Jaiberdroid system.</p>
 * <p>First we must call to createInstance() method. This method receives the application context. In second,
 * place, we must call to start().</p>
 * <p>Example:</p>
 * <pre>
 *   JaiberdroidInstance.<b>createInstance(</b><i>mycontext</i><b>)</b>;
 *   JaiberdroidInstance.<b>start()</b>;
 * </pre>
 * 
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
public final class JaiberdroidInstance {
	/** Tag name for log. */
	public static final String LOG_TAG = "jaiberdroid";

	/** Unique instance of the class. */ 
	private static JaiberdroidInstance instance;
	/** Boolean value that sets if instance if started. */
	private static boolean created = false;
	/** Indicates if Jaiberdroid is in debug mode. */
	private static boolean debug = false;

	/** Reference to Entity Manager. */
	private final EntityManager entityManager = new EntityManager();

	/** Application context. */
	private Context context;
	/** Instance of Query Manager. */
	private QueryManager queryManager;


	/**
	 * Private constructor for avoid external instances.
	 */
	private JaiberdroidInstance() {
	}


	/**
	 * Creates an instance of Jaiberdroid.
	 * @param  context  Application context.
	 */
	public static void createInstance(final Context context) {
		created = true;

		try {
			getInstance().context = context;
		} catch (final IllegalAccessException e) {
			Log.e(LOG_TAG, "Problem when instanciate Jaiberdroid: " + e.getMessage());
		}
	}


	/**
	 * Gets the instance of Jaiberdroid.
	 * @return The current Jaiberdroid instance.
	 * @throws IllegalAccessException When gets instance without call first createInstance().
	 */
	static JaiberdroidInstance getInstance() throws IllegalAccessException {
		if (!created) {
			throw new IllegalAccessException("You must to initialize the instance");
		} else if (null == instance) {
			instance = new JaiberdroidInstance();
		}

		return instance;
	}


	/**
	 * Starts the Jaiberdroid system.
	 * @throws IllegalAccessException When call without first createInstance().
	 * @throws JaiberdroidException   When an exception occurs when initialize.
	 */
	public static void start() throws IllegalAccessException, JaiberdroidException {
		getInstance().startJaiberdroid();
	}


	/**
	 * Starts the Jaiberdroid system.
	 * @throws JaiberdroidException   When an exception occurs when initialize.
	 */
	private void startJaiberdroid() throws JaiberdroidException {
		ContextLoader.loadContext(context);

		// Load entities into entity manager.
		try {
			final String[] entities = ContextLoader.getContext().getEntities();
			for (String entity : entities) {
				entityManager.add(Class.forName(entity));
			}
		} catch (final ClassNotFoundException e) {
			throw new JaiberdroidException("Class not found: " + e.getMessage());
		}

		// Configures debug.
		debug = ContextLoader.getContext().isDebug();

		// Create new query manager.
		queryManager = new QueryManager(context, entityManager, ContextLoader.getContext().getVersion(),
										ContextLoader.getContext().getDatabase());
	}


	/**
	 * Stops the Jaiberdroid system.
	 * @throws IllegalAccessException When call without first createInstance().
	 */
	public static void stop() throws IllegalAccessException {
		getInstance().stopJaiberdroid();
	}


	/**
	 * Stops the Jaiberdroid system.
	 */
	private void stopJaiberdroid() {
		SQLiteDatabase.releaseMemory();
		entityManager.clear();
	}


	/**
	 * Gets the Query Manager.
	 * @return Instance of the Query Manager.
	 */
	static QueryManager getQueryManager() {
		try {
			return getInstance().queryManager;
		} catch (final IllegalAccessException e) {
			return null;
		}
	}


	/**
	 * Gets the Entity Manager.
	 * @return Instance of the Entity Manager.
	 */
	static EntityManager getEntityManager() {
		try {
			return getInstance().entityManager;
		} catch (final IllegalAccessException e) {
			return null;
		}
	}


	/**
	 * Gets an entity with it's type.
	 * @param  type  Type of entity to search.
	 * @return Entity finded or null when error.
	 * @throws JaiberdroidException When a problem appears in entity search.
	 * @deprecated  This method will be deleted in 1.0 version.
	 * @todo        Will be deleted in 1.0 version.
	 */
	@SuppressWarnings("rawtypes")
	static Entity getEntity(final Class type) throws JaiberdroidException {
		return getEntityManager().getEntity(type);
	}


	/**
	 * Retuns a boolean value that indicates if Jaiberdroid is in debug mode.
	 * @return Boolean value that indicates if Jaiberdroid is in debug mode.
	 */
	public static boolean isDebug() {
		return debug;
	}
}
