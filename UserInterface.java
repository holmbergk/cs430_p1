// Author: Jesse Babcock
import java.util.*;
import java.text.*;
import java.io.*;
import java.math.BigDecimal;

public class UserInterface {
	private static UserInterface userInterface;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;
	private static final int EXIT = 0;
	private static final int ADD_CLIENT = 1;
	private static final int ADD_PRODUCT = 2;
	private static final int ADD_SUPPLIER = 3;
	private static final int ASSIGN_PRODUCT_SUPPLIER = 4;
	private static final int REMOVE_PRODUCT_SUPPLIER = 5;
	private static final int SHOW_CLIENTS = 6;
	private static final int SHOW_PRODUCTS = 7;
	private static final int SHOW_SUPPLIERS = 8;
	private static final int QUERY_PRODUCT_BY_GIVEN_SUPP = 9;
	private static final int QUERY_SUPP_BY_GIVEN_PRODUCT = 10;
	private static final int ACCEPT_PAYMENT = 11;
	private static final int ACCEPT_ORDER = 12;
	private static final int SHOW_UNPAID_BALANCES = 13;
	private static final int SHOW_WAITLIST_FOR_A_PRODUCT = 14;
	private static final int SHOW_ORDERS_FOR_A_CLIENT = 15;
	private static final int SHOW_INVOICES_FOR_A_CLIENT = 16;
	private static final int SHOW_ALL_TRANSACTIONS_FOR_A_CLIENT = 17;
	private static final int SAVE = 18;
	private static final int RETRIEVE = 19;
	private static final int HELP = 20;

	private UserInterface() {
		if (yesOrNo("Look for saved data and use it?")) {
			retrieve();
		} else {
			warehouse = Warehouse.instance();
		}
	}

