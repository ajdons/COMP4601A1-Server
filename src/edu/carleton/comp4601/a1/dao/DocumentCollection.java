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
		ConcurrentHashMap<Object, Object> d = new ConcurrentHashMap<>();
		List<String> tags = new ArrayList<String>();
		tags.add("tag1");
		tags.add("tag2");
		tags.add("tag3");
		
		List<String> links = new ArrayList<String>();
		links.add("http://www.link1.com");
		links.add("http://www.link2.com");
		links.add("http://www.link3.com");
		
		d.put("name", "Richard");
		d.put("id", 0);
		d.put("text", "Some text" );
		d.put("tags", tags);
		d.put("links", links);
		
		Document doc = new Document(d);
		
		ConcurrentHashMap<Object, Object> d2 = new ConcurrentHashMap<>();
		List<String> tags2 = new ArrayList<String>();
		tags2.add("tag1");
		tags2.add("tag2");
		tags2.add("tag3");
		
		List<String> links2 = new ArrayList<String>();
		links2.add("http://www.link1.com");
		links2.add("http://www.link2.com");
		links2.add("http://www.link3.com");
		
		d2.put("name", "Adam");
		d2.put("id", 1);
		d2.put("text", "Some text" );
		d2.put("tags", tags2);
		d2.put("links", links2);
		
		Document doc2 = new Document(d2);
		
		documents.add(doc);
		documents.add(doc2);
		
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
	
	public Document find(int id){
		return documents.get(new Integer (id));
	}
	
	public Document open(String name, int id, int score, String text, ArrayList<String> tags, ArrayList<String> links){
		ConcurrentHashMap<Object, Object> d = new ConcurrentHashMap<>();
		d.put("name", (String) name);
		d.put("id", (int) id);
		d.put("score",(int) score);
		d.put("text", (String) text );
		d.put("tags", (ArrayList<String>) tags);
		d.put("links", (ArrayList<String>) links);
		
		Document doc = new Document(d);
		
		documents.add(doc);
		
		return doc;
	}
	
	public boolean close(int id) {
		if (find(id) != null) {
			Integer no = new Integer(id);
			documents.remove(no);
			return true;
		}
		else
			return false;
	}
	
	public List<Document> getModel() {
		return documents;
	}
	public int size() {
		return documents.size() + 1;
	}
	
}