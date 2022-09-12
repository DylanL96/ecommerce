package ca.sheridancollege.law.database;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.law.beans.Cart;
import ca.sheridancollege.law.beans.Grocery;
import ca.sheridancollege.law.beans.PreviousCart;
import ca.sheridancollege.law.beans.User;

@Repository
public class DatabaseAccess {

  @Autowired
  protected NamedParameterJdbcTemplate jdbc;

///////////////////////////////////////////////USER RELATED////////////////////////////////////////////////////////////////////////
  	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user WHERE userName=:userName";
		parameters.addValue("userName", userName);
		// this will query our table and try to find collection of users that match. in this case, there should be only 1 user in this collection.
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters, new BeanPropertyRowMapper<User>(User.class)); 
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void addUser(String userName, String password) {
		// this line we add to use named parameters
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO SEC_User "
		+ "(userName, encryptedPassword, ENABLED)"
		+ " VALUES (:userName, :encryptedPassword, 1)";
		parameters.addValue("userName", userName);
		parameters.addValue("encryptedPassword",
		passwordEncoder.encode(password));
		jdbc.update(query, parameters);
	
	}

	public void addRole(long userID, long roleID) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_role (userID, roleID)"
		+ "values (:userID, :roleID);";
		parameters.addValue("userID", userID);
		parameters.addValue("roleID", roleID);
		jdbc.update(query, parameters);
	}
////////////////////////////////////////GROCERY RELATED////////////////////////////////////////////////////////////////////////////////
	
	public void addGrocery(String groceryName, String groceryCategory, double groceryPrice, int groceryQuantity){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();

	    String query = "INSERT INTO GROCERYLIST(groceryName, groceryCategory, groceryPrice, groceryQuantity) VALUES(:groceryName1, :groceryCategory1, :groceryPrice1, :groceryQuantity1)";
	    namedParameters.addValue("groceryName1", groceryName);
	    namedParameters.addValue("groceryCategory1", groceryCategory);
	    namedParameters.addValue("groceryPrice1", groceryPrice);
	    namedParameters.addValue("groceryQuantity1", groceryQuantity);
	    jdbc.update(query, namedParameters);
	}
	  
	 public void deleteGroceryByGroceryID(Long groceryID){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "DELETE FROM GROCERYLIST WHERE groceryID=:groceryID";
	    namedParameters.addValue("groceryID", groceryID);
	    jdbc.update(query, namedParameters);
	  }
	  
	 public void editGroceryByID(Grocery grocery){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "UPDATE GROCERYLIST SET groceryName=:groceryName, groceryCategory=:groceryCategory, groceryPrice=:groceryPrice, groceryQuantity=:groceryQuantity WHERE groceryID=:groceryID";
	    namedParameters.addValue("groceryID", grocery.getGroceryID());
	    namedParameters.addValue("groceryName", grocery.getGroceryName());
	    namedParameters.addValue("groceryCategory", grocery.getGroceryCategory());
	    namedParameters.addValue("groceryPrice", grocery.getGroceryPrice());
	    namedParameters.addValue("groceryQuantity", grocery.getGroceryQuantity());
	    jdbc.update(query, namedParameters);
	 }
	 
	 public void decrementGroceryQuantity(Long groceryID) {
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 String query = "UPDATE GROCERYLIST SET groceryQuantity = groceryQuantity - 1 WHERE groceryID=:groceryID AND groceryQuantity > 0";
		 namedParameters.addValue("groceryID", groceryID);
		 jdbc.update(query, namedParameters);
	 }
