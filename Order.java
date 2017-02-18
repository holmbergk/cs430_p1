// Author: Jesse Babcock
import java.util.*;
import java.lang.*;
import java.io.*;

public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String ORDER_STRING = "o";
	private String clientId;
	private String id;
	private Calendar date;
	private List<Record> records = new LinkedList<Record>();

	public Order(String clientId, Product product, int quantity) {
		this.clientId = clientId;
		Record record = new Record(product, quantity);
		records.add(record);
		date = new GregorianCalendar();
		date.setTimeInMillis(System.currentTimeMillis());
		id = ORDER_STRING + (OrderIdServer.instance()).getId();
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
	
	public Iterator getRecords() {
		return records.iterator();
	}

	public String getId() {
		return id;
	}

	public String getClientId() {
		return clientId;
	}
	
	public Calendar getDate() {
		return date;
	}

	public boolean equals(String id) {
		return this.id.equals(id);
	}

	public String toString() {
		String string = "OrderId: " + id + ", clientId: " +	clientId + "\r\n";// + ", date: " + date;
		string += "Records:\r\n";
		for (Iterator iterator = records.iterator(); iterator.hasNext();) {
			Record record = (Record) iterator.next();
			string += "ProductId: " + record.getProduct().getId();
			string += ", Quantity: " + record.getQuantity() + "\r\n";
		}
		return string;
	}
}