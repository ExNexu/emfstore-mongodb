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
package org.eclipse.emf.emfstore.server.model.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.common.model.ModelElementId;
import org.eclipse.emf.emfstore.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;

/**
 * Class for managing customLabelProvider for specified operations.
 * 
 * @author Michael Kagel
 * @author emueller
 */
public class CustomOperationLabelProviderManager {

	private static List<AbstractOperationCustomLabelProvider> list;
	private final Map<ModelElementId, EObject> modelElementMap;

	/**
	 * Constructor.
	 * 
	 * @param modelElementMap a model element/ID mapping that must
	 *            contain all the model elements and their IDs that are involved with this operation.
	 */
	public CustomOperationLabelProviderManager(Map<ModelElementId, EObject> modelElementMap) {
		this.modelElementMap = modelElementMap;
		if (list == null) {
			initExtensions();
		}
	}

	/**
	 * Provides a customLabelProvider for a specified operation.
	 * 
	 * @param operation for which the method should provide a customLabelProvider.
	 * @return The customLabelProvider for the given operation or null if there is no customLabelProvider.
	 */
	public AbstractOperationCustomLabelProvider getCustomLabelProvider(AbstractOperation operation) {

		AbstractOperationCustomLabelProvider highestVisualizer = null;

		int highestNumber = 0;
		int currentRenderState = 0;

		for (AbstractOperationCustomLabelProvider changePackageVisualizer : list) {
			currentRenderState = changePackageVisualizer.canRender(operation);
			// Take the highest provider
			if (currentRenderState > highestNumber) {
				highestNumber = currentRenderState;
				highestVisualizer = changePackageVisualizer;
			}
		}

		return highestVisualizer;
	}

	private void initExtensions() {
		IConfigurationElement[] attributecontrols = Platform.getExtensionRegistry().getConfigurationElementsFor(
			"org.eclipse.emf.emfstore.server.model.edit.customOperationLabelProvider");
		list = new ArrayList<AbstractOperationCustomLabelProvider>();
		for (IConfigurationElement extension : attributecontrols) {
			try {
				AbstractOperationCustomLabelProvider provider = (AbstractOperationCustomLabelProvider) extension
					.createExecutableExtension("class");
				provider.setModelElementMap(modelElementMap);
				list.add(provider);
			} catch (CoreException exception) {
				ModelUtil.logException("Exception occured while initializing custom label provider extensions!",
					exception);
			}
		}
	}
}
