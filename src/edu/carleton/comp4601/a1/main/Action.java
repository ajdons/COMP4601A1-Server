package edu.carleton.comp4601.a1.main;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.a1.dao.DocumentCollection;
import edu.carleton.comp4601.a1.dao.MongoDBManager;
import edu.carleton.comp4601.a1.model.Document;

public class Action {

	@Context 
	UriInfo uriInfo;
	@Context
	Request request;
	
	String id;
	
	MongoDBManager db;
	
	DocumentCollection docColl;
	
	public Action(UriInfo uriInfo, Request request, String id) throws UnknownHostException, MalformedURLException {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.db = new MongoDBManager(); 
		this.docColl = db.findAll();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocument() throws NumberFormatException, MalformedURLException {
		Document a = docColl.find(new Integer(id));
		if (a == null) {
			throw new RuntimeException("No such account: " + id);
		}
		return a;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocumentTag() throws NumberFormatException, MalformedURLException {
		Document a = docColl.find(new Integer(id));
		if (a == null) {
			throw new RuntimeException("No such tag: " + id);
		}
		return a;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putDocument(JAXBElement<Document> doc) throws MalformedURLException {
		Document c = doc.getValue();
		return putAndGetResponse(c);
	}

	@DELETE
	public void deleteDocument() throws NumberFormatException, MalformedURLException {
		if (!docColl.close(new Integer(id)))
			throw new RuntimeException("Document " + id + " not found");
	}
	

	private Response putAndGetResponse(Document doc) throws MalformedURLException {
		Response res;
		if (docColl.getModel().contains(doc.getId())) {
			//res = Response.noContent().build();
			res = Response.ok().build();
			
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		docColl.getModel().set(doc.getId(), doc);
		return res;
	}
	
	
}
