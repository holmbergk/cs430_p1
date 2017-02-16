// Author: Jesse Babcock
import java.util.*;
import java.lang.*;
import java.io.*;
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String id;
    private String upc;
    private static final String PRODUCT_STRING = "p";
    private List<String> listOfSuppliers = new LinkedList<String>();
    private List<WaitlistEntry> waitlist = new LinkedList<WaitlistEntry>();

    public Product(String name, String upc) {
        this.name = name;
        this.upc = upc;
        id = PRODUCT_STRING + (ProductIdServer.instance()).getId();
    }

    public boolean addSupplier(String supplierId) {
        if (!listOfSuppliers.contains(supplierId)) {
            listOfSuppliers.add(supplierId);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeSupplier(String supplierId) {
        if (listOfSuppliers.contains(supplierId)) {
            listOfSuppliers.remove(supplierId);
            return true;
        } else {
            return false;
        }
    }

    public boolean addWaitlistentry(WaitlistEntry entry) {
        if (!waitlist.contains(entry)) {
            waitlist.add(entry);
            return true;
        }
        return false;
    }

    public boolean removeWaitlistentry(WaitlistEntry entry) {
        if (waitlist.contains(entry)) {
            waitlist.remove(entry);
            return true;
        }
        return false;
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
