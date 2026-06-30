import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* =========================================================
   1. UTILITIES & EXCEPTIONS
   ========================================================= */

// [HELPER CLASS]
// This class contains static tools used throughout the project.
// It handles Input (Scanner), Date-Time formatting, and Validation.
class Utils {

    // Global Scanner object to be used by all classes for input.
    static Scanner sc = new Scanner(System.in);

    // Methods to draw separator lines for better UI/UX.
    static void line() { System.out.println("----------------------------------------------------------------------------------"); }
    static void doubleLine() { System.out.println("=================================================================================="); }

    // Method to print a centered, uppercase header with double lines.
    static void header(String title) {
        doubleLine();
        System.out.println("      " + title.toUpperCase());
        doubleLine();
    }

    // [TIMESTAMP GENERATOR]
    // Returns the current Date and Time in "yyyy-MM-dd HH:mm:ss" format.
    static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // [CRASH-PROOF NUMBER INPUT METHOD]
    // This method handles "InputMismatchException".
    // If the user types text (e.g., "abc") instead of a number,
    // it catches the error and asks for input again instead of crashing.
    static int getValidInt(String prompt) {
        int input = -1;
        while(true) {
            System.out.print(prompt);
            try {
                input = sc.nextInt(); // Try to read an integer
                return input;         // If successful, return the number
            } catch (InputMismatchException e) {
                sc.next(); // Clear the invalid input from the buffer
                System.out.println("   [ERROR] Invalid Input! Please enter a number.");
            }
        }
    }

    // [MOBILE VALIDATION - SYLLABUS SAFE LOOPS]
    // Validates mobile number using basic Loops and Character methods (Unit 7/9).
    static String getValidMobile() {
        while(true) {
            String mobile = sc.next();

            // Check 1: Length must be exactly 10 digits.
            if(mobile.length() != 10) {
                System.out.println("   [INVALID] Must be exactly 10 digits.");
                System.out.print("   Try again: ");
                continue;
            }

            // Check 2: The first digit must be 6, 7, 8, or 9.
            char first = mobile.charAt(0);
            if(first != '6' && first != '7' && first != '8' && first != '9') {
                System.out.println("   [INVALID] Must start with 6, 7, 8, or 9.");
                System.out.print("   Try again: ");
                continue;
            }

            // Check 3: Iterate through all characters to ensure they are digits.
            boolean allDigits = true;
            for(int i=0; i<mobile.length(); i++) {
                if(!Character.isDigit(mobile.charAt(i))) {
                    allDigits = false;
                    break;
                }
            }

            if(allDigits) return mobile; // Return valid mobile number
            else {
                System.out.println("   [INVALID] Only numbers allowed.");
                System.out.print("   Try again: ");
            }
        }
    }
}

/* =========================================================
   2. DATA CLASSES (INHERITANCE)
   ========================================================= */

// [PARENT CLASS] - SYLLABUS TOPIC: INHERITANCE
// Represents the basic attributes of an item in the shop.
class Item {
    int id;
    String name;

    // Constructor to initialize ID and Name
    Item(int id, String name) { this.id = id; this.name = name; }

    // [METHOD TO BE OVERRIDDEN]
    // Displays basic item details. Child class will override this.
    void display() { System.out.printf("| %-4d | %-20s ", id, name); }
}

// [CHILD CLASS] - SYLLABUS TOPIC: INHERITANCE
// Extends Item class to add Price, Stock, and Large Item flag.
class Product extends Item {
    double price;
    int stock;
    boolean isLargeItem; // Used to determine delivery vehicle (Van vs Bike)

    Product(int id, String name, double price, int stock, boolean isLarge) {
        super(id, name); // Calls the Parent (Item) constructor
        this.price = price;
        this.stock = stock;
        this.isLargeItem = isLarge;
    }

    // [METHOD OVERRIDING]
    // Overrides the display method to include Price and Stock status.
    @Override
    void display() {
        super.display(); // Call parent display method first

        // Logic to show "Available" or "Out of Stock"
        String status = (stock > 0) ? stock + " Available" : "Out of Stock";
        System.out.printf("| Rs.%-7.0f | %-12s |\n", price, status);
    }
}

