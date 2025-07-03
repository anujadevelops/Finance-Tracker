# 📌 Finance Tracker
## 📜 Description
### Finance Tracker is an advanced, desktop-based Personal Finance Management Application built using Java (Swing) and backed by a MySQL database.  Designed for individuals looking to take control of their finances, it provides a seamless and intuitive interface for tracking Income, Expenses, Budgets, and Investments — all in one place.With its user-friendly dashboards, graphical insights, and export options Finance Tracker makes personal finance management simple, visual, and effective.

## 🚀 Features
### 🔐 User Registration & Login
### Register new users with unique usernames and passwords.
### Secure login system backed by MySQL database.
### User-specific data — budgets, expenses, and investments are stored per user.

### 💰 Income & Expense Management
### Add, edit, and delete expense records by category.
### Category selection with emoji/icon indicators for better clarity.
### Automatic calculation of total expenses and remaining balance.
### Visual budget warnings when expenses exceed the set category budgets.
### Monthly filtering of expenses (deducts investments from current month income automatically).
### Supports saving and exporting expenses to .txt or .csv formats.

### 🎯 Budget Manager
### Set and manage budgets for different categories.
### Automatic calculation of total monthly budget.
### Synchronization with expenses — categories linked to budgets for comparison.
### Easy-to-use interface for adding, deleting, and viewing budgets.
### Export budget records to .txt or .csv files.

### 📈 Investment Tracker
### Track multiple types of investments:
### 📈 Stock Investments: Auto-calculates returns based on current prices and quantity.
### 📊 Mutual Funds: Supports SIP/Lump Sum types; calculates returns based on NAV.
### 🏠 Real Estate: Tracks property value, rental income, and remarks.
### 💼 Other Investments: General investment tracking with returns calculation.
### Auto-calculates Return on Investment (ROI %).
### Optionally deducts investments from monthly income for realistic financial reports.
### Organized and expandable investment forms based on selected category.
### Export investments to .txt or .csv formats.

### 📊 Dashboard & Statistics
### Summarized financial overview including:
#### Total Income
#### Total Expenses
#### Remaining Balance

### Integrated Pie Charts (powered by JFreeChart) for:
#### Expense distribution by category.
#### Investment distribution by category.
#### Dynamic updates — charts automatically refresh when data changes.

### 📂 Data Export & Backup
### Save any table (Expenses, Budgets, Investments) into:
### TXT (tab-separated) for easy readability.
### CSV for spreadsheet applications like Microsoft Excel or Google Sheets.
### Export functionality is available directly within the app.

### 🎨 Modern, User-Friendly Interface
### Built using Java Swing with FlatLaf for modern look & feel.
### Responsive, scrollable panels to fit different screen sizes.
### Clean, intuitive navigation with clear section headings and buttons.
### Consistent use of colors and fonts for professional appearance.

### 🔧 Database-Driven Persistence
### MySQL-based storage for all users, budgets, expenses, and investments.
### Easy-to-configure database connection via DBConnection.java.
### All financial data is stored securely for future retrieval.

### ✅ Additional Functionalities:
### Reset budgeted amount for categories removed from the budget.
### Real-time financial computations without needing manual refresh.
### Form validation to prevent invalid entries.
### Modular codebase for easy maintenance and future feature expansion.


## 💻 Installation
## 1️⃣ Clone the repository:
```
git clone https://github.com/anujadevelops/Finance-Tracker.git
```
## 2️⃣ Navigate to the project directory:
``` 
Copy
Edit
cd Finance-Tracker
```
## 3️⃣ Compile and run the project:

### compile using the command line:
```
javac AppLauncher.java
java AppLauncher
```
## 4️⃣ Run the application:

### Follow these steps to run the Finance Tracker application on your machine:

### ✅ 1. Prerequisites
### Java Development Kit (JDK 17 or higher)
### MySQL Database Server
### External Libraries (Add these JAR files to your project’s classpath):
### MySQL Connector/J (for MySQL database connectivity)
### FlatLaf (for modern UI styling)
### JFreeChart (for generating Pie Charts)
### JCalendar (for date picker components)

### ✅ 2. Database Setup
### Launch MySQL Command Line or Workbench.
### Create the required database and tables

### ✅ 3. Compile the Project
Run the following command from the project root (adjust JAR paths if needed):
### javac -cp ".;libs/*" com/yourapp/expense/*.java (Assuming you’ve placed all required JAR files inside a libs/ folder.)

### ✅ 4. Launch the Application
### Run the following command to start the app:
### java -cp ".;libs/*" com.yourapp.expense.RegisterForm
### Register a new user via the registration form.
### After successful registration, you’ll be redirected to the Login Page.
### Once logged in, you can: Manage Income, Budgets, Expenses, and Investments.Export data to .txt or .csv.Visualize spending using Pie Charts.

