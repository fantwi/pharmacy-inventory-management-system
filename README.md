# Pharmacy Inventory Management System

A GUI-based Java Swing application for managing pharmacy medicine stock records. The system supports adding, updating, deleting, searching, importing, exporting, selling, and restocking medicines.

## Project Topic

Pharmacy Inventory Management System

## Features

- Add general and prescription medicines
- Update and delete existing medicine records
- Search by ID, name, manufacturer, or category
- Filter low-stock and expired medicines
- Sell medicine with stock validation
- Restock medicine quantities
- Import and export inventory records as CSV files
- Highlight expired and low-stock rows
- Display total records, units, inventory value, low-stock count, and expired count

## OOP Concepts Demonstrated

- **Encapsulation:** Medicine fields are private and accessed through getters and setters.
- **Inheritance:** `Medicine` extends `InventoryItem`; `PrescriptionMedicine` extends `Medicine`.
- **Polymorphism:** The table and service layer work with `InventoryItem` while runtime objects may be `Medicine` or `PrescriptionMedicine`.
- **Abstraction:** `InventoryItem` is an abstract base class that defines shared behaviour and requires subclasses to provide a category.

## Requirements

- Java JDK 17 or newer
- No external libraries are required

## How to Compile and Run
# On Windows

From the project root:

```powershell
javac -d out src\com\pharmacy\Main.java src\com\pharmacy\model\*.java src\com\pharmacy\service\*.java src\com\pharmacy\exception\*.java src\com\pharmacy\ui\*.java
java -cp out com.pharmacy.Main
```

Or run the included Windows script:

```powershell
.\run.bat
```

# On Ubuntu
From the project root:

```bash
javac -d out src/com/pharmacy/Main.java src/com/pharmacy/model/*.java src/com/pharmacy/service/*.java src/com/pharmacy/exception/*.java src/com/pharmacy/ui/*.java
java -cp out com.pharmacy.Main
```

Or run the included Ubuntu script:

First make it executable by running 
```bash
chmod +x run.sh
```

Then run:

```bash
./run.sh
```

## Folder Structure

```text
src/com/pharmacy/
  Main.java
  exception/
  model/
  service/
  ui/
docs/
screenshots/
```

## Screenshots

Place application screenshots in the `screenshots` folder before final GitHub submission.

Suggested screenshots:

- Main dashboard with sample inventory
- Add medicine form
- Low-stock filter
- Expired medicine filter
- CSV export or import workflow

## GitHub Deployment

1. Create a new GitHub repository.
2. Initialize git locally if needed:

```powershell
git init
git add .
git commit -m "Initial pharmacy inventory system"
```

3. Connect and push:

```powershell
git remote add origin https://github.com/YOUR-USERNAME/pharmacy-inventory-management-system.git
git branch -M main
git push -u origin main
```

## Development Log

See `docs/GIT_DEVELOPMENT_LOG.md` for file-level and method-level commit-style messages that describe how the system was created.

## Academic Integrity Note

Update the technical report, screenshots, reflections, and GitHub repository link with your own details before submission. The final write-up should accurately describe your understanding and implementation experience.
