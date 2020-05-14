package Twitter;

import java.util.Properties;
import java.util.Scanner;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());
    public TwitterProducer(){}
    public static void main(String[] args) {


        new TwitterProducer().run();
    }
    public void run(){
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);
       //create a twitter client
        // Attempts to establish a connection.
        Client client= TwitterClient(msgQueue);
        client.connect();
       // create a kafka producer
        KafkaProducer<String, String> producer = createProducer();

        //adding a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down the application");
            logger.info("Shutting down the client");
            client.stop();
            producer.close();
            logger.info("done");
        }));

       //  send tweets to kafka */
        while (!client.isDone()) {
            String msg = null;
            try {
                //System.out.println("Mesage is null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null){
                logger.info(msg);
                producer.send(new ProducerRecord<>("twitter-tweets", null, msg), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e!=null){
                            logger.info("Error !!!",e);
                        }
                    }
                });
            }

        } logger.info("End of application");



    }
    public Client TwitterClient(BlockingQueue<String> msgQueue){

        Scanner obj1 = new Scanner(System.in);
        String consumerkey;
        System.out.println("Enter ConsumerKey ");
        consumerkey = obj1.next();
        Scanner obj2 = new Scanner(System.in);
        String consumersecret;
        System.out.println("Enter ConsumerSecret ");
        consumersecret = obj2.next();
        Scanner obj3 = new Scanner(System.in);
        String token;
        System.out.println("Enter token ");
        token = obj3.next();
        Scanner obj4 = new Scanner(System.in);
        String secret;
        System.out.println("Enter secret ");
        secret = obj4.next();



        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();


        List<String> terms = Lists.newArrayList("Kafka");

        hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerkey, consumersecret, token, secret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));


        Client hosebirdClient = builder.build();
        return hosebirdClient;

    }

    public KafkaProducer<String,String> createProducer(){
        // create properties
        String bootstrapServer = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");

        //high throughput properties

        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024)); //32KB batch size
        //create a kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;



    }
}
