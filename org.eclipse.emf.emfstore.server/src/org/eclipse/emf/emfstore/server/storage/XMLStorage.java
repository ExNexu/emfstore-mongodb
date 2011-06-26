/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.emfstore.server.storage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.emfstore.server.ServerConfiguration;
import org.eclipse.emf.emfstore.server.exceptions.FatalEmfStoreException;

/**
 * Implementation of a {@link ResourceStorage} backed by an XMLResource.
 * 
 * @author koegel
 */
public class XMLStorage implements ResourceStorage {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.server.storage.ResourceStorage#init(java.util.Properties)
	 */
	public URI init(Properties properties) throws FatalEmfStoreException {
		ResourceSet resourceSet = new ResourceSetImpl();
		String pathName = ServerConfiguration.getServerMainFile();
		URI fileURI = URI.createFileURI(pathName);
		File serverFile = new File(pathName);
		if (!serverFile.exists()) {
			try {
				Resource resource = resourceSet.createResource(fileURI);
				resource.save(null);
			} catch (IOException e) {
				throw new FatalEmfStoreException("Could not init XMLRessource", e);
			}
		}
		return fileURI;
	}
}
