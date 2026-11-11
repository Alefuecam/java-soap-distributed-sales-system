

\# Distributed Sales Management System — Java EE \& SOAP Web Services



A distributed sales and inventory management system developed using Java EE, SOAP web services, Enterprise JavaBeans (EJB), JPA, Apache Derby, and BPEL orchestration.



The project simulates an e-commerce platform for a construction materials distributor, integrating independent services for order processing, inventory management, credit authorization, and shipping.



Developed as an academic project for the \*\*Electronic Commerce Systems\*\* course at Instituto Tecnológico Autónomo de México (ITAM).



\## Project Overview



The objective was to design and implement a distributed application capable of processing customer purchases through multiple interconnected web services.



Rather than implementing all business operations within a single application, the system separates responsibilities into independent services, each with its own database and business logic.



These services are coordinated through a Business Process Execution Language (BPEL) workflow, enabling the execution of a complete purchasing process.



The project also includes functional tests, stress-testing utilities, and experiments involving multiple client computers communicating with the deployed services.



\## Technologies Used



| Technology | Purpose |

|------------|---------|

| Java EE | Enterprise application development |

| SOAP / JAX-WS | Web service communication |

| Enterprise JavaBeans (EJB) | Business logic and persistence integration |

| Java Persistence API (JPA) | Object-relational mapping |

| Apache Derby | Relational database management |

| SQL | Database schemas and initialization |

| BPEL | Business process orchestration |

| OpenESB | Service integration |

| GlassFish | Java EE application server |

| Apache NetBeans | Development environment |

| WSDL / XML / XSD | Service contracts and data definitions |

| Java POJOs | Service clients and testing |

| Java RMI | Distributed stress-testing utilities |



\## System Architecture



The application follows a service-oriented architecture consisting of three primary SOAP web services coordinated by a BPEL orchestration layer.



\### 1. Order and Inventory Service



Responsible for managing products, customer orders, and inventory availability.



Main operations include:



\- Product and inventory management.

\- Customer order creation.

\- Product availability verification.

\- Inventory updates during order processing.

\- Order confirmation generation.

\- Inventory restoration operations.



The service uses JPA entities and EJB facades to interact with its relational database.



\### 2. Credit Authorization Service



Responsible for validating customer credit before completing a purchase.



Main operations include:



\- Customer credit management.

\- Credit availability verification.

\- Authorization of requested amounts.

\- Updating customer credit balances.

\- Handling insufficient credit and nonexistent customer exceptions.



Financial calculations use Java's `BigDecimal` to represent monetary amounts.



\### 3. Shipping Service



Responsible for registering and managing shipping requests associated with customer orders.



Main operations include:



\- Shipping request creation.

\- Customer and delivery information management.

\- Order and store identification.

\- Shipment record management.



\### 4. BPEL Orchestration



The three services are integrated through a BPEL process implemented using OpenESB.



The orchestration coordinates the purchasing workflow, connecting order processing, credit validation, and shipping operations.



It also handles business exceptions, including insufficient inventory and unavailable customer credit.



This architecture separates business responsibilities while allowing the services to participate in a single integrated process.



\## Database Architecture



The application uses Apache Derby relational databases, with separate schemas for the primary services.



\### Order and Inventory Database



Includes tables for:



\- Customers

\- Products

\- Product categories

\- Customer orders

\- Ordered products



Relationships between tables are implemented using primary and foreign keys.



\### Credit Database



Stores customer information and available credit balances.



\### Shipping Database



Stores shipment records and associated customer delivery information.



Database initialization scripts are included in the repository:



```text

derby\_bd\_parcial\_02\_credito\_v01.sql

derby\_bd\_parcial\_02\_envios\_v01.sql

derby\_bd\_parcial\_02\_pedidos\_v01.sql

```



\## Project Structure



The repository contains multiple NetBeans projects representing the individual services, orchestration components, clients, and testing utilities.



```text

ProyectoFinalSCE/

|

|-- WS\_Pedidos/

|-- WS\_Credito/

|-- WS\_Envios/

|

|-- BPELOrquestador/

|-- CAConstructora/

|

|-- Pojo\_PedidoReal/

|-- Pojo\_Credito/

|-- Pojo\_Envios/

|-- Pojo\_CABpelConstructora/

|

|-- InterfazServicioEstressConstructora/

|-- InterfazServicioEstressPojos/

|

|-- SimpleRMIDes\_Constructora/

|-- SimpleRMIDesacoplado\_Pojos/

|

|-- tstRMI\_des\_Constructora/

|-- tstRMI\_desacoplado\_Pojos/

|

|-- derby\_bd\_parcial\_02\_credito\_v01.sql

|-- derby\_bd\_parcial\_02\_envios\_v01.sql

|-- derby\_bd\_parcial\_02\_pedidos\_v01.sql

|

|-- README.md

|-- .gitignore

```



