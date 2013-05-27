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
