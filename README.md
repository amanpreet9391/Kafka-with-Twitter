 Kafka-with-twitter <br>
 Kafka is a high-throughput distributed messaging system. In this Kafka is used
  with Twitter API. <br>
  Main idea behind this workflow is first to fetch required data(tweets) with the help of Twitter API and then storing that data in 
  ElasticSearch. Kafka is used as a messaging system. It collect data from Twitter and store in ElasticSearch. Lets try to make things a bit more clear with the help of a flow-diagram.
 
 
 
 
 
 <br>
 <br>
 
 
 fetch realtime data from twitter - <br>
 * Create a twitter client<br>
 * Create Kafka Producer
 * You need to manually create the topic used by producer. In my case it is twitter-tweets.
 `kafka-topics --zookeeper localhost:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1`
 * Create Elasticsearch client
 * Create consumer
 
