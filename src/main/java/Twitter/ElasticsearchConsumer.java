package Twitter;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient.FailureListener;
public class ElasticsearchConsumer {
    //https://62i7k0lrwx:8w0g7da9ww@kafk-with-twitter-4824160048.eu-west-1.bonsaisearch.net:443
    // create an elasticsearch client
    public static RestHighLevelClient createClient(){

        String hostname = "";
        String username = "";
        String password = "";
        // need to provide credentials in order to access elasticsearch cluster in cloud
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username,password));
        //connect over http with the hostname indicated above over the port 443. Encrypted connection to the cloud.
        RestClientBuilder builder = RestClient.builder( new
                HttpHost(hostname,443,"https"))
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







    public static void main(String[] args) {


        RestHighLevelClient client = createClient();
        //This index request will fail if index "twitter" doesnot exits.
        IndexRequest indexRequest = new IndexRequest("twitter","tweets");


    }
}
