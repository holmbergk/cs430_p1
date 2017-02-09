import java.util.*;
import java.io.*;
public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String phone;
  private String id;
  private static final String SUPPLIER_STRING = "s";
  private List<Product> productsSupplied = new LinkedList<Product>();

  public Supplier (String name, String address, String phone) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    id = SUPPLIER_STRING + (SupplierIdServer.instance()).getId();
  }

  public boolean addProduct(Product product) {
    if (!productsSupplied.contains(product)) {
      productsSupplied.add(product);
      return true;
    } else {
      return false;
    }
  }

  public boolean removeProduct(Product product) {
    if (productsSupplied.contains(product)) {
      productsSupplied.remove(product);
      return true;
    } else {
      return false;
    }
  }
  
  public Iterator getProductList() {
	    return productsSupplied.iterator();
}
  
  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getAddress() {
    return address;
  }

  public String getId() {
    return id;
  }

  public void setName(String newName) {
    name = newName;
  }

  public void setAddress(String newAddress) {
    address = newAddress;
  }

  public void setPhone(String newPhone) {
    phone = newPhone;
  }

  public boolean equals(String id) {
    return this.id.equals(id);
  }

  public String toString() {
    String string = "Supplier name: " + name + ", address: " + address + ", id: " + id + ", phone: " + phone;
    return string;
  }
}
