 Kafka-with-twitter <br>
 fetch realtime data from twitter - <br>
 * Create a twitter client<br>
 * Create Kafka Producer
 * You need to manually create the topic used by producer. In my case it is twitter-tweets.
 `kafka-topics --zookeeper localhost:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1`
 
