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
package org.eclipse.emf.emfstore.server.accesscontrol.authentication;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.emfstore.server.exceptions.AccessControlException;

/**
 * @author wesendon
 */
public class VerifierChain extends AbstractAuthenticationControl {

	private ArrayList<AbstractAuthenticationControl> verifier;

	/**
	 * Constructs an empty verifier chain.
	 */
	public VerifierChain() {
		super();
		verifier = new ArrayList<AbstractAuthenticationControl>();
	}

	/**
	 * Returns the list of verifier. can be used to add and remove verifier.
	 * 
	 * @return list of verifier
	 */
	public List<AbstractAuthenticationControl> getVerifier() {
		return verifier;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.server.accesscontrol.authentication.AbstractAuthenticationControl#verifyPassword(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	protected boolean verifyPassword(String username, String password) throws AccessControlException {
		for (AbstractAuthenticationControl verifier : this.verifier) {
			if (verifier.verifyPassword(username, password)) {
				return true;
			}
		}
		return false;
	}

}
