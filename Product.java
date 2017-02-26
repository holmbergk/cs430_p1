
// Author: Jesse Babcock
import java.util.*;
import java.lang.*;
import java.io.*;

public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private String upc;
	private int currentStock;
	private float productCost;
	private static final String PRODUCT_STRING = "p";
	private List<String> listOfSuppliers = new LinkedList<String>();
	private List<WaitlistEntry> waitlist = new LinkedList<WaitlistEntry>();

	public Product(String name, String upc, int currentStock, float productCost) {
		this.name = name;
		this.upc = upc;
		this.currentStock = currentStock;
		this.productCost = productCost;
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

	public boolean addWaitlistEntry(String clientId, String orderId, int quantity) {
		WaitlistEntry entry = new WaitlistEntry(clientId, orderId, quantity);
		if (waitlist.add(entry))
			return true;
		else
			return false;
	}

	public boolean removeWaitlistentry(WaitlistEntry entry) {
		if (waitlist.contains(entry)) {
			waitlist.remove(entry);
			return true;
		}
		return false;
	}

	public boolean updateWaitlistEntry(WaitlistEntry entry) {
		for (int i = 0; i < waitlist.size(); i++) {
			if (entry.getId().equals(waitlist.get(i).getId())) {
				waitlist.set(i, entry);
				return true;
			}
		}

		return false;
	}

	public void addToCurrentStock(int amount) {
		currentStock += amount;
	}

	public boolean reduceCurrentStock(int amount) {
		if (currentStock < 0 || amount > currentStock) {
			return false;
		} else {
			currentStock -= amount;
			return true;
		}
	}

	public Iterator getSupplierList() {
		return listOfSuppliers.iterator();
	}

	public Iterator getWaitList() {
		return waitlist.iterator();
	}

	public WaitlistEntry getWaitlistEntry(int index) {
		if (index >= waitlist.size())
			return null;
		else
			return waitlist.get(index);
	}

	public String getUPC() {
		return upc;
	}

	public String getId() {
		return id;
	}

	public int getCurrentStock() {
		return currentStock;
	}

	public float getProductCost() {
		return productCost;
	}

	public String toString() {
		return "Product name: " + name + ", UPC: " + upc + ", id: " + id + ", current stock: " + currentStock
				+ ", product cost: " + productCost;
	}
}
