// Author: Kyle Holmberg
import java.util.*;
import java.io.*;
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private float amount;
    private Calendar date;

    public Transaction (String description, float amount) {
        this.description = description;
        this.amount = amount;
        date = new GregorianCalendar();
        date.setTimeInMillis(System.currentTimeMillis());
    }

    public boolean onDate(Calendar date) {
        return ((date.get(Calendar.YEAR) == this.date.get(Calendar.YEAR)) &&
                (date.get(Calendar.MONTH) == this.date.get(Calendar.MONTH)) &&
                (date.get(Calendar.DATE) == this.date.get(Calendar.DATE)));
    }

    public String getDescription() {
        return description;
    }

    public float getAmount() {
        return amount;
    }

    public String getDate() {
        return date.get(Calendar.MONTH) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR);
    }

    public String toString(){
        return (description + "   $" + amount);
    }
}