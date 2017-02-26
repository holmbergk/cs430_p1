
// Author: Kyle Holmberg
import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int OPERATION_SUCCESS = 0;
	public static final int CLIENT_NOT_FOUND = 1;
	public static final int PRODUCT_NOT_FOUND = 2;
	public static final int SUPPLIER_NOT_FOUND = 3;
	public static final int OPERATION_FAILED = 4;
	public static final int ORDER_NOT_CREATED = 5;
	public static final int RECORD_NOT_ADDED = 6;
	public static final int INVOICE_NOT_ADDED = 7;
	public static final int WAITLIST_NOT_FOUND = 8;
	public static final int WAITLIST_PARTIALLY_FILLED = 9;
	public static final int REMOVE_WAITLIST_ENTRY_ERR = 10;
	public static final int UPDATE_WAITLIST_ENTRY_ERR = 11;
	public static final int END_OF_STOCK = 12;

	private Inventory inventory;
	private ClientList clientList;
	private SupplierList supplierList;
	private String invoiceId; // needed to add to current invoice
	private String orderId; // needed to add more products to current order
	private int currQuantity; // available shipment leftover
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
			WaitlistEntryIdServer.instance();
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

	public int assignProduct(String supplierID, String productID) {
		boolean addProductResult;
		boolean addSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			addProductResult = supplier.addProduct(productID);
			addSupplierResult = product.addSupplier(supplierID);
		} else {
			return (product == null ? PRODUCT_NOT_FOUND : SUPPLIER_NOT_FOUND);
		}

		return (addProductResult && addSupplierResult ? OPERATION_SUCCESS : OPERATION_FAILED);
	}

	public int removeProductFromSupplier(String supplierID, String productID) {
		boolean removeProductResult;
		boolean removeSupplierResult;
		Product product = inventory.search(productID);
		Supplier supplier = supplierList.search(supplierID);

		if (product != null && supplier != null) {
			removeProductResult = supplier.removeProduct(productID);
			removeSupplierResult = product.removeSupplier(supplierID);
		} else {
			return (product == null ? PRODUCT_NOT_FOUND : SUPPLIER_NOT_FOUND);
		}

		return (removeProductResult && removeSupplierResult ? OPERATION_SUCCESS : OPERATION_FAILED);
	}

	public Client getClient(String clientId) {
		Client client = clientList.search(clientId);
		return client;
	}

	public Product getProduct(String productId) {
		Product product = inventory.search(productId);
		return product;
	}

	public boolean makePayment(String clientId, float amountReceived) {
		Client client = clientList.search(clientId);
		if (client == null) {
			return false;
		}

		client.deductAmountOwed(amountReceived);
		return true;
	}

	public int createOrder(String clientId, String productId, int quantity) {
		int errCode;
		Client client = clientList.search(clientId);
		Product product = inventory.search(productId);

		if (client == null || product == null) {
			return (client == null ? CLIENT_NOT_FOUND : PRODUCT_NOT_FOUND);
		}

		orderId = client.createNewOrder(product, quantity);

		if (orderId.equals("false")) {
			return ORDER_NOT_CREATED;
		}

		errCode = processOrder(client, product, quantity, true);

		return errCode;
	}

	public int continueOrder(String clientId, String productId, int quantity) {
		boolean success;
		int errCode;
		Client client = clientList.search(clientId);
		Product product = inventory.search(productId);

		if (client == null || product == null) {
			return (client == null ? CLIENT_NOT_FOUND : PRODUCT_NOT_FOUND);
		}

		success = client.addRecord(product, quantity, orderId);

		if (!success) {
			return RECORD_NOT_ADDED;
		}

		errCode = processOrder(client, product, quantity, false);

		return errCode;
	}

	public String getOrderId() {
		return orderId;
	}

	private int processOrder(Client client, Product product, int quantity, boolean firstProduct) {
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
			return INVOICE_NOT_ADDED;

		if (errStock && errInvoice && errWaitList) {
			client.updateTransTotal(cost * quantity);
			return OPERATION_SUCCESS;
		}

		return OPERATION_FAILED;
	}

	public int processWaitlistEntry(String productId, int index) {

		boolean errRemWaitList = true, errUpdWaitList = true;

		Product product = inventory.search(productId);

		if (product == null)
			return PRODUCT_NOT_FOUND;

		WaitlistEntry entry = product.getWaitlistEntry(index);

		if (entry == null)
			return WAITLIST_NOT_FOUND;

		int waitlistQuantity = entry.getQuantity();
		float cost = product.getProductCost();
		Client client = clientList.search(entry.getClientId());

		// enough in shipment
		if (waitlistQuantity <= currQuantity && currQuantity > 0) {
			invoiceId = client.createNewInvoice(productId, waitlistQuantity, cost);
			if (invoiceId.equals("false"))
				return INVOICE_NOT_ADDED;
			currQuantity -= waitlistQuantity;
			if (product.removeWaitlistentry(entry))
				return OPERATION_SUCCESS;
			else
				return REMOVE_WAITLIST_ENTRY_ERR;
		} else if (waitlistQuantity > currQuantity && currQuantity > 0) {
			invoiceId = client.createNewInvoice(productId, currQuantity, cost);
			if (invoiceId.equals("false"))
				return INVOICE_NOT_ADDED;
			entry.setQuantity(waitlistQuantity - currQuantity);
			if (product.updateWaitlistEntry(entry)) {
				currQuantity = 0;
				return END_OF_STOCK;
			} else
				return UPDATE_WAITLIST_ENTRY_ERR;
		} else
			return END_OF_STOCK;
	}

	public void setCurrQuantity(int quantity) {
		currQuantity = quantity;
	}

	public String getWaitlistEntryInfo(String productId, int index) {
		Product product = inventory.search(productId);
		WaitlistEntry entry;
		if (product != null) {
			entry = product.getWaitlistEntry(index);
			if (entry != null)
				return entry.toString();
			else
				return "null";
		} else
			return "null";

	}

	public void updateCurrentStock(String productId) {
		Product product = inventory.search(productId);
		product.addToCurrentStock(currQuantity);
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
			WaitlistEntryIdServer.retrieve(input);
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
			output.writeObject(WaitlistEntryIdServer.instance());
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
