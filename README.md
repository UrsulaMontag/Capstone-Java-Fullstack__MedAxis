# MedAxis

## Overview

The MedAxis software is a comprehensive solution for the management and organisation of hospital data. This
software platform was developed to enable integrated and efficient management of patient information, doctors, wards and
bed occupancy. It offers powerful tools for the precise management of medical data and for analysing key figures and
statistics. Particular attention has been paid to the secure handling of data: Sensitive information is protected by
encryption and is only accessible to authorised users. By combining state-of-the-art technologies in the front end and
back end, the application offers a robust and reliable solution for the demanding requirements of hospital management.

## Features

- **Patient Management**: Comprehensive overview and management of patient data.
- **Doctor Management**: Administration of doctors and their specialties.
- **Ward Management**: Handling of wards and bed occupancy.
- **Statistics**: Generation and display of statistics based on available data.
- **Secure Data Handling**: Data encryption and secure authentication for data processing.

## Technology Stack

### Frontend

- **Library**: React
- **Styling**: Styled-Components
- **State Management**: Zustand.js
- **TypeScript**: Yes
- **Build Tools**: Vite
- **Dependencies**:
    - `axios`: For HTTP requests
    - `react-router-dom`: For navigation
    - `zustand`: For state management
- **Development Commands**:
    - `dev`: `vite` – Start the development server
    - `build`: `tsc -b && vite build` – Build for production
    - `lint`: `eslint . --ext ts,tsx` – Code linting
    - `preview`: `vite preview` – Preview the build

### Backend

- **Framework**: Spring Boot
- **Database**: MongoDB
- **Security**: Spring Security for authentication and authorization
- **Encryption**: Spring Security Crypto for secure data handling
- **ICD Data**: Integration with the WHO ICD API for disease data retrieval
- **Build Tools**: Maven
- **Testing Framework**: JUnit, Jacoco for code coverage
- **Dependencies**:
    - `spring-boot-starter-data-mongodb`: MongoDB integration
    - `spring-boot-starter-web`: Web development
    - `spring-security-crypto`: Encryption
    - `lombok`: Code simplification
    - `de.flapdoodle.embed.mongo.spring3x`: Embedded MongoDB for testing

### DevOps

- **Build Management**: Maven
- **Testing Management**: Surefire
- **Containerization**: Docker
- **Deployment**: Render

## Installation

### Prerequisites

- **Node.js** (v14+)
- **npm** or **yarn**
- **Java** (JDK 11+)
- **Maven**

### Clone the Repository

Clone the monorepo that contains both the frontend and backend projects:

```bash
git clone <repository URL>
cd <repository-directory>
```

### Frontend setup:

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend

2. **Install dependencies:**:
   ```bash
   npm install
   
   or
   
   yarn install
   ```
3. **Start development server:**:
   ```bash
   npm run dev
   
   or
   
   yarn dev
   ```

4. **Build for production:**
   ```bash
   npm run build
   
   or
   
   yarn build
   ```

### Backend

1. **Navigate to the backend directory**:
   ```bash
   cd backend

2. **Download dependencies and build the project**:
   ```bash
   mvn clean install

3. **Run the application**:
   ```bash
   mvn spring-boot:run

## Deployment

The project is containerized using Docker and deployed on Render. For detailed information about the deployment process,
refer to the [Render Documentation](https://render.com/docs).

## Security

- **Data Encryption**: All sensitive data is encrypted when stored and decrypted when retrieved.
- **Authentication**: Access to confidential data is restricted to authorized users only, managed through Spring
  Security.

## License

## Contact

For questions or support, please
contact [Contact Email](mailto:montagu@gmail.com).
