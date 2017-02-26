
// Author: Jesse Babcock
import java.util.*;
import java.io.*;

public class WaitlistEntry implements Serializable {
	private static final String WAITLIST_ENTRY_STRING = "w";
	private String clientId;
	private String orderId;
	private int quantity;
	private String id;

	public WaitlistEntry(String clientId, String orderId, int quantity) {
		this.clientId = clientId;
		this.orderId = orderId;
		this.quantity = quantity;
		id = WAITLIST_ENTRY_STRING + (WaitlistEntryIdServer.instance()).getId();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int newQuantity) {
		quantity = newQuantity;
	}

	public String getClientId() {
		return clientId;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getId() {
		return id;
	}

	public String toString() {
		return "clientId: " + clientId + ", OrderId: " + orderId + ", quantity: " + quantity + ", id: " + id;
	}
}
