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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.emfstore.common.EMFStoreResource;
import org.eclipselabs.mongo.emf.MongoURIHandlerImpl;

/**
 * A MongoDBResource.
 * 
 * @author Stefan Bleibinhaus
 * @author Tobias Verhoeven
 */
public class MongoDBResource extends EMFStoreResource {

	/** The uri handler. */
	private static org.eclipse.emf.ecore.resource.URIHandler uriHandler;

	/**
	 * Instantiates a new mongo db resource.
	 * 
	 * @param uri the uri
	 */
	public MongoDBResource(URI uri) {
		super(uri);
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#getURIConverter()
	 */
	@Override
	protected URIConverter getURIConverter() {
		URIConverter result = getDefaultURIConverter();
		if (uriHandler == null) {
			uriHandler = new MongoURIHandlerImpl();
			EList<org.eclipse.emf.ecore.resource.URIHandler> uriHandlers = result.getURIHandlers();
			uriHandlers.add(0, uriHandler);
		}
		return result;
	}

}
