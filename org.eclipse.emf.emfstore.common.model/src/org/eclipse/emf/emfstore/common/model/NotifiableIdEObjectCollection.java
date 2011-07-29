/**
 * <copyright> Copyright (c) 2008-2009 Jonas Helming, Maximilian Koegel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html </copyright>
 */
package org.eclipse.emf.emfstore.common.model;

import org.eclipse.emf.emfstore.common.model.util.EObjectChangeObserver;

/**
 * @author emueller
 * 
 */
public interface NotifiableIdEObjectCollection extends IdEObjectCollection,
		EObjectChangeObserver {

}