// [DATA CLASS]
// Represents a single transaction record for the Ledger.
class Transaction {
    String type;      // CREDIT (Income) or DEBIT (Expense)
    String desc;      // Description of the transaction
    double amount;    // Total Amount
    double unitPrice; // Price per unit
    int qty;          // Quantity

    Transaction(String t, String d, double a, int q, double r) {
        type=t; desc=d; amount=a; qty=q; unitPrice=r;
    }
}

// [LOGIC CLASS]
// Manages the financial records (MoneyBank) of the shop.
class Ledger {
    // [ARRAYLIST] - SYLLABUS TOPIC
    // Dynamic list to store unlimited transactions.
    static ArrayList<Transaction> transactions = new ArrayList<>();

    // Adds a new transaction to the list
    static void addEntry(String type, String desc, double amount, int qty, double rate) {
        transactions.add(new Transaction(type, desc, amount, qty, rate));
    }

    // Generates the Deep Ledger Report with Profit/Loss calculation
    static void showReport() {
        Utils.header("MONEYBANK: DEEP LEDGER REPORT");
        System.out.printf("| %-8s | %-30s | %-4s | %-9s | %-10s |\n", "TYPE", "DESCRIPTION", "QTY", "RATE", "NET AMT");
        Utils.line();
        double income = 0, expense = 0;

        // Loop through transactions to calculate totals
        for (Transaction t : transactions) {
            System.out.printf("| %-8s | %-30s | %-4d | Rs.%-6.0f | Rs.%-8.2f |\n",
                    t.type, t.desc, t.qty, t.unitPrice, t.amount);

            if (t.type.equals("CREDIT")) income += t.amount;
            else expense += t.amount;
        }

        Utils.line();
        System.out.printf(" TOTAL SALES (Income)  : Rs. %.2f\n", income);
        System.out.printf(" TOTAL STOCK (Expense) : Rs. %.2f\n", expense);

        double profit = income - expense;
        // Ternary Operator to check Profit or Loss
        System.out.printf(" %-21s : Rs. %.2f\n", (profit >= 0 ? "NET PROFIT" : "NET LOSS"), profit);
        Utils.doubleLine();
    }
}

// [HELPER CLASS]
// Represents an item inside the customer's shopping cart.
class CartItem {
    Product p;
    int qty;
    double total;

    CartItem(Product p, int qty) {
        this.p = p;
        this.qty = qty;
        this.total = p.price * qty;
    }
}

/* =========================================================
   3. ADMIN SYSTEM
   ========================================================= */
class AdminPortal {
    // Inner class to hold Admin Credentials
    static class AdminCreds {
        String name, id, pass;
        AdminCreds(String n, String i, String p){ name=n; id=i; pass=p; }
    }

    // List to store multiple admins
    static ArrayList<AdminCreds> admins = new ArrayList<>();

    // Static block to initialize the default Admin account
    static { admins.add(new AdminCreds("System Owner", "admin", "1234")); }

    // Admin Login Logic
    static void start() {
        Utils.header("ADMIN PORTAL");
        System.out.println("1. Login");
        System.out.println("2. Register New Admin");

        int c = Utils.getValidInt("Select: ");

        if (c == 1) {
            System.out.print("ID: "); String id = Utils.sc.next();
            System.out.print("Pass: "); String pass = Utils.sc.next();

            // Loop to verify credentials
            AdminCreds loggedInUser = null;
            for(AdminCreds a : admins) if(a.id.equals(id) && a.pass.equals(pass)) loggedInUser = a;

            if(loggedInUser != null) {
                System.out.println("\n>> WELCOME ADMIN: " + loggedInUser.name.toUpperCase());
                dashboard();
            } else System.out.println(">> Access Denied.");
        }
        else if (c == 2) {
            Utils.sc.nextLine(); // Clear buffer
            System.out.print("Enter Full Name: "); String name = Utils.sc.nextLine();
            System.out.print("New ID: "); String id = Utils.sc.next();
            System.out.print("New Pass: "); String pass = Utils.sc.next();
            admins.add(new AdminCreds(name, id, pass));
            System.out.println(">> Registration Successful for " + name);
        }
    }

