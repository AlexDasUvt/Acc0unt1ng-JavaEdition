package GUI;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPSender {
    public int SendHTTP(String route, String JSON) throws MalformedURLException {
        // TODO Add threads (if needed)
        URL url = switch (route) {
            case "main" -> new URL("http://127.0.0.1:8080/main");
            case "transfer" -> new URL("http://127.0.0.1:8080/transfer");
            case "read" -> new URL("http://127.0.0.1:8080/read");
            default -> null;
        };

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = JSON.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            return connection.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
