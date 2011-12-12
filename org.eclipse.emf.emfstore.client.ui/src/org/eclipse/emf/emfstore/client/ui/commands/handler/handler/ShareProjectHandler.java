package org.eclipse.emf.emfstore.client.ui.commands.handler.handler;

import org.eclipse.core.commands.IHandler;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.ui.commands.handler.AbstractEMFStoreHandler;
import org.eclipse.emf.emfstore.client.ui.commands.handler.RequiredSelectionException;
import org.eclipse.emf.emfstore.client.ui.commands.handler.controller.UIShareProjectController;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;

public class ShareProjectHandler extends AbstractEMFStoreHandler implements IHandler {

	@Override
	public void handle() {
		try {
			new UIShareProjectController(getShell()).share(requireSelection(ProjectSpace.class));
		} catch (RequiredSelectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmfStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
