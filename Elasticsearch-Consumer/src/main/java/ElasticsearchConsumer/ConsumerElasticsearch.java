package ElasticsearchConsumer;

import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indexlifecycle.StopILMRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerElasticsearch {
    // create an elasticsearch client

    public static RestHighLevelClient createClient() {


        /** Enter your own hostname, username and password. you can use bonsai.io to get free elasticsearch cluster with 3 nodes.     */
        String hostname = "";
        String username = "";
        String password = "";

        // need to provide credentials in order to access elasticsearch cluster in cloud
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        //connect over http with the hostname indicated above over the port 443. Encrypted connection to the cloud.
        RestClientBuilder builder = RestClient.builder(new
                HttpHost(hostname, 443, "https"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

                    }
                });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
        //returning a client which will allow us to insert data in elasticsearch.

    }
    public static KafkaConsumer<String, String> createConsumer(String topic){
        String bootstrapServer = "127.0.0.1:9092";
        String groupId = "twitter-consumer";
        //String topic = "twitter-tweets";
        //consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //create kafka consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }






    public static void main(String[] args) throws IOException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(ConsumerElasticsearch.class.getName());

        /** just used for testing */
        //String jsonString = "{ \"foo \" : \" bar \"      }";
        RestHighLevelClient client = createClient();
        //This index request will fail if index "twitter" doesnot exits.


        KafkaConsumer<String, String> consumer = createConsumer("twitter-tweets") ;
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for(ConsumerRecord<String,String> record: records){
                //where we insert data into elasticsearch
                    record.value();
                    String id = extractIdfromTwitter(record.value());
                    IndexRequest indexRequest = new IndexRequest("twitter","tweets",id).source(record.value(), XContentType.JSON);
                    //id is to make consumer idempotent
                    IndexResponse indexResponse =client.index(indexRequest, RequestOptions.DEFAULT);


                    logger.info(indexResponse.getId());
                    Thread.sleep(1000);  //introduced small delay to see results clearly

                }


        }
            ///client.close();
        //This will insert jsonString text in the index(twitter) and return id to us.
    }
    private static JsonParser jsonParser= new JsonParser();
    private static String extractIdfromTwitter(String tweetjason){
        //use gson library
        return jsonParser.parse(tweetjason).getAsJsonObject().get("id_str").getAsString();

    }









}