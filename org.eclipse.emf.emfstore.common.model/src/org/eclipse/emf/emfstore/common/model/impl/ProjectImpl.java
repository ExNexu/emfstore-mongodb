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
package org.eclipse.emf.emfstore.common.model.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.emfstore.common.model.IdEObjectCollection;
import org.eclipse.emf.emfstore.common.model.ModelElementId;
import org.eclipse.emf.emfstore.common.model.ModelFactory;
import org.eclipse.emf.emfstore.common.model.ModelPackage;
import org.eclipse.emf.emfstore.common.model.Project;
import org.eclipse.emf.emfstore.common.model.util.EObjectChangeNotifier;
import org.eclipse.emf.emfstore.common.model.util.EObjectChangeObserver;
import org.eclipse.emf.emfstore.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver;

/**
 * @author koegel, naughton
 * @generated
 */
public class ProjectImpl extends EObjectImpl implements Project {

	/**
	 * The cached value of the '{@link #getModelElements()
	 * <em>Model Elements</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getModelElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> modelElements;

	/**
	 * The cached value of the '{@link #getCutElements() <em>Cut Elements</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCutElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> cutElements;

	protected EMap<ModelElementId, EObject> modelElementIdToEObjectsCache;

	private List<ProjectChangeObserver> observers;
	private boolean isNotifiying;
	private Set<ProjectChangeObserver> exceptionThrowingObservers;
	private Set<ProjectChangeObserver> observersToRemove;
	private Set<ProjectChangeObserver> undetachableObservers;

	private Set<EObject> eObjectsCache;

	private Map<EObject, ModelElementId> eObjectToIdCache;

	private Map<EObject, ModelElementId> deletedEObjectToIdMap;

	private Map<EObject, ModelElementId> newEObjectToIdMap;

	private Map<ModelElementId, EObject> idToEObjectCache;

	private boolean cachesInitialized;

	/**
	 * Will be used to cache all model elements of a project in order to avoid
	 * fetching those multiple times when trying to retrieve a model element ID.
	 * 
	 * @see ProjectImpl#getModelElementId(EObject)
	 */
	private Set<EObject> containedModelElements;

	private EObjectChangeNotifier changeNotifier;

