package group5.model.net.apiFunctionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group5.model.beans.MBeans;

public class MovieAPIHandler {

    private static final String API_ENDPOINT = "http://www.omdbapi.com/";
    private static final String API_KEY = "b253e842";

    public MovieAPIHandler() {
    }

    /**
     * gets a list of MBeans to add.
     *
     * @param title the title of a movie
     * @param yearRange the year/range of years
     * @return the list of mbeans to add
     */
    public static List<MBeans> getMoreSourceBeans(String title, String yearRange) {
        List<MBeans> movieList = new ArrayList<>();
        List<apiBeans> apiList = getMovieListFromAPI(title);
        if (apiList == null) {
            System.out.println("No movies found for the given title.");
            return null;
        }

        for (apiBeans apiBean : apiList) {
            if (yearRange.contains("-")) {
                String[] years = yearRange.split("-");
                int startYear = Integer.parseInt(years[0]);
                int endYear = Integer.parseInt(years[1]);
                // get the high and low year if posible

                if (apiBean.getYear() >= startYear && apiBean.getYear() <= endYear) {
                    // if range
                    movieList.add(getMovie(apiBean.getID()));
                    // if inbewtween add
                }
            } else {
                int year = Integer.parseInt(yearRange);
                if (apiBean.getYear() == year) {
                    movieList.add(getMovie(apiBean.getID()));
                    // if no range then if same add
                }
            }
        }
        return movieList;
    }

    /**
     * gets the api beans from a title search
     *
     * @param title the title of a search
     * @return the list of apibeans containing the movies
     */
    public static List<apiBeans> getMovieListFromAPI(String title) {
        try {
            URL url = new URL(API_ENDPOINT + "?apikey=" + API_KEY + "&s=" + title);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

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
     * gets the specific movie using an imdb id.
     *
     * @param imdbID the unique movie code
     * @return the mBean of the movie
     */
    public static MBeans getMovie(String imdbID) {
        try {
            URL url = new URL(API_ENDPOINT + "?apikey=" + API_KEY + "&i=" + imdbID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return parseMovieFromAPI(conn.getInputStream());
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
     * handles when the api is not reached.
     *
     * @param conn the connection
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
     * creates a list of apiBeans from the output of the api.
     *
     * @param inputStream the output of the api.
     * @return the list of apibeans
     */
    public static List<apiBeans> parseAPITitle(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode searchResults = root.path("Search");
            List<apiBeans> apiList = new ArrayList<>();
            if (searchResults.isArray()) {
                for (JsonNode node : searchResults) {
                    apiList.add(mapper.treeToValue(node, apiBeans.class));
                }
            }
            return apiList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * creates an MBeans from the output of the getmovie
     *
     * @param inputStream the output of the get movie api call
     * @return an MBeans
     */
    public static MBeans parseMovieFromAPI(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode root = mapper.readTree(inputStream);
            return mapper.treeToValue(root, MBeans.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
