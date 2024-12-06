# Smart Contact Management Application

A **Spring Boot-based Smart Contact Management System** that enables users to manage their personal or professional contacts effectively. It uses PostgreSQL as the database and is designed with scalability, security, and user-friendliness in mind.

---

## 🚀 Features

- **User Authentication**:
  - Secure login and registration using Spring Security.
  - Role-based access control (Admin/User).

- **Contact Management**:
  - Add, edit, delete, and view contacts.
  - Search contacts by name or other criteria.
  - Pagination and sorting for large contact lists.

- **Profile Management**:
  - Update personal profile details.
  - Upload profile pictures with file storage.

- **Responsive UI**:
  - Modern and user-friendly interface.
  - Compatible with mobile and desktop devices.

- **Database Integration**:
  - PostgreSQL for data storage.
  - Hibernate and JPA for ORM.

---

## 🛠️ Tech Stack

- **Backend**:
  - Java (Spring Boot Framework)
  - Spring Security
  - Spring Data JPA
  - Hibernate ORM

- **Frontend**:
  - Thymeleaf (or React/Angular if used)
  - Bootstrap for UI styling

- **Database**:
  - PostgreSQL

- **Deployment**:
  - Hosted on [Railway](https://railway.app)

---

## 📂 Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   ├── com.example.smartcontact/
│   │   │   ├── controller/    # Controllers for handling HTTP requests
│   │   │   ├── model/         # Entity classes for database
│   │   │   ├── repository/    # JPA repositories
│   │   │   ├── service/       # Business logic
│   │   │   ├── security/      # Security configurations
│   ├── resources/
│   │   ├── templates/         # Thymeleaf templates (HTML files)
│   │   ├── static/            # Static assets (CSS, JS, images)
│   │   ├── application.properties
├── test/                      # Unit and integration tests