	// begin of custom code
	/**
	 * Constructor. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected ProjectImpl() {
		super();
		observers = new ArrayList<ProjectChangeObserver>();
		isNotifiying = false;
		exceptionThrowingObservers = new HashSet<ProjectChangeObserver>();
		observersToRemove = new HashSet<ProjectChangeObserver>();
		undetachableObservers = new HashSet<ProjectChangeObserver>();
		eObjectToIdCache = new HashMap<EObject, ModelElementId>();
		deletedEObjectToIdMap = new HashMap<EObject, ModelElementId>();
		newEObjectToIdMap = new HashMap<EObject, ModelElementId>();
		eObjectsCache = new HashSet<EObject>();
		idToEObjectCache = new HashMap<ModelElementId, EObject>();
	}

	// end of custom code

	/**
	 * <!-- begin-user-doc --> .<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EObject> getModelElements() {
		if (modelElements == null) {
			modelElements = new EObjectContainmentEList.Resolving<EObject>(EObject.class, this,
				ModelPackage.PROJECT__MODEL_ELEMENTS);
		}
		return modelElements;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<EObject> getCutElements() {
		if (cutElements == null) {
			cutElements = new EObjectContainmentEList.Resolving<EObject>(EObject.class, this,
				ModelPackage.PROJECT__CUT_ELEMENTS);
		}
		return cutElements;
	}

	public void addModelElement(EObject me) {
		getModelElements().add(me);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getAllModelElements()
	 * @generated NOT
	 */
	public Set<EObject> getAllModelElements() {
		if (!isCacheInitialized()) {
			initCaches();
		}

		return eObjectsCache;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getAllModelElementIds()
	 */
	public Set<ModelElementId> getAllModelElementIds() {
		if (!isCacheInitialized()) {
			initCaches();
		}

		return idToEObjectCache.keySet();
	}

	private boolean isCacheInitialized() {
		return cachesInitialized;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getAllModelElementsbyClass(org.eclipse.emf.ecore.EClass)
	 * @generated NOT
	 */
	public <T extends EObject> EList<T> getAllModelElementsbyClass(EClass modelElementClass, EList<T> list) {
		return getAllModelElementsbyClass(modelElementClass, list, true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getAllModelElementsbyClass(org.eclipse.emf.ecore.EClass)
	 * @generated NOT
	 */
	// two casts below are guarded by initial sanity check and if statement
	@SuppressWarnings("unchecked")
	public <T extends EObject> EList<T> getAllModelElementsbyClass(EClass modelElementClass, EList<T> list,
		Boolean subclasses) {

		if (subclasses) {
			for (ModelElementId modelElementId : getIdToEObjectCache().keySet()) {
				EObject modelElement = this.getModelElement(modelElementId);
				if (modelElementClass.isInstance(modelElement)) {
					list.add((T) modelElement);
				}
			}
		} else {
			for (ModelElementId modelElementId : getIdToEObjectCache().keySet()) {
				EObject modelElement = this.getModelElement(modelElementId);
				if (modelElement.eClass() == modelElementClass) {
					list.add((T) modelElement);
				}
			}
		}

		return list;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getModelElementsByClass(org.eclipse.emf.ecore.EClass)
	 * @generated NOT
	 */
	// cast below is guarded by sanity check
	@SuppressWarnings("unchecked")
	public <T extends EObject> EList<T> getModelElementsByClass(EClass modelElementClass, EList<T> list) {

		for (EObject modelElement : this.getModelElements()) {
			if (modelElementClass.isInstance(modelElement)) {
				list.add((T) modelElement);
			}
		}
		return list;
	}

	// end of custom code

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.PROJECT__MODEL_ELEMENTS:
			return ((InternalEList<?>) getModelElements()).basicRemove(otherEnd, msgs);
		case ModelPackage.PROJECT__CUT_ELEMENTS:
			return ((InternalEList<?>) getCutElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelPackage.PROJECT__MODEL_ELEMENTS:
			return getModelElements();
		case ModelPackage.PROJECT__CUT_ELEMENTS:
			return getCutElements();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelPackage.PROJECT__MODEL_ELEMENTS:
			getModelElements().clear();
			getModelElements().addAll((Collection<? extends EObject>) newValue);
			return;
		case ModelPackage.PROJECT__CUT_ELEMENTS:
			getCutElements().clear();
			getCutElements().addAll((Collection<? extends EObject>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelPackage.PROJECT__MODEL_ELEMENTS:
			getModelElements().clear();
			return;
		case ModelPackage.PROJECT__CUT_ELEMENTS:
			getCutElements().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelPackage.PROJECT__MODEL_ELEMENTS:
			return modelElements != null && !modelElements.isEmpty();
		case ModelPackage.PROJECT__CUT_ELEMENTS:
			return cutElements != null && !cutElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	// begin of custom code

	/**
	 * this methods implements the adapter interface which is needed by the
	 * navigator.
	 * 
	 * @param adapter
	 *            the adapter class
	 * @return the adapter
	 * @author shterev
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @generated NOT
	 * @see org.eclipse.emf.emfstore.common.model.Project#contains(org.eclipse.emf.ecore.EObject)
	 */
	public boolean contains(ModelElementId id) {
		if (!isCacheInitialized()) {
			initCaches();
		}
		return getIdToEObjectCache().containsKey(id);
	}

	private Map<ModelElementId, EObject> getIdToEObjectCache() {
		if (!isCacheInitialized()) {
			initCaches();
		}

		return idToEObjectCache;
	}

	private Set<EObject> getEObjectsCache() {
		if (!isCacheInitialized()) {
			initCaches();
		}

		return eObjectsCache;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#initCaches()
	 */
	public void initCaches() {

		if (isCacheInitialized()) {
			return;
		}

		for (EObject modelElement : getModelElements()) {
			// put model element into cache
			ModelElementId modelElementId = getIdForModelElement(modelElement);
			putIntoCaches(modelElement, modelElementId);

			// put children of model element into cache
			TreeIterator<EObject> it = modelElement.eAllContents();
			while (it.hasNext()) {
				EObject obj = it.next();
				ModelElementId id = getIdForModelElement(obj);
				putIntoCaches(obj, id);
				// TODO:PlainEObjectMode, these 2 lines are needed for
				// migration, currently the IDs is thus setted
				// twice
				// when adding a new model element
			}
		}

		// TODO: EM, changeNotifier moved to OperationRecorder
		if (changeNotifier == null) {
			changeNotifier = new EObjectChangeNotifier(this, this);
		}

		cachesInitialized = true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#initCaches(java.util.Map, java.util.Map)
	 */
	public void initCaches(Map<EObject, ModelElementId> eObjectToIdMap, Map<ModelElementId, EObject> idToEObjectMap) {
		// 1. maps setzen
		// 2. cacheinit auf true
		// 3. notifier erzeugen
		cachesInitialized = true;
		eObjectToIdCache = eObjectToIdMap;
		idToEObjectCache = idToEObjectMap;
		eObjectsCache = eObjectToIdMap.keySet();
		changeNotifier = new EObjectChangeNotifier(this, this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver#modelElementAdded(org.eclipse.emf.emfstore.common.model.Project,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public void modelElementAdded(final IdEObjectCollection project, final EObject eObject) {
		addModelElementAndChildrenToCache(eObject);

		EObjectChangeObserverNotificationCommand command = new EObjectChangeObserverNotificationCommand() {
			public void run(EObjectChangeObserver projectChangeObserver) {
				projectChangeObserver.modelElementAdded(project, eObject);
			}
		};
		notifyProjectChangeObservers(command);
	}

	private void notifyProjectChangeObservers(EObjectChangeObserverNotificationCommand command) {
		isNotifiying = true;
		for (ProjectChangeObserver projectChangeObserver : this.observers) {
			try {
				command.run(projectChangeObserver);
				// BEGIN SUPRESS CATCH EXCEPTION
			} catch (RuntimeException ex) {
				// END SUPRESS CATCH EXCEPTION
				if (exceptionThrowingObservers.contains(projectChangeObserver)) {
					if (!undetachableObservers.contains(projectChangeObserver)) {
						observersToRemove.add(projectChangeObserver);
						ModelUtil.logException(
							"Project Change Observer threw an exception again, it has been detached, UI may not update now: "
								+ projectChangeObserver.getClass().getName(), ex);
					} else {
						ModelUtil.logException(
							"Project Change Observer threw an exception again, but it will not be detached."
								+ projectChangeObserver.getClass().getName(), ex);
					}
				} else {
					exceptionThrowingObservers.add(projectChangeObserver);
					ModelUtil.logWarning("Project Change Observer threw an exception: "
						+ projectChangeObserver.getClass().getName(), ex);
				}
			}
		}
		isNotifiying = false;
		for (ProjectChangeObserver observer : this.observersToRemove) {
			removeProjectChangeObserver(observer);
		}
		this.observersToRemove.clear();
	}

	private void addModelElementAndChildrenToCache(EObject eObject) {

		List<ModelElementId> removableIds = new ArrayList<ModelElementId>();

		// first check whether ID should be reassigned
		ModelElementId id = newEObjectToIdMap.get(eObject);

		if (id == null) {
			// if not, create a new ID
			id = ModelFactory.eINSTANCE.createModelElementId();
		} else {
			removableIds.add(id);
		}

		if (isCacheInitialized()) {
			putIntoCaches(eObject, id);
		}

		for (EObject child : ModelUtil.getAllContainedModelElements(eObject, false)) {
			// first check whether ID should be reassigned
			ModelElementId childId = newEObjectToIdMap.get(child);

			if (childId == null) {
				// if not, create a new ID
				childId = ModelFactory.eINSTANCE.createModelElementId();
			} else {
				removableIds.add(childId);
			}

			if (isCacheInitialized()) {
				putIntoCaches(child, childId);
			}
		}

		// remove all IDs that are in use now
		newEObjectToIdMap.values().removeAll(removableIds);
	}

	private void putIntoCaches(EObject modelElement, ModelElementId modelElementId) {
		eObjectToIdCache.put(modelElement, modelElementId);
		idToEObjectCache.put(modelElementId, modelElement);
		if (!eObjectsCache.contains(modelElement)) {
			eObjectsCache.add(modelElement);
		}
	}

	private void removeModelElementAndChildrenFromCache(EObject modelElement) {

		ModelElementId id = getModelElementId(modelElement);
		deletedEObjectToIdMap.put(modelElement, id);
		newEObjectToIdMap.put(modelElement, id);

		removeFromCaches(modelElement);
		eObjectToIdCache.remove(modelElement);

		for (EObject child : ModelUtil.getAllContainedModelElements(modelElement, false)) {
			ModelElementId childId = getModelElementId(child);
			deletedEObjectToIdMap.put(child, childId);
			newEObjectToIdMap.put(child, childId);
			removeFromCaches(child);
			eObjectToIdCache.remove(child);
		}
	}

	/**
	 * Removes the given model element from the EObject cache and the
	 * idToEObject cache in case the caches have been initialized.
	 * 
	 * @param modelElement
	 *            the model element to be removed from the caches
	 */
	private void removeFromCaches(EObject modelElement) {
		if (isCacheInitialized()) {
			ModelElementId id = this.getModelElementId(modelElement);
			getEObjectsCache().remove(modelElement);
			getIdToEObjectCache().remove(id);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver#notify(org.eclipse.emf.common.notify.Notification,
	 *      org.eclipse.emf.emfstore.common.model.Project, org.eclipse.emf.ecore.EObject)
	 */
	public void notify(final Notification notification, final IdEObjectCollection project, final EObject modelElement) {
		EObjectChangeObserverNotificationCommand command = new EObjectChangeObserverNotificationCommand() {
			public void run(EObjectChangeObserver projectChangeObserver) {
				projectChangeObserver.notify(notification, project, modelElement);
			}
		};
		notifyProjectChangeObservers(command);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getModelElement(org.eclipse.emf.emfstore.common.model.ModelElementId)
	 */
	public EObject getModelElement(ModelElementId modelElementId) {

		if (!isCacheInitialized()) {
			initCaches();
		}

		EObject eObject = getIdToEObjectCache().get(modelElementId);

		return eObject != null ? eObject : ModelUtil.getSingleton(modelElementId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#addProjectChangeObserver(org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver)
	 */
	public void addProjectChangeObserver(ProjectChangeObserver projectChangeObserver) {
		initCaches();
		this.observers.add(projectChangeObserver);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#removeProjectChangeObserver(org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver)
	 */
	public void removeProjectChangeObserver(ProjectChangeObserver projectChangeObserver) {
		if (isNotifiying) {
			observersToRemove.add(projectChangeObserver);
			return;
		}
		this.observers.remove(projectChangeObserver);
		exceptionThrowingObservers.remove(projectChangeObserver);
		undetachableObservers.remove(projectChangeObserver);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#containsInstance(org.eclipse.emf.ecore.EObject)
	 */
	public boolean containsInstance(EObject modelElement) {
		return getEObjectsCache().contains(modelElement);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#deleteModelElement(org.eclipse.emf.ecore.EObject)
	 */
	public void deleteModelElement(final EObject modelElement) {
		if (!this.containsInstance(modelElement)) {
			throw new IllegalArgumentException("Cannot delete a model element that is not contained in this project.");
		}

		// remove cross references
		ModelUtil.deleteOutgoingCrossReferences(modelElement, true, false);
		ModelUtil.deleteIncomingCrossReferencesFromParent(modelElement, this, true, false);

		// remove containment
		EObject containerModelElement = ModelUtil.getContainerModelElement(modelElement);
		if (containerModelElement == null) {
			// removeModelElementAndChildrenFromCache(modelElement);
			// getEobjectsIdMap().remove(modelElement);
			this.getModelElements().remove(modelElement);
		} else {
			XMIResource res = (XMIResource) modelElement.eResource();
			EReference containmentFeature = modelElement.eContainmentFeature();
			if (containmentFeature.isMany()) {
				EList<?> containmentList = (EList<?>) containerModelElement.eGet(containmentFeature);
				containmentList.remove(modelElement);
			} else {
				containerModelElement.eSet(containmentFeature, null);
			}
			ModelUtil.removeModelElementAndChildrenFromResource(res, modelElement);
		}
	}

	/**
	 * Make a project change observer undetachable.
	 * 
	 * @param observer
	 *            the observer
	 */
	public void setUndetachable(ProjectChangeObserver observer) {
		undetachableObservers.add(observer);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#delete()
	 */
	public void delete() {
		final Project project = this;
		EObjectChangeObserverNotificationCommand command = new EObjectChangeObserverNotificationCommand() {
			public void run(EObjectChangeObserver projectChangeObserver) {
				ProjectChangeObserver observer = (ProjectChangeObserver) projectChangeObserver;
				observer.projectDeleted(project);
			}
		};
		notifyProjectChangeObservers(command);
	}

	/**
	 * Handle the removal of an element from the containment hierarchy.
	 * 
	 * @param projectImpl
	 *            the project
	 * @param modelElement
	 *            the model element
	 */
	public void modelElementRemoved(final IdEObjectCollection projectImpl, final EObject modelElement) {
		removeModelElementAndChildrenFromCache(modelElement);
		EObjectChangeObserverNotificationCommand command = new EObjectChangeObserverNotificationCommand() {
			public void run(EObjectChangeObserver projectChangeObserver) {
				projectChangeObserver.modelElementRemoved(projectImpl, modelElement);
			}
		};
		notifyProjectChangeObservers(command);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#getModelElementId(org.eclipse.emf.ecore.EObject)
	 */
	public ModelElementId getModelElementId(EObject eObject) {

		if (!eObjectToIdCache.containsKey(eObject) && !isCacheInitialized()) {

			if (containedModelElements == null) {
				containedModelElements = ModelUtil.getAllContainedModelElements(this, false);
			}

			if (!containedModelElements.contains(eObject)) {
				return null;
			}

			// EObject contained in project, load ID from resource
			try {
				Resource resource = eObject.eResource();

				// EM: is this a potential error case we have to consider?
				if (!(resource instanceof XMIResource)) {
					return null;
				}

				XMIResource xmiResource = (XMIResource) resource;
				xmiResource.load(null);
				ModelElementId modelElementId = ModelFactory.eINSTANCE.createModelElementId();

				String id = xmiResource.getID(eObject);
				if (id != null) {
					// change ID
					modelElementId.setId(id);
					eObjectToIdCache.put(eObject, modelElementId);
					return ModelUtil.clone(modelElementId);
				}

				// return new ID
				eObjectToIdCache.put(eObject, modelElementId);
				return ModelUtil.clone(modelElementId);

			} catch (IOException e) {
				throw new RuntimeException("Couldn't load resource for model element " + eObject);
			}
		}

		ModelElementId id = eObjectToIdCache.get(eObject);

		return id != null ? ModelUtil.clone(id) : ModelUtil.getSingletonModelElementId(eObject);
	}

	/**
	 * Retrieve the {@link ModelElementId} for an EObject.
	 * 
	 * @param deletedModelElement
	 *            the deleted EObject
	 * @return the {@link ModelElementId}
	 */
	public ModelElementId getDeletedModelElementId(EObject deletedModelElement) {

		ModelElementId id = deletedEObjectToIdMap.get(deletedModelElement);

		return id != null ? ModelUtil.clone(id) : ModelUtil.getSingletonModelElementId(deletedModelElement);
	}

	/**
	 * Get the deleted model element with the given id from the project.
	 * 
	 * @param modelElementId
	 *            the model element id
	 * @return the deleted model element or null if it is not in the project
	 */
	public EObject getDeletedModelElement(ModelElementId modelElementId) {
		for (Map.Entry<EObject, ModelElementId> entry : deletedEObjectToIdMap.entrySet()) {
			if (entry.getValue().equals(modelElementId)) {
				return entry.getKey();
			}
		}

		return ModelUtil.getSingleton(modelElementId);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.Project#addModelElement(org.eclipse.emf.ecore.EObject,
	 *      java.util.Collection)
	 */
	public void addModelElement(EObject newModelElement, Map<EObject, ModelElementId> map) {

		// since id is contained in map, all IDs should be cloned
		ModelElementId newModelElementId = ModelUtil.clone(map.get(newModelElement));

		// check whether the model element is already contained in the project
		if (contains(newModelElementId)) {
			throw new IllegalStateException("Model element ID " + newModelElementId + " already contained in project.");
		}

		for (Map.Entry<EObject, ModelElementId> entry : map.entrySet()) {
			EObject modelElement = entry.getKey();
			ModelElementId modelElementId = entry.getValue();
			newEObjectToIdMap.put(modelElement, modelElementId);
		}

		getModelElements().add(newModelElement);
	}

	/**
	 * Returns the ModelElementId for the given model element. If no such ID
	 * exists, a new one will be created.
	 * 
	 * @param modelElement
	 * @return the ModelElementId for the given modelelement
	 */
	private ModelElementId getIdForModelElement(EObject modelElement) {

		Resource resource = modelElement.eResource();

		if (resource != null && resource instanceof XMIResource) {
			// resource available, read ID
			XMIResource xmiResource = (XMIResource) resource;
			try {
				xmiResource.load(null);
			} catch (IOException e) {
				throw new RuntimeException("Resource of model element " + modelElement + " couldn't be loaded");
			}
			String id = xmiResource.getID(modelElement);
			if (id != null) {
				ModelElementId objId = ModelFactory.eINSTANCE.createModelElementId();
				objId.setId(id);
				return objId;
			}
		}

		// create new ID
		return ModelFactory.eINSTANCE.createModelElementId();
	}

	/**
	 * Copies the current project.
	 * 
	 * @return the copied project
	 */
	public Project copy() {

		if (this.changeNotifier != null) {
			this.changeNotifier.disableNotifications(true);
		}

		Copier copier = new ProjectCopier();
		ProjectImpl result = (ProjectImpl) copier.copy(this);
		result.cachesInitialized = true;
		copier.copyReferences();

		if (changeNotifier != null) {
			changeNotifier.disableNotifications(false);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.common.model.util.ProjectChangeObserver#projectDeleted(org.eclipse.emf.emfstore.common.model.Project)
	 */
	public void projectDeleted(Project project) {

	}

	/**
	 * @return
	 */
	public EObjectChangeNotifier getChangeNotifier() {
		return changeNotifier;
	}
}
