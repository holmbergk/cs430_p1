// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Invoice implements Serializable {
    private static final String INVOICE_STRING = "i";
    private List<InvoiceEntry> entries = new LinkedList<InvoiceEntry>();
    private String clientId;
    private Calendar date;
    private float totalCost;
    private String id;

    public Invoice(String clientId, String productId, int quantity, float cost) {
        this.clientId = clientId;
        InvoiceEntry entry = new InvoiceEntry(productId, quantity, cost);
        entries.add(entry);
        totalCost = quantity * cost;
        date = new GregorianCalendar();
        date.setTimeInMillis(System.currentTimeMillis());
        id = INVOICE_STRING + (InvoiceIdServer.instance()).getId();
    }

    public boolean addEntry(InvoiceEntry entry) {
    	if(!entries.contains(entry)){
    		entries.add(entry);
            totalCost += (entry.getPrice() * entry.getQuantity());
            return true;            
        }
        return false;
    }
    
    public Calendar getDate() {
        return date;
    }

    public String getClientId() {
        return clientId;
    }

    public String getId() {
        return id;
    }

    public float getTotalCost() {
        return totalCost;
    }
    public Iterator getEntries() {
        return (entries.listIterator());
    }
    
    public String toString() {
		String string = "ClientId: " + id + ", total cost: " +	totalCost + "\r\n";
		string += "Entries:\r\n";
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			InvoiceEntry entry = (InvoiceEntry) iterator.next();
			string += "ProductId: " + entry.getProductId();
			string += ", Quantity: " + entry.getQuantity();
			string += ", Price: " + entry.getPrice() + "\r\n";
		}
		return string;
    }
}

