// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Invoice implements Serializable {
    private static final String INVOICE_STRING = "i";
    private List<InvoiceEntry> entries = new LinkedList<InvoiceEntry>();
    private Client client;
    private Calendar date;
    private float totalCost;
    private String id;

    public Invoice(Client client) {
        this.client = client;
        date = new GregorianCalendar();
        date.setTimeInMillis(System.currentTimeMillis());
        totalCost = 0.0f;
        id = INVOICE_STRING + (InvoiceIdServer.instance()).getId();
    }

    public boolean addEntry(InvoiceEntry entry) {
        if (entries.add(entry)) {
            totalCost += (entry.getPrice() * entry.getQuantity());
            return true;
        }
        return false;
    }

    public Calendar getDate() {
        return date;
    }

    public Client getClient() {
        return client;
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
}

