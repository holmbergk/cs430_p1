// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CLIENT_STRING = "c";
    private String name;
    private String address;
    private String phone;
    private String id;
    private float amountOwed;
    private float currentTransCost;
    private List<Transaction> transactions = new LinkedList<Transaction>();
    private List<Order> orders = new LinkedList<Order>();
    private List<Invoice> invoices = new LinkedList<Invoice>();

    public Client (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        amountOwed = 0.0f;
        currentTransCost = 0.0f;
        id = CLIENT_STRING + (ClientIdServer.instance()).getId();
    }

    public String createNewOrder (Product product, int quantity){
    	Order order = new Order(id, product, quantity);
    	orders.add(order);
    	return order.getId();
    }
    
    public boolean addRecord (Product product, int quantity, String orderId) {
    	for(int i = 0; i < orders.size(); i++) {
          if (orderId.equals(orders.get(i).getId())) {
            Order order = orders.get(i);
            Record record = new Record(product, quantity);
            boolean success = order.addRecord(record);
            if(success) {
            	orders.set(i, order);
            	return true;
            } else {
            	return false;       
            }
          }
        }
    	
    	return false;
    }
    
    public String createNewInvoice(String productId, int quantity, float cost){
    	Invoice invoice = new Invoice(id, productId, quantity, cost);
    	invoices.add(invoice);
    	amountOwed = quantity * cost;
    	return invoice.getId();
    }
    
    public boolean addInvoiceEntry(String productId, int quantity, float cost, String invoiceId) {
    	for(int i = 0; i < invoices.size(); i++) {
          if (invoiceId.equals(invoices.get(i).getId())) {
            Invoice invoice = invoices.get(i);
            InvoiceEntry entry = new InvoiceEntry(productId, quantity, cost);
        	boolean success = invoice.addEntry(entry);
            if (success) {
            	invoices.set(i, invoice);
            	amountOwed += (quantity * cost);
            	return true;
            } else {
            	return false; 
            }
          }
        }
    	
    	return false;
    }
    
    public boolean createTransaction(String orderId) {
    	Transaction transaction = new Transaction(orderId, currentTransCost);
    	if (transactions.add(transaction)) {
    		currentTransCost = 0.0f;
    		return true;
    	} else {
    		return false;
        }
    }
    
    public void updateTransTotal(float total){
    	currentTransCost += total;
    }
    
    public Iterator getAllTransactions(){
    	return transactions.iterator();
    }

    public Iterator getTransactions(Calendar date) {
        List<Transaction> result = new LinkedList<Transaction>();
        for (Iterator iterator = transactions.iterator(); iterator.hasNext();) {
            Transaction transaction = (Transaction) iterator.next();
            if (transaction.onDate(date)) {
                result.add(transaction);
            }
        }
        return (result.iterator());
    }

    public Iterator getOrders() {
        return orders.iterator();
    }
    
    public Iterator getInvoices() {
        return invoices.iterator();
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

    public float getAmountOwed() {
        return amountOwed;
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

    public void increaseAmountOwed(float amt) {
        amountOwed += amt;
    }

    public void deductAmountOwed(float amt) {
        amountOwed -= amt;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public String toString() {
        String string = "Client name: " + name + ", address: " + address + ", phone: "
        				 + phone + ", id: " + id;
        return string;
    }
}
