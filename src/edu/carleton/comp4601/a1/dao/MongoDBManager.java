package edu.carleton.comp4601.a1.dao;


import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.BSONObject;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import edu.carleton.comp4601.a1.model.Document;

public class MongoDBManager {
	DBCollection coll;
	
	public MongoDBManager() throws UnknownHostException{
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB( "comp4601a1db" );
		coll =	db.getCollection("documents");
	}
	
//	public void addDoc(Integer id, Integer score, String name, String text,
//			ArrayList<String> tags, ArrayList<String> links){
//		//Check if doc id is already in the database
//		if (findDoc(id) == null){
//			ConcurrentHashMap<Object, Object> d = new ConcurrentHashMap<>();	
//			d.put("id", id);
//			d.put("score", score);
//			d.put("name", name);
//			d.put("text", text);
//			d.put("tags", tags);
//			d.put("links", links);
//		
//			Document doc = new Document(d);
//			
//			System.out.println("Adding document..");
//			coll.insert(doc);
//		}  else {	
//			System.out.println("Document was aldready added");
//		}
//		
//		
//	};
	
	public boolean addDoc(Document doc){
		Document found = findDoc(doc.getId());
		if(found == null){
			coll.insert(doc);
			return true;
		}else{
			return false;
		}	
	}
	
	public Document findDoc(int id){
		BasicDBObject query	= new BasicDBObject("id", id);	
		DBCursor cursor = coll.find(query);
		
		coll.setObjectClass(Document.class); 
		
		if(cursor.hasNext()){
			Document obj = (Document) cursor.next();	 
			int objId = (Integer) obj.get("id");
			System.out.println("Document id:" +objId );
			cursor.close();
			return obj;
		} else {
			System.out.println("Couldn't find doc");
			cursor.close();
			return null;	
		}
	}
	
	public void removeDoc(int id){		
		BasicDBObject query = new BasicDBObject("id", id);
		DBCursor cursor = coll.find(query);	
		coll.setObjectClass(Document.class);	
		Document obj = (Document) cursor.next();
	
		System.out.println("Deleting document " + obj.getId());
		coll.remove(obj);	
		
		cursor.close();
	}
	
	public void updateDoc(int id, int score, String name, String text,
		ArrayList<String> tags, ArrayList<String> links){
		
		BasicDBObject query = new BasicDBObject("id", id);
		BasicDBObject updateObj = new BasicDBObject();
		
		
		updateObj.put("id", id);
		updateObj.put("score", score);
		updateObj.put("name", name);
		updateObj.put("text", text);
		updateObj.put("tags", tags);
		updateObj.put("links", links);
		
		System.out.println("Updating..");
		
		coll.update(query, updateObj);
		
	}

	public DocumentCollection findAll() throws MalformedURLException{
		DocumentCollection docColl = new DocumentCollection();
		docColl.setDocuments(new ArrayList<Document>());
		DBCursor cursor=coll.find();
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			Integer id = (Integer) obj.get("id");
			Document doc = findDoc(id);
			docColl.getDocuments().add(doc);
		}
		return docColl;
	}
	
	public DocumentCollection searchDocColl(List<String> tags) throws MalformedURLException{
		DocumentCollection docColl = new DocumentCollection();
		docColl.setDocuments(new ArrayList<Document>());
		DBCursor cursor=coll.find();
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			Integer id = (Integer) obj.get("id");
			Document doc = findDoc(id);
			if(doc.getTags().containsAll(tags)){
				docColl.getDocuments().add(doc);
			}
		}
		return docColl;
	}
	
	public boolean removeSet(List<String> tags){
		boolean res = false;
		DBCursor cursor=coll.find();
		while(cursor.hasNext()){
			DBObject obj = cursor.next();
			Integer id = (Integer) obj.get("id");
			Document doc = findDoc(id);
			if(doc.getTags().containsAll(tags)){
				removeDoc(id);
				res = true;
			}
		}
		
		return res;
	}
	
	public static void main(String args[]) throws UnknownHostException, MalformedURLException{
		MongoDBManager db = new MongoDBManager();
//		
//		ArrayList<String> t = new ArrayList<String>();
//		t.add("tag1");
//		t.add("tag2");
//		t.add("tag3");
//		
//		ArrayList<String> l = new ArrayList<String>();
//		l.add("http://www.richardison.com");
//		l.add("http://www.adamdonegan.com");
//		
//		db.addDoc(1, 2, "Richard", "text text text", t, l);
//		db.findDoc(1);
//		db.addDoc(2, 2, "Richard", "text text text", t, l);
//		db.findDoc(2);
//		db.addDoc(3, 2, "Richard", "text text text", t, l);
//		db.findDoc(3);
//		db.addDoc(1, 2, "Richard", "text text text", t, l);
//		db.addDoc(4, 2, "Richard", "text text text", t, l);
//		db.addDoc(5, 2, "Richard", "text text text", t, l);
//		db.addDoc(16, 2, "Richard", "text text text", t, l);
		
		//db.updateDoc(2, 999, "Adam", "text2", t, l);
		
	//	db.removeDoc(3);
		
		
		//ArrayList<Document> list = db.findAllDocs();
		
	//	for (Document d : list){
	//		System.out.print(d.toString());
	//	}
		
	}
	

}
