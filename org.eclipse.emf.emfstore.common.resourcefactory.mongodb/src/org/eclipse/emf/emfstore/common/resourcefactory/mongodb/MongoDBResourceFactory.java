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

import java.util.regex.Pattern;

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

	private static final String MONGO_PROTOCOL = "mongo://";
	private static final Pattern ILLEGAL_CHARACTER_PATTERN = Pattern.compile("\\.|/");
	private static final String REPLACEMENT_STRING = "|";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Resource createResource(URI uri) {
		if (!uri.toString().startsWith(MONGO_PROTOCOL)) {
			uri = URI.createURI(MongoDBResourceConfiguration.mongoDBResourceURI
				+ ILLEGAL_CHARACTER_PATTERN.matcher(uri.devicePath()).replaceAll(REPLACEMENT_STRING));
		}
		return new MongoDBResource(uri);
	}

}