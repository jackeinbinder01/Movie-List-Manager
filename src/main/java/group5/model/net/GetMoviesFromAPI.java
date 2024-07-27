package group5.model.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetMoviesFromAPI {

    private static final String API_KEY = "b253e842"; // Replace with your actual API key
    private static String urlString;

    public GetMoviesFromAPI() {
        // Constructor
    }

    public static String makeURLString(String title) {
        return String.format("https://www.omdbapi.com/?apikey=%s&t=%s", API_KEY, URLEncoder.encode(title, StandardCharsets.UTF_8));
    }

    public static String makeURLString(String title, String year) {
        return String.format("https://www.omdbapi.com/?apikey=%s&t=%s&y=%s", API_KEY, URLEncoder.encode(title, StandardCharsets.UTF_8), URLEncoder.encode(year, StandardCharsets.UTF_8));
    }

    public static String makeURLString(String title, String year, String type) {
        return String.format("https://www.omdbapi.com/?apikey=%s&t=%s&y=%s&type=%s", API_KEY, URLEncoder.encode(title, StandardCharsets.UTF_8), URLEncoder.encode(year, StandardCharsets.UTF_8), URLEncoder.encode(type, StandardCharsets.UTF_8));
    }

    public static void setURL(String url) {
        urlString = url;
    }

    public static String getURL() {
        return urlString;
    }

    public static InputStream getMovie(String title, String year, String type) {
        try {
            if (year == null) {
                if (type == null) {
                    setURL(GetMoviesFromAPI.makeURLString(title));
                } else {
                    setURL(GetMoviesFromAPI.makeURLString(title, "", type));
                }
            } else {
                if (type == null) {
                    setURL(GetMoviesFromAPI.makeURLString(title, year));
                } else {
                    setURL(GetMoviesFromAPI.makeURLString(title, year, type));
                }
            }

            // Create URL object
            URL url = new URL(getURL());

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
                // BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // String inputLine;
                // StringBuilder response = new StringBuilder();

                // while ((inputLine = in.readLine()) != null) {
                //     response.append(inputLine);
                // }
                // in.close();
                // // Print result
                // System.out.println(response.toString());
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();
                System.out.println("Error Response: " + errorResponse.toString());
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