	public static UserInterface instance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		} else {
			return userInterface;
		}
	}

	public String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
				if (tokenizer.hasMoreTokens()) {
					return tokenizer.nextToken();
				}
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);
	}

	private boolean yesOrNo(String prompt) {
		String more = getToken(prompt + " (Y|y)[es] or anything else for no");
		if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
			return false;
		}
		return true;
	}

	public int getNumber(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				Integer num = Integer.valueOf(item);
				return num.intValue();
			} catch (NumberFormatException nfe) {
				System.out.println("Please input a number ");
			}
		} while (true);
	}

	public Calendar getDate(String prompt) {
		do {
			try {
				Calendar date = new GregorianCalendar();
				String item = getToken(prompt);
				DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
				date.setTime(df.parse(item));
				return date;
			} catch (Exception fe) {
				System.out.println("Please input a date as mm/dd/yy");
			}
		} while (true);
	}

	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number");
			}
		} while (true);
	}

	public void help() {
		System.out.println("Enter a number between 0 and 13 as explained below:");
		System.out.println(EXIT + " to Exit");
		System.out.println(ADD_CLIENT + " to add a client");
		System.out.println(ADD_PRODUCT + " to add a product");
		System.out.println(ADD_SUPPLIER + " to add a supplier");
		System.out.println(ASSIGN_PRODUCT_SUPPLIER + " to assign a product to a supplier");
		System.out.println(REMOVE_PRODUCT_SUPPLIER + " to remove a product from a supplier");
		System.out.println(SHOW_CLIENTS + " to print clients");
		System.out.println(SHOW_PRODUCTS + " to print products");
		System.out.println(SHOW_SUPPLIERS + " to print suppliers");
		System.out.println(QUERY_PRODUCT_BY_GIVEN_SUPP + " to query products of a given supplier");
		System.out.println(QUERY_SUPP_BY_GIVEN_PRODUCT + " to query suppliers of a given product");
		System.out.println(ACCEPT_PAYMENT + " to accept a payment for a client");
		System.out.println(ACCEPT_ORDER + " to start an order");
		System.out.println(SHOW_UNPAID_BALANCES + " to show all unpaid balances for clients");
		System.out.println(SHOW_WAITLIST_FOR_A_PRODUCT + " to show a waitlist for a product");
		System.out.println(SHOW_ORDERS_FOR_A_CLIENT + " to show orders for a client");
		System.out.println(SHOW_INVOICES_FOR_A_CLIENT + " to show invoices for a client");
		System.out.println(SHOW_ALL_TRANSACTIONS_FOR_A_CLIENT + " to show all transactions for a client");
		System.out.println(SAVE + " to save data");
		System.out.println(RETRIEVE + " to retrieve");
		System.out.println(HELP + " for help");
	}

	public void addClient() {
		String name = getToken("Enter Client name");
		String address = getToken("Enter address");
		String phone = getToken("Enter phone");
		Client result;
		result = warehouse.addClient(name, address, phone);
		if (result == null) {
			System.out.println("Could not add client");
		}
		System.out.println(result);
	}

	public void addSupplier() {
		String name = getToken("Enter Supplier name");
		String address = getToken("Enter address");
		String phone = getToken("Enter phone");
		Supplier result;
		result = warehouse.addSupplier(name, address, phone);
		if (result == null) {
			System.out.println("Could not add Supplier");
		}
		System.out.println(result);
	}

	public void addProduct() {
		Product result;
		float cost;
		
		String name = getToken("Enter product name");
		String upc = getToken("Enter UPC");
		int amount = getNumber("Enter inventory amount");
		String costStr = getToken("Enter product cost");
		
		cost = round(Float.parseFloat(costStr), 2);
		result = warehouse.addProduct(name, upc, amount, cost);
		
		if (result != null) {
			System.out.println(result);
		} else {
			System.out.println("Product could not be added");
		}
	}

	public void assignProductToSupplier() {
		String productID = getToken("Enter Product ID");
		String supplierID = getToken("Enter Supplier ID");
		boolean result = warehouse.assignProduct(supplierID, productID);
		if (result == false) {
			System.out.println("Could not assign product to supplier");
			return;
		}
		System.out.println("productID " + productID + " has been assigned to supplierID " + supplierID);
	}

	public void removeProductFromSupplier() {
		String productID = getToken("Enter Product ID");
		String supplierID = getToken("Enter Supplier ID");
		boolean result = warehouse.removeProductFromSupplier(supplierID, productID);
		if (!result) {
			System.out.println("Could not remove product and supplier connection");
			return;
		}
		System.out.println("productID " + productID + " has been removed from supplierID " + supplierID);
	}

	public void showProducts() {
		Iterator allProducts = warehouse.getProducts();
		while (allProducts.hasNext()) {
			Product product = (Product) (allProducts.next());
			System.out.println(product.toString());
		}
	}

	public void showClients() {
		Iterator allClients = warehouse.getClients();
		while (allClients.hasNext()) {
			Client client = (Client) (allClients.next());
			System.out.println(client.toString());
		}
	}

	public void showSuppliers() {
		Iterator allSuppliers = warehouse.getSuppliers();
		while (allSuppliers.hasNext()) {
			Supplier supplier = (Supplier) (allSuppliers.next());
			System.out.println(supplier.toString());
		}
	}

	public void queryProdfromAsupplier() {
		String supplierID = getToken("Enter supplierID");
		Iterator allProducts = warehouse.getSupplierProductList(supplierID);
		if (allProducts == null) {
			System.out.println("Supplier ID " + supplierID + " not valid");
			return;
		}
		System.out.println("List of products for supplierID " + supplierID);
		while (allProducts.hasNext()) {
			Product product = (Product) (allProducts.next());
			System.out.println(product.toString());
		}
	}

	public void querySupplierOfAProduct() {
		String productID = getToken("Enter productID");
		Iterator allSuppliers = warehouse.getProductSupplierList(productID);
		if (allSuppliers == null) {
			System.out.println("Product ID " + productID + " not valid");
			return;
		}

		System.out.println("List of suppliers for productID " + productID);
		while (allSuppliers.hasNext()) {
			Supplier supplier = (Supplier) (allSuppliers.next());
			System.out.println(supplier.toString());
		}
	}

	public void acceptPayment() {
		String clientId;
		String input;
		float amountReceived;
		float amountOwed;

		clientId = getToken("Enter client ID:");
		amountOwed = warehouse.getAmountOwed(clientId);

		if (Float.compare(amountOwed, 0) == 0) {
			System.out.println("Client does not owe any money");
			return;
		}

		do {

			input = getToken("Enter amount received:");
			amountReceived = round(Float.parseFloat(input), 2);

			if (Float.compare(amountReceived, 0) < 0) {
				System.out.println("Please enter an amount greater than 0");
				continue;
			} else if (Float.compare(amountReceived, amountOwed) > 0) {
				System.out.println("Please enter an amount less than or equal to the amount owed; amount owed is ["
						+ amountOwed + "]");
				continue;
			}
			break;
		} while (true);

		// Change to int for error codes
		int result = warehouse.makePayment(clientId, amountReceived);

		if (result != 0) {
			System.out.println("Could not make a payment to client's account");
			return;
		}

		System.out.println("Payment successfully made");
	}
	
	public void acceptOrder(){
		String clientId;
		String productId;
		String orderId = "";
		int quantity;
		boolean firstProduct = true, success;
		
		clientId = getToken("Enter clientId:");
		
		do{
			productId = getToken("Enter productId:");
			quantity = getNumber("Enter quantity:");
			
			if (firstProduct)
				orderId = warehouse.createOrder(clientId, productId, quantity);
			else
			    warehouse.continueOrder(clientId, productId, quantity, orderId);
			
			String input = getToken("Would you like to add another product: yes or no");
			
			if (input.equals("yes")){
				firstProduct = false;
				continue;
			} else
				break;
			
		}while (true);
		
		warehouse.createTransaction(clientId, orderId);
		System.out.println("Your transaction is complete. OrderId: " + orderId);		
	}

	public void showUnpaidBalances() {
		Iterator unpaidBalances = warehouse.getAllUnpaidBalances();
		
		while (unpaidBalances.hasNext()) {
			String clientBalance = (String) (unpaidBalances.next());
			System.out.println(clientBalance.toString());
		}
	}
	
	public void showWaitlistForAProduct() {
		String productId = getToken("Enter productId to show waitlist:");
		Iterator waitlist = warehouse.getWaitlist(productId);
		
		while (waitlist.hasNext()) {
			WaitlistEntry waitlistEntry = (WaitlistEntry) (waitlist.next());			
			System.out.println(waitlistEntry.toString());
		}
	}
	
	public void showOrdersForAClient() {
		String clientId = getToken("Enter clientId to show their orders:");
		Iterator orders = warehouse.getOrders(clientId);
		
		while (orders.hasNext()) {
			Order order = (Order) (orders.next());
			System.out.println(order.toString());
		}
	}
	
	public void showInvoicesForAClient() {
		String clientId = getToken("Enter clientId to show their invoices:");
		Iterator invoices = warehouse.getInvoices(clientId);
		
		while (invoices.hasNext()) {
			Invoice invoice = (Invoice) (invoices.next());
			System.out.println(invoice.toString());
		}
	}
	
	public void showAllTransactionsForAClient() {
		String clientId = getToken("Enter clientId to show their transactions:");
		Iterator transactions = warehouse.getAllTransactions(clientId);
		
		while (transactions.hasNext()) {
			Transaction transaction = (Transaction) (transactions.next());
			System.out.println(transaction.toString());
		}
	}

	public float round(float f, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(f));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private void save() {
		if (warehouse.save()) {
			System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n");
		} else {
			System.out.println(" There has been an error in saving \n");
		}
	}

	private void retrieve() {
		try {
			Warehouse tempWarehouse = Warehouse.retrieve();
			if (tempWarehouse != null) {
				System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n");
				warehouse = tempWarehouse;
			} else {
				System.out.println("File doesnt exist; creating new warehouse");
				warehouse = Warehouse.instance();
			}
		} catch (Exception cnfe) {
			cnfe.printStackTrace();
		}
	}

	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case ADD_CLIENT:
				addClient();
				break;
			case ADD_PRODUCT:
				addProduct();
				break;
			case ADD_SUPPLIER:
				addSupplier();
				break;
			case SHOW_CLIENTS:
				showClients();
				break;
			case SHOW_PRODUCTS:
				showProducts();
				break;
			case SHOW_SUPPLIERS:
				showSuppliers();
				break;
			case ASSIGN_PRODUCT_SUPPLIER:
				assignProductToSupplier();
				break;
			case REMOVE_PRODUCT_SUPPLIER:
				removeProductFromSupplier();
				break;
			case QUERY_PRODUCT_BY_GIVEN_SUPP:
				queryProdfromAsupplier();
				break;
			case QUERY_SUPP_BY_GIVEN_PRODUCT:
				querySupplierOfAProduct();
				break;
			case ACCEPT_PAYMENT:
				acceptPayment();
				break;
			case ACCEPT_ORDER:
				acceptOrder();
				break;
			case SHOW_UNPAID_BALANCES:
				showUnpaidBalances();
				break;
			case SHOW_WAITLIST_FOR_A_PRODUCT:
				showWaitlistForAProduct();
				break;
			case SHOW_ORDERS_FOR_A_CLIENT:
				showOrdersForAClient();
				break;
			case SHOW_INVOICES_FOR_A_CLIENT:
				showInvoicesForAClient();
				break;
			case SHOW_ALL_TRANSACTIONS_FOR_A_CLIENT:
				showAllTransactionsForAClient();
				break;
			case SAVE:
				save();
				break;
			case RETRIEVE:
				retrieve();
				break;
			case HELP:
				help();
				break;
			}
		}
	}

	public static void main(String[] s) {
		UserInterface.instance().process();
	}
}
