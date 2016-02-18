package com.maven.spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class HelloController {

	@RequestMapping("/hello")
	public ModelAndView hello() {

		ModelAndView mv = new ModelAndView();
		mv.addObject("spring", "spring mvc");
		mv.setViewName("hello");
		return mv;

	}
	
	@RequestMapping("/greet")
	public ModelAndView greet() {

		ModelAndView mv = new ModelAndView();
		mv.addObject("spring", "spring mvc");
		mv.setViewName("greet");
		return mv;

	}

}