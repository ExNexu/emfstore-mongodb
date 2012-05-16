package org.eclipse.emf.emfstore.common.resourcefactory.mongodb;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;


public class MongoDBResourceFactory extends XMIResourceFactoryImpl {
	@Override
	public Resource createResource(URI uri) {
		return new MongoDBResource(uri);
	}

}
