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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * A factory for creating MongoDBResource objects.
 * 
 * @author Stefan Bleibinhaus
 * @author Tobias Verhoeven
 */
public class MongoDBResourceFactory extends XMIResourceFactoryImpl {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Resource createResource(URI uri) {
		//TODO: load mongoDB-URL from a property file
		if (!uri.toString().startsWith("mongo://")) {
			uri = URI.createURI("mongo://127.0.0.1:27017/emf/objects/" + uri.devicePath().replace("/", "-"));
		}
		return new MongoDBResource(uri);
	}

}
