import java.util.*;
import java.lang.*;
import java.io.*;
public class Inventory implements Serializable {
  private static final long serialVersionUID = 1L;
  private List products = new LinkedList();
  private static Inventory inventory;

  private Inventory() {
  }

  public static Inventory instance() {
    if (inventory == null) {
      return (inventory = new Inventory());
    } else {
      return inventory;
    }
  }
  
  public boolean insertProduct(Product product) {
    products.add(product);
    return true;
  }

  public Iterator getProducts() {
    return products.iterator();
  }

  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(inventory);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }

  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (inventory != null) {
        return;
      } else {
        input.defaultReadObject();
        if (inventory == null) {
          inventory = (Inventory) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      System.out.println("in Inventory readObject \n" + ioe);
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }

  public String toString() {
    return products.toString();
  }
}