    // Admin Dashboard Menu
    static void dashboard() {
        while(true) {
            Utils.header("ADMIN DASHBOARD");
            System.out.println("1. Stock Check (< 5 Alert)");
            System.out.println("2. Restock (Supplier Split System)");
            System.out.println("3. Update Price");
            System.out.println("4. MoneyBank (Deep Ledger)");
            System.out.println("5. Logout");

            int ch = Utils.getValidInt("Select: ");

            if(ch == 5) break;

            switch(ch) {
                case 1:
                    System.out.println("\n--- LOW STOCK ALERT (< 5) ---");
                    // Accessing Inventory from Main Class
                    for(Product p : TechnoTrader.inventory) { if(p.stock < 5) p.display(); }
                    break;
                case 2: handleSplitRestock(); break;
                case 3:
                    int uid = Utils.getValidInt("Enter Product ID: ");
                    Product up = TechnoTrader.findProduct(uid);
                    if(up!=null) {
                        System.out.println("Editing: " + up.name);
                        System.out.print("Current: " + up.price + " | New Price: ");
                        try { up.price = Utils.sc.nextDouble(); } catch(Exception e) { Utils.sc.next(); System.out.println("Invalid Price"); }
                        System.out.println(">> Price Updated.");
                    }
                    break;
                case 4: Ledger.showReport(); break;
            }
        }
    }

    // [ADVANCED FEATURE] - Supplier Split Restock System
    static void handleSplitRestock() {
        TechnoTrader.showInventory();
        int rid = Utils.getValidInt("Enter Product ID to Restock: ");
        Product p = TechnoTrader.findProduct(rid);

        if(p == null) { System.out.println("Invalid ID"); return; }

        System.out.println("\nSelected: " + p.name);
        String[] supNames = {"Alpha Tech", "Beta Corp", "Gamma Sol"};
        double[] supRates = {p.price * 0.60, p.price * 0.55, p.price * 0.62};

        System.out.println("--- SUPPLIER QUOTATION ---");
        for(int i=0; i<3; i++) System.out.printf("%d. %-12s | Rate: Rs. %-8.2f\n", (i+1), supNames[i], supRates[i]);

        int totalNeeded = Utils.getValidInt("\nTotal Quantity Required: ");
        int remaining = totalNeeded;
        int[] qtySplit = new int[3];

        System.out.println("\n>> DISTRIBUTE ORDER (Remaining: " + totalNeeded + ")");
        for(int i=0; i<3; i++) {
            if(remaining <= 0) break;
            int q = Utils.getValidInt("Qty from " + supNames[i] + " (Max " + remaining + "): ");

            if(q <= remaining) {
                qtySplit[i] = q;
                remaining -= q;
            } else {
                System.out.println("Error: Cannot exceed remaining amount.");
                i--;
            }
        }

        if(remaining == 0) {
            System.out.println("\n>> ORDER CONFIRMED. GENERATING RECEIPTS...");
            p.stock += totalNeeded;
            for(int i=0; i<3; i++) {
                if(qtySplit[i] > 0) {
                    double cost = qtySplit[i] * supRates[i];
                    // Update Ledger with Product Name included
                    Ledger.addEntry("DEBIT", "Stock ["+p.name+"]: " + supNames[i], cost, qtySplit[i], supRates[i]);

                    // [FEATURE: FULL RECEIPT BOX]
                    System.out.println("\n   +-----------------------------+");
                    System.out.println("   |   SUPPLIER RECEIPT          |");
                    System.out.printf("   |   %-15s           |\n", supNames[i]);
                    System.out.println("   +-----------------------------+");
                    System.out.printf("   |   Item: %-19s |\n", p.name);
                    System.out.printf("   |   Qty : %-5d               |\n", qtySplit[i]);
                    System.out.printf("   |   Rate: %-8.2f            |\n", supRates[i]);
                    System.out.println("   |   -----------------------   |");
                    System.out.printf("   |   TOTAL: Rs. %-10.2f     |\n", cost);
                    System.out.println("   +-----------------------------+");
                }
            }
            System.out.println("\n>> Inventory Updated.");
        } else {
            System.out.println("Order Cancelled.");
        }
    }
}

