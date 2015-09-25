package com.truedash.security.saml.web;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TruedashSAMLController {

	private final Logger log = LoggerFactory.getLogger(TruedashSAMLController.class);

	@RequestMapping(value = "/user")
	public ModelAndView metadataList() throws MetadataProviderException {
		log.info("******inside custom handler****");
		ModelAndView model = new ModelAndView("index.jsp");

		return model;

	}
}
