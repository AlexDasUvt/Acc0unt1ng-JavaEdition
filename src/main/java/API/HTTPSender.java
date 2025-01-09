package API;

import DBObjects.ResultData;
import Interfaces.Debuggable;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPSender implements Debuggable {
    URL mainURL;
    URL tranURL;
    URL readURL;

    public HTTPSender() throws MalformedURLException {
        mainURL = new URL("http://127.0.0.1:8080/add/main");
        tranURL = new URL("http://127.0.0.1:8080/add/transfer");
        readURL = new URL("http://127.0.0.1:8080/read/raw");
    }

    public int AddRecordHTTP(String route, String JSON) {
        URL url = null;
        if (route.equals("main")) {
            url = mainURL;
        } else if (route.equals("transfer")) {
            url = tranURL;
        }

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

    public ResultData ReadDBHTTP(String JSON) {
        try {
            Debug("JSON:\n " + JSON);
            HttpURLConnection connection = (HttpURLConnection) readURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = JSON.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Debug("Response code: " + responseCode);
                // Read the response from the input stream
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Convert the response (JSON string) to ResultData object
                    ObjectMapper objectMapper = new ObjectMapper();

                    return objectMapper.readValue(response.toString(), ResultData.class);
                }
            } else {
                System.out.println("Error: HTTP response code " + responseCode);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void Debug(String message) {
        if (isDebugMode) {
            String className = this.getClass().getSimpleName();
            long timestamp = System.currentTimeMillis();
            System.out.println("DEBUG: [" + timestamp + "] " + className + ": " + message);
        }
    }
}
