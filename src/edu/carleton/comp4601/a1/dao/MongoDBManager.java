package edu.carleton.comp4601.a1.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import edu.carleton.comp4601.a1.model.Document;

public class MongoDBManager {
	DBCollection coll;
	
	public MongoDBManager() throws UnknownHostException{
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB( "comp4601a1db" );
		coll =	db.getCollection("documents");
	}
	
	
	public void addObject() throws MalformedURLException{
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
		
		System.out.println("Adding document..");
		coll.insert(doc);
	}
	
	public void findObject(){
		BasicDBObject query	= new BasicDBObject("id", 0);	
		DBCursor cursor = coll.find(query);
		
		coll.setObjectClass(Document.class); 
		
		Document obj = (Document) cursor.next();
		int id = (Integer) obj.get("id");
		System.out.println("Document id:" + id );
	}
	
	public void updateObject(){
		BasicDBObject query = new BasicDBObject("id", 0);
		BasicDBObject updateObj = new BasicDBObject();
		
		updateObj.put("id", 2);
		updateObj.put("name", "adam");
		updateObj.put("text", "updated");
		
		System.out.println("Updating..");
		coll.update(query, updateObj);
	}
	
	public void removeObject(){		
		BasicDBObject query = new BasicDBObject("id", 2);
		DBCursor cursor = coll.find(query);	
		coll.setObjectClass(Document.class);	
		Document obj = (Document) cursor.next();
	
		System.out.println("Deleting document " + obj.getId());
		coll.remove(obj);	
	}
	
	public static void main(String args[]) throws UnknownHostException, MalformedURLException{
		MongoDBManager db = new MongoDBManager();
		db.addObject();
		db.findObject();
		db.updateObject();
		db.removeObject();
	}
	

}
