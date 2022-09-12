package ca.sheridancollege.law.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousCart {
	  private String sessionToken;
	  private String groceryName;
	  private String groceryCategory;
	  private double groceryPrice;
	  private int groceryQuantity;
}
