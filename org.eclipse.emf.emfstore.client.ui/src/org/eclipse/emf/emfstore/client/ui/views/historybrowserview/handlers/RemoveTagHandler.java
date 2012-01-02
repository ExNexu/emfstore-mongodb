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
package org.eclipse.emf.emfstore.client.ui.views.historybrowserview.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecp.common.util.DialogHandler;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.ui.commands.ServerRequestCommandHandler;
import org.eclipse.emf.emfstore.client.ui.views.historybrowserview.HistoryBrowserView;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.versioning.HistoryInfo;
import org.eclipse.emf.emfstore.server.model.versioning.PrimaryVersionSpec;
import org.eclipse.emf.emfstore.server.model.versioning.TagVersionSpec;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * CheckoutHandler for creating a new repository.
 * 
 * @author Shterev
 */
public class RemoveTagHandler extends AbstractHistoryViewHandler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object run(ExecutionEvent event, HistoryBrowserView view, TreeNode node) {

		final LabelProvider tagLabelProvider = new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((TagVersionSpec) element).getName();
			}
		};

		HistoryInfo historyInfo = (HistoryInfo) node.getValue();

		ElementListSelectionDialog dlg = new ElementListSelectionDialog(PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getShell(), tagLabelProvider);
		dlg.setElements(historyInfo.getTagSpecs().toArray());
		dlg.setTitle("Tag selection");
		dlg.setBlockOnOpen(true);
		dlg.setMultipleSelection(true);
		int ret = dlg.open();
		if (ret != Window.OK) {
			return null;
		}
		Object[] tags = dlg.getResult();
		for (Object tag : tags) {
			removeTag(view.getProjectSpace(), historyInfo.getPrimerySpec(), (TagVersionSpec) tag);
		}
		view.refresh();

		return null;
	}

	/**
	 * Removes a tag to a version.
	 * 
	 * @param versionSpec the version
	 * @param tag the tag
	 */
	private void removeTag(final ProjectSpace projectSpace, final PrimaryVersionSpec versionSpec,
		final TagVersionSpec tag) {

		ServerRequestCommandHandler handler = new ServerRequestCommandHandler() {

			@Override
			protected Object run() throws EmfStoreException {
				projectSpace.removeTag(versionSpec, tag);
				return null;
			}

			@Override
			public String getTaskTitle() {
				return "Resolving project versions...";
			}
		};
		try {
			handler.execute(new ExecutionEvent());
		} catch (ExecutionException e) {
			DialogHandler.showErrorDialog(e.getMessage());
		}

	}
}
