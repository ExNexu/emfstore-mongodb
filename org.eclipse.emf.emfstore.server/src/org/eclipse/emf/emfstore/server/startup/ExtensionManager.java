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
package org.eclipse.emf.emfstore.server.startup;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.emfstore.server.EmfStoreInterface;
import org.eclipse.emf.emfstore.server.accesscontrol.AccessControlImpl;
import org.eclipse.emf.emfstore.server.connection.ConnectionHandler;
import org.eclipse.emf.emfstore.server.model.ProjectHistory;
import org.eclipse.emf.emfstore.server.model.ServerSpace;

/**
 * This class calls the startup listeners, registered at the extension point.
 * 
 * @author wesendon
 */
public final class ExtensionManager {

	private ExtensionManager() {
	}

	/**
	 * Calls the startup listeners.
	 * 
	 * @param projects list of projects
	 */
	public static void notifyStartupListener(EList<ProjectHistory> projects) {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.emfstore.server.startuplistener");

		// get all providers from the extension points
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("class");
				if (o instanceof StartupListener) {
					((StartupListener) o).startedUp(projects);
				}
			} catch (CoreException e1) {
				// fail silently
				// e1.printStackTrace();
			}
		}
	}

	/**
	 * Notifies the post startup listener.
	 * 
	 * @param serverspace serverspace
	 * @param accessControl accessscontrol
	 * @param connectionHandlers set of connection handlers
	 */
	public static void notifyPostStartupListener(ServerSpace serverspace, AccessControlImpl accessControl,
		Set<ConnectionHandler<? extends EmfStoreInterface>> connectionHandlers) {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.emfstore.server.poststartuplistener");

		// get all providers from the extension points
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("class");
				if (o instanceof PostStartupListener) {
					((PostStartupListener) o).postStartUp(serverspace, accessControl, connectionHandlers);
				}
			} catch (CoreException e1) {
				// fail silently
				// e1.printStackTrace();
			}
		}
	}

}
