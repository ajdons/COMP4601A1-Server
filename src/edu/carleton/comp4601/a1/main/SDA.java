package edu.carleton.comp4601.a1.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.a1.dao.DocumentCollection;
import edu.carleton.comp4601.a1.dao.MongoDBManager;
import edu.carleton.comp4601.a1.model.Document;

@Path("/sda")
public class SDA {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	MongoDBManager db;
	DocumentCollection docColl;

	private String name;

	public SDA() throws MalformedURLException, UnknownHostException {
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
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newDocument(@FormParam("id") Integer id,
			@FormParam("score") Integer score,
			@FormParam("name") String name,
			@FormParam("text") String text,
			@FormParam("tags") String tag,
			@FormParam("links") String link,
			@Context HttpServletResponse servletResponse) throws IOException {

		
		String newName = name;
		if (newName == null)
			newName = "";
		
		String newText = text;
		if (newText == null)
			newText = "";
		
		ArrayList<String> t = new ArrayList<String>();
		t.add(tag);
		
		ArrayList<String> l = new ArrayList<String>();
		l.add(link);
		
		
		String newTag = tag;
		if (newTag == null)
			newTag = "";
		
		String newLink = link;
		if (newLink == null)
			newLink = "";

		int newId = new Integer(id).intValue();
		int newScore = new Integer(score).intValue();
		
		docColl.open(newName, newId, newScore, newText, t, l);
		
		db.addDoc(newId, newScore, newName, newText, t, l);

		servletResponse.sendRedirect("../create_document.html");
	}
	
	@GET
	@Path("documents")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> getDocuments() throws MalformedURLException {
		List<Document> loa = new ArrayList<Document>();
		loa.addAll(docColl.getModel());
		
		return loa;
	}
	
	@Path("{doc}")
	public Action getAccount(@PathParam("doc") String id) throws UnknownHostException {
		return new Action(uriInfo, request, id);
	}
	

}
