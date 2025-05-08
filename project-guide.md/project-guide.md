Homework & Project Guideline

*Objective:* Your primary goal is to collaboratively design and develop an application in Java with Eclipse JDK 11 or 17 preferred. This project will be implemented in two phases: a console-based application (midterm) and a desktop GUI application (final). This project will test your skills in software engineering, object-oriented programming, understanding and application of UML diagrams, and effective team collaboration. You'll start by using a provided Java Maven template for your project setup, adhering to a comprehensive list of development, documentation, and testing standards to ensure a robust application.

             *Project Phases:*

1. *Midterm Phase (Console Application):* Develop a fully functional console-based application implementing all core business logic and features.

2. *Final Phase (GUI Application):* Extend your console application by adding a graphical user interface while maintaining your original business logic architecture.
   
        *Application Development (Midterm: Console Application)*
- *Console Application Design:* Develop a console application, ensuring the core functionality is modularized in a separate library. Depending on project requirements, incorporate binary file storage or mock-ups for simulating network communications.
- *Interface Design:* Craft a user-friendly console interface that supports navigation through keyboard inputs (e.g., arrows, tabs).
- *Object-Oriented Architecture:* Design your application following solid object-oriented principles. Your code should demonstrate a clear understanding of classes, objects, inheritance, and polymorphism.
- *Architecture Planning:* Design your application with future GUI extension in mind. Use appropriate architectural patterns (like MVC, MVVM) that will facilitate the addition of a GUI layer in the final phase.

*Extended Application Development (Final: GUI Application)*

- *GUI Implementation:* Extend your console application with a graphical user interface using JavaFX or Swing.
- *Architectural Integrity:* Maintain the core business logic from your console application, demonstrating proper separation of concerns between UI and business logic.
- *User Experience:* Create an intuitive, responsive, and visually appealing user interface with proper event handling.
- *Cross-Platform Testing:* Ensure your GUI application works correctly on different platforms (Windows, Linux, macOS if possible).

*OOP Requirements and Principles*

- *Class Hierarchy:* Implement a meaningful class hierarchy that demonstrates proper inheritance relationships. Your classes should have clearly defined parent-child relationships where appropriate.

- *Encapsulation:* Apply proper encapsulation techniques by using access modifiers (private, protected, public) appropriately. Data should be hidden within classes and accessed through getter and setter methods.

- *Polymorphism:* Demonstrate polymorphic behavior through method overriding and/or method overloading. Include examples of runtime polymorphism where appropriate.

- *Abstraction:* Use abstract classes and/or interfaces to define common behaviors and enforce contracts between classes.

- *Design Patterns:* Implement at least two design patterns (e.g., Singleton, Factory, Observer, Strategy) that are appropriate for your application's requirements.

- *SOLID Principles:* Apply SOLID principles (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) in your code design.

- *Testing:* Develop comprehensive unit tests for all functionalities, striving for 100% test coverage. Tests should verify that:

- Classes function correctly individually

- Inheritance relationships work as expected

- Polymorphic behavior produces correct results

- Design patterns are implemented correctly

- GUI components interact properly with business logic (for final phase)

 *Finalization and Submission*

- *Midterm Submission:* Submit your console application with comprehensive documentation and testing by the midterm deadline.

- *Final Submission:* Submit your complete GUI application with additional documentation and testing by the final deadline.

- *Presentation:* Prepare a concise presentation deck (up to 10 slides) and a video presentation (up to 4 minutes) that summarizes the project's architecture, functionality, and key learning points.

- *Code and Documentation Submission:* Ensure your code is well-commented, adheres to the coding standards, and is thoroughly tested on both WSL/Linux and Windows OS. Submit a detailed project report and the source code named *ce204-hw-name-surname.rar*, containing the GitHub cloned templates.
  
  *OOP-Specific Evaluation Criteria*

Your project will be evaluated based on the following OOP-specific criteria:

- *Class Design:* How well your classes are designed (cohesion, coupling, granularity)

- *Inheritance Implementation:* Appropriate use of inheritance, avoiding unnecessary inheritance relationships

- *Polymorphism Usage:* Effective use of polymorphism to simplify code and improve extensibility

- *Abstraction Quality:* Appropriate use of abstract classes and interfaces

- *Encapsulation Practice:* Proper hiding of implementation details and data protection

- *Design Pattern Implementation:* Correct implementation of design patterns and explanation of why they were chosen

- *Code Reusability:* How reusable and modular your code components are

- *Exception Handling:* Proper implementation of exception handling for robust code

- *GUI Implementation (Final):* Quality of GUI implementation and integration with business logic

- *Architecture Evolution:* How effectively your design evolved from console to GUI application

- ### 42-Basic Task Scheduler:[¶](https://ucoruh.github.io/ce204-object-oriented-programming/project-guide/#42-basic-task-scheduler "Permanent link")
  
  - Task creation: Add and categorize tasks.
  
  - Deadline setting: Assign deadlines to tasks.
  
  - Reminder system: Notify users of upcoming deadlines.
  
  - Task prioritization: Mark tasks by importance.

#### Common Features:[¶](https://ucoruh.github.io/ce204-object-oriented-programming/project-guide/#common-features_41 "Permanent link")

- User Authentication: Allow users to create accounts or profiles within the application to personalize their task lists, set reminders, and manage their tasks. This feature is optional but can enhance the user experience.

- Task Creation: Implement tools for users to create tasks, including task names, descriptions, categories, and due dates. Users can organize tasks by categories such as work, personal, and more.

- Deadline Setting: Enable users to assign deadlines to tasks. Users can specify due dates and times for each task.

- Reminder System: Provide a reminder system that notifies users of upcoming task deadlines. Users can receive notifications via email, SMS, or in-app notifications.

- Task Prioritization: Allow users to prioritize tasks by marking them as high, medium, or low importance. Users can also reorder tasks within categories based on priority.
  
  -
