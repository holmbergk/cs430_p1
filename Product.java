import java.util.*;
import java.lang.*;
import java.io.*;
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String id;
  private String upc;
  private static final String PRODUCT_STRING = "p";
  private List<Supplier> listOfSuppliers = new LinkedList<Supplier>();

  public Product(String name, String upc) {
    this.name = name;
    this.upc = upc;
    //this.id = id;
    id = PRODUCT_STRING + (ProductIdServer.instance()).getId();
  }
  
  public boolean addSupplier(Supplier supplier) {
    if (!listOfSuppliers.contains(supplier)) {
    	listOfSuppliers.add(supplier);
      return true;
    } else {
      return false;
    }
  }

  public boolean removeSupplier(Supplier supplier) {
    if (listOfSuppliers.contains(supplier)) {
    	listOfSuppliers.remove(supplier);
      return true;
    } else {
      return false;
    }
  }
  
  public Iterator getSupplierList() {
	    return listOfSuppliers.iterator();
  }
  
  public String getUPC() {
    return upc;
  }

  public String getId() {
    return id;
  }

  public String toString() {
      return "Product name: " + name + ", UPC: " + upc + ", id: " + id;
  }
}
