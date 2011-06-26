/**
 * <copyright> Copyright (c) 2008-2009 Jonas Helming, Maximilian Koegel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html </copyright>
 */
package org.eclipse.emf.emfstore.client.test.integration.reversibility;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.emfstore.client.test.integration.forward.IntegrationTestHelper;
import org.eclipse.emf.emfstore.common.model.util.SerializationException;
import org.eclipse.emf.emfstore.server.exceptions.EmfStoreException;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.junit.Test;

/**
 * @author Hodaie
 */
public class ReferenceOperationsReversibilityTest extends OperationsReversibilityTest {

	private long randomSeed = 1;

	/**
	 * Takes a random ME (meA). Takes randomly one of its containment references. Creates a new ME matching containment
	 * reference type (meB). Adds created meB to meA's containment reference.
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void containmentReferenceAddNewReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("ContainmentReferenceAddNewReversibilityTest");

		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				testHelper.doContainemntReferenceAddNew();
				getTestProjectSpace().revert();
			}

		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"ContainmentReferenceAddNewReversibilityTest"));

	}

	/**
	 * This takes a random model element (meA). Takes one of its containments (meToMove). Takes containing reference of
	 * meToMove. Finds another ME of type meA (meB). Moves meToMove to meB. Finds yet another ME of type meA (meC) .
	 * Moves meToMove to meC.
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void containmentRefTransitiveChangeReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("ContainmentRefTransitiveChangeReversibilityTest");

		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				testHelper.doContainmentRefTransitiveChange();
				getTestProjectSpace().revert();
			}

		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"ContainmentRefTransitiveChangeReversibilityTest"));

	}

	/**
	 * This move an element in a many reference list to another position.
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void multiReferenceMoveReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("MultiReferenceMoveReversibilityTest");
		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				testHelper.doMultiReferenceMove();
				getTestProjectSpace().revert();
			}
		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"MultiReferenceMoveReversibilityTest"));

	}

	/**
	 * Select a random ME (meA). Select one of its non-containment references. Find an ME matching reference type (meB).
	 * Add meB to meA.
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void nonContainmentReferenceAddReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("NonContainmentReferenceAddReversibilityTest");
		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				testHelper.doNonContainmentReferenceAdd();
				getTestProjectSpace().revert();
			}

		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"NonContainmentReferenceAddReversibilityTest"));
	}

	/**
	 * Removes a referenced model element form a non-containment reference of a randomly selected ME.
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void nonContainmentReferenceRemoveReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("NonContainmentReferenceRemoveReversibilityTest");

		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				testHelper.doNonContainmentReferenceRemove();
				getTestProjectSpace().revert();
			}
		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"NonContainmentReferenceRemoveReversibilityTest"));

	}

	/**
	 * Takes a random ME (meA). Takes randomly one of its containment references. Finds an existing ME in project
	 * matching the reference type (meB). Adds meB to this reference of meA (moves meB from its old parent to meA).
	 * 
	 * @throws EmfStoreException EmfStoreException
	 * @throws SerializationException SerializationException
	 */
	@Test
	public void containmentReferenceMoveReversibilityTest() throws SerializationException, EmfStoreException {
		System.out.println("ContainmentReferenceMoveReversibilityTest");
		final IntegrationTestHelper testHelper = new IntegrationTestHelper(randomSeed, getTestProject());
		TransactionalEditingDomain domain = IntegrationTestHelper.getDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				testHelper.doContainmentReferenceMove();
				getTestProjectSpace().revert();
			}
		});

		assertTrue(IntegrationTestHelper.areEqual(getTestProject(), getCompareProject(),
			"ContainmentReferenceMoveReversibilityTest"));

	}

}
