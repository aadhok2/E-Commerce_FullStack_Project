# E-Commerce Full Stack Project
## Overview
This repository houses the source code for a robust E-Commerce website, developed with React for the frontend and Java for the backend. The project is designed around an Event-Driven Architecture (EDA) incorporating key patterns such as Saga, Event Sourcing, and Command Query Responsibility Segregation (CQRS). For efficient service discovery, Eureka and Spring API Gateway are employed, while the Axon Framework facilitates the implementation of the Event-Driven Architecture. Docker is utilized to locally run the Axon Framework.

## Features
### Frontend: 
The user interface is crafted using React.js, providing an intuitive and responsive E-Commerce experience.

### Backend: 
Java is used for the backend, implementing Event-Driven Architecture with Axon Framework, Saga pattern, Event Sourcing, and CQRS.

#### Microservices: 
The backend consists of microservices including:

##### API Gateway Service: 
Utilizes Spring's API Gateway for efficient routing and load balancing.
##### Discovery Service (Eureka): 
Implements Eureka for dynamic service registration and discovery, enhancing system scalability.
##### Product Service: 
Manages product-related functionalities.
##### Order Service: 
Handles order processing.
##### User Service: 
Manages user-related operations.
##### Payment Service: 
Facilitates payment processing.
#### Event-Driven Architecture
The project is built on the principles of Event-Driven Architecture with the following key components:

##### Saga Pattern: 
Ensures reliable and consistent transaction management across microservices.
##### Event Sourcing: 
Maintains a comprehensive log of events for each microservice, providing a reliable source of truth.
##### CQRS: 
Separates read and write operations, optimizing system performance.
#### Service Discovery
##### Eureka Discovery: 
Used for dynamic service registration and discovery, enabling seamless communication between microservices.
#### API Gateway
##### Spring API Gateway: 
Serves as the central API Gateway, providing a unified entry point for requests and efficient routing.
#### Axon Framework
The backend leverages Axon Framework to implement and manage events, ensuring a robust and scalable Event-Driven Architecture.
#### Docker
Axon Framework: Docker is utilized to locally run the Axon Framework, facilitating a smooth development environment.
#### Project Structure
> - frontend/          # Frontend React application
>  - backend/
>  - api-gateway/     # API Gateway service
>  - discovery/       # Eureka Discovery service
>  - products/        # Product microservice
>  - orders/          # Order microservice
>  - users/           # User microservice
>  - payments/        # Payment microservice

#### Getting Started
- Clone the repository: git clone https://github.com/your-username/e-commerce.git.
- Ensure all required dependencies are installed.
- Start the Axon Server first followed by backend services, and then frontend application(npm start).
- Feel free to reach out for any questions or issues. Happy coding!
