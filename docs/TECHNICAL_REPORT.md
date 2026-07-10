# Pharmacy Inventory Management System Technical Report

## 1. Title Page

**Project Title:** Pharmacy Inventory Management System  
**Technology Used:** Java Swing  
**Student Name:** Frank Akrasi Antwi  
**Programme:** MSc. Information Technology  
**Course:** Object Oriented Programming (OOP)  
**Date:** 10th July, 2026.

## 2. Introduction

This project is simple a GUI-based Pharmacy Inventory Management System developed in Java Swing. It helps a pharmacy maintain medicine records, monitor stock levels, identify expired medicines, and perform stock operations such as sales and restocking.

## 3. Problem Statement

Manual pharmacy inventory tracking can lead to stock shortages, expired medicine remaining on shelves, pricing errors, and slow record retrieval. A computerized GUI system reduces these risks by organizing records and validating stock operations.

## 4. Objectives of the System

- Store medicine inventory records in a structured format.
- Provide a user-friendly graphical interface.
- Allow staff to add, update, delete, search, sell, and restock medicines.
- Highlight low-stock items in pale yellow and expired items in pale red.
- Support CSV import and export for simple data portability.

## 5. Scope of the System

The system focuses on medicine inventory management for a small or medium pharmacy. It does not include advanced accounting, supplier billing, barcode scanning, multi-user login, or database server integration.

## 6. Methodology

The application was designed using a modular object-oriented approach. The model package contains medicine classes, the service package handles inventory operations, the exception package handles validation errors, and the UI package contains the Swing interface. The system was tested by compiling with Java 17 and exercising the main GUI workflows.

## 7. System Design

The system uses a layered structure:

- **Model layer:** Represents inventory items and medicine types.
- **Service layer:** Manages collections, validation, searching, stock updates, and CSV persistence.
- **User interface layer:** Provides forms, buttons, tables, filters, and event handling.
- **Exception layer:** Provides meaningful error messages for invalid operations.

## 8. Description of Classes and Methods

### `InventoryItem`

An abstract superclass for inventory records. It stores common fields such as ID, name, manufacturer, quantity, reorder level, price, and expiry date. It also provides methods such as `getStockStatus()`, `isExpired()`, `daysUntilExpiry()`, and `getInventoryValue()`.

### `Medicine`

Extends `InventoryItem` and adds a dosage field. It overrides `getCategory()` to return `General Medicine`.

### `PrescriptionMedicine`

Extends `Medicine` and adds a controlled class field. It overrides `getCategory()` to return `Prescription Medicine`.

### `InventoryManager`

Stores medicine records in an `ArrayList`. Important methods include `addItem()`, `updateItem()`, `removeItem()`, `restock()`, `sell()`, `search()`, `getLowStockItems()`, `getExpiredItems()`, `saveToCsv()`, and `loadFromCsv()`.

### `InventoryTableModel`

Extends `AbstractTableModel` and adapts inventory objects for display in a `JTable`.

### `MainFrame`

Builds the Swing GUI, handles button events, validates user input, and connects the interface to `InventoryManager`.

## 9. GUI Design Explanation

The interface is organized into a header, data table, form panel, action buttons, and summary footer. The table gives a clear overview of inventory records. The form panel allows focused data entry and editing. Low-stock rows are highlighted in pale yellow and expired rows are highlighted in pale red to support quick decision-making.

## 10. OOP Concepts Implemented

### Encapsulation

Fields in the model classes are private and accessed through public getter and setter methods. This protects object state and supports controlled updates.

### Inheritance

`Medicine` inherits shared inventory fields and methods from `InventoryItem`. `PrescriptionMedicine` inherits from `Medicine` and adds prescription-specific information.

### Polymorphism

The service and table model store and display objects using the `InventoryItem` type. At runtime, the objects can behave as general or prescription medicines through overridden methods.

### Abstraction

`InventoryItem` is abstract because it defines shared inventory behaviour while leaving medicine category details to subclasses.

## 11. Screenshots and Outputs

Screenshots of the application can be fount in the `screenshots` folder.

Screenshots include:

- Main dashboard with sample inventory
- Add medicine form
- Low-stock filter
- Expired medicine filter
- CSV export or import workflow
- Search functionality
- Summary statistics

## 12. GitHub Repository Link

```text
https://github.com/fantwi/pharmacy-inventory-management-system
```

## 13. Challenges Encountered

One challenge was keeping validation rules separate from the GUI so the interface remained readable. This was addressed by placing inventory rules inside `InventoryManager` and using `InventoryException` for clear error messages. Another challenge was displaying different medicine types in one table, which was solved using polymorphism and runtime type checks where type-specific fields were required.

## 14. Conclusion

The Pharmacy Inventory Management System demonstrates practical use of object-oriented programming, event-driven programming, collections, validation, exception handling, and modular design.

## 15. Recommendations

Future improvements could include a database backend, login roles, barcode scanning, supplier management, sales reports, and automatic expiry notifications.

## 16. References

- Oracle Java Documentation: https://docs.oracle.com/en/java/
- Java Swing Tutorial: https://docs.oracle.com/javase/tutorial/uiswing/
