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
package org.eclipse.emf.emfstore.server.connection.xmlrpc;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.emfstore.server.AdminEmfStore;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.emfstore.server.model.ProjectId;
import org.eclipse.emf.emfstore.server.model.ProjectInfo;
import org.eclipse.emf.emfstore.server.model.SessionId;
import org.eclipse.emf.emfstore.server.model.accesscontrol.ACGroup;
import org.eclipse.emf.emfstore.server.model.accesscontrol.ACOrgUnit;
import org.eclipse.emf.emfstore.server.model.accesscontrol.ACOrgUnitId;
import org.eclipse.emf.emfstore.server.model.accesscontrol.ACUser;
import org.eclipse.emf.emfstore.server.model.accesscontrol.roles.Role;

/**
 * XML RPC connection interface for adminemfstore.
 * 
 * @author wesendon
 */
public class XmlRpcAdminEmfStoreImpl implements AdminEmfStore {

	private AdminEmfStore getAdminEmfStore() {
		return XmlRpcAdminConnectionHander.getAdminEmfStore();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addMember(SessionId sessionId, ACOrgUnitId group, ACOrgUnitId member) throws EmfStoreException {
		getAdminEmfStore().addMember(sessionId, group, member);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addParticipant(SessionId sessionId, ProjectId projectId, ACOrgUnitId participant)
		throws EmfStoreException {
		getAdminEmfStore().addParticipant(sessionId, projectId, participant);
	}

	/**
	 * {@inheritDoc}
	 */
	public void changeOrgUnit(SessionId sessionId, ACOrgUnitId orgUnitId, String name, String description)
		throws EmfStoreException {
		getAdminEmfStore().changeOrgUnit(sessionId, orgUnitId, name, description);
	}

	/**
	 * {@inheritDoc}
	 */
	public void changeRole(SessionId sessionId, ProjectId projectId, ACOrgUnitId orgUnit, EClass role)
		throws EmfStoreException {
		getAdminEmfStore().changeRole(sessionId, projectId, orgUnit, role);
	}

	/**
	 * {@inheritDoc}
	 */
	public ACOrgUnitId createGroup(SessionId sessionId, String name) throws EmfStoreException {
		return getAdminEmfStore().createGroup(sessionId, name);
	}

	/**
	 * {@inheritDoc}
	 */
	public ACOrgUnitId createUser(SessionId sessionId, String name) throws EmfStoreException {
		return getAdminEmfStore().createUser(sessionId, name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteGroup(SessionId sessionId, ACOrgUnitId group) throws EmfStoreException {
		getAdminEmfStore().deleteGroup(sessionId, group);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteUser(SessionId sessionId, ACOrgUnitId user) throws EmfStoreException {
		getAdminEmfStore().deleteUser(sessionId, user);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACGroup> getGroups(SessionId sessionId) throws EmfStoreException {
		return getAdminEmfStore().getGroups(sessionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACGroup> getGroups(SessionId sessionId, ACOrgUnitId user) throws EmfStoreException {
		return getAdminEmfStore().getGroups(sessionId, user);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACOrgUnit> getMembers(SessionId sessionId, ACOrgUnitId groupId) throws EmfStoreException {
		return getAdminEmfStore().getMembers(sessionId, groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	public ACOrgUnit getOrgUnit(SessionId sessionId, ACOrgUnitId orgUnitId) throws EmfStoreException {
		return getAdminEmfStore().getOrgUnit(sessionId, orgUnitId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACOrgUnit> getOrgUnits(SessionId sessionId) throws EmfStoreException {
		return getAdminEmfStore().getOrgUnits(sessionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACOrgUnit> getParticipants(SessionId sessionId, ProjectId projectId) throws EmfStoreException {
		return getAdminEmfStore().getParticipants(sessionId, projectId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ProjectInfo> getProjectInfos(SessionId sessionId) throws EmfStoreException {
		return getAdminEmfStore().getProjectInfos(sessionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Role getRole(SessionId sessionId, ProjectId projectId, ACOrgUnitId orgUnit) throws EmfStoreException {
		return getAdminEmfStore().getRole(sessionId, projectId, orgUnit);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ACUser> getUsers(SessionId sessionId) throws EmfStoreException {
		return getAdminEmfStore().getUsers(sessionId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGroup(SessionId sessionId, ACOrgUnitId user, ACOrgUnitId group) throws EmfStoreException {
		getAdminEmfStore().removeGroup(sessionId, user, group);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeMember(SessionId sessionId, ACOrgUnitId group, ACOrgUnitId member) throws EmfStoreException {
		getAdminEmfStore().removeMember(sessionId, group, member);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeParticipant(SessionId sessionId, ProjectId projectId, ACOrgUnitId participant)
		throws EmfStoreException {
		getAdminEmfStore().removeParticipant(sessionId, projectId, participant);
	}
}