\### Main Components



\*\*WS\_Pedidos, WS\_Credito, WS\_Envios\*\*



Java EE applications implementing the individual SOAP web services.



\*\*BPELOrquestador\*\*



Contains the BPEL orchestration process, WSDL contracts, and XML schemas used to coordinate the services.



\*\*CAConstructora\*\*



Composite application project used to integrate and deploy the orchestration components.



\*\*Pojo\_\*\*\*



Java clients used to invoke and test the web services.



\*\*SimpleRMIDes\_\*, InterfazServicioEstress\*, tstRMI\_\*\*\*



Utilities and test applications used to perform stress testing and exercise the individual services and integrated application.



\## Functional Testing



Functional testing was performed on the individual services and the integrated purchasing workflow.



The tests included:



\- Successful purchase transactions.

\- Credit authorization.

\- Insufficient customer credit.

\- Insufficient product inventory.

\- Order processing and database updates.

\- Shipping request creation.

\- Integrated service execution.



SOAP request and response messages were examined to verify the expected behavior.



Database records were also inspected to validate changes produced by service operations.



\## Stress Testing



The project includes Java-based stress-testing utilities designed to evaluate the behavior of individual web services and the integrated application under concurrent requests.



\### Testing Approach



1\. Initialize the databases with predefined records.

2\. Deploy the individual SOAP web services.

3\. Execute POJO clients to verify functional behavior.

4\. Use Java RMI-based utilities to generate concurrent requests.

5\. Observe service execution and database modifications.

6\. Examine performance statistics and resource utilization.

7\. Repeat testing against the integrated BPEL application.



The documented tests used two clients and three services.



The project's nonfunctional requirements specified a target of 100 transactions per minute across supported operations.



This value represents a design requirement rather than a claimed measured throughput.



\### Distributed Client Demonstration



The application was also demonstrated using multiple computers connected over a network.



The demonstration involved configuring service connectivity, adjusting endpoint and port settings, and using WSDL service definitions to enable client communication.



This provided practical experience with distributed client-server communication and web service deployment.



\## Development and Deployment



\### Original Development Environment



The project was developed using:



\- Java EE

\- Apache NetBeans

\- GlassFish Application Server

\- OpenESB / BPEL tooling

\- Apache Derby

\- Java RMI



The original implementation depends on Java EE and OpenESB tooling that may require compatible legacy software versions.



\### General Setup Procedure



1\. Install compatible versions of Java, NetBeans, GlassFish, OpenESB, and Apache Derby.

2\. Open the individual NetBeans projects.

3\. Configure the Apache Derby database connections.

4\. Execute the SQL initialization scripts.

5\. Configure the application server and required persistence resources.

6\. Build and deploy the three SOAP web services.

7\. Verify that their WSDL endpoints are accessible.

8\. Review the endpoint references used by the BPEL orchestration project.

9\. Build and deploy the composite application.

10\. Run the POJO clients to test individual services and the integrated workflow.

11\. Execute the stress-testing utilities if required.



\*\*Note:\*\* These are general deployment steps based on the original architecture. The repository is preserved as an academic implementation and is not presented as a verified, one-command installation for modern Java environments.



\## My Contributions



I was the primary developer responsible for the technical implementation, integration, and testing of this academic team project.



My contributions included:



\- Implementing the SOAP web services in Java.

\- Developing business logic using EJBs, entities, and facades.

\- Implementing relational database persistence.

\- Integrating the services through BPEL orchestration.

\- Developing and executing functional tests.

\- Implementing and running stress-testing components.

\- Troubleshooting integration and deployment issues.

\- Configuring and validating service communication during distributed client testing.



Other team members contributed to the written documentation and assisted with the multi-computer demonstration.



\## Key Learning Outcomes



This project provided hands-on experience with:



\- Service-oriented architecture.

\- Enterprise Java application development.

\- SOAP-based communication and WSDL contracts.

\- Distributed application integration.

\- Database persistence and entity relationships.

\- Business process orchestration.

\- Exception handling across service boundaries.

\- Functional and nonfunctional software testing.

\- Networked client-server communication.

\- Troubleshooting dependencies between enterprise components.



\## Project Context



This repository contains an academic implementation intended to demonstrate distributed systems development, enterprise application architecture, and service integration.



The project was developed for educational purposes and is not intended for production deployment without additional security hardening, modernization, and testing.



