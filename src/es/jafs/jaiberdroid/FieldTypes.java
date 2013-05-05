package es.jafs.jaiberdroid;


/**
 * Data types of SQLite.
 * @author  Jose Antonio Fuentes Santiago
 * @version 0.5
 */
enum FieldTypes {
	/** The value is a NULL value. */
	NULL,
	/** The value is a signed integer, stored in 1-8 bytes depending on the magnitude of the value. */
	INTEGER,
	/** The value is a floating point value, stored as an 8-byte IEEE floating point number. */
	REAL,
	/** The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE). */
	TEXT
}