/////////////////////////////////////////CART RELATED///////////////////////////////////////////////////////////////////////////////

	 
	 
	public void addGroceryToCart(Long groceryID) {
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 String query = "SELECT * FROM CART WHERE groceryID=:groceryID"; 
		 namedParameters.addValue("groceryID", groceryID);
		 List<Cart> cartList = (List<Cart>)jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Cart>(Cart.class));
		 switch(cartList.size()) {
		 		 // if the cartList is empty, then it will add data from groceryList table to Cart table with query1.
			case 0:
				 String query1 = "INSERT INTO CART SELECT groceryID, groceryName, groceryCategory, groceryPrice, groceryQuantity FROM groceryLIST where groceryID=:groceryID";
				 namedParameters.addValue("groceryID", groceryID);
				 jdbc.update(query1, namedParameters);
				 
				 // update groceryQuantity to be 1 in the cart because we are adding to the cart
				 String updateQuery1 = "UPDATE CART SET groceryQuantity=:groceryQuantity WHERE groceryID = :groceryID";
				 namedParameters.addValue("groceryID", groceryID);	
				 namedParameters.addValue("groceryQuantity", 1);	
				 jdbc.update(updateQuery1, namedParameters);
				 break;
			default: 
				// if the cartList is not empty, update groceryQuantity + 1 with every click of the add cart button
				 String updateQuery2 = "UPDATE CART SET groceryQuantity = groceryQuantity + 1 WHERE groceryID = :groceryID";
				 namedParameters.addValue("groceryID", groceryID);	
				 jdbc.update(updateQuery2, namedParameters);
				
		}

	}
	 
	public void addPreviousCartToCurrentCart(String sessionToken) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO CART (groceryName, groceryCategory, groceryPrice, groceryQuantity) SELECT groceryName, groceryCategory, groceryPrice, groceryQuantity FROM PREVIOUSCART";
		namedParameters.addValue("sessionToken", sessionToken);
		jdbc.update(query, namedParameters);
	}

		
	public void addPreviousCartToTable(String sessionToken){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO PREVIOUSCART (groceryName, groceryCategory, groceryPrice, groceryQuantity) SELECT groceryName, groceryCategory, groceryPrice, groceryQuantity FROM CART";
		jdbc.update(query, namedParameters);

		String initialQuery = "UPDATE PREVIOUSCART SET sessionToken = :sessionToken";
		namedParameters.addValue("sessionToken", sessionToken);
		jdbc.update(initialQuery, namedParameters);
	}
		
	public void deleteCart(){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM CART";
		jdbc.update(query, namedParameters);
	}
	 
	 
  
///////////////////////////////////////////LISTS/////////////////////////////////////////////////////////////////////////////

	 public List<Grocery> getGroceryListByGroceryID(Long groceryID){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryID=:groceryID";
	    namedParameters.addValue("groceryID", groceryID);
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	  }

	
	 public List<Grocery> getGroceryList(){
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 String query = "SELECT * FROM GROCERYLIST";
		 return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	}
	 
	 public List<Cart> getCartList(){
		 MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		 String query = "SELECT DISTINCT groceryName, groceryCategory, groceryPrice, groceryQuantity FROM CART";
		 return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Cart>(Cart.class));
	}
	 
	 public List<String> getRolesByID(long userID) {
		 ArrayList<String> roles = new ArrayList<String>();
		 MapSqlParameterSource parameters = new MapSqlParameterSource();
		  // We need to match roleID to roleName. We need to query for the specific userId from the  USER_ROLE table and get the roles associated with it from the SEC_ROLE table. This query will do that. 
		  // Then, we want to read the column name (role name) and get the result and store it in a list of strings, and then we return the list of roles
		 String query = "SELECT user_role.userID, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleID "
				+ "AND userID=:userID";
		 parameters.addValue("userID", userID);
		 List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		 for (Map<String, Object> row : rows) {
			 roles.add((String)row.get("roleName"));
		 }
		 return roles;
	}
	  
	public List<PreviousCart> retrievePreviousOrders(String sessionToken){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT DISTINCT * FROM PREVIOUSCART WHERE sessionToken = :sessionToken";
		namedParameters.addValue("sessionToken", sessionToken);
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<PreviousCart>(PreviousCart.class));
	}

	public List<Cart> retrievePreviousOrdersOnCurrentCart(String sessionToken){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT DISTINCT groceryName, groceryCategory, groceryPrice, groceryQuantity FROM CART";
		namedParameters.addValue("sessionToken", sessionToken);
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Cart>(Cart.class));
	}

	public List<Grocery> sortGroceryByGrain(){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryCategory LIKE '%Grains%'";
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	 }
	
	public List<Grocery> sortGroceryByMeat(){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryCategory LIKE '%Meat%'";
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	 }
	
	public List<Grocery> sortGroceryByVegetable(){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryCategory LIKE '%Vegetable%'";
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	 }
	
	public List<Grocery> sortGroceryByDairy(){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryCategory LIKE '%Dairy%'";
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	 }
	
	public List<Grocery> sortGroceryByFruit(){
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    String query = "SELECT * FROM GROCERYLIST WHERE groceryCategory LIKE '%Fruit%'";
	    return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Grocery>(Grocery.class));
	 }
	
		
}

