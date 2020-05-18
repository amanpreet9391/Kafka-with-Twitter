 Kafka-with-twitter <br>
 In this project, Kafka is used
  with Twitter API. <br>
  Main idea behind this workflow is first to fetch required data(tweets) with the help of Twitter API and then storing that data in 
  ElasticSearch. Kafka is used as a messaging system. It collect data from Twitter and store in ElasticSearch. Lets try to make things a bit more clear with the help of a flow-diagram.<br>
 
 <br>
 <img width="835" alt="Screenshot 2020-05-19 at 1 47 38 AM" src="https://user-images.githubusercontent.com/25201552/82255734-c47bf180-9972-11ea-9164-8e594afac7ca.png">
 

 
 
 
 
 <br>
 <br>
 
 Steps -
 * <b>Create Twitter Client </b><br>
 We don't want all the tweets, just the selected one. In this case we have taken the condition that, only take those tweets 
 with keyword "bitcoin" and "kafka". We can take as many terms as we want. Declare the host you want to connect to, the endpoint, and authentication.
 To connect to Twitter API first condition is to create a [Twitter Developer Account](https://developer.twitter.com/en), and create an application.
 You'll get few credentials which are required for the authentication. Connect to the client `client.connect()` and we are done. <br>
 
 * <b>Create Kafka Producer</b> <br>
 After the client let's create the producer. Client will fetch the desired tweets from Twitter and store in an ArrayList(msgQueue).
 For producer, first of all define the `Kafka-Topic`. We need to manually create this topic through `kafka-topics --zookeeper localhost:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1`.
 My topic name is `twitter-tweets`. The message(messageQueue.poll()) will be taken from the messageQueue to be produced to Kafka. To learn more refer the [link](https://docs.confluent.io/current/clients/java.html). <br>
 
 * <b>Create Elasticsearch Client</b> <br>
 For free ElasticSearch cluster refer to [app.bonsai.io](https://bonsai.io/). Signup and create free cluster with 3 nodes. This will give your own elasticsearch cluster.
 You need to provide credentials here as well, which are available in `Access`section of bonsai. This client will allow us to insert data in elasticsearch.
 <br>
 * Create Kafka Consumer <br>
 For consumer, first of all create properties, then create consumer and then subscribe the consumer with the 
 Kafka topic i.e. `twitter-tweets`.For now we are displaying the number of recieved tweets and then forwarding them to 
 ElasticSearch. An ID is generated through which you can access the exact tweet in Elasticsearch by `GET /twitter-tweets/tweets/ID`
 <br>
                                                                                                      
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 