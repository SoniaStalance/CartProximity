# Smart Cart Location Processor - Replit Project

## Overview
This is a Java Spring Boot microservice that implements an event-driven geospatial processing system for a Smart Cart application. The service consumes real-time cart location data from Apache Kafka, performs proximity calculations using Azure Cosmos DB geospatial queries, and publishes item recommendations back to Kafka.

## Recent Changes
- **2025-10-29**: Initial project setup and reliability improvements
  - Created complete Spring Boot microservice with Kafka and Azure Cosmos DB integration
  - Implemented consumer for cart-locations topic with manual offset acknowledgment
  - Implemented geospatial query with 10-meter radius proximity calculation using ST_DISTANCE
  - Implemented producer for cart-recommendations topic with idempotent writes
  - Configured all necessary Spring Boot components and dependencies
  - Enhanced reliability: disabled auto-commit and added manual acknowledgment to prevent message loss
  - Added comprehensive documentation for Azure Cosmos DB key configuration

## Project Architecture

### Event-Driven Flow
1. **Input**: Cart location messages from `cart-locations` Kafka topic
2. **Processing**: Geospatial query against Azure Cosmos DB (10-meter radius)
3. **Output**: Recommendation messages to `cart-recommendations` Kafka topic

### Technology Stack
- Java 17
- Spring Boot 3.2.0
- Spring Kafka
- Azure Cosmos DB Java SDK
- Maven for dependency management

### Key Components
- **CartLocationConsumer**: Kafka listener for cart location messages
- **StoreItemRepository**: Azure Cosmos DB repository with geospatial queries
- **LocationProcessingService**: Core business logic for proximity calculations
- **CartRecommendationProducer**: Kafka producer for recommendations

## Configuration Requirements

### Required Environment Variables
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka server address
- `AZURE_COSMOS_ENDPOINT`: Azure Cosmos DB endpoint URL
- `AZURE_COSMOS_KEY`: Azure Cosmos DB access key
- `AZURE_COSMOS_DATABASE`: Database name (default: smartcart)
- `AZURE_COSMOS_CONTAINER`: Container name (default: store-items)

### Optional Configuration
- `PROXIMITY_RADIUS_METERS`: Proximity radius (default: 10 meters)
- `KAFKA_TOPIC_CART_LOCATIONS`: Input topic name (default: cart-locations)
- `KAFKA_TOPIC_CART_RECOMMENDATIONS`: Output topic name (default: cart-recommendations)

## Running the Application
The application runs as a Spring Boot service that continuously processes messages from Kafka.

Command: `mvn spring-boot:run`

## User Preferences
None recorded yet.
