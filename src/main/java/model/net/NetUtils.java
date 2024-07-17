package model.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {

    /**
     * Format required for the API request. There are many options, but keeping it simple for now.
     */
    private static final String API_URL_FORMAT = "http://www.omdbapi.com/?apikey=b253e842&r=json&t=%s&y=%s&type=%s";

    /**
     * Private constructor to prevent instantiation.
     */
    private NetUtils() {
        // Prevent instantiation
    }

    /**
     * Returns the URL for the API request.
     *
     * Defaults to JSON format for more detailed results.
     *
     * @param ip The IP address to look up.
     * @return The URL for the API request.
     */
    private static String getApiUrl(String title, String year, String type) {
        return String.format(API_URL_FORMAT, title, year, type);
    }

    /**
     * Gets the contents of a URL as an InputStream.
     *
     * @param urlStr the URL to get the contents of
     * @return the contents of the URL as an InputStream, or the null InputStream if the connection
     *         fails
     *
     */
    private static InputStream getUrlContents(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            int status = con.getResponseCode();
            if (status == 200) {
                return con.getInputStream();
            } else {
                System.err.println("Failed to connect to " + urlStr);
            }

        } catch (Exception ex) {
            System.err.println("Failed to connect to " + urlStr);
        }
        return InputStream.nullInputStream();
    }

    /**
     * Gets media details using the omdb API.
     *
     * @param ip the IP address to get the information about
     * @param format the format of the response
     * @return the contents of the URL as an InputStream, or the null InputStream if the connection
     */
    public static InputStream getMediaDetails(String title, String year, String type) {
        String urlStr = getApiUrl(title, year, type);
        return getUrlContents(urlStr);
    }
}
