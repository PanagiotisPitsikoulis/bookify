package com.bookify.app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SampleDataGenerator {
        private static final Random random = new Random();
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Lists of sample data
        private static final List<String> FIRST_NAMES = Arrays.asList(
                        "John", "Jane", "Michael", "Emma", "David", "Olivia", "Daniel", "Sophia", "Matthew", "Ava",
                        "James", "Isabella", "Joseph", "Mia", "Andrew", "Charlotte", "Thomas", "Amelia", "Benjamin",
                        "Harper",
                        "Alexander", "Evelyn", "William", "Abigail", "Christopher", "Emily", "Joshua", "Elizabeth",
                        "Nicholas",
                        "Sofia",
                        "Ethan", "Avery", "Anthony", "Ella", "Ryan", "Scarlett", "Jacob", "Grace", "Samuel", "Lily",
                        "Jonathan", "Chloe", "Christian", "Victoria", "Noah", "Aria", "Dylan", "Zoe", "Robert",
                        "Natalie",
                        "Nathan", "Addison", "Kevin", "Aubrey", "Brandon", "Brooklyn", "Jason", "Audrey", "Justin",
                        "Leah",
                        "Sarah", "Maria", "Jessica", "Jennifer", "Lisa", "Linda", "Patricia", "Elizabeth", "Sandra",
                        "Dorothy",
                        "Ashley", "Kimberly", "Amanda", "Michelle", "Melissa", "Stephanie", "Laura", "Rebecca",
                        "Sharon", "Cynthia",
                        "Sophia", "Margaret", "Catherine", "Virginia", "Jacqueline", "Janet", "Ruth", "Helen", "Debra",
                        "Frances",
                        "Rachel", "Carolyn", "Diane", "Christine", "Julie", "Emma", "Olivia", "Hannah", "Samantha",
                        "Nicole");

        private static final List<String> LAST_NAMES = Arrays.asList(
                        "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore",
                        "Taylor",
                        "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez",
                        "Robinson",
                        "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young", "Hernandez", "King",
                        "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson", "Carter",
                        "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards",
                        "Collins",
                        "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey",
                        "Rivera", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray",
                        "Ramirez",
                        "James", "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross",
                        "Henderson", "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores",
                        "Washington",
                        "Butler", "Simmons", "Foster", "Gonzales", "Bryant", "Alexander", "Russell", "Griffin", "Diaz",
                        "Hayes");

        private static final List<String> EMAIL_DOMAINS = Arrays.asList(
                        "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com", "icloud.com",
                        "protonmail.com", "mail.com", "zoho.com", "gmx.com", "yandex.com", "comcast.net");

        private static final List<String> PHONE_PREFIXES = Arrays.asList(
                        "201", "202", "205", "206", "207", "208", "209", "210", "212", "213",
                        "214", "215", "216", "217", "218", "219", "224", "225", "228", "229");

        private static final List<String> STREET_NAMES = Arrays.asList(
                        "Main Street", "Oak Avenue", "Park Road", "Maple Drive", "Cedar Lane",
                        "Pine Street", "Washington Avenue", "Lake View Drive", "River Road", "Highland Avenue",
                        "Sunset Boulevard", "Orchard Lane", "Forest Drive", "Mountain View Drive", "Meadow Lane",
                        "Valley Road", "Spring Street", "Willow Drive", "Hillside Avenue", "Brookside Drive",
                        "Fairway Drive", "Jefferson Street", "Lincoln Avenue", "Franklin Street", "Colonial Drive");

        private static final List<String> CITIES = Arrays.asList(
                        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
                        "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville", "Fort Worth", "Columbus",
                        "Indianapolis", "Charlotte", "San Francisco", "Seattle", "Denver", "Boston",
                        "Nashville", "Portland", "Las Vegas", "Miami", "Atlanta", "Minneapolis", "Tampa",
                        "Cleveland", "Raleigh", "Orlando", "Sacramento", "St. Louis", "Pittsburgh", "Cincinnati");

        private static final List<String> STATES = Arrays.asList(
                        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                        "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ");

        private static final List<String> COUNTRIES = Arrays.asList(
                        "France", "Italy", "Spain", "Greece", "Germany", "United Kingdom",
                        "Portugal", "Netherlands", "Switzerland", "Austria", "Croatia", "Japan",
                        "Thailand", "Indonesia", "Vietnam", "Australia", "New Zealand", "Canada",
                        "Mexico", "Brazil", "Argentina", "South Africa", "Morocco", "Egypt",
                        "UAE", "Turkey", "Jordan", "India", "Singapore", "South Korea");

        private static final List<String> FAMOUS_PLACES = Arrays.asList(
                        "Paris", "Rome", "Barcelona", "Athens", "Berlin", "London", "Lisbon", "Amsterdam",
                        "Zurich", "Vienna", "Dubrovnik", "Tokyo", "Bangkok", "Bali", "Hanoi", "Sydney",
                        "Auckland", "Vancouver", "Cancun", "Rio de Janeiro", "Buenos Aires", "Cape Town",
                        "Marrakech", "Cairo", "Dubai", "Istanbul", "Petra", "Mumbai", "Singapore City", "Seoul",
                        "Santorini", "Venice", "Florence", "Madrid", "Prague", "Budapest", "Stockholm", "Copenhagen",
                        "Oslo", "Helsinki", "Reykjavik", "Kyoto", "Phuket", "Ho Chi Minh City", "Melbourne",
                        "Quebec City", "Montreal", "Toronto", "San Francisco", "New York");

        private static final List<String> DESTINATION_DESCRIPTIONS = Arrays.asList(
                        "A stunning city with beautiful architecture, world-class museums, and exquisite cuisine.",
                        "Famous for its ancient ruins, delicious food, and vibrant nightlife.",
                        "Beautiful beaches, crystal-clear waters, and lush mountainous landscapes.",
                        "Rich in history, culture, and offers a unique blend of ancient and modern attractions.",
                        "Known for its picturesque landscapes, charming villages, and outdoor activities.",
                        "A bustling metropolis with excellent shopping, dining, and cultural experiences.",
                        "A perfect destination for nature lovers with its pristine beaches and hiking trails.",
                        "Offers a perfect mix of historical sites, art galleries, and vibrant street life.",
                        "Famous for its stunning architecture, canals, and rich cultural heritage.",
                        "A paradise for food enthusiasts with its world-renowned culinary scene.",
                        "Beautiful coastal views, historic sites, and relaxing beach experiences.",
                        "Known for its technology, traditions, and breathtaking cherry blossoms.",
                        "Vibrant street markets, ancient temples, and delicious street food.",
                        "Paradise island with lush rice terraces, beautiful beaches, and unique culture.",
                        "Rich in culture with its colonial architecture, traditional cuisine, and friendly locals.",
                        "Featuring iconic landmarks, beautiful harbors, and diverse wildlife.",
                        "Breathtaking landscapes, adventure activities, and Maori cultural experiences.",
                        "Known for its stunning natural beauty, friendly people, and outdoor adventures.",
                        "Beautiful beaches, ancient Mayan ruins, and vibrant nightlife.",
                        "Famous for its carnival, beautiful beaches, and vibrant culture.",
                        "Known for its tango, steak, and European-influenced architecture.",
                        "Offering stunning coastal views, safari experiences, and diverse cultures.",
                        "Famous for its colorful markets, historic medinas, and delicious cuisine.",
                        "Home to the pyramids, ancient temples, and rich historical heritage.",
                        "Ultra-modern architecture, luxury shopping, and desert adventures.",
                        "Where East meets West, offering rich history, bazaars, and stunning mosques.",
                        "Famous for its ancient city carved into rose-colored stone cliffs.",
                        "Vibrant culture, delicious food, and colorful festivals.",
                        "Ultra-modern, clean, and offering excellent food and shopping experiences.",
                        "High-tech city with a perfect blend of modern skyscrapers and traditional temples.");

        private static final List<String> BOOKING_STATUSES = Arrays.asList(
                        "Confirmed", "Pending", "Completed", "Cancelled", "In Progress");

        /**
         * Generate sample data for the database
         */
        public static void generateSampleData() {
                try {
                        Connection conn = DatabaseConnection.getConnection();

                        // Generate customers (100)
                        generateCustomers(conn, 100);

                        // Generate destinations (30)
                        generateDestinations(conn, 30);

                        // Generate bookings (200)
                        generateBookings(conn, 200);

                        System.out.println("Sample data generated successfully!");

                } catch (Exception e) {
                        System.err.println("Error generating sample data: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        /**
         * Generate sample customers
         */
        private static void generateCustomers(Connection conn, int count) throws Exception {
                System.out.println("Generating " + count + " customers...");

                // First, clear existing customers
                Statement clearStmt = conn.createStatement();
                clearStmt.executeUpdate("DELETE FROM customers");

                // Prepare the insert statement
                String sql = "INSERT INTO customers (name, email, phone, address) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                // Generate random customers
                for (int i = 0; i < count; i++) {
                        String firstName = FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size()));
                        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
                        String name = firstName + " " + lastName;

                        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" +
                                        EMAIL_DOMAINS.get(random.nextInt(EMAIL_DOMAINS.size()));

                        String phone = PHONE_PREFIXES.get(random.nextInt(PHONE_PREFIXES.size())) + "-" +
                                        (100 + random.nextInt(900)) + "-" +
                                        (1000 + random.nextInt(9000));

                        String address = (10 + random.nextInt(9990)) + " " +
                                        STREET_NAMES.get(random.nextInt(STREET_NAMES.size())) + ", " +
                                        CITIES.get(random.nextInt(CITIES.size())) + ", " +
                                        STATES.get(random.nextInt(STATES.size())) + " " +
                                        (10000 + random.nextInt(90000));

                        pstmt.setString(1, name);
                        pstmt.setString(2, email);
                        pstmt.setString(3, phone);
                        pstmt.setString(4, address);
                        pstmt.executeUpdate();
                }

                pstmt.close();
        }

        /**
         * Generate sample destinations
         */
        private static void generateDestinations(Connection conn, int count) throws Exception {
                System.out.println("Generating " + count + " destinations...");

                // First, clear existing destinations
                Statement clearStmt = conn.createStatement();
                clearStmt.executeUpdate("DELETE FROM destinations");

                // Prepare the insert statement
                String sql = "INSERT INTO destinations (name, country, description, price_per_person) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                // Use all the famous places, but don't exceed count
                int placesToUse = Math.min(count, FAMOUS_PLACES.size());

                // Make a copy of the lists to shuffle
                List<String> places = new ArrayList<>(FAMOUS_PLACES.subList(0, placesToUse));
                List<String> countries = new ArrayList<>(COUNTRIES);
                List<String> descriptions = new ArrayList<>(DESTINATION_DESCRIPTIONS);

                // Generate random destinations
                for (int i = 0; i < count; i++) {
                        String place = i < places.size() ? places.get(i)
                                        : FAMOUS_PLACES.get(random.nextInt(FAMOUS_PLACES.size()));

                        String country = countries.get(random.nextInt(countries.size()));
                        String description = descriptions.get(random.nextInt(descriptions.size()));
                        double price = 500 + random.nextInt(2000) + random.nextDouble();

                        pstmt.setString(1, place);
                        pstmt.setString(2, country);
                        pstmt.setString(3, description);
                        pstmt.setDouble(4, Math.round(price * 100.0) / 100.0);
                        pstmt.executeUpdate();
                }

                pstmt.close();
        }

        /**
         * Generate sample bookings
         */
        private static void generateBookings(Connection conn, int count) throws Exception {
                System.out.println("Generating " + count + " bookings...");

                // First, clear existing bookings
                Statement clearStmt = conn.createStatement();
                clearStmt.executeUpdate("DELETE FROM bookings");

                // Get all customer IDs
                List<Integer> customerIds = new ArrayList<>();
                Statement custStmt = conn.createStatement();
                ResultSet custRs = custStmt.executeQuery("SELECT id FROM customers");
                while (custRs.next()) {
                        customerIds.add(custRs.getInt("id"));
                }

                // Get all destination IDs and prices
                List<Integer> destinationIds = new ArrayList<>();
                List<Double> destinationPrices = new ArrayList<>();
                Statement destStmt = conn.createStatement();
                ResultSet destRs = destStmt.executeQuery("SELECT id, price_per_person FROM destinations");
                while (destRs.next()) {
                        destinationIds.add(destRs.getInt("id"));
                        destinationPrices.add(destRs.getDouble("price_per_person"));
                }

                // Prepare the insert statement
                String sql = "INSERT INTO bookings (customer_id, destination_id, travel_date, booking_date, number_of_people, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                // Generate random bookings
                LocalDate today = LocalDate.now();
                for (int i = 0; i < count; i++) {
                        // Select a random customer and destination
                        int customerIndex = random.nextInt(customerIds.size());
                        int destinationIndex = random.nextInt(destinationIds.size());

                        int customerId = customerIds.get(customerIndex);
                        int destinationId = destinationIds.get(destinationIndex);
                        double basePrice = destinationPrices.get(destinationIndex);

                        // Generate a random travel date (between now and 1 year from now)
                        LocalDate travelDate = today.plusDays(random.nextInt(365));

                        // Generate a random booking date (between 30 days ago and today)
                        LocalDate bookingDate = today.minusDays(random.nextInt(30));

                        // Random number of people and calculate total price
                        int numberOfPeople = 1 + random.nextInt(5);
                        double totalPrice = basePrice * numberOfPeople;

                        // Random status
                        String status = BOOKING_STATUSES.get(random.nextInt(BOOKING_STATUSES.size()));

                        pstmt.setInt(1, customerId);
                        pstmt.setInt(2, destinationId);
                        pstmt.setString(3, travelDate.format(formatter));
                        pstmt.setString(4, bookingDate.format(formatter));
                        pstmt.setInt(5, numberOfPeople);
                        pstmt.setDouble(6, Math.round(totalPrice * 100.0) / 100.0);
                        pstmt.setString(7, status);
                        pstmt.executeUpdate();
                }

                pstmt.close();
        }
}