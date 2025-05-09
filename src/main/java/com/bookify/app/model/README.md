# Model Classes (Data Stuff)

This folder has all the data classes that store information. Pretty boring tbh.

## What Models Are

Just classes that store data:

- Hold info about things like customers and bookings
- Getters and setters for each piece of data
- These are the easiest part of the project lol
- Basically just containers for moving data around

## How I Made Them

Each class has:

1. A bunch of private variables (name, id, etc.)
2. Getters and setters for everything
3. Constructors to create new objects
4. Some validation code (to make sure data isn't garbage)
5. Some JavaFX property stuff (which is confusing)

## Main Classes

- `Customer.java`: Stores info about customers
- `Destination.java`: Keeps track of places people can go
- `Booking.java`: Links customers to destinations

## Database Connection

Models are basically just Java versions of database tables. Each object = one row in the database table.

> **Note**: These are super easy to write but SO BORING. Just a bunch of getters and setters. Literally copy-pasted most of this code and changed the variable names.
