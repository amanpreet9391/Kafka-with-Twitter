package Twitter;

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
import org.apache.kafka.common.protocol.types.Field;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterProducer {
    public TwitterProducer(){}
    public static void main(String[] args) {


        new TwitterProducer().run();
    }
    public void run(){
       /** create a twitter client

        create a kafka producer

         send tweets to kafka */




    }
    public void TwitterClient(){

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


        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(100000);
        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms

        List<String> terms = Lists.newArrayList("kafka");

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
// Attempts to establish a connection.
        hosebirdClient.connect();

    }
}
