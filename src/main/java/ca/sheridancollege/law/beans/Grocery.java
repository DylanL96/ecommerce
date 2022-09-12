package ca.sheridancollege.law.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grocery {
	private Long groceryID;
	private String groceryName;
	private String groceryCategory;
	private double groceryPrice;
	private int groceryQuantity;
	private String sortSelection;
}
