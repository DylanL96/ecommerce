package ca.sheridancollege.law.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@GetMapping("/user")
	public String userIndex(HttpSession session) {
		session.setAttribute("sessionID", session.getId());
		return "user/index";
	}
}
