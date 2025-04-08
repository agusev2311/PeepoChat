package agusev.peepochat.client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionChecker {

    // URL вашего API (замените на актуальный адрес, если это необходимо)
    private static final String API_URL = "http://localhost:5000/api/version";
    private static final Gson gson = new Gson();

    public static VersionResponse checkForUpdate(String clientVersion) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"version\": \"" + clientVersion + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
            }

            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            br.close();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Ошибка при получении обновления: " + response.toString());
                return null;
            }

            VersionResponse versionResponse = gson.fromJson(response.toString(), VersionResponse.class);
            return versionResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * VersionChecker.VersionResponse response = VersionChecker.checkForUpdate("1.0");
     * if (response != null && response.has_update) { ... }
     */
    public static class VersionResponse {
        public String version_status;
        public boolean has_update;
        public String changelog;
        public String new_version_link;
    }
}