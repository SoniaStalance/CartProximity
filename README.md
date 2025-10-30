# Smart Cart Location Processor

A Java Spring Boot microservice that processes real-time shopping cart location data from Apache Kafka, performs geospatial proximity calculations using Azure Cosmos DB, and publishes item recommendations back to Kafka.

## Architecture

```
Cart Location (Kafka) → Location Processor → Azure Cosmos DB (Geospatial Query)
                             ↓
                    Cart Recommendations (Kafka)
```

## Features

- **Kafka Consumer**: Reads real-time cart location data from `cart-locations` topic
- **Geospatial Processing**: Queries Azure Cosmos DB for store items within 20-meter radius
- **Kafka Producer**: Publishes recommendations to `cart-recommendations` topic
- **Event-Driven Architecture**: Fully asynchronous processing loop

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Kafka
- Azure Cosmos DB Java SDK
- Lombok
- Jackson for JSON processing

## Configuration

### Environment Variables

Set the following environment variables:

```bash
# Kafka Configuration
export KAFKA_BOOTSTRAP_SERVERS=your-kafka-server:9092
export KAFKA_TOPIC_CART_LOCATIONS=cart-locations
export KAFKA_TOPIC_CART_RECOMMENDATIONS=cart-recommendations
export KAFKA_CONSUMER_GROUP_ID=location-processor-group

# Azure Cosmos DB Configuration
# IMPORTANT: AZURE_COSMOS_KEY must be a valid base64-encoded primary or secondary key
# You can find this in Azure Portal under your Cosmos DB account > Keys section
export AZURE_COSMOS_ENDPOINT=https://your-cosmos-account.documents.azure.com:443/
export AZURE_COSMOS_KEY=your-base64-encoded-cosmos-key
export AZURE_COSMOS_DATABASE=smartcart
export AZURE_COSMOS_CONTAINER=store-items

# Optional: Configure proximity radius (default: 20 meters)
export PROXIMITY_RADIUS_METERS=20
```

### Azure Cosmos DB Setup

Your Cosmos DB container should have items with the following structure:

```json
{
  "id": "unique-id",
  "itemId": "item-123",
  "name": "Product Name",
  "latitude": 37.7749,
  "longitude": -122.4194,
  "location": {
    "type": "Point",
    "coordinates": [-122.4194, 37.7749]
  }
}
```

**Important**: Create a geospatial index on the `location` field for optimal performance:

```json
{
  "indexingMode": "consistent",
  "spatialIndexes": [
    {
      "path": "/location/*",
      "types": ["Point"]
    }
  ]
}
```

## Message Formats

### Input: Cart Location (cart-locations topic)

```json
{
  "cartId": "cart-456",
  "latitude": 37.7749,
  "longitude": -122.4194
}
```

### Output: Cart Recommendation (cart-recommendations topic)

```json
{
  "cartId": "cart-456",
  "nearbyItemIds": ["item-123", "item-456", "item-789"]
}
```

## Building and Running

### Build with Maven

```bash
mvn clean package
```

### Run the application

```bash
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar target/location-processor-1.0.0.jar
```

## How It Works

1. **Consumer**: The service listens to the `cart-locations` Kafka topic
2. **Processing**: When a cart location message arrives:
   - Parses the JSON to extract cart ID, latitude, and longitude
   - Executes a geospatial query against Azure Cosmos DB using ST_DISTANCE
   - Finds all store items within 20 meters of the cart's position
3. **Producer**: Creates a recommendation message with cart ID and nearby item IDs
4. **Publishing**: Sends the recommendation to the `cart-recommendations` Kafka topic

## Geospatial Query

The service uses Cosmos DB's geospatial capabilities with the ST_DISTANCE function:

```sql
SELECT * FROM c 
WHERE ST_DISTANCE(c.location, {'type': 'Point', 'coordinates':[longitude, latitude]}) <= 20
```

This query efficiently finds all items within the specified radius using the geospatial index.

## Logging

The application provides detailed logging:
- Cart location message reception
- Geospatial query execution
- Items found within proximity
- Recommendation publishing status

## Error Handling

- JSON parsing errors are logged without crashing the consumer
- Kafka connection issues are handled with automatic retries
- Cosmos DB query failures are logged with full context
- Manual offset acknowledgment prevents message loss if publishing fails

## Reliability Features

- **Manual Offset Acknowledgment**: Kafka offsets are only committed after successful processing and publishing, preventing message loss
- **Idempotent Producer**: Ensures exactly-once delivery semantics for recommendations
- **Geospatial Indexes**: Fast proximity queries even with millions of items
- **Direct Connection Mode**: Lower latency for Cosmos DB queries

## Troubleshooting

### Application fails to start with "Illegal base64 character" error

This error occurs when the `AZURE_COSMOS_KEY` environment variable is not set or contains an invalid key. 

**Solution**: Ensure you have set a valid base64-encoded primary or secondary key from your Azure Cosmos DB account. You can find this key in the Azure Portal under:
1. Navigate to your Cosmos DB account
2. Go to "Keys" section
3. Copy either the "Primary Key" or "Secondary Key" (not the connection string)
4. Set it as the `AZURE_COSMOS_KEY` environment variable

## Project Structure

```
src/main/java/com/smartcart/locationprocessor/
├── LocationProcessorApplication.java
├── config/
│   ├── CosmosConfig.java
│   ├── JacksonConfig.java
│   ├── KafkaConsumerConfig.java
│   └── KafkaProducerConfig.java
├── kafka/
│   ├── CartLocationConsumer.java
│   └── CartRecommendationProducer.java
├── model/
│   ├── CartLocation.java
│   ├── CartRecommendation.java
│   └── StoreItem.java
├── repository/
│   └── StoreItemRepository.java
└── service/
    └── LocationProcessingService.java
```

## License

This project is part of the Smart Cart system.
