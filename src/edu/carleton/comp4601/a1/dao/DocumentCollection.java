package edu.carleton.comp4601.a1.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.carleton.comp4601.a1.model.Document;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentCollection {
	@XmlElement(name="documents")
	private List<Document> documents;
	private static DocumentCollection instance;
	
	public DocumentCollection() throws MalformedURLException{
		documents =  new ArrayList<Document>();
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	
	public static void setInstance(DocumentCollection instance) {
		DocumentCollection.instance = instance;
	}
	
	public static DocumentCollection getInstance() throws MalformedURLException {
		if (instance == null)
			instance = new DocumentCollection();
		return instance;
	}

	
	public List<Document> getModel() {
		return documents;
	}
	public int size() {
		return documents.size() + 1;
	}
	
}
