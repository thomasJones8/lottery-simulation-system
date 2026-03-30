# Lottery Simulation System (Totolotek)

A comprehensive, object-oriented simulation of a national lottery system, modeling business rules for local branch operations, ticket processing, automated prize distribution, and tax calculations.

The project demonstrates solid object-oriented design, data management using the Java Collections Framework, and business logic verified by JUnit testing. Additionally, monetary operations are handled exclusively using `long` integers to prevent floating-point inaccuracies.

*Note: This project was developed in June 2025 as the second major assignment for the Object-Oriented Programming course (1st Year Computer Science) at the MIMUW faculty. It received a final grade of 9.45/10. For the detailed, original academic requirements (in Polish), see [SPECIFICATION_PL.md](SPECIFICATION_PL.md).*

### Tech Stack
* **Language:** Java
* **Testing:** JUnit 5
* **Key Concepts:** Object-Oriented Design, Java Collections Framework, Unit & Integration Testing

### Project Structure
* `src/` - Contains the core domain logic (Central, Branches, Tickets, Players).
* `test/` - Contains comprehensive JUnit 5 test suites verifying edge cases, domain logic, and sales transactions.

### How to Run
Compile and run the main simulation class from the terminal:
```bash
javac -d bin src/totolotek/**/*.java
java -cp bin totolotek.Main
