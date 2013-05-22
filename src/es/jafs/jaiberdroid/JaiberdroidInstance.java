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
 * @mainpage Jaiberdroid 0.5 beta
 * <p>Jaiberdroid is an Object-relational mapping (ORM) library for Android apps that uses SQLite. This allows
 * that with only a few lines of code, we have a complete funcionality for working with databases.</p>
 * <h2>Adding Jaiberdroid to your project</h2>
 * <p>There are several methods to add the Jaiberdroid library to your project. The first one is heavier as it
 * requires downloading the entire project and link. The second is more simple because you only have to add
 * jaiberdroid.x.x.jar file.</p>
 * <h3>Method 1: Link the project</h3>
 * <h4>Eclipse</h4>
 * <ol><li>Once downloaded Jaiberdroid project, will be imported to the environment. </li>
 * <li>Now in your project, open its properties (right click on the project, <b>Settings</b> option). </li>
 * <li>On the <b>Android</b> tab, section <b>Library</b>, click the <b>Add...</b> button and select your
 * project.</li></ol>
 * <blockquote><b>Note</b>: has certainty that there are problems with the link library projects if they are
 * not in the same directory, so it's good to have your project and Jaiberdroid in the same
 * directory.</blockquote>
 * <h3>Method 2: Include .jar library file</h3>
 * <ol>
 * <li>Inside your project, check if there is a directory called <b>libs</b>. If not, you must create it.</li>
 * <li>Now just have to copy the file jaiberdroid.x.x.jar to that directory.</li>
 * </ol>
 * <h2>Configuration</h2>
 * <p>Jaiberdroid configuration requires three parameters to work, these parameters are defined in a
 * <b>xml</b> file inside the folder <b>values</b>. Let us see them quickly:</p>
 * <ul><li><b>jaiberdroid_entities</b>: array of strings (string-array) in which each element defines an
 * entity class to use. It must specify its package and its name.</li>
 * <li><b>jaiberdroid_database</b>: string that stores the name of the database.</li>
 * <li><b>jaiberdroid_version</b>: string that contains the version number of the database.</li>
 * <li><b>jaiberdroid_debug</b>: boolean value (<b>true</b> or <b>false</b>) that indicates if all SQL queries
 * and other debug traces are written in system Log. By default, if this field not exists, debug mode is set
 * to false.</li></ul>
 * <p>Below is a sample configuration file:</p>
 * <blockquote>
 * <pre>
 * &lt;?xml version="1.0" encoding="utf-8"?&gt;
 * &lt;resources&gt;
 *   &lt;string-array name="jaiberdroid_entities"&gt;
 *     &lt;item&gt;paquete.de.clase.Entity1&lt;/item&gt;
 *     &lt;item&gt;paquete.de.clase.Entity2&lt;/item&gt;
 *     &lt;item&gt;paquete.de.clase.EntitydN&lt;/item&gt;
 *   &lt;/string-array&gt;
 *   &lt;string name="jaiberdroid_database"&gt;my_database&lt;/string&gt;
 *   &lt;string name="jaiberdroid_version"&gt;1&lt;/string&gt;
 *   &lt;string name="jaiberdroid_debug"&gt;true&lt;/string&gt;
 * &lt;/resources&gt;
 * </pre></blockquote>
 * <h2>Using Jaiberdroid</h2>
 * <h3>Jaiberdroid load</h3>
 * <p>To start the library Jaiberdroid you only have to run two methods:</p>
 * <blockquote>
 * <pre>JaiberdroidInstance.createInstance(this);
 * JaiberdroidInstance.start();</pre>
 * </blockquote>
 * <p>In the first statement, we indicate that the library start. It will proceed to initialize the
 * system.</p>
 * <p>The second command gets the entities defined in the parameter <b>jaiberdroid_entities</b> and analyzes
 * its structure. Returns a JaiberdroidException in case a problem occurs with the load. This will also gets
 * the version and name of the database and create it.</p>
 * <p>When you finish using the library, it would be recommended to execute the following method:</p>
 * <blockquote><pre>JaiberdroidInstance.stop();</pre></blockquote>
 * <p>This method will stop the library and free the memory used. After that, you can not re-use the library,
 * so it should always be called in the application shutdown.</p>
 * <h3>Entity Classes</h3>
 * <p>Entity classes in Jaiberdroid are classes that define a database table. For an entity class, so just add
 * the tag <b>@@Table</b>. This label will indicate the nature of the class to Jaiberdroid.</p>
 * <p>Next, add the tag <b>@@Column</b> to each attribute that represents a database column. Is mandatory to
 * create an attribute of type <b>int</b> called <b>_id</b>, as all tables in Android SQLite use that
 * identifier as <b>Primary Key</b>.</p>
 * <p>Finally, we must have methods <b>get</b> and <b>set</b> for each attribute mapped. So that Jaiberdroid
 * can execute them when read and assign the values of the attributes.</p>
 * <blockquote><pre>
 * @@Table
 * public class File {
 *   @@Column(primary = true, nullable = false)
 *   private int _id;
 *   @@Column(nullable = false, unique = true)
 *   private String name;
 *   @@Column(index = true)
 *   private String path;
 *   @@Column()
 *   private Long date;
 *   @@Column(nullable = false, index = true, ascOrder = false)
 *   private int version;
 *
 *   public final int get_id() {
 *     return _id;
 *   }
 *   public final void set_id(int _id) {
 *     this._id = _id;
 *   }
 *   public final String getName() {
 *     return name;
 *   }
 *   public final void setName(String name) {
 *     this.name = name;
 *   }
 *   public final String getPath() {
 *     return path;
 *   }
 *   public final void setPath(String path) {
 *     this.path = path;
 *   }
 *   public final Long getDate() {
 *     return date;
 *   }
 *   public final void setDate(Long date) {
 *     this.date = date;
 *   }
 *   public final int getVersion() {
 *     return version;
 *   }
 *   public final void setVersion(int version) {
 *     this.version = version;
 *   }
 * }
 * </pre></blockquote>
 * <p>In the example above, you can see how you can specify parameters for the labels <b>@@Column</b> to
 * better define each attribute.</p>
 * <h4>Column Tags</h4>
 * <p>All entity fields in a Entity Class must be indicated by using @@Colum tag. If a field not has this tag,
 * will be ignored by Jaiberdroid.
 * <ul><li><b>primary</b>: boolean value that indicates if the field is primary key. The field that has this
 * tag, must be called _id, of type int. Default <i>false</i>.</li>
 * <li><b>unique</b>: boolean value that indicates if the field has unique key. Default <i>false</i>.</li>
 * <li><b>nullable</b>: boolean value that indicates if the field can store null values. Only objects can be
 * null. You can use primitive types when nullable is false. Default <i>true</i>.</li>
 * <li><b>defaultValue</b>: string that indicates default value for current column. Values are always added as
 * strings. Default <i>""</i> (empty string).</li>
 * <li><b>index</b>: boolean value that indicates if current column is an index. Jaiberdroid creates an index
 * for this field called <i>index_tablename_fieldname</i>.</li>
 * <li><b>ascOrder</b>: boolean value that indicates order in index. If it's <i>true</i> order will be
 * ascending, in other case order will be descending. Default <i>true</i>.</li></ul>
 * <h3>Queries</h3>
 * <p>To request data, we will use classes called Queries, forgive the redundancy. You can use those classes
 * or instantiate the class GenericQuery. In instantiation of classes, a parameter is defined that will be the
 * entity class to use.</p>
 * <p>This depends on whether we want to create our own queries, or just going to use the default queries.</p>
 * <h4>GenericQuery inheritance example</h4>
 * <h5>FileQuery.java file</h5>
 * <blockquote><pre>public class FileQuery extends GenericQuery&lt;File&gt; {
 *   ...
 * }</pre></blockquote>
 * <h5>MainActivity.java file</h5>
 * <blockquote><pre>final FileQuery query = new FileQuery();
 *  * ...</pre></blockquote>
 * <h4>GenericQuery direct inheritance example</h4>
 * <blockquote><pre>final GenericQuery query = new GenericQuery&lt;File&gt;;
 * ...</pre></blockquote>
 * <h3>Default queries</h3>
 * <p>When creating a query, we have access to a series of default queries. Such queries are:</p>
 * <table><tr>
 * <th>Method</th><th>Parameter</th><th>Return</th><th>Description</th>
 * </tr><tr>
 * <td><b>count()</b></td>
 * <td><em>-</em></td>
 * <td><em>long</em></td>
 * <td>Returns a long value with the number of rows.</td>
 * </tr><tr>
 * <td><b>exists()</b></td>
 * <td><em>int id</em></td>
 * <td><em>boolean</em></td>
 * <td>Returns a boolean value that will be true when the id received exits.</td>
 * </tr><tr>
 * <td><b>findByPk()</b></td>
 * <td><em>int id</em></td>
 * <td><em>Object</em></td>
 * <td>Returns an object of the type of entity class with data of row which id has been received.</td>
 * </tr><tr>
 * <td><b>insert()</b></td>
 * <td><em>Object row</em></td>
 * <td><em>boolean</em></td>
 * <td>Stores in database the object received as parameter. Returns a boolean value that indicates if the
 * insert was right, and also updates the id in the object.</td>
 * </tr><tr>
 * <td><b>remove()</b></td>
 * <td><em>int id</em></td>
 * <td><em>boolean</em></td>
 * <td>Deletes the row which id receives as parameter, returns if row has been deleted.</td>
 * </tr><tr>
 * <td><b>removeAll()</b></td>
 * <td><em>-</em></td>
 * <td><em>long</em></td>
 * <td>Delete all rows in the table. Returns a long value that indicates the number of rows deleted.</td>
 * </tr><tr>
 * <td><b>update()</b></td>
 * <td><em>Object row</em></td>
 * <td><em>boolean</em></td>
 * <td>Updates entity object received. Returns a boolean value that indicates if the update query ends
 * successfully.</td>
 * </tr></table>
 */


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
	 * Retuns a boolean value that indicates if Jaiberdroid is in debug mode.
	 * @return Boolean value that indicates if Jaiberdroid is in debug mode.
	 */
	public static boolean isDebug() {
		return debug;
	}
}
