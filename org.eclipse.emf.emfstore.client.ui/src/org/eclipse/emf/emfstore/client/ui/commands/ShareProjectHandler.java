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

import java.util.Collection;

import org.eclipse.emf.ecp.common.util.DialogHandler;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.Usersession;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.accesscontrol.AccessControlHelper;
import org.eclipse.emf.emfstore.client.model.util.WorkspaceUtil;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * Share a project with the server.
 * 
 * @author koegel
 */
public class ShareProjectHandler extends ServerRequestCommandHandler {

	/**
	 * Default constructor.
	 */
	public ShareProjectHandler() {
		setTaskTitle("Sharing project...");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.client.ui.commands.ServerRequestHandler#initUsersession()
	 */
	@Override
	protected void initUsersession() {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		ElementListSelectionDialog dlg = new ElementListSelectionDialog(shell,
				new AdapterFactoryLabelProvider(adapterFactory));
		Workspace currentWorkspace = WorkspaceManager.getInstance()
				.getCurrentWorkspace();
		Collection<Usersession> allSessions = currentWorkspace
				.getUsersessions();
		dlg.setElements(allSessions.toArray());
		dlg.setTitle("Select Usersession");
		dlg.setBlockOnOpen(true);
		if (dlg.open() == Window.OK) {
			Object result = dlg.getFirstResult();
			if (result instanceof Usersession) {
				setUsersession((Usersession) result);
			}
		}
		adapterFactory.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	protected Object run() throws EmfStoreException {

		final ProjectSpace projectSpace = getProjectSpace();
		try {
			AccessControlHelper accessControlHelper = new AccessControlHelper(
					getUsersession());
			try {
				accessControlHelper.checkServerAdminAccess();
			} catch (org.eclipse.emf.emfstore.server.exceptions.AccessControlException e) {
				MessageDialog
						.openError(this.getShell(), "",
								"Only administrators can create new projects on the server.");
				setUsersession(null);
				return null;
			}
			createProject(projectSpace);
			// BEGIN SUPRESS CATCH EXCEPTION
		} catch (RuntimeException e) {
			DialogHandler.showExceptionDialog(e);
			WorkspaceUtil.logWarning("RuntimeException in "
					+ ShareProjectHandler.class.getName(), e);
			// throw e;
		}
		// END SUPRESS CATCH EXCEPTION
		setUsersession(null);
		return null;
	}

	/**
	 * @param projectSpace
	 * @throws EmfStoreException
	 */
	private void createProject(ProjectSpace projectSpace)
			throws EmfStoreException {
		projectSpace.shareProject(getUsersession());
		MessageDialog.openInformation(getShell(), null,
				"Your project was successfully shared!");
	}

}
