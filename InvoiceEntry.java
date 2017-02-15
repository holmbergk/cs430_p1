// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class InvoiceEntry implements Serializable {
    private Product product;
    private float quantity;
    private float price;

    public InvoiceEntry(Product product, float quantity, float price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }
}

