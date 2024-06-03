# Distributed Order Management System

## Overview

This project implements a distributed order management system using the Saga pattern with microservices architecture. The system is composed of several services that handle different aspects of the order processing workflow, coordinated by an Orchestrator service. Kafka is used for event-driven communication between services. Kafka is running via Docker Compose.

## Technologies

- **Java 17**
- **Spring Boot 3.2.5**
- **Apache Kafka**
- **Docker Compose**

## Services

The system includes the following services:

1. **Order Service**: Handles order creation and management.
2. **Payment Service**: Processes payments.
3. **Stock Service**: Manages inventory and stock reservations.
4. **Notification Service**: Sends notifications to customers.
5. **Orchestrator Service**: Coordinates the entire workflow, handling events and triggering compensating actions when necessary.

## Architecture

![alt text](https://github.com/abbes-larbaoui/saga-pattern/blob/master/architecture_diagram.jpg?raw=true)


### Saga Pattern

The Saga pattern is used to manage distributed transactions. Each service performs its local transaction and publishes an event. The Orchestrator handles these events and triggers the next step in the workflow or compensating actions in case of failures.

### Workflow

1. **Order Creation**: An order is created and an `ORDER_CREATED` event is sent.
2. **Payment Processing**: The Orchestrator listens for the `ORDER_CREATED` event and triggers a `PROCESS_PAYMENT` event.
3. **Stock Reservation**: Upon successful payment (`PAYMENT_COMPLETED` event), the Orchestrator triggers a `RESERVE_STOCK` event.
4. **Order Completion**: If stock reservation is successful (`STOCK_RESERVED` event), the Orchestrator sends an `ORDER_COMPLETED` event.
5. **Notifications**: The Notification Service sends notifications based on events.

### Rollback Mechanism

If any step in the process fails (e.g., payment fails or stock reservation fails), the Orchestrator handles the rollback by sending compensating events to undo the previous actions.

### Timeout Management

A Timeout Manager is implemented to detect if an expected event does not arrive within a certain time frame, thereby triggering compensating actions to handle service unavailability.

## Getting Started

### Prerequisites

- Java 17
- Docker & Docker Compose