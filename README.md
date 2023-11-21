# Kafka Spring Boot SASL PLAIN Demo Project

Spring Boot application demonstrating authenticating with Kafka using SASL PLAIN.

## Run Spring Boot Application

### Build
```
mvn clean install
```

### Run Docker Containers

From the root dir run the `docker-compose` files to start dockerised Kafka, Zookeeper, and Conduktor Gateway:
```
docker-compose up -d
```

### Start Demo Spring Boot Application

To start the application use:
```
java -jar target/kafka-sasl-plain-1.0.0.jar
```

### Produce an inbound event:

Produce a message to `demo-inbound-topic`:
```
docker exec -it kafka  /bin/sh /usr/bin/kafka-console-producer \
--topic demo-inbound-topic \
--bootstrap-server kafka:29092
```
Now enter the message:
```
{"sequenceNumber": "1"}
```
The demo-inbound message is consumed by the application, which emits a resulting demo-outbound message.

### Consume Events

Check for the emitted message on the `demo-outbound-topic`:
```
docker exec -it kafka  /bin/sh /usr/bin/kafka-console-consumer \
--topic demo-outbound-topic \
--bootstrap-server kafka:29092 \
--from-beginning
```
Output:
```
{"sequenceNumber":"1"}
```

## Integration Tests

Run integration tests with `mvn clean test`

The tests demonstrate sending events to an embedded in-memory Kafka that are consumed by the application, resulting in outbound events being published.

SASL is disabled for the integration tests (in `src/test/resources/application-test.yml` config `kafka.sasl.enabled: false`.  This is because these tests uses the embedded Kafka broker which does not support SASL.

## Component Tests

### Overview

The tests demonstrate sending events to a dockerised Kafka that are consumed by the dockerised application, resulting in outbound events being published.

SASL PLAIN authentication is enabled in the Component Test Framework, via the `maven-surefire-plugin` `component` plugin configuration.  `kafka.sasl.plain.enabled` is set to `true`.  This requires that consumers and producers connecting to Kafka authenticate using SASL PLAIN.  This applies to both the application and the test consumers and producers. 

For more on the component tests see: https://github.com/lydtechconsulting/component-test-framework

### Build

Build Spring Boot application jar:
```
mvn clean install
```

Build Docker container:
```
docker build -t ct/kafka-sasl-plain:latest .
```

### Test Execution

Run tests (by default the containers are torn down after the test run):
```
mvn test -Pcomponent
```

Run tests leaving the containers up at the end:
```
mvn test -Pcomponent -Dcontainers.stayup=true
```

## Docker Clean Up

Manual clean up (if left containers up):
```
docker rm -f $(docker ps -aq)
```

Further docker clean up (if network issues and to remove old networks/volumes):
```
docker network prune
docker system prune
docker volume prune
```
