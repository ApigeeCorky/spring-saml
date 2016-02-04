package com.truedash.security.saml.web;

import java.util.Calendar;

import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceView;

import com.mongodb.WriteResult;
import com.truedash.security.exception.NoSuchResourceFound;
import com.truedash.security.exception.UnauthorizedException;
import com.truedash.security.saml.util.EncryptDecryptUtil;

@Controller
@RequestMapping("/redirect")
public class RedirectController {

	private final Logger log = LoggerFactory.getLogger(RedirectController.class);

	@Autowired
	private MongoOperations mongoOperations;

	@RequestMapping(value = "/401")
	public ModelAndView errorPage(HttpServletRequest request) {
		
		return new ModelAndView(new InternalResourceView("/401.jsp", true));
	}

	@RequestMapping(value = "/truedash")
	public ModelAndView generateMetadata(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication)
					throws UnauthorizedException, IllegalBlockSizeException, NoSuchResourceFound {

		if (authentication != null) {
			log.info("***** auth found  *** " + authentication.getName());
		} else {
			log.info("********No auth found redirecting to saml login page***");
			return new ModelAndView("redirect:/saml/login");
		}
		String userName = authentication.getName();
		// TODO: remove hard coded username and uncomment above line
		// String userName = "dariusz.zbik";
		// check collection exists

		try{
				if (mongoOperations.collectionExists("user")) {
					log.info("*****USER COLLECTION FOUND IN THE DB******");
					Query query = new Query();
					query.addCriteria(Criteria.where("username").is(userName));
					if (mongoOperations.exists(query, "user")) {
						log.info("Authorized username {} found in the request...", userName);
						// update document with query
						String samlKey = null;
		
						try {
							Calendar cal = Calendar.getInstance();
							samlKey = EncryptDecryptUtil.encrypt(userName + ":" + cal.getTimeInMillis());
						} catch (Exception e) {
							throw new IllegalBlockSizeException(userName + " encrypting failed");
						} 
		
						// Update update = new Update();
						// String encodedSamlKey = new
						// String(Base64.encodeBase64(samlKey.getBytes()));
						// Update.update("samlKey", encodedSamlKey);
						WriteResult results = mongoOperations.updateFirst(query, Update.update("samlKey", samlKey), "user");
		
						log.info(query.toString());
						log.info("ACKS flag " + results.wasAcknowledged());
		
						String url = "https://app.truedash.com/login?key=" + samlKey;
						log.info("user being redirected to " + url);
						return new ModelAndView("redirect:" + url);
		
					} else {
		
						log.error("Unauthorized username {} found in the request...", userName);
						// throw new UnauthorizedException(userName + " user not
						// authorized");
						return new ModelAndView(new InternalResourceView("/401.jsp", true));
					}
				} else {
					throw new NoSuchResourceFound("No collection found in db..");
				}
		}
			 catch (Exception e) {
					log.info("*****Some sort of error ****** "+e);
		}

	}
}