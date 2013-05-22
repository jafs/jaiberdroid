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
