package es.jafs.jaiberdroid;

/**
 * Class that control a exception in Sqlite.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
public final class JaiberdroidException extends Exception {
	/** Serial number of the class. */
	private static final long serialVersionUID = -3880174110511716737L;


	/**
	 * Default constructor.
	 * @param  message  String with message of exception.
	 */
	public JaiberdroidException(final String message) {
		super(message);
	}
}
