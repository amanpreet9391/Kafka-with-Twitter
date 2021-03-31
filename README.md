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
 We don't want all the tweets, just the selected ones. In this case we have taken the condition that, only take those tweets 
 with keywords "bitcoin" and "kafka". We can take as many terms as we want. Declare the host you want to connect to, the endpoint, and authentication.
 To connect to Twitter API first condition is to create a [Twitter Developer Account](https://developer.twitter.com/en), and create an application.
 You'll get credentials which are required for the authentication. Connect to the client `client.connect()` and we are done. <br>
 
 * <b>Create Kafka Producer</b> <br>
 After the client let's create the producer. Client will fetch the desired tweets from Twitter and store in an ArrayList(msgQueue).
 For producer, first of all define the `Kafka-Topic`. We need to manually create this topic through `kafka-topics --zookeeper localhost:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1`.
 My topic name is `twitter-tweets`. The message(messageQueue.poll()) will be taken from the messageQueue to be produced to Kafka. To learn more refer the [link](https://docs.confluent.io/current/clients/java.html). <br>
 <img width="1680" alt="Screenshot 2020-05-19 at 2 43 41 AM" src="https://user-images.githubusercontent.com/25201552/82261546-d5316500-997c-11ea-85da-90eaeca28bf0.png"><br>
 Here is the output format from producer. It will show the desired tweets. <br>
 

 * <b>Create Elasticsearch Client</b> <br>
 For free ElasticSearch cluster refer to [app.bonsai.io](https://bonsai.io/). Signup and create free cluster with 3 nodes. This will give you your own elasticsearch cluster.
 You need to provide credentials here as well, which are available in `Access`section of bonsai. This client will allow us to insert data in elasticsearch.<br>
 
 * <b>Create Kafka Consumer</b> <br>
 For consumer, first of all create properties, then create consumer and then subscribe the consumer with the 
 Kafka topic i.e. `twitter-tweets`.For now we are displaying the number of recieved tweets and then forwarding them to 
 ElasticSearch. An ID is generated through which you can access the exact tweet in Elasticsearch by `GET /twitter-tweets/tweets/ID`<br>
 <img width="1668" alt="Screenshot 2020-05-19 at 2 58 16 AM" src="https://user-images.githubusercontent.com/25201552/82262010-c13a3300-997d-11ea-901b-fceb83a27162.png">
We got an ID `hbuuKXIBvbRgTsLvXfCH`, then after running `GET twitter-tweets/tweets/hbuuKXIBvbRgTsLvXfCH`, we got the complete information of that
tweet in JSON format.<br>
 
 
Note - Before trying to run producer and consumer, there are two things one has to make sure first. <br>
 (1) Zookeeper should be running.
 To run zookeeper - ` zookeeper-server-start config/zookeeper.properties`.
 (2) Kafka server should be running.
 To run a kafka server - ` kafka-server-start config/kafka.properties`.    <br>
 <b> Upcoming changes</b><br>
 In future I am also planning to perform monitoring with the help of tools like grafana or cprometheus. So stay tuned!
  To learn more about Kafka refer to official [documentation](https://kafka.apache.org/documentation/).                                                                                                 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
