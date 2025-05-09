# DAO Folder (Database Access Objects)

This folder has all the classes that talk to the database. DAO stands for "Database Access Objects" (I had to Google that lol).

## What DAOs Do

These classes:

- Talk to the database so other code doesn't have to
- Do all the SQL stuff (which is super confusing)
- Convert database data into Java objects
- Basically hide all the messy database stuff

## How I Made Them

Each DAO has methods like:

1. `save()` - adds new stuff to the database
2. `findById()`, `getAll()` - gets data from database
3. `update()` - changes existing data
4. `delete()` - removes data
5. A bunch of boring SQL statements

## Main Classes

- `CustomerDAO.java`: Handles customer table stuff
- `DestinationDAO.java`: For destination table
- `BookingDAO.java`: For booking table

## Why We Need These

My teacher said this is better than putting SQL code everywhere. I guess it makes it easier to change the database later, but it means writing a LOT more code.

> **Note**: These were super hard to debug. If you're reading this, prof, I spent like 15 hours fixing a bug that was just a single quote in an SQL statement.
