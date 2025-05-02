-- Clear existing data
DELETE FROM bookings;
DELETE FROM customers;
DELETE FROM destinations;

-- Reset auto-increment counters
DELETE FROM sqlite_sequence WHERE name='customers';
DELETE FROM sqlite_sequence WHERE name='destinations';
DELETE FROM sqlite_sequence WHERE name='bookings';

-- Insert sample customers
INSERT INTO customers (name, email, phone, address) VALUES 
('John Smith', 'john.smith@example.com', '+30 694 123 4567', 'Athens, Greece'),
('Maria Garcia', 'maria.garcia@example.com', '+30 697 234 5678', 'Thessaloniki, Greece'),
('Dimitris Papadopoulos', 'dimitris@example.com', '+30 698 345 6789', 'Heraklion, Crete'),
('Sophie Johnson', 'sophie@example.com', '+30 691 456 7890', 'Rhodes, Greece'),
('Alexandros Ioannou', 'alexis@example.com', '+30 695 567 8901', 'Patras, Greece');

-- Insert sample destinations
INSERT INTO destinations (name, country, description, price_per_person) VALUES 
('Santorini', 'Greece', 'Beautiful island with white and blue houses', 850.00),
('Mykonos', 'Greece', 'Famous party island with beautiful beaches', 750.00),
('Athens', 'Greece', 'Historical city with ancient ruins', 550.00),
('Crete', 'Greece', 'Largest Greek island with rich history', 650.00),
('Rome', 'Italy', 'Eternal city with ancient history', 700.00),
('Barcelona', 'Spain', 'Vibrant city with unique architecture', 680.00),
('Paris', 'France', 'City of lights and romance', 750.00),
('London', 'United Kingdom', 'Historic city with modern attractions', 800.00);

-- Insert sample bookings
INSERT INTO bookings (customer_id, destination_id, booking_date, travel_date, number_of_people, total_price, status) VALUES 
(1, 1, '2025-01-15', '2025-06-20', 2, 1700.00, 'Confirmed'),
(2, 3, '2025-01-20', '2025-07-10', 4, 2200.00, 'Pending'),
(3, 2, '2025-02-01', '2025-08-05', 2, 1500.00, 'Confirmed'),
(4, 5, '2025-02-10', '2025-09-15', 3, 2100.00, 'Confirmed'),
(5, 4, '2025-03-05', '2025-07-25', 2, 1300.00, 'Pending'),
(1, 6, '2025-03-10', '2025-10-10', 2, 1360.00, 'Pending'),
(2, 7, '2025-03-15', '2025-08-20', 3, 2250.00, 'Cancelled'),
(3, 8, '2025-04-01', '2025-09-05', 4, 3200.00, 'Confirmed');

-- Show newly inserted data counts
SELECT 'Customers: ' || COUNT(*) FROM customers;
SELECT 'Destinations: ' || COUNT(*) FROM destinations;
SELECT 'Bookings: ' || COUNT(*) FROM bookings; 