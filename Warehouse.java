// Author: Kyle Holmberg
import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Inventory inventory;
	private ClientList clientList;
	private SupplierList supplierList;
	private String invoiceId; // need to add to current invoice
	private String orderId; // need to add more products current order
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
			OrderIdServer.instance();
			return (warehouse = new Warehouse());
		} else {
			return warehouse;
		}
	}

	public Product addProduct(String name, String upc, int amount, float cost) {
		Product product = new Product(name, upc, amount, cost);
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

	public boolean assignProduct(String supplierID, String productID) {
		boolean addProductResult;
		boolean addSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			addProductResult = supplier.addProduct(productID);
			addSupplierResult = product.addSupplier(supplierID);
		} else {
			return false;
		}

		return (addProductResult && addSupplierResult);
	}

	public boolean removeProductFromSupplier(String supplierID, String productID) {
		boolean removeProductResult;
		boolean removeSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			removeProductResult = supplier.removeProduct(productID);
			removeSupplierResult = product.removeSupplier(supplierID);
		} else {
			return false;
		}

		return (removeProductResult && removeSupplierResult);
	}

	public boolean makePayment(String clientId, float amountReceived) {
		Client client = clientList.search(clientId);
		if (client == null) {
			return false;
		}

		client.deductAmountOwed(amountReceived);
		return true;
	}

	public boolean createOrder(String clientId, String productId, int quantity) {
		boolean success;
		Client client = clientList.search(clientId);
		Product product = inventory.search(productId);

		if (client == null || product == null) {
			return false;
		}

		orderId = client.createNewOrder(product, quantity);

		if (orderId.equals("false"))
			return false;

		success = processOrder(client, product, quantity, true);

		return true;
	}

	public boolean continueOrder(String clientId, String productId, int quantity) {
		boolean success;
		Client client = clientList.search(clientId);
		Product product = inventory.search(productId);

		if (client == null || product == null) {
			return false;
		}

		success = client.addRecord(product, quantity, orderId);

		if (success == false)
			return false;

		success = processOrder(client, product, quantity, false);

		return success;
	}

	public String getOrderId() {
		return orderId;
	}

	private boolean processOrder(Client client, Product product, int quantity, boolean firstProduct) {
		int currentInventory = product.getCurrentStock();
		String productId = product.getId();
		String clientId = client.getId();
		float cost = product.getProductCost();
		int waitlistQuantity = quantity - currentInventory;
		boolean errWaitList = true, errStock = true, errInvoice = true;

		// enough in stock
		if (quantity <= currentInventory && currentInventory > 0) {
			if (firstProduct) {
				// create new invoice
				invoiceId = client.createNewInvoice(productId, quantity, cost);
				errStock = product.reduceCurrentStock(quantity);
			} else {
				// add invoice entry
				errInvoice = client.addInvoiceEntry(productId, quantity, cost, invoiceId);
				errStock = product.reduceCurrentStock(quantity);
			}
		} else if (quantity > currentInventory && currentInventory > 0) {
			// have stock but not enough to fill order
			if (firstProduct) {
				// create new invoice and waitlist
				invoiceId = client.createNewInvoice(productId, currentInventory, cost);
				errWaitList = product.addWaitlistEntry(clientId, orderId, waitlistQuantity);
				errStock = product.reduceCurrentStock(currentInventory);
			} else {
				// add invoice and waitlist entries
				errInvoice = client.addInvoiceEntry(productId, currentInventory, cost, invoiceId);
				errWaitList = product.addWaitlistEntry(clientId, orderId, waitlistQuantity);
				errStock = product.reduceCurrentStock(currentInventory);
			}
		} else {
			// nothing in stock
			errWaitList = product.addWaitlistEntry(clientId, orderId, quantity);
		}
		
		// check for errors
		if (invoiceId.equals("false"))
			return false;
		
		if (errStock && errInvoice && errWaitList) {
			client.updateTransTotal(cost * quantity);
			return true;
		}

		return false;
	}

	public boolean createTransaction(String clientId) {
		Client client = clientList.search(clientId);
		if (client == null) {
			return false;
		}
		return client.createTransaction(orderId);
	}

	public float getAmountOwed(String clientId) {
		Client client = clientList.search(clientId);
		if (client == null) {
			return -1.0f;
		}

		return client.getAmountOwed();
	}

	public Iterator getAllUnpaidBalances() {
		List<String> unpaidBalances = new LinkedList<String>();

		Iterator allClients = clientList.getClients();

		while (allClients.hasNext()) {
			Client client = (Client) (allClients.next());

			if (client.getAmountOwed() >= 0) {
				unpaidBalances.add("ClientId: " + client.getId() + ", Amount owed: " + client.getAmountOwed());
			}
		}
		return unpaidBalances.iterator();
	}

	public Iterator getWaitlist(String productID) {
		Product product = inventory.search(productID);
		if (product != null) {
			return product.getWaitList();
		} else {
			return null;
		}
	}

	public Iterator getOrders(String clientId) {
		Client client = clientList.search(clientId);
		if (client != null) {
			return client.getOrders();
		} else {
			return null;
		}
	}

	public Iterator getAllTransactions(String clientId) {
		Client client = clientList.search(clientId);
		if (client != null) {
			return client.getAllTransactions();
		} else {
			return null;
		}
	}

	public Iterator getInvoices(String clientId) {
		Client client = clientList.search(clientId);
		if (client != null) {
			return client.getInvoices();
		} else {
			return null;
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
			OrderIdServer.retrieve(input);
			return warehouse;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException cnfe) {
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
			output.writeObject(OrderIdServer.instance());
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	private void writeObject(java.io.ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(warehouse);
		} catch (IOException ioe) {
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
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return inventory + "\n" + clientList + "\n" + supplierList;
	}
}
