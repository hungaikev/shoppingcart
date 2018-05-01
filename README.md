# ShoppingCart

Lagom Playground. 

Running the Project

```
sbt runAll

```

Adding an Item to our Cart 

```
curl -X POST -d '{ "product": "Apples" }' -i -H “Content-Type: application/json” http://localhost:9000/api/add-to-cart/21

```

Add another Item

```
curl -X POST -d '{ "product": "Mongo" }' -i -H “Content-Type: application/json” http://localhost:9000/api/add-to-cart/21

```


```
curl -X POST -d '{ "product": "Oranges" }' -i -H “Content-Type: application/json” http://localhost:9000/api/add-to-cart/21

```

Find out how many Items are in our Cart? 

```
curl http://localhost:9000/api/cart/21

```


