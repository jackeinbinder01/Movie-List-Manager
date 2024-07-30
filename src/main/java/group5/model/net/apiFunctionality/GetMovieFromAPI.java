package group5.model.net.apiFunctionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group5.model.beans.MBeans;

public class GetMovieFromAPI {

    private static final String API_KEY = "b253e842"; // Replace with your actual API key
    private static String urlString;

    /**
     * the empty constructor for the class.
     */
    public GetMovieFromAPI() {
    }

    /**
     * makes a url string for only title.
     *
     * @param title
     * @return
     */
    public static String makeURLString(String title) {
        return buildURL(title, null, null);
    }

    /**
     * makes a url string for title & year.
     *
     * @param title
     * @param year
     * @return
     */
    public static String makeURLString(String title, String year) {
        return buildURL(title, year, null);
    }

    /**
     * makes a url string for title, year & type.
     *
     * @param title
     * @param year
     * @param type
     * @return String of a url
     */
    public static String makeURLString(String title, String year, String type) {
        return buildURL(title, year, type);
    }

    /**
     * a url builder for the methods above.
     *
     * @param title
     * @param year
     * @param type
     * @return a string of the url
     */
    private static String buildURL(String title, String year, String type) {
        StringBuilder urlBuilder = new StringBuilder("https://www.omdbapi.com/?apikey=");
        urlBuilder.append(API_KEY).append("&t=").append(URLEncoder.encode(title, StandardCharsets.UTF_8));
        if (year != null && !year.isEmpty()) {
            urlBuilder.append("&y=").append(URLEncoder.encode(year, StandardCharsets.UTF_8));
        }
        if (type != null && !type.isEmpty()) {
            urlBuilder.append("&type=").append(URLEncoder.encode(type, StandardCharsets.UTF_8));
        }
        return urlBuilder.toString();
    }

    /**
     * setter for the url.
     *
     * @param url
     */
    public static void setURL(String url) {
        urlString = url;
    }

    /**
     * getter for the url.
     *
     * @return a string of the url.
     */
    public static String getURL() {
        return urlString;
    }

    /**
     * gets a single movie from the api.
     *
     * @param title
     * @param year
     * @param type
     * @return the input stream containing the details of a movie.
     */
    public static MBeans getMovie(String title, String year, String type) {
        try {
            setURL(buildURL(title, year, type));

            // Create URL object and open connection
            URL url = new URL(getURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get response code and handle response
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return parseAPITitle(conn.getInputStream());
            } else {
                handleErrorResponse(conn);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * handles the error when a pull doesnt work.
     *
     * @param conn
     * @throws IOException
     */
    private static void handleErrorResponse(HttpURLConnection conn) throws IOException {
        System.out.println("GET request failed. Response Code: " + conn.getResponseCode());
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();

        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        System.out.println("Error Response: " + errorResponse.toString());
    }

    /**
     * creates an MBean from the stream form api.
     *
     * @param inputStream
     * @return an MBean
     */
    public static MBeans parseAPITitle(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode searchResults = root.path("Search");
            if (searchResults.isArray()) {
                for (JsonNode node : searchResults) {
                    return mapper.treeToValue(node, MBeans.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
