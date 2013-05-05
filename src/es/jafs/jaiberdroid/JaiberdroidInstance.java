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
	public static final String LOG_TAG = "jaiberdroid";

	/** Name of field that stores Jaiberdroid Entities list. */
	public static final String CFG_ENTITIES = "jaiberdroid_entities";
	/** Name of field that stores Jaiberdroid database name. */
	public static final String CFG_DB_NAME = "jaiberdroid_database";
	/** Name of field that stores Jaiberdroid database version. */
	public static final String CFG_DB_VERSION = "jaiberdroid_version";

	/** Type of data for arrays. */
	private static final String DATA_ARRAY = "array";
	/** Type of data for strings. */
	private static final String DATA_STRING = "string";

	/** Unique instance of the class. */ 
	private static JaiberdroidInstance instance;
	/** Boolean value that sets if instance if started. */
	private static boolean created = false;

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
	 * @return
	 * @throws Exception 
	 */
	public static JaiberdroidInstance getInstance() throws IllegalAccessException {
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
	public void startJaiberdroid() throws JaiberdroidException {
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
	public void stopJaiberdroid() {
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

		queryManager = new QueryManager(context, entityManager);
	}


	/**
	 * Load the system entities to be used.
	 * @param  resource  Id of resource for load entities.
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
	 * @return Boolean value that it's true when all it's ok.
	 * @throws JaiberdroidException 
	 */
	private void loadVersion() throws JaiberdroidException {
		try {
			QueryManager.setVersion(Integer.parseInt(context.getResources().getString(
																context.getResources().getIdentifier(
																				CFG_DB_VERSION,
																				DATA_STRING,
																				context.getPackageName()))));
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_DB_VERSION + " doesn't exits");
		} catch (final NumberFormatException e) {
			throw new JaiberdroidException("Invalid database version number: " + e.getMessage());
		}
	}


	/**
	 * Loads the database's name.
	 * @return Boolean value that it's true when all it's ok.
	 * @throws JaiberdroidException 
	 */
	private void loadName() throws JaiberdroidException {
		try {
			final String database = context.getResources().getString(context.getResources().getIdentifier(
																				CFG_DB_NAME,
																				DATA_STRING,
																				context.getPackageName()));
			if (null == database && TextUtils.isEmpty(database)) {
				throw new JaiberdroidException("Database name can't be empty");
			} else {
				QueryManager.setName(database);
			}
		} catch (final NotFoundException e) {
			throw new JaiberdroidException("The resource " + CFG_DB_NAME + " doesn't exits");
		}
	}


	/**
	 * Gets the Query Manager.
	 * @return Instance of the Query Manager.
	 */
	QueryManager getQueryManager() {
		return queryManager;
	}


	/**
	 * Gets the Entity Manager.
	 * @return Instance of the Entity Manager.
	 */
	EntityManager getEntityManager() {
		return entityManager;
	}


	/**
	 * Gets an entity with it's type.
	 * @param  type  Type of entity to search.
	 * @return Entity finded or null when error.
	 * @throws JaiberdroidException 
	 */
	@SuppressWarnings("rawtypes")
	static Entity getEntity(final Class type) throws JaiberdroidException {
		Entity entity = null;

		try {
			entity = getInstance().getEntityManager().getEntity(type);
		} catch (final IllegalAccessException e) {
			throw new JaiberdroidException(e.getMessage());
		}

		return entity;
	}
}
