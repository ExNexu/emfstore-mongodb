/*******************************************************************************
 * Copyright (c) 2008-2012 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.emfstore.common.resourcefactory.mongodb;

import org.eclipse.osgi.util.NLS;
/**
 * This class helps accessing the properties of this plugin.
 * 
 * @author Stefan Bleibinhaus
 * @author Tobias Verhoeven
 *
 */
public final class MongoDBResourceConfiguration extends NLS {
	
	/** The Constant BUNDLE_NAME. */
	private static final String BUNDLE_NAME = "org.eclipse.emf.emfstore.common.resourcefactory.mongodb.mongodb"; //$NON-NLS-1$
	
	/** the MongoDBResourceURI loaded from the properties file. */
	private static String mongoDBResourceURI;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MongoDBResourceConfiguration.class);
	}

	/**
	 * do not instantiate this class.
	 */
	private MongoDBResourceConfiguration() {
	}

	/**
	 * Gets the mongo db resource uri.
	 *
	 * @return the mongo db resource uri
	 */
	public static String getMongoDBResourceURI() {
		return mongoDBResourceURI;
	}

	/**
	 * Sets the mongo db resource uri.
	 *
	 * @param mongoDBResourceURI the new mongo db resource uri
	 */
	public static void setMongoDBResourceURI(String mongoDBResourceURI) {
		MongoDBResourceConfiguration.mongoDBResourceURI = mongoDBResourceURI;
	}
}
