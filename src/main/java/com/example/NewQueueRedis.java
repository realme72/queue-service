import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpstashQueue {

    private static final String UPSTASH_API_URL = "https://your-upstash-url.com";
    private static final String QUEUE_KEY = "my_queue";

    public static void main(String[] args) {
        // Example usage
        UpstashQueue queue = new UpstashQueue();
        queue.enqueue("Message 1");
        queue.enqueue("Message 2");
        System.out.println(queue.dequeue()); // Output: Message 1
        System.out.println(queue.dequeue()); // Output: Message 2
    }

    public void enqueue(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(UPSTASH_API_URL + "/RPUSH/" + QUEUE_KEY);
            StringEntity params = new StringEntity(message);
            request.addHeader("content-type", "text/plain");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.err.println("Failed to enqueue message: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String dequeue() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(UPSTASH_API_URL + "/LPOP/" + QUEUE_KEY);
            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                        return reader.readLine();
                    }
                }
            } else {
                System.err.println("Failed to dequeue message: " + response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
