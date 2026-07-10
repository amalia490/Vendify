# Smart Vending Machine (Intelligent Vending Machine Management System)

This project represents a Java application for simulating and managing a smart vending machine. The architecture adheres to OOP principles (Encapsulation, Inheritance, Polymorphism, Abstraction) and utilizes design patterns (e.g., Singleton) to separate business logic from data models.

## 1. Project Architecture and Classes

The project is structured into standard packages (Model, Service, Exception, Repository, Config), as follows:

### com.pao.proiect.tonomat.model (Domain Models)
* **Product Hierarchy:**
  * `Product` (Abstract base class, implements Comparable and Discountable)
  * `Eatable` (Abstract class, extends Product - for products with calories and expiration dates)
  * `Drink` (Extends Eatable - adds volume and flavors)
  * `Snack` (Extends Eatable - adds weight and vegan specification)
  * `Gadget` (Extends Product - for electronics with warranties)
* **Payment and Promotion System:**
  * `PaymentMethod` (Abstract class for payment processing)
  * `CashPayment` (Extends PaymentMethod - calculates cash change)
  * `CardPayment` (Extends PaymentMethod - validates card details)
  * `PromoCode` (Manages discount coupons applicable to orders)
* **Management and Administration:**
  * `Compartment` (Represents a physical slot in the vending machine, stores quantity and product)
  * `Administrator` (Manages data and access credentials for maintenance)
  * `MachineReport` (Final immutable class for generating daily reports)
* **Interfaces:**
  * `Discountable` (Contract for applying discounts on products/compartments)
  * `StockObserver` (Observer pattern for automatic administrator alerts on critical stock)

### com.pao.proiect.tonomat.service (Business Logic)
All services are implemented using the Singleton pattern to ensure a single instance in memory.
* `InventoryService`: Manages CRUD operations on shelves and stocks in memory.
* `PaymentService`: Manages collections, promotional coupons, and cash withdrawals.
* `ReportService`: Handles the collection of financial data and prints the transaction history.
* `AuditService`: Manages application logging. Every modification or query action performed in the system is automatically recorded in a text file (`audit.csv`), accompanied by the exact date and time (timestamp), ensuring complete traceability.

### com.pao.proiect.tonomat.repository (Data Access - JDBC / DAO Pattern)
To adhere to the "Single Responsibility" principle and decouple business logic from SQL commands, the system uses Repository classes for data persistence (CRUD operations):
* `ProductRepository`: Saves and extracts products using a "Single Table" architecture (stores Drink, Snack, Gadget in the same table based on a discriminator column). Allows price updates and product deletion.
* `CompartmentRepository`: Manages the mapping between physical products and machine shelves. Synchronizes stock in real-time with the database during sales or restocking.
* `PromoCodeRepository`: Stores discount codes, allowing validation, discount percentage updates, and deletion of expired ones.
* `TransactionRepository`: Permanently archives financial information, recording details of every successful sale (digital receipt).

### Database and JDBC (Java Database Connectivity) Technology
Communication between the Java application and the MySQL database is handled exclusively through the JDBC API, using the external dependency `mysql-connector-java.jar` (JDBC Driver). This acts as a translator between standard Java commands and the specific MySQL server protocol.

JDBC technical elements implemented in the architecture:
* **Centralized Connection (Singleton):** The database connection is opened in a controlled manner at application startup via the `DatabaseConnection` class and is efficiently shared across all Repository classes.
* **PreparedStatement for Security:** All `INSERT`, `UPDATE`, and `DELETE` operations are executed using `PreparedStatement`. This class pre-compiles SQL queries and prevents SQL Injection attacks by safely mapping parameters with specific methods (`setString`, `setInt`, `setDouble`).
* **ResultSet (Virtual Cursor):** For `SELECT` queries (e.g., reading products or history), the data returned by MySQL is stored in a `ResultSet` object. This acts as an efficient memory cursor: it iterates through the result table row by row (via the `next()` method), allowing the application to extract information in real-time and rebuild complex Java objects (e.g., `Drink`, `Snack`) in RAM without overloading the system.

### com.pao.proiect.tonomat.config (System Configuration)
* `DatabaseConnection`: Singleton class responsible for initializing and maintaining the communication tunnel with the MySQL database.

### com.pao.proiect.tonomat.exception (Custom Exceptions)
* `StocEpuizatException` (Out of Stock): Thrown when selecting a non-existent product or one with 0 quantity.
* `FonduriInsuficienteException` (Insufficient Funds): Thrown when the inserted money does not cover the cost.

---

## 2. Actions and Queries (Features)

The system exposes a series of features accessible through the Main class, delegated to specialized services and repositories:

### Data Synchronization (RAM vs. DB) and Seeding
* **Startup Load and Sync:** Upon launching the application, internal memory (RAM) is fully populated with information read from the MySQL database (products, shelves, coupons). The database acts as the Single Source of Truth.
* **Initial Population (Database Seeding):** If the application runs for the first time and detects an empty database, it will automatically inject a set of test data to prevent running an empty machine.

### Actions (System Modifications)
* **Adding a new product type:** Allocating a new product to a compartment of the vending machine using `adaugaRaft()`.
* **Restocking:** Updating the quantity for an existing product by the Administrator (`suplimenteazaStoc()`), synchronized across both RAM and DB.
* **Selecting a product:** Searching for the compartment object by entering the code (e.g., "B2") using `obtineRaftDupaCod()`.
* **Processing payment and dispensing:** Verifying funds via `proceseazaPlata()`, decreasing stock by 1 via `elibereazaProdus()`, recording the transaction in the database, and calculating cash change.
* **Administrator authentication:** Validating based on an ID and PIN code to unlock maintenance features (`autentificareAdmin()`).
* **Cash withdrawal:** Collecting money from the machine by the Administrator and resetting the internal balance (`retrageNumerar()`).
* **Applying a promo code:** The system validates if the PromoCode object exists in memory and is unused, decreasing the final price before payment (`aplicaCodPromo()`).

### Advanced Back-Office Actions (Admin CRUD)
* **Real-time price modification:** The Administrator can apply UPDATE operations directly in the products table to adjust the selling price.
* **Promotions management:** Offering the ability to change the discount percentage of an existing code or delete (DELETE) a promo code from the database.
* **History maintenance:** The option to correct an incorrectly recorded transaction or completely delete old sales history from the database.

### Queries (Searches and Data Retrieval)
* **Displaying the Full Menu:** Querying all available products in the machine with stock > 0 (`afiseazaProduseDisponibile()`).
* **Displaying products sorted by price:** Generating a formatted catalog using a TreeSet collection that automatically sorts objects implementing the Comparable interface (`afiseazaCatalogSortatDupaPret()`).
* **Critical stock query:** Iterating through shelves and triggering an automatic alert for products with quantities below a certain limit via `afiseazaStocCritic()`.
* **History Generation (Daily Report):** Printing aggregated data in the MachineReport object regarding revenue, number of items sold, and best-selling product.
* **Filtering products by specific traits:** Displaying all "sugar-free" drinks (`filtreazaBauturiFaraZahar()`), demonstrating pattern matching and the use of specific subclass conditions.

### Auditing (Logging)
* **Writing to the .csv file:** Any action (e.g., purchasing a product, authentication) or query (e.g., displaying the catalog) triggers the `AuditService` in the background. It opens the file in *append* mode and adds a new line in the format `[Action_Name], [YYYY-MM-DD HH:mm:ss]`.
