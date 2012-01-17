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
package org.eclipse.emf.emfstore.client.ui.views.emfstorebrowser.provider;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.emfstore.client.model.ServerInfo;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.accesscontrol.AccessControlHelper;
import org.eclipse.emf.emfstore.client.model.provider.ModelItemProviderAdapterFactory;

/**
 * Content provider for the tree view.
 * 
 * @author shterev
 */
public class ESBrowserContentProvider extends AdapterFactoryContentProvider {

	private AccessControlHelper accessControl;

	/**
	 * Default constructor.
	 */
	public ESBrowserContentProvider() {
		super(new ModelItemProviderAdapterFactory());
	}

	/**
	 * Getter for the AccesscontrolHelper.
	 * 
	 * @return the AccesscontrolHelper
	 */
	public AccessControlHelper getAccesscontrolHelper() {
		return accessControl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getChildren(Object object) {
		Object value = object;
		Object[] children;

		if (value instanceof Workspace) {
			children = ((Workspace) value).getServerInfos().toArray();
		} else if (value instanceof ServerInfo) {
			ServerInfo serverInfo = (ServerInfo) value;
			children = getChildren(serverInfo);
		} else {
			children = super.getChildren(object);
		}

		return children;
	}

	private Object[] getChildren(ServerInfo serverInfo) {
		return serverInfo.getProjectInfos().toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof ServerInfo) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getElements(Object object) {
		return getChildren(object);
	}

}
