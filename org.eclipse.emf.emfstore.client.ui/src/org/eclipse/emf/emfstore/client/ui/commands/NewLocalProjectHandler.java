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
package org.eclipse.emf.emfstore.client.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.emfstore.client.ui.views.emfstorebrowser.views.CreateProjectDialog;
import org.eclipse.ui.PlatformUI;

/**
 * This is a handler for new local project command. It shows new project dialog
 * and the user can create a new project with out being logged in to server.
 * 
 * @author hodaie
 * 
 */
public class NewLocalProjectHandler extends AbstractHandler {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		CreateProjectDialog dialog = new CreateProjectDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
			null);

		dialog.open();

		return null;
	}

}
