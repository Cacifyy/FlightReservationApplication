-- Create database
-- DROP DATABASE IF EXISTS flight_reservation_db; -- Uncomment this line to reset the database
CREATE DATABASE IF NOT EXISTS flight_reservation_db;
USE flight_reservation_db;

-- Users table (for authentication)
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'AGENT', 'ADMIN') NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(200),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Aircraft table
CREATE TABLE IF NOT EXISTS aircraft (
    aircraft_id INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    airline VARCHAR(100) NOT NULL
);

-- Flights table
CREATE TABLE IF NOT EXISTS flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(20) UNIQUE NOT NULL,
    aircraft_id INT,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    available_seats INT NOT NULL,
    status ENUM('SCHEDULED', 'DELAYED', 'CANCELLED', 'COMPLETED') DEFAULT 'SCHEDULED',
    FOREIGN KEY (aircraft_id) REFERENCES aircraft(aircraft_id) ON DELETE SET NULL
);

-- Reservations table
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    flight_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('CONFIRMED', 'CANCELLED', 'PENDING') DEFAULT 'PENDING',
    seat_number VARCHAR(10),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT UNIQUE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'CASH') NOT NULL,
    card_number VARCHAR(20),
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') DEFAULT 'PENDING',
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE
);

-- Insert sample data for testing

-- Insert users
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('agent1', 'agent123', 'AGENT'),
('john_doe', 'customer123', 'CUSTOMER'),
('jane_smith', 'customer123', 'CUSTOMER');

-- Insert customers
INSERT INTO customers (user_id, first_name, last_name, email, phone, address) VALUES
(3, 'John', 'Doe', 'john.doe@email.com', '123-456-7890', '123 Main St, Calgary'),
(4, 'Jane', 'Smith', 'jane.smith@email.com', '098-765-4321', '456 Oak Ave, Calgary');

-- Insert aircraft
INSERT INTO aircraft (model, capacity, airline) VALUES
('Boeing 737', 180, 'Air Canada'),
('Airbus A320', 150, 'WestJet'),
('Boeing 787', 280, 'Air Canada'),
('Airbus A350', 300, 'WestJet');

-- Insert flights
INSERT INTO flights (flight_number, aircraft_id, origin, destination, departure_time, arrival_time, price, available_seats, status) VALUES
('AC101', 1, 'Calgary', 'Toronto', '2025-12-01 08:00:00', '2025-12-01 14:00:00', 450.00, 180, 'SCHEDULED'),
('WS202', 2, 'Calgary', 'Vancouver', '2025-12-01 10:00:00', '2025-12-01 11:30:00', 220.00, 150, 'SCHEDULED'),
('AC303', 3, 'Toronto', 'London', '2025-12-02 18:00:00', '2025-12-03 08:00:00', 1200.00, 280, 'SCHEDULED'),
('WS404', 4, 'Vancouver', 'Tokyo', '2025-12-03 15:00:00', '2025-12-04 16:00:00', 1500.00, 300, 'SCHEDULED'),
('AC505', 1, 'Calgary', 'Montreal', '2025-12-05 09:00:00', '2025-12-05 15:30:00', 480.00, 180, 'SCHEDULED');
