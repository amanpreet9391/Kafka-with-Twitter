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
 * Create Twitter Client <br>
 We don't want all the tweets, just the selected one. In this case we have taken the condition that, only take those tweets 
 with keyword "bitcoin" and "kafka". We can take as many terms as we want. Declare the host you want to connect to, the endpoint, and authentication.
 To connect to Twitter API first condition is to create a [Twitter Developer Account](https://developer.twitter.com/en), and create an application.
 You'll get few credentials which are required for the authentication. Connect to the client `client.connect()` and we are done. <br>
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 fetch realtime data from twitter - <br>
 * Create a twitter client<br>
 * Create Kafka Producer
 * You need to manually create the topic used by producer. In my case it is twitter-tweets.
 `kafka-topics --zookeeper localhost:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1`
 * Create Elasticsearch client
 * Create consumer
 
