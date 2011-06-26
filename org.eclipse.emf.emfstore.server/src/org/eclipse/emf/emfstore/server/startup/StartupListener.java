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

import java.util.List;

import org.eclipse.emf.emfstore.server.model.ProjectHistory;

/**
 * Interface for the startup listener extension point.
 * 
 * @author wesendon
 */
public interface StartupListener {

	/**
	 * This method will be called on startup and returns the projects on the server. Caution: The list of projects is NO
	 * copy.
	 * 
	 * @param projects projects.
	 */
	void startedUp(List<ProjectHistory> projects);
}
