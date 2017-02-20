// Author: Kyle Holmberg
import java.util.*;
import java.io.*;

public class InvoiceEntry implements Serializable {
	private String productId;
	private int quantity;
	private float price;

	public InvoiceEntry(String productId, int quantity, float price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getPrice() {
		return price;
	}

	public String getProductId() {
		return productId;
	}
}
