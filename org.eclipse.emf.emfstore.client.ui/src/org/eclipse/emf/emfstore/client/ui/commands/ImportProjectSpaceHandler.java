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

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.common.util.DialogHandler;
import org.eclipse.emf.ecp.common.util.PreferenceHelper;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.WorkspaceManager;
import org.eclipse.emf.emfstore.client.model.util.EMFStoreCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

/**
 * Imports a project space to the current workspace.
 * 
 * @author koegel
 */
public class ImportProjectSpaceHandler extends AbstractHandler {

	private static final String IMPORT_PROJECTSPACE_PATH = "org.eclipse.emf.emfstore.client.ui.importProjectSpacePath";

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		dialog.setFilterNames(ImportProjectHandler.FILTER_NAMES);
		dialog.setFilterExtensions(ImportProjectHandler.FILTER_EXTS);
		String initialPath = PreferenceHelper.getPreference(IMPORT_PROJECTSPACE_PATH, System.getProperty("user.home"));
		dialog.setFilterPath(initialPath);
		String fn = dialog.open();
		if (fn == null) {
			return null;
		}

		final File file = new File(dialog.getFilterPath(), dialog.getFileName());
		PreferenceHelper.setPreference(IMPORT_PROJECTSPACE_PATH, file.getParent());

		final ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getShell());

		new EMFStoreCommand() {
			@Override
			protected void doRun() {
				try {
					progressDialog.open();
					progressDialog.getProgressMonitor().beginTask("Export project...", 100);
					progressDialog.getProgressMonitor().worked(10);
					Workspace currentWorkspace = WorkspaceManager.getInstance().getCurrentWorkspace();
					currentWorkspace.importProjectSpace(file.getAbsolutePath());

				} catch (IOException e) {
					DialogHandler.showExceptionDialog(e);
				} finally {
					progressDialog.getProgressMonitor().done();
					progressDialog.close();
				}
			}
		}.run();

		MessageDialog.openInformation(null, "Import", "Imported project space from file: " + file.getAbsolutePath());
		return null;
	}

}
