// Author: Jesse Babcock
import java.util.*;
import java.lang.*;
import java.io.*;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private Client client;
	private Transaction transaction;
	private String id;
	private Calendar date;
	private static final String Order_STRING = "o";
	private List<Invoice> invoices = new LinkedList<Invoice>();
	private List<WaitList> waitListItems = new LinkedList<WaitList>();
	private List<Record> records = new LinkedList<Record>();

	public Order(Client client, Record record) {
		this.client = client;
		records.add(record);
		date = new GregorianCalendar();
		date.setTimeInMillis(System.currentTimeMillis());
		id = Order_STRING + (OrderIdServer.instance()).getId();
	}

	public boolean addRecord(Record record) {
		if (!records.contains(record)) {
			records.add(record);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeRecord(Record record) {
		if (records.contains(record)) {
			records.remove(record);
			return true;
		} else {
			return false;
		}
	}

	public boolean addInvoice(Invoice invoice) {
		if (!invoices.contains(invoice)) {
			invoices.add(invoice);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeInvoice(Invoice invoice) {
		if (invoices.contains(invoice)) {
			invoices.remove(invoice);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addWaitList(Waitlist waitlist) {
		if (!waitListItems.contains(waitlist)) {
			waitListItems.add(waitlist);
			return true;
		} else {
			return false;
		}
	}

	public boolean removeWaitList(Waitlist waitlist) {
		if (waitListItems.contains(waitlist)) {
			waitListItems.remove(waitlist);
			return true;
		} else {
			return false;
		}
	}
	
	public Iterator getRecords() {
		return records.iterator();
	}
	
	public Iterator getInvoices() {
		return invoices.iterator();
	}

	public Iterator getWaitListItems() {
		return waitListItems.iterator();
	}

	public String getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public Transaction getTransaction(){
		return transaction;
	}

	public boolean equals(String id) {
		return this.id.equals(id);
	}

	public String toString() {
		String string = "OrderId: " + id + ", clientId: " +	client.getId() + ", date: " + date;
		string += " Records: [";
		for (Iterator iterator = records.iterator(); iterator.hasNext();) {
			Record record = (Record) iterator.next();
			string += "ProductId: " + record.getProduct().getId();
			string += "Quantity: " + record.getQuantity();
		}
		string += "] Invoices: [";
		for (Iterator iterator = invoices.iterator(); iterator.hasNext();) {
			Invoice invoice = (Invoice) iterator.next();
			string += "InvoiceId: " + invoice.getId();
		}
		string += "] Wait List Items: [";
		for (Iterator iterator = waitListItems.iterator(); iterator.hasNext();) {
			Waitlist waitlist = (Invoice) iterator.next();
			string += "WaitlistId: " + waitlist.getId();
		}
		string += "]";
		return string;
	}
}