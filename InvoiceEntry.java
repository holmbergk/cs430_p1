// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class InvoiceEntry implements Serializable {
    private String productId;
    private float quantity;
    private float price;

    public InvoiceEntry(String productId, float quantity, float price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }
}

