// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class WaitlistEntry implements Serializable {
    private String clientId;
    private String orderId;
    private float quantity;

    public WaitlistEntry(String clientId, String orderId, float quantity) {
        this.clientId = clientId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getClientId() {
        return clientId;
    }

    public String getOrderId() {
        return orderId;
    }
    
    public String toString() {
        return "clientId: " + clientId + ", OrderId: " + orderId + ", quantity: " + quantity;
    }
}

