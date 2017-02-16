// Author - Jesse Babcock
import java.util.*;
import java.io.*;
public class UnpaidBalanceEntry implements Serializable {
    private String clientId;
    private float balance;

    public UnpaidBalanceEntry(String clientId, float balance) {
        this.clientId = clientId;
        this.balance = balance;
    }

    public String getClientId() {
        return clientId;
    }

    public float getBalance() {
        return balance;
    }
    
    public void makePayment(float amountPaid) {
        balance -= amountPaid;
    }
    
    public void addBalanceOwed(float BalanceOwed) {
        balance += BalanceOwed;
    }
}