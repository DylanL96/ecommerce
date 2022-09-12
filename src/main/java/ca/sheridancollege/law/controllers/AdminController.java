package ca.sheridancollege.law.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ca.sheridancollege.law.beans.Grocery;
import ca.sheridancollege.law.database.DatabaseAccess;

@Controller
public class AdminController {
	  @Autowired
	  DatabaseAccess databaseAccess;
	  
	  @GetMapping("/admin")
	  public String getAdmin(Model model){
	    Grocery grocery = new Grocery();
	    model.addAttribute("grocery", grocery);
	    model.addAttribute("groceryList", databaseAccess.getGroceryList());
	    
	    return "admin/admin";
	  }
	  
	  @GetMapping("/admin/AddGrocery")
	  public String getAddGrocery(Model model){
	    Grocery grocery = new Grocery();
	    model.addAttribute("grocery", grocery); 
	    return "admin/addGrocery";
	  }
	  
	  @PostMapping("/admin/AddGrocery")
	  public String AddTeam(Model model, @ModelAttribute Grocery grocery){
	    model.addAttribute("grocery", grocery);
	    databaseAccess.addGrocery(
	      grocery.getGroceryName(),
	      grocery.getGroceryCategory(),
	      grocery.getGroceryPrice(),
	      grocery.getGroceryQuantity()

	      );
	      model.addAttribute("groceryList", databaseAccess.getGroceryList());
	    return "admin/addGrocery";
	  }
	  
	  @GetMapping("/admin/DeleteGroceryByID/{groceryID}")
	  public String DeleteGrocery(Model model, @PathVariable Long groceryID){
	    model.addAttribute("grocery", new Grocery());
	    databaseAccess.deleteGroceryByGroceryID(groceryID);
	    model.addAttribute("groceryList", databaseAccess.getGroceryList());
	    return "admin/admin";
	  }
	  
	  @GetMapping("/admin/EditGroceryByID/{groceryID}")
	  public String getEditGrocery(Model model, @PathVariable Long groceryID){
	    model.addAttribute("grocery", new Grocery());
	    Grocery grocery = databaseAccess.getGroceryListByGroceryID(groceryID).get(0);
	    model.addAttribute("grocery", grocery);
	    model.addAttribute("groceryList", databaseAccess.getGroceryList());
	    return "admin/editGrocery";
	  }

	  @PostMapping("/admin/EditGrocery")
	  public String postEditGrocery(Model model, @ModelAttribute Grocery grocery, RedirectAttributes redirAttrs){
	    databaseAccess.editGroceryByID(grocery);
	    model.addAttribute("groceryList", databaseAccess.getGroceryList());
	    redirAttrs.addFlashAttribute("message", "TEST");
	    model.addAttribute("successMessage", "Successfully Edited!");
	    return "admin/admin";
	  }

}
