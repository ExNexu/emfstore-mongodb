package org.eclipse.emf.emfstore.common.resourcefactory.mongodb;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipselabs.mongo.emf.MongoURIHandlerImpl;

public class MongoDBResource extends XMIResourceImpl {

	private static org.eclipse.emf.ecore.resource.URIHandler uriHandler;;

	public MongoDBResource(URI uri) {
		super(URI.createURI("mongo://127.0.0.1:27017/emf/" + uri.segment(uri.segmentCount() - 2) + "/"
			+ uri.lastSegment()));
		System.out.println("URI: " + uri);
	}

	@Override
	protected URIConverter getURIConverter() {
		URIConverter result = getDefaultURIConverter();
		if (uriHandler == null) {
			uriHandler = new MongoURIHandlerImpl();
			EList<org.eclipse.emf.ecore.resource.URIHandler> uriHandlers = result.getURIHandlers();
			uriHandlers.add(0, uriHandler);
		}
		return result;
	}

}