### ✅ 5. Optional: Using an IDE
### You can also import this project into popular IDEs like IntelliJ IDEA, Eclipse, or NetBeans:
#### Import as a Java Project.
#### Add external libraries (JAR files) to the project's dependencies.
#### Set RegisterForm or LoginPage as the main class.

## 🛠️ Features in Detail
### The Finance Tracker is a comprehensive desktop application designed for efficient financial management. It offers the following core features:

### ✅ 1. User Authentication (Login & Registration)
### Secure User Registration with username and password.
### Simple Login system with authentication against a MySQL database.
### Each user's data (Budgets, Expenses, Investments) is isolated for privacy.

### ✅ 2. Income Management
### Input and update your monthly income.
### Track income changes dynamically.
### Income is displayed in the dashboard summary panel.
### Expenses and Investments are deducted from your income to compute remaining balance.

### ✅ 3. Budget Management
### Set monthly budgets for various categories (like Groceries, Bills, Travel, etc.).
### View and edit budgets via an intuitive table.
### Delete budget entries easily.
### Automatic total budget calculation.
### Budget data stored securely in the database.
### Export Budget records to:
#### Text file (.txt)
#### CSV file (.csv) for Excel or spreadsheet apps.

### ✅ 4. Expense Tracking
### Add expenses with:
#### Category
#### Amount
#### Budget Allocation
#### Date of Expense

### Editable and deletable expense table.
### Highlights expenses that exceed allocated budgets.
### Automatically reduces your total income based on expenses in the current month.
### Easy Export to .txt or .csv formats.

### ✅ 5. Investment Management
### Supports tracking of multiple types of investments:
### Stock Investments
### Enter share quantity, purchase price, and current price.
### Auto-calculates invested amount and current value.
### Calculates returns in % automatically.

### Mutual Funds
### Supports both SIP and Lump Sum types.
### Enter investment details like NAV at purchase, current NAV, and amount invested.
### Auto-computes current value and returns.

### Real Estate
### Manage property purchases, current market value, rental income, and remarks.
### Auto-calculates returns.
### Other Investments
### Simple tracking for custom investment types with basic invested/current value and remarks.

### ✅ Features:
### Auto-calculate investment returns and performance.
### Visual investment breakdown by category.
### Data export to .txt and .csv.
### Option to deduct investments from monthly income (optional).

### ✅ 6. Visual Dashboard with Statistics
### Pie Chart Visualization using JFreeChart:
### Displays spending breakdown by expense category.
### Displays investment distribution by type.
### Auto-updated charts after every data change.

### Summary Labels showing:
#### Total Income
#### Total Expenses
#### Current Balance
#### Total Budget

### ✅ 7. Data Persistence
### All data is stored in a MySQL database.
### Easy export options:
### Save to a readable Text File (.txt).
### Export data to CSV for spreadsheet analysis.

### ✅ 8. Modern & Responsive UI
### Built using Java Swing with modern UI styling via FlatLaf.
### Organized via Tabbed Pane for better navigation.
### Responsive layouts with scrollable panels for small screens.
### Emojis & color-coded elements for better visual clarity.

### ✅ 9. Other Features
### Budget warnings (color indicators if expenses exceed budget).
### Investment and expense inputs with date pickers using JCalendar.
### Full CRUD operations (Create, Read, Update, Delete) for Budgets, Expenses, and Investments.
### Professional error handling and input validations.

## 🤝 Contributing
### 1️⃣ Fork the repository.
### 2️⃣ Create a new branch (e.g., git checkout -b feature-branch).
### 3️⃣ Make your changes and thoroughly test them.
### 4️⃣ Commit your changes (e.g., git commit -am "Add: Description of new feature or fix").
### 5️⃣ Push to your fork (e.g., git push origin feature-branch).
### 6️⃣ Submit a pull request describing your changes and the purpose of the contribution.

## 💡 Feedback
### If you have suggestions or encounter any issues, feel free to open an issue  or pull request for bugs, improvements, or suggestions! on the repository or reach out directly.

## ⚠️ Limitations
### 🔹 No Cloud Integration
### The application currently only supports local file storage and MySQL database on the local machine. No cloud backup or online sync is provided.

### 🔹 Simple UI
### While the UI is functional, it is minimalistic. Future versions may include advanced design, better theme options, and a more dynamic interface.

### 🔹 Basic Authentication
### The login and registration system is basic, without features like password encryption, OTP, or multi-factor authentication.

### 🔹 No Mobile or Cross-Platform Support
### The application is designed for desktop use (Java Swing) and doesn't have mobile or web versions.


## Thank you for checking out the Finance Tracker! 🎉
