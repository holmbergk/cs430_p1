import java.util.*;
import java.lang.*;
import java.io.*;
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String id;
  private String upc;

  public Product(String name, String upc, String id) {
    this.name = name;
    this.upc = upc;
    this.id = id;
  }

  public String getUPC() {
    return upc;
  }

  public String getId() {
    return id;
  }

  public String toString() {
      return "Product name " + name + " UPC " + upc + " id " + id;
  }
}
