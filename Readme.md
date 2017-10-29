# Bloomberg Data Warehouse Sample project using Java 8 and Morphia (JPA for Mongo DB)

## In order to download project, go to [https://github.com/bhupendrasingjava/bloomberg.git] 

This is a simple test project I used to show some Morphia features at MongoUK2011.

## Entities
The basic structure of the Entities looks like this

1: FileDetails (Basic Entity class that contain file information and Embedded entities list for (valid and invalid rows) entities)
2: OrderSummary ( To maintain accumulative count of deals per Ordering Currency "Columns : Currency ISO Code, CountOfDeals ")

## Requirements
*   JDK8+
*   Maven2+ (tested with Maven2 and Maven3)
*   MongoDB 3.4.2
*   Apache-tomcat-9.0.0 

An IDE like Eclipse or IntelliJ is highly recommended but not required. I used IntelliJ idea 2017.1.1



