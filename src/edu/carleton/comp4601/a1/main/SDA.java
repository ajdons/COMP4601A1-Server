package edu.carleton.comp4601.a1.main;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.carleton.comp4601.a1.dao.DocumentCollection;
import edu.carleton.comp4601.a1.model.Document;

@Path("/sda")
public class SDA {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String name;

	public SDA() throws MalformedURLException {
		name = "COMP4601 Searchable Document Archive";
		DocumentCollection.getInstance();
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
	
	@GET
	@Path("documents")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> getDocuments() throws MalformedURLException {
		List<Document> loa = new ArrayList<Document>();
		loa.addAll(DocumentCollection.getInstance().getModel());
		return loa;
	}
	
	@Path("{doc}")
	public Action getAccount(@PathParam("doc") String id) {
		return new Action(uriInfo, request, id);
	}
	

}
