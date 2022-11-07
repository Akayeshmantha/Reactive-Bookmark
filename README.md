# ShopList App
Spring-boot 2.7.3 🚀 + Reactive application for demo purposes.

## How to run 🏃
- Make sure you have docker and docker-compose in place
- Run docker-compose up and u will have the db running in seconds for the demo  
  (PS: Change the volume, or the port of the db to match you're needs)
- Connect to the db execute the sql script to create db and tables required.
- Make sure you've installed Java 11 and apache maven 3.8+
- `mvn spring-boot:run`

## What it serves for

With the ShopList app you can maintain shopping list or bookmark list for the customers of your ecommerce application

1. Customers can maintain more than one list.
2. Easy to add new products to customer list.
3. Change the quantity of the items in the list.
4. Get rid of useless lists and also useless products.

## Future work
1. Use openapi to define the contracts and generate Rest Controller interface based on the api specification.
2. Use a non-blocking JSON Serialize/Deserializer in db converter to make the whole process fully reactive.
3. Execute the db script during the application startup