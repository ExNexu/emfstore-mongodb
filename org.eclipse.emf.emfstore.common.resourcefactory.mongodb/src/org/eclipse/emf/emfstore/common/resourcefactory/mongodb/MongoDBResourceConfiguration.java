package org.eclipse.emf.emfstore.common.resourcefactory.mongodb;

import org.eclipse.osgi.util.NLS;

/**
 * This class helps accessing the properties of this plugin.
 * 
 * @author Stefan Bleibinhaus
 * @author Tobias Verhoeven
 *
 */
public class MongoDBResourceConfiguration extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.emf.emfstore.common.resourcefactory.mongodb.mongodb"; //$NON-NLS-1$
	/**
	 * the MongoDBResourceURI loaded from the properties file
	 */
	public static String MongoDBResourceURI;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MongoDBResourceConfiguration.class);
	}

	/**
	 * do not instantiate this class
	 */
	private MongoDBResourceConfiguration() {
	}
}
