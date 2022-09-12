package ca.sheridancollege.law.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	  private Long userID;
	  private String userName;
	  private String encryptedPassword;
}
