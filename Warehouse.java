// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Inventory inventory;
	private ClientList clientList;
	private SupplierList supplierList;
	private static Warehouse warehouse;

	private Warehouse() {
		inventory = inventory.instance();
		clientList = ClientList.instance();
		supplierList = SupplierList.instance();
	}

	public static Warehouse instance() {
		if (warehouse == null) {
			ClientIdServer.instance(); // instantiate all singletons
			SupplierIdServer.instance();
			ProductIdServer.instance();
			InvoiceIdServer.instance();
			return (warehouse = new Warehouse());
		} else {
			return warehouse;
		}
	}

	public Product addProduct(String name, String upc) {
		Product product = new Product(name, upc);
		if (inventory.insertProduct(product)) {
			return product;
		}
		return null;
	}

	public Client addClient(String name, String address, String phone) {
		Client client = new Client(name, address, phone);
		if (clientList.insertClient(client)) {
			return client;
		}
		return null;
	}

	public Supplier addSupplier(String name, String address, String phone) {
		Supplier supplier = new Supplier(name, address, phone);
		if (supplierList.insertSupplier(supplier)) {
			return supplier;
		}
		return null;
	}

	public boolean assignProduct(String supplierID, String productID){
		boolean addProductResult;
		boolean addSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			addProductResult = supplier.addProduct(product);
			addSupplierResult = product.addSupplier(supplier);
		} else {
			return false;
		}

		if (addProductResult == true && addSupplierResult == true) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean removeProductFromSupplier(String supplierID, String productID){
		boolean removeProductResult;
		boolean removeSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			removeProductResult = supplier.removeProduct(product);
			removeSupplierResult = product.removeSupplier(supplier);
		} else {
			return false;
		}

		if (removeProductResult == true && removeSupplierResult == true) {
			return true;
		}
		else {
			return false;
		}
	}

	public Iterator getProductSupplierList(String productID) {
		Product product = inventory.search(productID);
		if (product != null) {
			return product.getSupplierList(); 
		} else {
			return null;
		}
	}

	public Iterator getSupplierProductList(String supplierID) {
		Supplier supplier = supplierList.search(supplierID);
		if (supplier != null) {
			return supplier.getProductList();
		} else {
			return null;
		}
	}

	public Iterator getProducts() {
		return inventory.getProducts();
	}

	public Iterator getClients() {
		return clientList.getClients();
	}

	public Iterator getSuppliers() {
		return supplierList.getSuppliers();
	}

	public static Warehouse retrieve() {
		try {
			FileInputStream file = new FileInputStream("WarehouseData");
			ObjectInputStream input = new ObjectInputStream(file);
			input.readObject();
			ClientIdServer.retrieve(input);
			SupplierIdServer.retrieve(input);
			ProductIdServer.retrieve(input);
			InvoiceIdServer.retrieve(input);
			return warehouse;
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			return null;
		}
	}

	public static boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("WarehouseData");
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(warehouse);
			output.writeObject(ClientIdServer.instance());
			output.writeObject(SupplierIdServer.instance());
			output.writeObject(ProductIdServer.instance());
			output.writeObject(InvoiceIdServer.instance());
			return true;
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	private void writeObject(java.io.ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(warehouse);
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
	}

	private void readObject(java.io.ObjectInputStream input) {
		try {
			input.defaultReadObject();
			if (warehouse == null) {
				warehouse = (Warehouse) input.readObject();
			} else {
				input.readObject();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return inventory + "\n" + clientList + "\n" + supplierList;
	}
}
