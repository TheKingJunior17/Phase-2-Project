# Phase-2 Project - CSE 360 Question Management System# Student Q&A System - Team Project Phase 2 (TP2)



## Overview## Overview



This project contains a JavaFX-based Student Q&A System for CSE 360.In Team Project Phase 2 we will make a **Student Q&A System.**

This system allows students to:

## Organized Project Structure

- Ask questions and provide answers.

- **Application/** - Main JavaFX application (question management system)- Search for relevant questions and answers.

- **HW2.1/** - Eclipse project files and configuration- Suggest clarifications on existing questions or answers.

- Mark an answer as accepted when it resolves the issue.

## Features

In this project we will be building on **Individual Homework 2 (HW2)** and Team Project Phase 1 which uses functionality, including:

### Core Functionality

- User authentication and account management- Search functionality for finding relevant questions and answers.

- Question submission and management  - The ability to suggest clarifications for better understanding.

- Answer submission and validation- A feature to mark an answer as the accepted solution.

- Real-time search and filtering- Enhanced CRUD operations with validation.

- Database integration with H2- Automated and manual testing to ensure system reliability.

- Multiple user roles (Student, Admin, Reviewer, Staff)

---

### CRUD Operations

- Create, Read, Update, and Delete for questions and answers## Features Implemented

- Input validation prevents empty titles, descriptions, and answers

### 1. CRUD Operations

### Search & Management

- Search functionality for finding relevant questions and answers- Create, Read, Update, and Delete (CRUD) for questions and answers.

- Ability to suggest clarifications for better understanding- Input validation prevents empty titles, descriptions, and answers.

- Feature to mark an answer as the accepted solution

- Enhanced CRUD operations with validation### 2. Searching for Questions & Answers



## Running the Application- Students can search for questions by ID

- Searching allows students to find existing solutions and avoid duplicate questions.

1. Navigate to the `Application/` directory

2. Use the following command in PowerShell:### 3. Suggesting Clarifications



```powershell- Students can submit clarification requests on questions or answers.

java --module-path .\javafx-sdk\javafx-sdk-24.0.2\lib --add-modules javafx.controls,javafx.fxml -cp ".\bin;.\lib\h2-2.2.224.jar" application.StartCSE360

```### 4. Marking an Answer as Accepted



## Requirements- Students can mark an answer as Accepted to show that it resolved the original question.

- Accepted answers are highlighted so that future users can quickly find solutions.

- Java 24

- JavaFX 24.0.2 (included in project)### 5. Input Validation & Error Handling

- H2 Database (included in project)

- Ensures questions and answers cannot be blank.

## Author- Displays appropriate error messages for invalid operations.



Jose Mendez (jrmend13@asu.edu)### 6. Automated & Manual Testing

- GUI tests validate CRUD operations and input validation.
- Console output logs confirm correct system behavior.

---
