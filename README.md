# springboot-reactor-kafka

  # A. How to run the application

  1. Download and install Kafka locally
     - Visit: https://kafka.apache.org/quickstart
     - Click Download to download the tar file
     - Un-tar the tar-file in console with command for example: tar -xzf kafka_2.11-2.0.0.tgz
       
  2. Start Zookeeper in a console
     cd /bin/windows
     zookeeper-server-start.bat ../../config/zookeeper.properties
    
  3. Start Kafka in a second console
     cd /bin/windows
     kafka-server-start.bat ../../config/server.properties

  4. Run the spring boot application

  5. Send a request to endpoint (for example by using postman)
     localhost:8080/email

  6. You should be able to see in Consumer of Kafka the message (in console or in an endpoint)
     If in console, use the following comand line: 
     
     cd /bin/windows
     kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic <MY-TOPIC-NAME> --from-beginning
