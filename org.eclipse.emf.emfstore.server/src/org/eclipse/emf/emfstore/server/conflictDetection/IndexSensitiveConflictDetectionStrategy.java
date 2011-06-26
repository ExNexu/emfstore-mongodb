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
package org.eclipse.emf.emfstore.server.conflictDetection;

import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isAttribute;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isCreateDelete;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isMultiAtt;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isMultiAttMove;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isMultiAttSet;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isMultiRef;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isMultiRefSet;
import static org.eclipse.emf.emfstore.server.model.versioning.operations.util.OperationUtil.isSingleRef;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.emfstore.common.model.ModelElementId;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.AttributeOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.CompositeOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.ContainmentType;
import org.eclipse.emf.emfstore.server.model.versioning.operations.CreateDeleteOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.FeatureOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiAttributeMoveOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiAttributeOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiAttributeSetOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiReferenceMoveOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiReferenceOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.MultiReferenceSetOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.ReferenceOperation;
import org.eclipse.emf.emfstore.server.model.versioning.operations.SingleReferenceOperation;

/**
 * A conflict detection strategy that will operate on a per attribute and feature level.
 * 
 * @author chodnick
 * @author wesendon
 */
// BEGIN COMPLEX CODE
public class IndexSensitiveConflictDetectionStrategy implements ConflictDetectionStrategy {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.server.conflictDetection.ConflictDetectionStrategy#doConflict(org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation,
	 *      org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation)
	 */
	public boolean doConflict(AbstractOperation operationA, AbstractOperation operationB) {

		if (operationA instanceof CompositeOperation) {
			CompositeOperation compA = (CompositeOperation) operationA;
			for (AbstractOperation op : compA.getSubOperations()) {
				if (doConflict(op, operationB)) {
					return true;
				}
			}
			return false;
		} else if (operationB instanceof CompositeOperation) {
			CompositeOperation compB = (CompositeOperation) operationB;
			for (AbstractOperation op : compB.getSubOperations()) {
				if (doConflict(operationA, op)) {
					return true;
				}
			}
			return false;
		}

		return doConflictHard(operationA, operationB);

	}

	/**
	 * Hard conflicts change the toplogogy of the model element graph. This method returns, given the operations in
	 * question, whether such a conflict exists.
	 * 
	 * @param operationA any operation
	 * @param operationB any operation
	 * @return true if topology of the resulting model element graph depends on serialization of operationA and
	 *         operationB false otherwise
	 */
	private boolean doConflictHard(AbstractOperation operationA, AbstractOperation operationB) {

		if (isCreateDelete(operationA)) {
			return doConflictHardCreateDelete((CreateDeleteOperation) operationA, operationB);
		}

		if (isCreateDelete(operationB)) {
			return doConflictHardCreateDelete((CreateDeleteOperation) operationB, operationA);
		}

		if (isAttribute(operationA) && isAttribute(operationB)) {
			return doConflictHardAttributes((AttributeOperation) operationA, (AttributeOperation) operationB);
		}

		if (isMultiAtt(operationA) && isMultiAtt(operationB)) {
			return doConflictHardMultiAttributes((MultiAttributeOperation) operationA,
				(MultiAttributeOperation) operationB);
		}

		if (isMultiAttSet(operationA) && isMultiAttSet(operationB)) {
			return doConflictHardMultiAttributeSets((MultiAttributeSetOperation) operationA,
				(MultiAttributeSetOperation) operationB);
		}

		if (isMultiAttSet(operationA) && isMultiAtt(operationB)) {
			return doConflictHardMultiAttAndSet((MultiAttributeOperation) operationB,
				(MultiAttributeSetOperation) operationA);
		}

		if (isMultiAtt(operationA) && isMultiAttSet(operationB)) {
			return doConflictHardMultiAttAndSet((MultiAttributeOperation) operationA,
				(MultiAttributeSetOperation) operationB);
		}

		if (isMultiAttMove(operationA) && isMultiAttMove(operationB)) {
			return doConflictHardMultiAttributeMoves((MultiAttributeMoveOperation) operationA,
				(MultiAttributeMoveOperation) operationB);
		}

		if (isMultiAttMove(operationA) && isMultiAtt(operationB)) {
			return doConflictHardMultiAttAndMove((MultiAttributeMoveOperation) operationA,
				(MultiAttributeOperation) operationB);
		}

		if (isMultiAtt(operationA) && isMultiAttMove(operationB)) {
			return doConflictHardMultiAttAndMove((MultiAttributeMoveOperation) operationB,
				(MultiAttributeOperation) operationA);
		}

		if (isMultiAttSet(operationA) && isMultiAttMove(operationB)) {
			return doConflictHardMultiAttMoveAndSet((MultiAttributeMoveOperation) operationB,
				(MultiAttributeSetOperation) operationA);
		}

		if (isMultiAttMove(operationA) && isMultiAttSet(operationB)) {
			return doConflictHardMultiAttMoveAndSet((MultiAttributeMoveOperation) operationA,
				(MultiAttributeSetOperation) operationB);
		}

		if (isSingleRef(operationA) && isSingleRef(operationB)) {
			return doConflictHardSingleReferences((SingleReferenceOperation) operationA,
				(SingleReferenceOperation) operationB);
		}

		if (isSingleRef(operationA) && isMultiRef(operationB)) {
			return doConflictHardSingleMultiReferences((SingleReferenceOperation) operationA,
				(MultiReferenceOperation) operationB);
		}

		if (isMultiRef(operationA) && isSingleRef(operationB)) {
			return doConflictHardSingleMultiReferences((SingleReferenceOperation) operationB,
				(MultiReferenceOperation) operationA);
		}

		if (isSingleRef(operationA) && isMultiRefSet(operationB)) {
			return doConflictHardSingeMultiSet((SingleReferenceOperation) operationA,
				(MultiReferenceSetOperation) operationB);
		}

		if (isMultiRefSet(operationA) && isSingleRef(operationB)) {
			return doConflictHardSingeMultiSet((SingleReferenceOperation) operationB,
				(MultiReferenceSetOperation) operationA);
		}

		if (isMultiRef(operationA) && isMultiRefSet(operationB)) {
			return doConflictHardMultiReferenceAndSet((MultiReferenceOperation) operationA,
				(MultiReferenceSetOperation) operationB);
		}

		if (isMultiRefSet(operationA) && isMultiRef(operationB)) {
			return doConflictHardMultiReferenceAndSet((MultiReferenceOperation) operationB,
				(MultiReferenceSetOperation) operationA);
		}

		if (isMultiRefSet(operationA) && isMultiRefSet(operationB)) {
			return doConflictHardMultiReferenceSet((MultiReferenceSetOperation) operationA,
				(MultiReferenceSetOperation) operationB);
		}

		if (isMultiRef(operationA) && isMultiRef(operationB)) {
			return doConflictHardMultiReferences((MultiReferenceOperation) operationA,
				(MultiReferenceOperation) operationB);
		}

		return false;
	}

	private boolean doConflictHardSingeMultiSet(SingleReferenceOperation opA, MultiReferenceSetOperation opB) {
		if (!bothContaining(opA, opB)) {
			return false;
		}

		return (opA.getNewValue() != null && isSame(opA.getNewValue(), opB.getNewValue()));
	}

	private boolean doConflictHardMultiAttributeMoves(MultiAttributeMoveOperation operationA,
		MultiAttributeMoveOperation operationB) {
		// this is a soft conflict
		return false;
	}

	private boolean doConflictHardMultiAttributeSets(MultiAttributeSetOperation operationA,
		MultiAttributeSetOperation operationB) {

		if (!sameFeatureAndId(operationA, operationB)) {
			return false;
		}

		if (operationA.getIndex() == operationB.getIndex()
			&& isDifferent(operationA.getNewValue(), operationB.getNewValue())) {
			return true;
		}

		return false;
	}

	private boolean doConflictHardMultiReferenceSet(MultiReferenceSetOperation operationA,
		MultiReferenceSetOperation operationB) {

		if (sameFeatureAndId(operationA, operationB)) {

			if (operationA.getIndex() == operationB.getIndex()
				&& isDifferent(operationA.getNewValue(), operationB.getNewValue())) {
				return true;
			}
		} else {
			if (!bothContaining(operationA, operationB)) {
				return false;
			}

			return isSame(operationA.getNewValue(), operationB.getNewValue());
		}

		return false;
	}

	private boolean doConflictHardMultiReferenceAndSet(MultiReferenceOperation operationA,
		MultiReferenceSetOperation operationB) {

		if (sameFeatureAndId(operationA, operationB)) {

			if (!operationA.isAdd()) {
				for (ModelElementId id : operationA.getReferencedModelElements()) {
					if (id.equals(operationB.getOldValue())) {
						return true;
					}
				}
			}
		} else {
			if (!bothContaining(operationA, operationB)) {
				return false;
			}

			if (!operationA.isAdd()) {
				return false;
			}

			return containsId(operationA.getReferencedModelElements(), operationB.getNewValue());
		}

		return false;
	}

	private boolean doConflictHardMultiAttAndMove(MultiAttributeMoveOperation operationB,
		MultiAttributeOperation operationA) {

		if (!sameFeatureAndId(operationA, operationB)) {
			return false;
		}

		int index = getLowestIndex(operationA.getIndexes());
		if (index == -1) {
			return false;
		}

		return (index <= operationB.getOldIndex() || index <= operationB.getNewIndex());
	}

	private boolean doConflictHardMultiAttMoveAndSet(MultiAttributeMoveOperation operationA,
		MultiAttributeSetOperation operationB) {
		if (!sameFeatureAndId(operationA, operationB)) {
			return false;
		}

		if (between(operationB.getIndex(), operationA.getOldIndex(), operationA.getNewIndex())) {
			return true;
		}

		return false;
	}

	private boolean doConflictHardMultiAttAndSet(MultiAttributeOperation operationB,
		MultiAttributeSetOperation operationA) {
		if (!sameFeatureAndId(operationA, operationB)) {
			return false;
		}

		int index = getLowestIndex(operationB.getIndexes());
		if (index == -1) {
			return false;
		}

		return (index <= operationA.getIndex());
	}

	private boolean doConflictHardMultiAttributes(MultiAttributeOperation operationA, MultiAttributeOperation operationB) {
		if (!sameFeatureAndId(operationA, operationB)) {
			return false;
		}
		return true;
	}

	private boolean doConflictHardCreateDelete(CreateDeleteOperation opA, AbstractOperation opB) {
		// creates do not conflict with anything, by definition
		if (isCreateOperation(opA)) {
			return false;
		}

		if (isCreateOperation(opB)) {
			return false;
		}

		// we have a delete here

		// we need to check whether the other operation changes any of the deleted elements
		if (opB instanceof CreateDeleteOperation) {
			Set<ModelElementId> allDeletedElementsA = getAllDeletedElements(opA);
			Set<ModelElementId> allDeletedElementsB = getAllDeletedElements((CreateDeleteOperation) opB);
			boolean intersecting = allDeletedElementsA.removeAll(allDeletedElementsB);
			if (intersecting && !allDeletedElementsA.isEmpty()) {
				return true;
			}
			return false;
		} else {
			for (ModelElementId m : getAllDeletedElements(opA)) {
				if (changesModelElement(opB, m)) {
					return true;
				}
			}
		}

		// we need to check whether the cross reference change do conflict with the other op
		for (ReferenceOperation referenceOperation : opA.getSubOperations()) {
			if (doConflictHard(referenceOperation, opB)) {
				return true;
			}
		}

		return false;
	}

	private Set<ModelElementId> getAllDeletedElements(CreateDeleteOperation op) {
		// EObject element = op.getModelElement();
		return new HashSet<ModelElementId>(op.getEObjectToIdMap().values());
		// Set<EObject> allDeleteTreeElements = ModelUtil.getAllContainedModelElements(element, false);
		// allDeleteTreeElements.add(op.getModelElement());
		// Set<ModelElementId> result = new HashSet<ModelElementId>(allDeleteTreeElements.size());
		// for (EObject modelElement : allDeleteTreeElements) {
		// result.add(ModelUtil.getProject(modelElement).getModelElementId(modelElement));
		// }
		// return result;
	}

	private boolean doConflictHardMultiReferences(MultiReferenceOperation opA, MultiReferenceOperation opB) {

		if (sameFeatureAndId(opA, opB)) {

			// if add and remove meet, there might be a hard conflict
			if (opA.isAdd() != opB.isAdd()) {

				for (ModelElementId mA : opA.getOtherInvolvedModelElements()) {

					for (ModelElementId mB : opB.getOtherInvolvedModelElements()) {

						if (mA.equals(mB)) {
							return true;
						}
					}
				}

			}
			return false;

		} else {
			if (!bothContaining(opA, opB)) {
				return false;
			}

			if (!(opA.isAdd() == opB.isAdd())) {
				return false;
			}
			for (ModelElementId mA : opA.getOtherInvolvedModelElements()) {

				for (ModelElementId mB : opB.getOtherInvolvedModelElements()) {

					if (mA.equals(mB)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean doConflictHardSingleMultiReferences(SingleReferenceOperation opA, MultiReferenceOperation opB) {
		if (!bothContaining(opA, opB)) {
			return false;
		}

		if (!opB.isAdd()) {
			return false;
		}

		for (ModelElementId id : opB.getOtherInvolvedModelElements()) {
			if (id.equals(opA.getNewValue())) {
				return true;
			}
		}

		return false;
	}

	private boolean doConflictHardSingleReferences(SingleReferenceOperation opA, SingleReferenceOperation opB) {

		if (sameFeatureAndId(opA, opB)) {
			// unless both operations set the same new value
			if (isDifferent(opA.getNewValue(), opB.getNewValue())) {
				return true;
			}
			return false;

		} else {

			if (!bothContaining(opA, opB)) {
				return false;
			}

			return (opA.getNewValue() != null && isSame(opA.getNewValue(), opB.getNewValue()));
		}
	}

	private boolean doConflictHardAttributes(AttributeOperation opA, AttributeOperation opB) {

		// if same object's same feature is set, there's a potential conflict
		if (sameFeatureAndId(opA, opB)) {

			// unless both operations set the same new value
			if (isSame(opA.getNewValue(), opB.getNewValue())) {
				return false;
			}
			return true;

		}

		return false;

	}

	/**
	 * This method tests whether the both give operations change the same multifeature on the same object. If so, and
	 * additionally the order of items in the multifeature depends on the serialization of the operations, there is an
	 * index integrity conflict, and the method returns true. TODO: MultiAttribute and MultiReferenceSet operations only
	 * implemented for hard conflict.
	 * 
	 * @param operationA any operation
	 * @param operationB any operation
	 * @return true if an index integrity conflict exists, false otherwise
	 */
	public boolean doConflictIndexIntegrity(AbstractOperation operationA, AbstractOperation operationB) {

		if (operationA instanceof CompositeOperation) {
			CompositeOperation compA = (CompositeOperation) operationA;
			for (AbstractOperation op : compA.getSubOperations()) {
				if (doConflictIndexIntegrity(op, operationB)) {
					return true;
				}
			}
			return false;
		} else if (operationB instanceof CompositeOperation) {
			CompositeOperation compB = (CompositeOperation) operationB;
			for (AbstractOperation op : compB.getSubOperations()) {
				if (doConflictIndexIntegrity(operationA, op)) {
					return true;
				}
			}
			return false;
		}

		if (operationA instanceof MultiReferenceOperation && operationB instanceof MultiReferenceOperation) {
			return doConflictIndexIntegrityMultiReferences((MultiReferenceOperation) operationA,
				(MultiReferenceOperation) operationB);
		}

		if (operationA instanceof SingleReferenceOperation && operationB instanceof MultiReferenceOperation) {
			return doConflictIndexIntegritySingleMultiReferences((SingleReferenceOperation) operationA,
				(MultiReferenceOperation) operationB);
		}

		if (operationB instanceof SingleReferenceOperation && operationA instanceof MultiReferenceOperation) {
			return doConflictIndexIntegritySingleMultiReferences((SingleReferenceOperation) operationB,
				(MultiReferenceOperation) operationA);
		}

		if (operationA instanceof MultiReferenceMoveOperation && operationB instanceof MultiReferenceMoveOperation) {
			return doConflictIndexIntegrityMultiMoveReferences((MultiReferenceMoveOperation) operationA,
				(MultiReferenceMoveOperation) operationB);
		}

		if (operationA instanceof MultiReferenceMoveOperation && operationB instanceof MultiReferenceOperation) {
			return doConflictIndexIntegrityMultiMoveMultiReferences((MultiReferenceMoveOperation) operationA,
				(MultiReferenceOperation) operationB);
		}

		if (operationB instanceof MultiReferenceMoveOperation && operationA instanceof MultiReferenceOperation) {
			return doConflictIndexIntegrityMultiMoveMultiReferences((MultiReferenceMoveOperation) operationB,
				(MultiReferenceOperation) operationA);
		}

		if (operationA instanceof MultiReferenceMoveOperation && operationB instanceof SingleReferenceOperation) {
			return doConflictIndexIntegrityMultiMoveSingleReferences((MultiReferenceMoveOperation) operationA,
				(SingleReferenceOperation) operationB);
		}

		if (operationB instanceof MultiReferenceMoveOperation && operationA instanceof SingleReferenceOperation) {
			return doConflictIndexIntegrityMultiMoveSingleReferences((MultiReferenceMoveOperation) operationB,
				(SingleReferenceOperation) operationA);
		}

		if (operationA instanceof SingleReferenceOperation && operationB instanceof SingleReferenceOperation) {
			return doConflictIndexIntegritySingleReferences((SingleReferenceOperation) operationA,
				(SingleReferenceOperation) operationB);
		}

		return false;
	}

	private boolean doConflictIndexIntegritySingleReferences(SingleReferenceOperation opA, SingleReferenceOperation opB) {

		// Due to refactoring: The opposite case is covered by the second operation created by bidirectional links.

		return false;
	}

	private boolean doConflictIndexIntegrityMultiMoveSingleReferences(MultiReferenceMoveOperation opA,
		SingleReferenceOperation opB) {

		// Due to refactoring: MultiMove only occurs on multifeatures, so it can't conflict with a single operation. The
		// opposite case is covered by the second operation created by bidirectional links.
		return false;

	}

	private boolean doConflictIndexIntegrityMultiMoveMultiReferences(MultiReferenceMoveOperation opA,
		MultiReferenceOperation opB) {

		// 2 cases
		// regular vs. regular
		// regular vs. opposite

		// regular vs. regular
		if (opA.getModelElementId().equals(opB.getModelElementId())
			&& opA.getFeatureName().equals(opB.getFeatureName())) {

			if (containsId(opB.getReferencedModelElements(), opA.getReferencedModelElementId())) {
				if (opB.isAdd()) {
					return true;
				}
				return false;

			} else {

				// so there is an add or remove and a move going on on different objects but on the same feature
				if (opB.isAdd() && opB.getIndex() >= opA.getNewIndex()) {
					return true;
				} else if (!opB.isAdd()
					&& (opB.getIndex() + opB.getReferencedModelElements().size() - 1) < opA.getNewIndex()) {
					return true;
				}
				return false;

			}

		}

		return false;
	}

	private boolean doConflictIndexIntegrityMultiMoveReferences(MultiReferenceMoveOperation opA,
		MultiReferenceMoveOperation opB) {

		if (opA.getModelElementId().equals(opB.getModelElementId())
			&& opA.getFeatureName().equals(opB.getFeatureName())) {

			if (opA.getReferencedModelElementId().equals(opB.getReferencedModelElementId())) {
				return opA.getNewIndex() != opB.getNewIndex();
			} else {
				return opA.getNewIndex() == opB.getNewIndex();
			}

		}

		return false;
	}

	private boolean doConflictIndexIntegritySingleMultiReferences(SingleReferenceOperation opA,
		MultiReferenceOperation opB) {

		return false;

	}

	private boolean doConflictIndexIntegrityMultiReferences(MultiReferenceOperation opA, MultiReferenceOperation opB) {

		// 3 cases to look for
		// regular vs. regular
		// regular vs. opposite
		// opposite vs. regular

		// case 1: regular vs. regular
		if (opA.getModelElementId().equals(opB.getModelElementId())
			&& opA.getFeatureName().equals(opB.getFeatureName())) {
			if (opA.isAdd() && opB.isAdd()) {

				// make sure that some of the added things are different
				if (opA.getIndex() == opB.getIndex()) {
					for (ModelElementId mA : opA.getOtherInvolvedModelElements()) {
						if (!containsId(opB.getOtherInvolvedModelElements(), mA)) {
							return true;
						}
					}
					for (ModelElementId mB : opB.getOtherInvolvedModelElements()) {
						// if all were the same, this would be identical operations, thus no index conflict
						if (!containsId(opA.getOtherInvolvedModelElements(), mB)) {
							return true;
						}
					}

				} else {
					return true;
				}

			}

			if (opA.isAdd() != opB.isAdd()) {

				for (ModelElementId mA : opA.getOtherInvolvedModelElements()) {
					if (!containsId(opB.getOtherInvolvedModelElements(), mA)) {
						if (opB.getReferencedModelElements().size() == 1
							&& opA.getReferencedModelElements().size() == 1) {
							if (opA.isAdd()) {
								return opA.getIndex() > opB.getIndex() && opA.getIndex() != 0 && opB.getIndex() != 0;
							} else {
								return opA.getIndex() < opB.getIndex() && opA.getIndex() != 0 && opB.getIndex() != 0;
							}

						} else {
							return true;
						}
					}
				}
				for (ModelElementId mB : opB.getOtherInvolvedModelElements()) {
					if (!containsId(opA.getOtherInvolvedModelElements(), mB)) {
						if (opB.getReferencedModelElements().size() == 1
							&& opA.getReferencedModelElements().size() == 1) {
							if (opA.isAdd()) {
								return opA.getIndex() > opB.getIndex() && opA.getIndex() != 0 && opB.getIndex() != 0;
							} else {
								return opA.getIndex() < opB.getIndex() && opA.getIndex() != 0 && opB.getIndex() != 0;
							}

						} else {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc} TODO multiattribute and mulitreferenceset operations are not yet considerered
	 * 
	 * @see org.eclipse.emf.emfstore.server.conflictDetection.ConflictDetectionStrategy#isRequired(org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation,
	 *      org.eclipse.emf.emfstore.server.model.versioning.operations.AbstractOperation)
	 */
	public boolean isRequired(AbstractOperation requiredOperation, AbstractOperation operation) {

		// composite operation decomposition section
		if (requiredOperation instanceof CompositeOperation) {
			CompositeOperation compA = (CompositeOperation) requiredOperation;
			for (AbstractOperation op : compA.getSubOperations()) {
				if (isRequired(op, operation)) {
					return true;
				}
			}
			return false;
		} else if (operation instanceof CompositeOperation) {
			CompositeOperation compB = (CompositeOperation) operation;
			for (AbstractOperation op : compB.getSubOperations()) {
				if (isRequired(requiredOperation, op)) {
					return true;
				}
			}
			return false;
		}

		// non-composite op handling

		if (requiredOperation instanceof CreateDeleteOperation) {
			return isRequiredCreate((CreateDeleteOperation) requiredOperation, operation);
		} else if (requiredOperation instanceof MultiReferenceOperation
			&& operation instanceof MultiReferenceMoveOperation) {
			return isRequiredMultiByMoveReference((MultiReferenceOperation) requiredOperation,
				(MultiReferenceMoveOperation) operation);
		} else if (requiredOperation instanceof MultiReferenceOperation && operation instanceof MultiReferenceOperation) {
			return isRequiredMutiByMultiReference((MultiReferenceOperation) requiredOperation,
				(MultiReferenceOperation) operation);
		} else if (requiredOperation instanceof SingleReferenceOperation
			&& operation instanceof MultiReferenceMoveOperation) {
			return isRequiredSingleByMoveReference((SingleReferenceOperation) requiredOperation,
				(MultiReferenceMoveOperation) operation);
		} else if (requiredOperation instanceof SingleReferenceOperation
			&& operation instanceof MultiReferenceOperation) {
			return isRequiredSingleByMultiReference((SingleReferenceOperation) requiredOperation,
				(MultiReferenceOperation) operation);
		}

		return false;
	}

	private boolean isRequiredSingleByMultiReference(SingleReferenceOperation requiredOperation,
		MultiReferenceOperation operation) {

		return false;
	}

	private boolean isRequiredSingleByMoveReference(SingleReferenceOperation requiredOperation,
		MultiReferenceMoveOperation operation) {

		if (!requiredOperation.isBidirectional()) {
			return false;
		}

		boolean sameFeature = requiredOperation.getOppositeFeatureName().equals(operation.getFeatureName());
		boolean sameMovedElement = requiredOperation.getModelElementId()
			.equals(operation.getReferencedModelElementId());
		boolean sameElement = isSame(requiredOperation.getNewValue(), operation.getModelElementId());

		return (sameElement && sameFeature && sameMovedElement);

	}

	private boolean isRequiredMutiByMultiReference(MultiReferenceOperation requiredOperation,
		MultiReferenceOperation operation) {

		return false;

	}

	private boolean isRequiredMultiByMoveReference(MultiReferenceOperation requiredOperation,
		MultiReferenceMoveOperation operation) {

		boolean sameElement = requiredOperation.getModelElementId().equals(operation.getModelElementId());
		boolean sameFeature = requiredOperation.getFeatureName().equals(operation.getFeatureName());

		// require the add of the element, that is moved around
		if (sameElement && sameFeature) {
			for (ModelElementId modelElementId : requiredOperation.getReferencedModelElements()) {
				if (modelElementId.equals(operation.getReferencedModelElementId())) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isRequiredCreate(CreateDeleteOperation requiredOperation, AbstractOperation operation) {
		if (!requiredOperation.isDelete()) {
			// in case of a delete, it only requires the create if the deleted element is created,
			// creates of subelements in the deltree are not required
			if (operation instanceof CreateDeleteOperation) {
				CreateDeleteOperation op = (CreateDeleteOperation) operation;
				if (op.isDelete() && op.getModelElementId().equals(requiredOperation.getModelElementId())) {
					return true;
				}
				return false;
			}
			// in case of a single ref op, we don't need to require it if the create references the old value
			if (operation instanceof SingleReferenceOperation) {
				SingleReferenceOperation op = (SingleReferenceOperation) operation;
				if (isSame(op.getOldValue(), requiredOperation.getModelElementId())) {
					return false;
				}
			}

			if (changesModelElement(operation, requiredOperation.getModelElementId())) {
				return true;
			}
		}
		return false;
	}

	// helper methods

	private static boolean isDifferent(Object a, Object b) {

		// if both are null, they are not different
		if (a == null && b == null) {
			return false;
		}

		// if either is null now, the other is not, so the objects ARE different
		if (a == null || b == null) {
			return true;
		}

		// if neither is null, let the objects sort out equality
		return !a.equals(b);
	}

	private static boolean isSame(Object a, Object b) {

		// if both are null, they are the same
		if (a == null && b == null) {
			return true;
		}

		// if either is null now, the other is not, so the objects ARE different
		if (a == null || b == null) {
			return false;
		}

		// if neither is null, let the objects sort out equality
		return a.equals(b);
	}

	private boolean bothContaining(ReferenceOperation opA, ReferenceOperation opB) {
		return ContainmentType.CONTAINMENT.equals(opA.getContainmentType())
			&& ContainmentType.CONTAINMENT.equals(opB.getContainmentType());
	}

	private boolean sameFeatureAndId(FeatureOperation operationA, FeatureOperation operationB) {
		return (isSame(operationA.getModelElementId(), operationB.getModelElementId()) && isSame(
			operationA.getFeatureName(), operationB.getFeatureName()));
	}

	private boolean between(int index, int lower, int upper) {
		if (lower > upper) {
			return between(index, upper, lower);
		}
		return (lower <= index && index <= upper);
	}

	private int getLowestIndex(EList<Integer> indexes) {
		int result = -1;
		for (Integer tmp : indexes) {
			if (result >= tmp || result == -1) {
				result = tmp;
			}
		}
		return result;
	}

	private static boolean isCreateOperation(AbstractOperation op) {

		if (op instanceof CreateDeleteOperation) {
			CreateDeleteOperation cdop = (CreateDeleteOperation) op;
			return !cdop.isDelete();
		}
		return false;
	}

	private boolean containsId(Iterable<ModelElementId> list, ModelElementId id) {
		for (ModelElementId m : list) {
			if (m.equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether an operation changes a given model element.
	 * 
	 * @param op the op to examine
	 * @param id the modelelement to check
	 * @return if the operation changes the given model element
	 */
	public static boolean changesModelElement(AbstractOperation op, ModelElementId id) {

		for (ModelElementId m : op.getAllInvolvedModelElements()) {
			if (m.equals(id)) {
				return true;
			}
		}
		return false;
	}

	// END COMPLEX CODE
}
