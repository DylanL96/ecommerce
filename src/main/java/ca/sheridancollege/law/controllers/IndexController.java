package ca.sheridancollege.law.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.law.beans.Grocery;

@Controller
public class IndexController {
	@Autowired
	@Lazy
	private ca.sheridancollege.law.database.DatabaseAccess databaseAccess;
	
	@GetMapping("/")
	public String index(Model model, HttpSession session) {
	    Grocery grocery = new Grocery();
	    model.addAttribute("grocery", grocery);
	    model.addAttribute("groceryList", databaseAccess.getGroceryList());
		return "index";
	}
	
	@GetMapping("/register")
	public String getRegister() {
		return "register";
	}
	
	@PostMapping("/register")
	public String processRegistration(@RequestParam String name, @RequestParam String password){
		databaseAccess.addUser(name, password);
		long userId = databaseAccess.findUserAccount(name).getUserID();
		databaseAccess.addRole(userId, 2); // sets the role of every new registered user as "MEMBER"
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	
	@GetMapping("/access-denied")
	public String getAccessDenied(){
		return "/error/access-denied";
	}
	
    @GetMapping("/Sorted")
    public String DisplayResultsSorted(Model model, @ModelAttribute Grocery grocerySort){
    	Grocery grocery = new Grocery();
    	model.addAttribute(grocery);
	    if(grocerySort.getSortSelection().equals("grain")){
	    	model.addAttribute("groceryList", databaseAccess.sortGroceryByGrain());
	    } else if(grocerySort.getSortSelection().equals("meat")){
	    	model.addAttribute("groceryList", databaseAccess.sortGroceryByMeat());
	    } else if(grocerySort.getSortSelection().equals("vegetable")){
	    	model.addAttribute("groceryList", databaseAccess.sortGroceryByVegetable());
	    } else if(grocerySort.getSortSelection().equals("dairy")){
	    	model.addAttribute("groceryList", databaseAccess.sortGroceryByDairy());
		} else {
			model.addAttribute("groceryList", databaseAccess.sortGroceryByFruit());
		}
	    return "index";
	 }
	
}
