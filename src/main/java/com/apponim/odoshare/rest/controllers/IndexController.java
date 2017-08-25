package com.apponim.odoshare.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apponim.odoshare.rest.consts.ApplicationConsts;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String listAll(Model model) {
		
		model.addAttribute("version", ApplicationConsts.VERSION_NAME);

		return "home";
	}
}
