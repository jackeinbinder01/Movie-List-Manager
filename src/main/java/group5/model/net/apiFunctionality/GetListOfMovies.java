package group5.model.net.apiFunctionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group5.model.beans.MBeans;

public class GetListOfMovies {

    /**
     * constructor for the class.
     */
    public GetListOfMovies() {
    }

    /**
     * gets more source beans for generic searches.
     *
     * @param title
     * @param yearRange
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

                for (int i = startYear; i <= endYear; i++) {
                    movieList.add(GetMovieFromAPI.getMovie(apiBean.getTitle(), Integer.toString(i), null));
                }
            } else {
                int year = Integer.parseInt(yearRange);
                if (apiBean.getYear() == year) {
                    movieList.add(GetMovieFromAPI.getMovie(apiBean.getTitle(), yearRange, null));
                }
            }
        }
        return movieList;
    }

    /**
     * gets the list apibeans from the search of api.
     *
     * @param title
     * @return
     */
    public static List<apiBeans> getMovieListFromAPI(String title) {

        // Create URL object
        try {
            String urlString = String.format("https://www.omdbapi.com/?apikey=b253e842&s=%s", title);
            URL url = new URL(urlString);
            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get response code
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return parseAPISearch(conn.getInputStream());
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
        } catch (MalformedURLException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * creates the list of apibeans from the input stream of the search.
     *
     * @param inputStream
     * @return a list of apibeans
     */
    public static List<apiBeans> parseAPISearch(InputStream inputStream) {
        List<apiBeans> apiList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode searchResults = root.path("Search");
            if (searchResults.isArray()) {
                for (JsonNode node : searchResults) {
                    apiBeans movie = mapper.treeToValue(node, apiBeans.class);
                    apiList.add(movie);

                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return apiList;
    }
}