/* =========================================================
   4. MAIN SYSTEM (Techno Trader)
   ========================================================= */
public class TechnoTrader {
    // Inventory Array (Fixed Size: 5 Products)
    static Product[] inventory = new Product[5];

    public static void main(String[] args) {
        // Initialize Inventory with Dummy Data
        inventory[0] = new Product(101, "Smart Watch", 2500, 10, false);
        inventory[1] = new Product(102, "Headphones", 1500, 20, false);
        inventory[2] = new Product(103, "Smartphone", 18000, 8, false);
        inventory[3] = new Product(104, "LED TV 55\"", 45000, 5, true);
        inventory[4] = new Product(105, "Gaming PC", 65000, 3, true);

        // Start the Main Menu
        showMainMenu();
    }

    // [SYLLABUS TOPIC: RECURSION]
    // Uses Recursion to display the menu repeatedly instead of a 'while' loop.
    static void showMainMenu() {
        System.out.println("\n");
        Utils.header("TECHNO TRADER");
        System.out.println("1. Browse and Buy (Customer)");
        System.out.println("2. Admin Portal");
        System.out.println("3. Exit");

        // Uses the Crash-Proof Input Method
        int choice = Utils.getValidInt("Select Option: ");

        if (choice == 3) { System.out.println("Exiting System..."); return; }
        else if (choice == 2) { AdminPortal.start(); showMainMenu(); } // Recursive Call
        else if (choice == 1) { customerFlow(); showMainMenu(); }      // Recursive Call
        else { System.out.println("Invalid Option"); showMainMenu(); }
    }

    // Displays the Inventory Table
    static void showInventory() {
        System.out.printf("| %-4s | %-20s | %-10s | %-12s |\n", "ID", "Product", "Price", "Availability");
        Utils.line();
        for(Product p : inventory) p.display(); // Calls Overridden Method
        Utils.line();
    }

    // Helper to find Product by ID
    static Product findProduct(int id) {
        for(Product p : inventory) if(p.id == id) return p;
        return null;
    }

