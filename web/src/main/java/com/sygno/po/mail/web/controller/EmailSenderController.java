package com.sygno.po.mail.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sygno.po.mail.service.IEmailSenderService;
import com.sygno.po.mail.web.dto.UserDTO;

@Controller
@RequestMapping(value = "/")
public class EmailSenderController {

	private static final String MAILTO = "src/main/resources/properties/mailTo.properties";
	private static final String MESSAGE = "message";
	@Autowired
	private IEmailSenderService emailSenderService;

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView send(final HttpServletRequest req, @Valid @ModelAttribute("formModel") final UserDTO formModel,
			final BindingResult result) {
		final Map<String, Object> hashMap = new HashMap<String, Object>();
		if (req.getMethod().equalsIgnoreCase("get")) {
			return new ModelAndView("index");
		}
		if (result.hasErrors()) {
			return new ModelAndView("index", hashMap);
		} else {
			try {
				emailSenderService.sendEmail(formModel.getFirstName(), formModel.getFirstName(), MAILTO);
				hashMap.put(MESSAGE, "Your message was sended.");
			} catch (MessagingException | IOException e) {
				hashMap.put(MESSAGE, "Your message was not sended. Try again later. " + e.getMessage());
				hashMap.put("formModel", formModel);
			}
			return new ModelAndView("index", hashMap);
		}
	}

}
