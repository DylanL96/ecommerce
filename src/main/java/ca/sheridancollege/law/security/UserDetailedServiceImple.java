package ca.sheridancollege.law.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.law.database.DatabaseAccess;

@Service
public class UserDetailedServiceImple implements UserDetailsService{

  @Autowired
	@Lazy
  DatabaseAccess da;
  @Override
  // UserDetails is a Spring User.
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Find the user based on the user name
    // This User is coming from User class we created in beans package.
	  			ca.sheridancollege.law.beans.User user = da.findUserAccount(username);
				//If the user doesn't exist throw an exception
				if (user == null) {
					System.out.println("User not found:" + username);
					throw new UsernameNotFoundException
						("User " + username + " was not found in the database");
				}
        // If user does exist;
				//Get a list of roles of THAT user. 
				List<String> roleNames = da.getRolesByID(user.getUserID());
				//Change the list of roles into a list of GrantedAuthority
				List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        // then we add simpleGrantedAuthority.
				if (roleNames != null) {
					for(String role: roleNames) {
						grantList.add(new SimpleGrantedAuthority(role));
					}
				}
				//Create a user based on the information above.
        // This user is user object from UserDetails.
				UserDetails userDetails = (UserDetails)new User(user.getUserName(),user.getEncryptedPassword(), grantList);		
				
        // This class is a Spring user. This will replace the old user we did in the SecurityConfig
				return userDetails; 

	}

  
  
}
