// Author - Jesse Babcock
import java.util.*;
import java.io.*;

public class UnpaidBalances implements Serializable {
	private List<UnpaidBalanceEntry> unpaidBalances = new LinkedList<UnpaidBalanceEntry>();

	public UnpaidBalances(UnpaidBalanceEntry entry) {
		unpaidBalances.add(entry);
	}

	public boolean addEntry(UnpaidBalanceEntry entry) {
		if (unpaidBalances.add(entry)) {
			return true;
		}
		return false;
	}

	public boolean makeClientPayment(String clientId, float amountPaid) {
	  	for(int i = 0; i < unpaidBalances.size(); i++)
	  	{
	  		if (clientId.equals(unpaidBalances.get(i).getClientId()))
	  		{
	  			unpaidBalances.get(i).makePayment(amountPaid);
	  			
	  			if (unpaidBalances.get(i).getBalance() <= 0)
	  			{
	  				unpaidBalances.remove(i);
	  			}
	  			
	  			return true;
	  		}
	  	}
	  	
	  	return false;
    }

	public boolean addClientBalance(String clientId, float newBalanceOwed) {
	  	for(int i = 0; i < unpaidBalances.size(); i++)
	  	{
	  		if (clientId.equals(unpaidBalances.get(i).getClientId()))
	  		{
	  			unpaidBalances.get(i).addBalanceOwed(newBalanceOwed);
	  		}
	  		
	  		return true;
	  	}
	  	
	  	return false;
    }

	public Iterator getUnpaidBalances() {
		return unpaidBalances.iterator();
	}
	
}