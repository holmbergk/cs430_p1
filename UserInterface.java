import java.util.*;
import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCT = 2;
  private static final int ADD_SUPPLIER = 3;
  private static final int SHOW_CLIENTS = 4;
  private static final int SHOW_PRODUCTS = 5;
  private static final int SHOW_SUPPLIERS = 6;
  private static final int SAVE = 7;
  private static final int RETRIEVE = 8;
  private static final int HELP = 9;

  private UserInterface() {
    if (yesOrNo("Look for saved data and use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
  }

  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }

  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }

  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }

  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 12 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCT + " to add a product");
    System.out.println(ADD_SUPPLIER + " to add a supplier");
    System.out.println(SHOW_CLIENTS + " to print clients");
    System.out.println(SHOW_PRODUCTS + " to print products");
    System.out.println(SHOW_SUPPLIERS + " to print suppliers");
    System.out.println(SAVE + " to save data");
    System.out.println(RETRIEVE + " to retrieve");
    System.out.println(HELP + " for help");
  }

  public void addClient() {
    String name = getToken("Enter Client name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Client result;
    result = warehouse.addClient(name, address, phone);
    if (result == null) {
      System.out.println("Could not add client");
    }
    System.out.println(result);
  }

  public void addSupplier() {
    String name = getToken("Enter Supplier name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Supplier result;
    result = warehouse.addSupplier(name, address, phone);
    if (result == null) {
      System.out.println("Could not add Supplier");
    }
    System.out.println(result);
  }

  public void addProduct() {
    Product result;
      String name = getToken("Enter product name");
      String upc = getToken("Enter UPC");
      String id = getToken("Enter id");
      result = warehouse.addProduct(name, upc, id);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
  }

  public void showProducts() {
    Iterator allProducts = warehouse.getProducts();
    while (allProducts.hasNext()){
  	  Product product = (Product)(allProducts.next());
      System.out.println(product.toString());
    }
  }

  public void showClients() {
    Iterator allClients = warehouse.getClients();
    while (allClients.hasNext()){
      Client client = (Client)(allClients.next());
      System.out.println(client.toString());
    }
  }

  public void showSuppliers() {
    Iterator allSuppliers = warehouse.getSuppliers();
    while (allSuppliers.hasNext()){
      Supplier supplier = (Supplier)(allSuppliers.next());
      System.out.println(supplier.toString());
    }
  }

  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    } else {
      System.out.println(" There has been an error in saving \n" );
    }
  }

  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:        addClient();
                                break;
        case ADD_PRODUCT:       addProduct();
                                break;
        case ADD_SUPPLIER:      addSupplier();
                                break;
        case SHOW_CLIENTS:	    showClients();
                                break; 		
        case SHOW_PRODUCTS:	    showProducts();
                                break; 		
        case SHOW_SUPPLIERS:    showSuppliers();
                                break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;
        case HELP:              help();
                                break;
      }
    }
  }

  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
