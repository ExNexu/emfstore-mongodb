/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Maximilian Koegel
 ******************************************************************************/
package org.eclipse.emf.emfstore.common;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Implements a log for EMFStore Common.
 * 
 * @author koegel
 * 
 */
public class LogAdapter implements ILog {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.ILog#log(java.lang.String, java.lang.Exception, int)
	 */
	public void log(String message, Exception exception, int statusInt) {
		Status status = new Status(statusInt, Activator.getDefault().getBundle().getSymbolicName(), statusInt, message,
			exception);
		Activator.getDefault().getLog().log(status);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.ILog#logException(java.lang.String, java.lang.Exception)
	 */
	public void logException(String message, Exception e) {
		log(message, e, IStatus.ERROR);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.ILog#logWarning(java.lang.String, java.lang.Exception)
	 */
	public void logWarning(String message, Exception e) {
		log(message, e, IStatus.WARNING);
	}

}
