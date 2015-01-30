package edu.carleton.comp4601.a1.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DocumentNotFoundException extends WebApplicationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5892099411957762040L;
	
	public DocumentNotFoundException(){
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
	}
	
	public DocumentNotFoundException(String message){
		super(Response.status(Response.Status.NOT_FOUND).entity(message)
				.type(MediaType.TEXT_PLAIN).build());
		
	}

}
