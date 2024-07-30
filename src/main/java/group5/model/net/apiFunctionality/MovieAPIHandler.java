package group5.model.net.apiFunctionality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import group5.model.beans.MBeans;

public class MovieAPIHandler {

    public MovieAPIHandler() {
    }

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
                    movieList.add(getMovie(apiBean.getTitle(), Integer.toString(i), null));
                }
            } else {
                int year = Integer.parseInt(yearRange);
                if (apiBean.getYear() == year) {
                    movieList.add(getMovie(apiBean.getTitle(), yearRange, null));
                }
            }
        }
        return movieList;
    }

    public static List<apiBeans> getMovieListFromAPI(String title) {
        try {
            URL url = new URL("API_ENDPOINT" + title); // Use the actual API endpoint
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

    public static MBeans getMovie(String title, String year, String otherParameter) {
        try {
            URL url = new URL("API_ENDPOINT" + title + "&year=" + year); // Use the actual API endpoint
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

    public static List<apiBeans> parseAPITitle(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
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

    public static MBeans parseMovieFromAPI(InputStream inputStream) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(inputStream);
            return mapper.treeToValue(root, MBeans.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
