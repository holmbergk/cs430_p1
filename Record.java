// Author: Ji Zhao
import java.util.*;
import java.lang.*;
import java.io.*;

public class Record implements Serializable {
	private Product product;
	private int quantity;

	public Record(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public String toString() {
		return "product: " + product.toString() + ", quantity: " + quantity;
	}
}
