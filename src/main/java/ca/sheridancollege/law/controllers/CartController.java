package ca.sheridancollege.law.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


import ca.sheridancollege.law.beans.Grocery;

@Controller
public class CartController {
	@Autowired
	@Lazy
	private ca.sheridancollege.law.database.DatabaseAccess databaseAccess;
	
	// calculate total price for the cart
	private double calculateTotal(double total) {
		for(int i = 0; i < databaseAccess.getCartList().size(); i++) {
	    	total += databaseAccess.getCartList().get(i).getGroceryPrice() *  databaseAccess.getCartList().get(i).getGroceryQuantity();
	    }
		return total;
	}

	@GetMapping("/cart")
	public String cartIndex(Model model, HttpSession session) {
	    double total = 0.0;
	    total = calculateTotal(total);
	    model.addAttribute("cartList", databaseAccess.getCartList());
	    model.addAttribute("previousCart", databaseAccess.retrievePreviousOrders(session.getId()));
	    model.addAttribute("currentCartWithPreviousCart", databaseAccess.retrievePreviousOrdersOnCurrentCart(session.getId()));
	    model.addAttribute("total", total);
		return "cart";
	}
	
	@GetMapping("/cart/{groceryID}")
	public String addGroceryToCart(Model model, @ModelAttribute Grocery grocery, @PathVariable Long groceryID, HttpSession session) {
		databaseAccess.addGroceryToCart(groceryID);
		databaseAccess.decrementGroceryQuantity(groceryID);
		model.addAttribute("groceryList", databaseAccess.getGroceryList());
		return "redirect:/";
	}
	
	@GetMapping("/cart/submit")
	public String submitOrder(HttpSession session) {
		databaseAccess.addPreviousCartToTable(session.getId());
		databaseAccess.deleteCart();
		return "submitOrder";
	}
	
	@GetMapping("/cart/previousCart")
	public String previousCart(HttpSession session, Model model, @ModelAttribute Grocery grocery){
		databaseAccess.retrievePreviousOrders(session.getId());
		model.addAttribute("previousCartList", databaseAccess.retrievePreviousOrders(session.getId()));
		return "previousCart";
	}
	
	@GetMapping("/cart/previousCart/addPreviousCartToCurrent")
	public String addPreviousCartToCurrent(HttpSession session) {
		databaseAccess.addPreviousCartToCurrentCart(session.getId());
		return "redirect:/";
	}
	
	
}
