package edu.carleton.comp4601.a1.main;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.a1.dao.DocumentCollection;
import edu.carleton.comp4601.a1.dao.MongoDBManager;
import edu.carleton.comp4601.a1.model.Document;

@Path("/sda")
public class Main {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	MongoDBManager db;
	DocumentCollection docColl;

	private String name;

	public Main() throws MalformedURLException, UnknownHostException {
		name = "COMP4601 Searchable Document Archive";
		db = new MongoDBManager();
		docColl = db.findAll();
	}

	@GET
	public String printName() {
		return name;
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXML() {
		return "<?xml version=\"1.0\"?>" + "<sda> " + name + " </sda>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtml() {
		return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
				+ "</body></h1>" + "</html> ";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJSON() {
		return "{" + name + "}";
	}
	

	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addDocument(MultivaluedMap<String,String> multivaluedMap) throws UnknownHostException {
		Document doc = new Document(multivaluedMap);
		Response res;

		if(db.addDoc(doc)){
			res = Response.ok().build();
		}else{
			res = Response.noContent().build();
		}
		
		return res;
	}
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateDocument(@PathParam("id") String id, MultivaluedMap<String,String> multivaluedMap) throws UnknownHostException {
		Document doc = null;
		
		if (db.findDoc(new Integer(id)) != null){
			 doc = new Document(multivaluedMap);
		}
		
		System.out.println("updateDocument  "+ doc.getId());
		Response res;

		if(db.updateDoc(doc)){
			res = Response.ok().build();
		}else{
			res = Response.noContent().build();
		}
		
		return res;
	}
	@GET
	@Path("documents")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> getDocuments() throws MalformedURLException {
		List<Document> loa = new ArrayList<Document>();
		loa.addAll(docColl.getModel());
		
		return loa;
	}

	
	
	@GET
	@Path("{id}")
	@Produces(MediaType.TEXT_HTML)
	public String viewDocumentHTML(@PathParam("id") String id) throws NumberFormatException, UnknownHostException {
		Document doc = db.findDoc(new Integer(id));
		if (doc == null) {
			throw new RuntimeException("No such document with id:" + id);
		}
		System.out.println("viewDocumentHTML()");
		
		String html = "";
		
		html = html + "<html>";
		html = html + "<title>Document Information</title>";
		html = html + "<body><h1>Document:</body></h1>";
		html = html + "<body><h2>id: "+doc.getId()+"</body></h2>";
		html = html + "<body><h2>name: "+doc.getName()+"</body></h2>";
		html = html + "<body><h2>next: "+doc.getText()+"</body></h2>";
		
		if(doc.getLinks().size()!=0){
			html = html + "<body><h2>Links: ";
			for(String link: doc.getLinks())
				html = html + "<a href='"+ link +"'>" + link +"</a>"+"  ";
			html = html + "</body></h2></html>";
		}else{
			html = html + "<body><h2>Links: Link not found.</body></h2></html>";
		}
		
		html = html + "<body><h2>Tags: ";
		for(String tag: doc.getTags())
			html = html + "<a href='search/"+ tag +"'>" + tag +"</a>"+"  ";
		
		html = html + "</body></h2></html>";
		
		return html;
	}
	
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Document viewDocumentXML(@PathParam("id") String id) throws NumberFormatException, UnknownHostException {
		Document doc = db.findDoc(new Integer(id));
		if (doc == null) {
			throw new RuntimeException("No such document with id:" + id);
		}
		System.out.println("getDocumentXML()");
		return doc;
	}
	
	
	
	@GET
	@Path("search/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> searchDocumentWithTagsXML(@PathParam("tags") String tags) throws UnknownHostException, MalformedURLException{
		List<String> tagsList = new ArrayList<String>(Arrays.asList(tags.split(":")));
		
		DocumentCollection docColl = db.searchDocColl(tagsList);
		
		System.out.println("Tags: " + tagsList.toString());
		List<Document> loa = new ArrayList<Document>();
		loa.addAll(docColl.getModel());
		
		return loa;
		
	}
	
//	@GET
//	@Path("search/{tags}")
//	@Produces(MediaType.TEXT_HTML)
//	public String searchDocumentWithTagsHTML(@PathParam("tags") String tags) throws UnknownHostException, MalformedURLException{
//		List<String> tagsList = new ArrayList<String>(Arrays.asList(tags.split(":")));
//		
//		DocumentCollection docColl = db.searchDocColl(tagsList);
//		String html = "";
//		
//		if(docColl.getDocuments().size()!=0){
//			html = html + "<html>";
//			html = html + "<title>Documents Search Result</title>";
//			html = html + "<body><h1>Document(s) with tag(s): ";
//			for(String tag:tagsList)
//				html = html + tag + " ";
//			html = html + "</body></h1>";
//		
//			for(Document doc:docColl.getDocuments())
//				html = html +"<body><h2><a href='../"+ doc.getId() +"'>"+ doc.getName() + "</a></body></h2>";
//			html = html + "</html>";
//		}else{
//			html = html + "<html><title>Documents Search Result</title><body><h1>No documents found.</body></h1></html>";
//		}
//		
//		return html;
//	}
	
	
	@GET
	@Path("delete/{tags}")
	public Response deleteDocumentWithTags(@PathParam("tags") String tags) throws UnknownHostException{
		List<String> tagsList = new ArrayList<String>(Arrays.asList(tags.split(":")));
		System.out.println("deleteDocumentWithTags with tags: " + tagsList);
		Response res;
		
		if(db.removeSet(tagsList))
			res = Response.ok().build();
		else
			res = Response.noContent().build();
		
		return res;		
	}
	
	
	

}
