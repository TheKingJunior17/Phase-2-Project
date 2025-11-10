# UML Diagrams for TP3 - Architecture and Design

## Overview
This directory contains UML diagrams created using Astah Community Edition for the TP3 phase of the project. All diagrams support the architectural decisions and design documentation required for the assignment.

## Diagram List

### 1. Class Diagrams
- **TP3_ClassDiagram_Authentication.png** - Authentication and user management classes
- **TP3_ClassDiagram_Validation.png** - Validation and business logic components  
- **TP3_ClassDiagram_Database.png** - Entity classes and data access layer

### 2. Sequence Diagrams  
- **TP3_SequenceDiagram_LoginFlow.png** - Complete user authentication sequence
- **TP3_SequenceDiagram_RoleAccess.png** - Role-based permission checking workflow
- **TP3_SequenceDiagram_QuestionSubmission.png** - Question validation and submission process

### 3. Component Diagrams
- **TP3_ComponentDiagram_TestArchitecture.png** - JUnit test integration and automation
- **TP3_ComponentDiagram_SystemOverview.png** - High-level system architecture

## Astah File Information
- **Tool**: Astah Community Edition 9.x
- **Source Files**: Available upon request (`.asta` format)
- **Export Format**: PNG (1920x1080 resolution)
- **Last Updated**: November 9, 2025

## Design Rationale

### Authentication Architecture
The authentication system follows a layered approach:
- **Presentation Layer**: User input validation and session management
- **Service Layer**: Authentication logic and token generation  
- **Data Layer**: User credential storage and retrieval

### Validation Pipeline
The validation architecture uses the Chain of Responsibility pattern:
- **Input Validators**: Basic format and length checks
- **Business Validators**: Domain-specific rules and constraints
- **Integration Validators**: Cross-system validation and consistency checks

### Test Integration
The testing architecture mirrors the production structure:
- **Unit Tests**: Individual class and method validation
- **Integration Tests**: Multi-component workflow verification
- **System Tests**: End-to-end user scenario validation

## Mapping to Implementation
Each UML diagram directly maps to implemented code:
- Class diagrams correspond to actual Java classes in `src/main/java/`
- Sequence diagrams reflect method call chains in service classes
- Component diagrams show package dependencies and test relationships

## Instructions for Team Members

### Creating Diagrams in Astah
1. Install Astah Community Edition from https://astah.net/
2. Open the template project or create new diagrams
3. Follow naming conventions: `TP3_DiagramType_Description`
4. Export as PNG with 1920x1080 resolution
5. Place exported files in this directory

### Updating This Documentation
When adding new diagrams:
1. Update the diagram list above
2. Add design rationale explanation
3. Verify mapping to implementation code
4. Commit both .png files and updated README

## Usage in Screencasts
These diagrams will be featured in:
- **Screencast 2**: Vision-to-design alignment demonstration
- **PDF Submission**: Architecture documentation section
- **Code Reviews**: Design decision explanations

---
**Note**: If Astah is not available, use alternative UML tools and export diagrams in this format. The requirement is professional UML diagrams that accurately represent the system design.