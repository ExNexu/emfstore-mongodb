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
package org.eclipse.emf.emfstore.common;

import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.emfstore.common.extensionpoint.ExtensionElement;
import org.eclipse.emf.emfstore.common.extensionpoint.ExtensionPoint;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
/**
 * The Class ResourceFactoryRegistry.
 *
 * @author emueller
 */
public class ResourceFactoryRegistry extends XMIResourceFactoryImpl implements Resource.Factory.Registry {
	
	/** The factory. */
	private ResourceFactoryImpl factory;
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl#createResource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Resource createResource(URI uri) {
		return new EMFStoreResource(uri);
	}

	/** {@inheritDoc}*/
	public Factory getFactory(URI uri) {
		// The first time this method is called it will look for a registered resourcefactory-plugin, if there
		// is none the default XMI-Storage will be used.
		if (this.factory == null) {
			ExtensionElement element = new ExtensionPoint("org.eclipse.emf.emfstore.common.resourcefactory").getElementWithHighestPriority();
			if (element != null) {
				this.factory = element.getClass("class", ResourceFactoryImpl.class);
			}
			if (this.factory == null) {
				this.factory = this;
			}
		}
		return this.factory;
	}

	/** {@inheritDoc}*/
	public Factory getFactory(URI uri, String contentType) {
		return this.getFactory(null);
	}

	/** {@inheritDoc}*/
	public Map<String, Object> getProtocolToFactoryMap() {
		return null;
	}

	/** {@inheritDoc}*/
	public Map<String, Object> getExtensionToFactoryMap() {
		return null;
	}

	/** {@inheritDoc}*/
	public Map<String, Object> getContentTypeToFactoryMap() {
		return null;
	}
}