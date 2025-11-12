# Smart Cart Location Processor - Replit Project

## Overview
This is a Java Spring Boot microservice that implements an event-driven geospatial processing system for a Smart Cart application. The service consumes real-time cart location data from Apache Kafka, performs proximity calculations using MongoDB geospatial queries, and publishes item recommendations back to Kafka.

## Recent Changes
- **2025-11-05**: Migrated from Azure Cosmos DB to MongoDB
  - Replaced Azure Cosmos DB SDK with Spring Data MongoDB
  - Updated StoreItem model to use @Document annotation and GeoJsonPoint for geospatial data
  - Created MongoConfig to replace CosmosConfig
  - Rewrote StoreItemRepository to use MongoTemplate with $nearSphere operator
  - Updated all configuration files (application.properties, application.yml) for MongoDB connection
  - Updated README with MongoDB setup instructions and 2dsphere index creation
  - Maintained geospatial query functionality with 10-meter radius proximity calculation
  
- **2025-10-29**: Initial project setup and reliability improvements
  - Created complete Spring Boot microservice with Kafka integration
  - Implemented consumer for cart-locations topic with manual offset acknowledgment
  - Implemented geospatial query with 10-meter radius proximity calculation
  - Implemented producer for cart-recommendations topic with idempotent writes
  - Configured all necessary Spring Boot components and dependencies
  - Enhanced reliability: disabled auto-commit and added manual acknowledgment to prevent message loss

## Project Architecture

### Event-Driven Flow
1. **Input**: Cart location messages from `cart-locations` Kafka topic
2. **Processing**: Geospatial query against MongoDB using $nearSphere (10-meter radius)
3. **Output**: Recommendation messages to `cart-recommendations` Kafka topic

### Technology Stack
- Java 17
- Spring Boot 3.2.0
- Spring Kafka
- Spring Data MongoDB
- Maven for dependency management

### Key Components
- **CartLocationConsumer**: Kafka listener for cart location messages
- **StoreItemRepository**: MongoDB repository with geospatial queries using $nearSphere
- **LocationProcessingService**: Core business logic for proximity calculations
- **CartRecommendationProducer**: Kafka producer for recommendations

## Configuration Requirements

### Required Environment Variables
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka server address
- `MONGODB_URI`: MongoDB connection URI (e.g., mongodb://localhost:27017/smartcart)
- `MONGODB_DATABASE`: Database name (default: smartcart)

### Optional Configuration
- `PROXIMITY_RADIUS_METERS`: Proximity radius (default: 10 meters)
- `KAFKA_TOPIC_CART_LOCATIONS`: Input topic name (default: cart-locations)
- `KAFKA_TOPIC_CART_RECOMMENDATIONS`: Output topic name (default: cart-recommendations)

### MongoDB Setup
**Important**: The MongoDB collection must have a 2dsphere geospatial index on the `location` field:
```javascript
db.getCollection("store-items").createIndex({ "location": "2dsphere" })
```

## Running the Application
The application runs as a Spring Boot service that continuously processes messages from Kafka.

Command: `mvn spring-boot:run`

## User Preferences
None recorded yet.
