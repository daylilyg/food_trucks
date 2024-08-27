# Food Truck search service

## 1. Introduction
This is a project based on [San Francisco's food truck open dataset](https://data.sfgov.org/api/views/rqzj-sfat/rows.csv)  
You can use it to find the nearby food truck quickly.  
Here is the demo [link](https://364000.imdo.co)

## 2. Architecture
This project use framework and middlewares below:
* Spring Boot 3.3.3 GA  
* ElasticSearch server 8.15.0 
* Spring Data Elasticsearch 3.3.3  
* Frontend framework React 18.3.1  

## 3. How to run it locally?
* First you should download [Elasticsearch 8.15.0](https://www.elastic.co/cn/downloads/elasticsearch) 
and [Kibana 8.15.9](https://www.elastic.co/cn/downloads/kibana)
* Then start the Elasticsearch and Kibana, use kibana csv import function to init the food truck index 
* Finally, you can change the password in application.yaml and build frontend by running script build_frontend.bat
* Now you can run and debug the service in you IDE

## 4. How to deploy it?
* First you should prepare an Elasticsearch instance, you can refer the official document to do it.
* The frontend is integrated to backend service, if you want to separate it you shouldn't use build_frontend.bat, 
you need build it following food-truck-frontend/README.md. Note that if you want to use different domain name for 
backend and frontend, you should add CORS config in backend service.
* Run command "mvn package" first, then you can copy the target jar (target/food_trucks-1.0-SNAPSHOT.jar)
to prod machine by the method you like; The launch command is "java -jar path/to/food_trucks-1.0-SNAPSHOT.jar".
* If you want to use container image to deliver it, you can build image using docker or podman, 
then you can use Kubernetes to deploy it.  
Note that the project doesn't provide the docker file so you need write it by yourself

