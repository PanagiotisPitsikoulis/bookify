# Database Code

This folder has the code that connects to the SQLite database. Super boring but important I guess.

## What This Stuff Does

These classes:

- Make connections to the database
- Set up the database tables first time you run the app
- Have a bunch of utility methods for database stuff
- Handle errors when the database freaks out

## Main Files

- `DatabaseConnection.java`: Singleton class (whatever that means) that connects to SQLite
- `DatabaseInitializer.java`: Creates the tables if they don't exist
- `SqliteHelper.java`: Some random helper methods I found on Stack Overflow

## How Database Connections Work

1. Using SQLite which is just a file (easiest option)
2. The `DatabaseConnection` class handles all connections
3. The DAO classes ask this class for connections
4. Everything closes properly (I think?) to avoid memory leaks

## Tables I Made

- `customers`: For customer info
- `destinations`: For travel destinations
- `bookings`: For booking records

## Tips for Anyone Reading This

If you're working with databases:

- Use prepared statements or you'll get hacked
- Always close your connections or bad things happen
- Catch SQL exceptions or your app will crash randomly
- Don't put database code in UI code (my teacher was VERY clear about this)

> **Note**: I spent 2 days figuring out why my app crashed only to realize I forgot to close a database connection. SQLite is easy but still annoying.
