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
package org.eclipse.emf.emfstore.server.accesscontrol.authentication.factory;

import java.util.Properties;

import org.eclipse.emf.emfstore.server.ServerConfiguration;
import org.eclipse.emf.emfstore.server.accesscontrol.authentication.AbstractAuthenticationControl;
import org.eclipse.emf.emfstore.server.accesscontrol.authentication.LDAPVerifier;
import org.eclipse.emf.emfstore.server.accesscontrol.authentication.SimplePropertyFileVerifier;
import org.eclipse.emf.emfstore.server.accesscontrol.authentication.VerifierChain;
import org.eclipse.emf.emfstore.server.exceptions.FatalEmfStoreException;
import org.eclipse.emf.emfstore.server.exceptions.InvalidPropertyException;

/**
 * Default authentication control factory.
 * 
 * @author wesendon
 */
public class AuthenticationControlFactoryImpl implements AuthenticationControlFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.server.accesscontrol.authentication.factory.AuthenticationControlFactory#createAuthenticationControl()
	 */
	public AbstractAuthenticationControl createAuthenticationControl() throws FatalEmfStoreException {

		// under construction
		// this factory reproduces the legacy behavior of unicase until new config is implemented

		String property = ServerConfiguration.getProperties().getProperty(ServerConfiguration.AUTHENTICATION_POLICY,
			ServerConfiguration.AUTHENTICATION_POLICY_DEFAULT);

		if (property.equals(ServerConfiguration.AUTHENTICATION_LDAP)) {
			VerifierChain chain = new VerifierChain();
			Properties properties = ServerConfiguration.getProperties();
			int count = 1;
			while (count != -1) {

				String ldapUrl = properties.getProperty(ServerConfiguration.AUTHENTICATION_LDAP_PREFIX + "." + count
					+ "." + ServerConfiguration.AUTHENTICATION_LDAP_URL);
				String ldapBase = properties.getProperty(ServerConfiguration.AUTHENTICATION_LDAP_PREFIX + "." + count
					+ "." + ServerConfiguration.AUTHENTICATION_LDAP_BASE);
				String searchDn = properties.getProperty(ServerConfiguration.AUTHENTICATION_LDAP_PREFIX + "." + count
					+ "." + ServerConfiguration.AUTHENTICATION_LDAP_SEARCHDN);
				String authUser = properties.getProperty(ServerConfiguration.AUTHENTICATION_LDAP_PREFIX + "." + count
					+ "." + ServerConfiguration.AUTHENTICATION_LDAP_AUTHUSER);
				String authPassword = properties.getProperty(ServerConfiguration.AUTHENTICATION_LDAP_PREFIX + "."
					+ count + "." + ServerConfiguration.AUTHENTICATION_LDAP_AUTHPASS);

				if (ldapUrl != null && ldapBase != null && searchDn != null) {
					LDAPVerifier ldapVerifier = new LDAPVerifier(ldapUrl, ldapBase, searchDn, authUser, authPassword);
					chain.getVerifier().add(ldapVerifier);
					count++;
				} else {
					count = -1;
				}
			}

			return chain;

		} else if (property.equals(ServerConfiguration.AUTHENTICATION_SPFV)) {

			return new SimplePropertyFileVerifier(ServerConfiguration.getProperties().getProperty(
				ServerConfiguration.AUTHENTICATION_SPFV_FILEPATH, ServerConfiguration.getDefaultSPFVFilePath()));

		} else {
			throw new InvalidPropertyException();
		}
	}

}
