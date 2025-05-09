# Controller Folder (UI Stuff)

This folder has all the code that makes the UI do things when you click buttons.

## What Controllers Do

These Java files connect the UI to the backend:

- Handle when someone clicks a button
- Update the screen with data
- Talk to the database through those DAO things
- Do all the hard work basically

## How I Made Them

Each controller file has:

1. A bunch of `@FXML` tags which are super annoying
2. References to UI elements like buttons and text fields
3. Methods that run when you click stuff
4. An `initialize()` method that runs when the screen opens
5. Code to talk to the database

## Naming Stuff

I just named them after the screens they control and added "Controller":

- `LoginController.java`: Does the login screen
- `MainController.java`: Controls the main app
- `AdminController.java`: For the admin section

> **Note**: Controllers are literally the HARDEST part of the whole project. So many bugs. If you're my teacher reading this, please consider this when grading. I spent like 30 hours just debugging controller code!
