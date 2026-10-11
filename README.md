# Crumb Bakery Order & Custom Cake Booking Platform

A group project for **SE1020 â€“ Object Oriented Programming**. The platform is intended to bring bakery product ordering and custom cake bookings into one web application, with customer and staff/admin workflows.

## Project goals

- Apply object-oriented programming concepts such as encapsulation, inheritance, abstraction, and polymorphism.
- Implement CRUD operations for the projectâ€™s main features.
- Persist and retrieve application data.
- Provide a clear, consistent interface for customers and staff.
- Keep each memberâ€™s work visible through focused Git branches, commits, and pull requests.

## Planned features

- User accounts and login
- Bakery product catalogue and management
- Standard bakery orders
- Custom cake bookings
- Delivery and pickup details/status
- Customer feedback and reviews

## Technology

The current development approach in this repository is **Java with Spring Boot and MySQL**, with a web interface. The supplied project guide also discusses JSP/Servlets and text-file storage as a recommended sample implementation. Follow the lecturerâ€™s latest requirements if they differ, and agree as a group before changing the stack or storage approach.

## Team assignments

| Student ID | Member | Assigned feature | Suggested branch |
|---|---|---|---|
| IT26100784 | Wettasinghe K.K.D.S.T | Order | `order` |
| IT26100828 | Rathnayake R.M.P.R | Delivery and pickup | `deliveryAndPickup` |
| IT26101207 | Kumara T.G.V.S.S | Custom cake | `customCake` |
| IT26100779 | Munasinghe D.A | Feedback and review | `feedbackAndReview` |
| IT26101192 | Rushdi F.R | Product | `product` |
| IT26100620 | Dilhara K.B.O.U | Delivery and pickup | `deliveryAndPickup` |

> **Allocation to confirm:** Delivery and pickup appears twice in the supplied assignments, while the project structure/brief also includes a `user` feature. Please agree whether the final member shares delivery/pickup work or owns user management, then update this table and branch assignment together.

## Getting started

1. Install a compatible JDK, IntelliJ IDEA, and MySQL Server/MySQL Workbench.
2. Clone the GitHub repository and open it in IntelliJ IDEA.
3. Let IntelliJ import/sync the Maven project.
4. Create the local database required by the project. The current local setup has used `bakery_db`.
5. Configure each developerâ€™s local database credentials. Keep real passwords out of tracked files and GitHub; use environment variables or an untracked local configuration.
6. Run the Spring Boot application from its class annotated with `@SpringBootApplication`.

Exact JDK, Spring Boot, and database settings should match `pom.xml` and the teamâ€™s agreed configuration.

## Repository layout

The repositoryâ€™s Java source is organized by responsibility/feature. Typical Spring Boot roles are:

- `model` â€” entity/domain classes that represent application data.
- `repository` â€” persistence operations.
- `service` â€” business rules and coordination.
- `controller` â€” HTTP endpoints and request handling.

Feature folders/packages include order, product, custom cake, delivery/pickup, feedback/review, and user management as the implementation develops. Keep classes under the package containing the main Spring Boot application class so component scanning can find them.

## Collaboration

Each member should work on their assigned feature branch, commit their own changes with clear messages, push the branch, and open a pull request to `main`. See [CONTRIBUTING.md](CONTRIBUTING.md) for the workflow and conventions.