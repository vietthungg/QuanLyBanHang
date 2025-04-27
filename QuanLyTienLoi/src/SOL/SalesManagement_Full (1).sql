
-- Tạo cơ sở dữ liệu
DROP DATABASE IF EXISTS SalesManagement;
GO
CREATE DATABASE SalesManagement;
GO
USE SalesManagement;
GO

-- Bảng danh mục sản phẩm
CREATE TABLE Categories (
    CategoryID NVARCHAR(20) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL
);

-- Bảng sản phẩm
CREATE TABLE Products (
    ProductID NVARCHAR(20) PRIMARY KEY,
    ProductName NVARCHAR(100) NOT NULL,
    CategoryID NVARCHAR(20),
    Price DECIMAL(10, 2) NOT NULL,
    StockQuantity INT NOT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

-- Bảng khách hàng
CREATE TABLE Customers (
    CustomerID NVARCHAR(20) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Phone NVARCHAR(20)
);

-- Bảng đơn hàng
CREATE TABLE Orders (
    OrderID NVARCHAR(20) PRIMARY KEY,
    CustomerID NVARCHAR(20),
    OrderDate DATETIME NOT NULL,
    TotalAmount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
);

-- Bảng chi tiết đơn hàng
CREATE TABLE OrderDetails (
    OrderDetailID NVARCHAR(20) PRIMARY KEY,
    OrderID NVARCHAR(20),
    ProductID NVARCHAR(20),
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);