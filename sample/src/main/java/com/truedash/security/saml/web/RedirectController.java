package com.truedash.security.saml.web;

import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.ws.wssecurity.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.truedash.security.exception.NoSuchResourceFound;
import com.truedash.security.exception.UnauthorizedException;
import com.truedash.security.saml.util.EncryptDecryptUtil;

@Controller
@RequestMapping("/redirect")
public class RedirectController {
	
	 private final Logger log = LoggerFactory.getLogger(RedirectController.class);
	 
	 @Autowired
	 private MongoOperations mongoOperations;
	 
	@RequestMapping(value = "/truedash")
    public String generateMetadata(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UnauthorizedException, IllegalBlockSizeException, NoSuchResourceFound {
		
		if(authentication != null) {
			log.info("***** auth found  *** " +authentication.getName());
		}else{
			log.info("********No auth found redirecting to saml login page***");
			 return "redirect:/saml/login";
		}
		String userName = authentication.getName();
		//check collection exists
		if (mongoOperations.collectionExists("user")) {
			//mongoOperations.dropCollection(Person.class);
			log.info("*****USER COLLECTION FOUND IN THE DB******");
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is("dariusz.zbik"));
			if(mongoOperations.exists(query, "user")){
				log.info("Authorized username {} found in the request...", userName);
			}else{
				
				log.error("Unauthorized username {} found in the request...", userName);
				throw new UnauthorizedException(userName + " user not authorized");
			}
		} else{
			throw new NoSuchResourceFound("No collection found in db..");
		}
		
		MongoClient mongoClient = new MongoClient("172.17.2.214", 27017);
		DB db = mongoClient.getDB("datawarehouse");
		
		
		DBCollection coll = db.getCollection("user");
		
		BasicDBObject query = new BasicDBObject("username", userName);	
		
		DBObject user = coll.findOne(query);		
		
		if (user == null) {
			throw new UnauthorizedException(userName + " user not authorized");
		}

		BasicDBObject newDocument = new BasicDBObject();
		String key = null; 
        try {
        	key = EncryptDecryptUtil.encrypt(userName);        	  
        } catch (Exception e) {
        	throw new IllegalBlockSizeException(userName + " encrypting failed");
        }
				
		newDocument.append("$set", new BasicDBObject().append("samlKey", key));		

		coll.update(query, newDocument);
		
		String url = "https://dev.truedash.com/login?key="+ key;
		
	    return "redirect:" + url;

    }
}