package org.eclipse.emf.emfstore.common.resourcefactory.mongodb;

import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.emfstore.common.EMFStoreResource;
import org.eclipselabs.mongo.emf.MongoURIHandlerImpl;

public class MongoDBResource extends XMIResourceImpl {

	private static org.eclipse.emf.ecore.resource.URIHandler uriHandler;;

	public MongoDBResource(URI uri_) {
		super(mongoDbUriFromEmfUri(uri_));
	
		System.out.println("URI-: " + uri_);
	}
	
	private static URI mongoDbUriFromEmfUri(URI uri) {
		if (uri.toString().startsWith("mongo")) {
			return uri;
		}
	
		return URI.createURI("mongo://127.0.0.1:27017/emf/objects/"
			+ uri.devicePath().replace("/", "-"));
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