    // [CORE LOGIC] - Customer Shopping Experience
    static void customerFlow() {
        Utils.sc.nextLine(); // Clear Scanner Buffer
        Utils.header("CUSTOMER DETAILS");
        System.out.print("Name: "); String name = Utils.sc.nextLine();
        System.out.print("Mobile (10 digits): "); String mobile = Utils.getValidMobile();
        Utils.sc.nextLine();

        // Address Validation Loop (No Special Characters)
        String address = "";
        while(true) {
            System.out.print("Address: "); address = Utils.sc.nextLine();
            boolean isSafe = true;
            // Iterate through each character to ensure safety
            for(int i=0; i<address.length(); i++) {
                char c = address.charAt(i);
                // Allow only Letters, Numbers, Space, Comma, Dot, and Dash.
                if(!Character.isLetterOrDigit(c) && c != ' ' && c != ',' && c != '.' && c != '-') {
                    isSafe = false; break;
                }
            }
            if(isSafe && address.length() > 0) break;
            else System.out.println("   [INVALID] Special characters (@#$%) not allowed.");
        }

        // Generate Unique Order ID (Name Initial + Last 4 digits of Mobile)
        String nameInit = (name.length() > 0) ? name.substring(0, 1).toUpperCase() : "X";
        String orderId = nameInit + "-" + ((mobile.length()>4)?mobile.substring(mobile.length()-4):mobile);

        ArrayList<CartItem> cart = new ArrayList<>();
        boolean hasLargeItems = false; // Flag for Van Delivery

        // Buying Loop
        while(true) {
            showInventory();
            int id = Utils.getValidInt("Enter ID to Buy (0 to Bill): ");
            if(id == 0) break;

            Product p = findProduct(id);
            if(p != null) {
                System.out.println("   >> Selected: " + p.name);
                if(p.stock > 0) {
                    int qty = Utils.getValidInt("   Enter Quantity: ");
                    if(qty <= p.stock) {
                        cart.add(new CartItem(p, qty));
                        p.stock -= qty; // Deduct stock temporarily
                        if(p.isLargeItem) hasLargeItems = true;
                        System.out.println("   >> Added to Cart.");
                    } else System.out.println("   >> Low Stock! Available: " + p.stock);
                } else System.out.println("   >> Out of Stock!");
            } else System.out.println("   >> Invalid ID.");
        }

        if(cart.isEmpty()) return;

        // DELIVERY SETUP
        Utils.header("DELIVERY SETUP");
        String vehicle = hasLargeItems ? "Van" : "Bike";
        System.out.println("Recommended: " + vehicle);
        System.out.println("1. Accept (" + vehicle + ")\n2. Manual Override");

        int dChoice = Utils.getValidInt("Select: ");
        if(dChoice == 2) {
            int vSel = Utils.getValidInt("Select: 1. Bike  2. Van : ");
            vehicle = (vSel == 2) ? "Van" : "Bike";
        }

        // Calculate Delivery Charge (Van costs more, Bike is free unless Fast Delivery)
        double deliveryCharge = (vehicle.equals("Van") && !hasLargeItems) ? 200 : 0;

        int fast = Utils.getValidInt("Fast Delivery? (1.Yes 2.No): ");
        if(fast == 1) deliveryCharge += 150;

        // PAYMENT SETUP
        System.out.println("Payment: 1.Online  2.COD");
        int pm = Utils.getValidInt("Select: ");

        String pMode = "COD";
        if (pm == 1) pMode = "Online";
        else pMode = "COD";

        // FINAL BILL GENERATION
        double cartTotal = 0;
        Utils.doubleLine();
        System.out.println("       OFFICIAL INVOICE - TECHNO TRADER");
        Utils.doubleLine();
        System.out.println("Date     : " + Utils.getTimestamp());
        System.out.println("ORDER ID : " + orderId);
        System.out.println("Customer : " + name + " | Mo: " + mobile);
        System.out.println("Address  : " + address);
        Utils.line();
        System.out.printf("%-20s %-5s %-10s\n", "Item", "Qty", "Total");

        for(CartItem c : cart) {
            System.out.printf("%-20s %-5d Rs.%-8.2f\n", c.p.name, c.qty, c.total);
            cartTotal += c.total;
            // Add Sale Record to Ledger
            Ledger.addEntry("CREDIT", "Sale [" + orderId + "]: " + c.p.name, c.total, c.qty, c.p.price);
        }
        Utils.line();
        double gst = cartTotal * 0.18;
        Utils.line();
        System.out.printf("Sub Total        :         Rs. %.2f\n", cartTotal);
        System.out.printf("GST (18%%)        :         Rs. %.2f\n", gst);
        System.out.printf("Delivery Charges :         Rs. %.2f\n", deliveryCharge);
        System.out.printf("GRAND TOTAL      :         Rs. %.2f\n", (cartTotal + gst + deliveryCharge));
        Utils.line();
        System.out.println("Payment Mode     :         "+ pMode);
        Utils.doubleLine();

        // [FEATURE: DETAILED DISPATCH INFO]
        String dName = vehicle.equals("Bike") ? "Raju" : "Amit";
        String dContact = vehicle.equals("Bike") ? "9898098980" : "7676076760";
        System.out.println("DISPATCH DETAILS: " + vehicle + " | Agent: " + dName + " (" + dContact + ")");

        System.out.println("\nGurantee And Warranty Are Applicable as per Company Policy.\nThank You For Shopping With Techno Trader!");
    }
}
