// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String address;
    private String phone;
    private String id;
    private float amountOwed;
    private static final String CLIENT_STRING = "c";
    private List<Transaction> transactions = new LinkedList<Transaction>();
    private List<Order> orders = new LinkedList<Order>();
    private List<Invoice> invoices = new LinkedList<Invoice>();

    public Client (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        amountOwed = 0.0f;
        id = CLIENT_STRING + (ClientIdServer.instance()).getId();
    }

    public boolean addOrder(Order order) {
        return orders.add(order);
    }
    
    public boolean addRecord(Product product, int quantity, String orderId) {
    	Order order;
    	
    	for(int i = 0; i < orders.size(); i++)
        {
          if (orderId.equals(orders.get(i).getId()))
          {
            order = orders.get(i);
            Record record = new Record(product, quantity);
            boolean success = order.addRecord(record);
            if(success){
            	orders.set(i, order);
            	return true;
            } else
            	return false;       
          }
        }
    	
    	return false;
    }
    
    public String createNewOrder(Product product, int quantity){
    	Order order = new Order(id, product, quantity);
    	orders.add(order);
    	return order.getId();
    }

    public boolean addInvoice(Invoice invoice) {
        if (invoices.add(invoice)) {
            transactions.add(new Transaction ("Invoice added", invoice.getTotalCost()));
            return true;
        }
        return false;
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
