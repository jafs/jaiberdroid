package es.jafs.jaiberdroid;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.lang.IllegalAccessException;

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
	static final String LOG_TAG = "jaiberdroid";

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
	private String database;
	private int version;


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
	 * @throws Exception 
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
	 * @throws IllegalAccessException 
	 * @throws JaiberdroidException 
	 */
	public static void start() throws IllegalAccessException, JaiberdroidException {
		getInstance().startJaiberdroid();
	}


	/**
	 * Starts the Jaiberdroid system.
	 * @throws JaiberdroidException 
	 */
	private void startJaiberdroid() throws JaiberdroidException {
		loadDebug();
		loadEntities();
		loadDatabase();
	}


	/**
	 * Stops the Jaiberdroid system.
	 * @throws IllegalAccessException 
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
	 * Loads the database.
	 * @throws JaiberdroidException 
	 */
	private void loadDatabase() throws JaiberdroidException {
		loadVersion();
		loadName();

		queryManager = new QueryManager(context, entityManager, version, database);
	}


	/**
	 * Load the system entities to be used.
	 * @throws JaiberdroidException 
	 */
	private void loadEntities() throws JaiberdroidException {
		try {
			final int id = context.getResources().getIdentifier(CFG_ENTITIES, DATA_ARRAY, context.getPackageName());

			final String[] entities = context.getResources().getStringArray(id);
			for (String entity : entities) {
				entityManager.add(Class.forName(entity));
			}
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_ENTITIES + " doesn't exits");
		} catch (final ClassNotFoundException e) {
			throw new JaiberdroidException("Class not found: " + e.getMessage());
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
			Log.i(LOG_TAG, "Debug configuration parameter not found. Debug mode is off");
		}
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
	 * @throws JaiberdroidException 
	 * @deprecated  This method will be deleted in 1.0 version.
	 * @todo        Delete in 1.0 version.
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
