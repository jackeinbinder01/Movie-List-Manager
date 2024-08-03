
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import group5.model.beans.MBeans;
import group5.model.net.apiFunctionality.MovieAPIHandler;
import group5.model.net.apiFunctionality.APIBeans;

public class TestMovieAPIHandler {

    @Test
    public void testGetMoreSourceBeansWithSpecificSearch() {
        String title = "The Matrix";
        String yearRange = "1999";
        List<MBeans> movieList = MovieAPIHandler.getMoreSourceBeans(title, yearRange);

        Assertions.assertNotNull(movieList, "Movie list should not be null");
        Assertions.assertFalse(movieList.isEmpty(), "Movie list should not be empty");
        for (MBeans movie : movieList) {
            // Add any specific assertions based on known data
        }
    }

    @Test
    public void testGetMoreSourceBeansWithYearRange() {
        String title = "The Matrix";
        String yearRange = "1998-2000";
        List<MBeans> movieList = MovieAPIHandler.getMoreSourceBeans(title, yearRange);

        Assertions.assertNotNull(movieList, "Movie list should not be null");
        Assertions.assertFalse(movieList.isEmpty(), "Movie list should not be empty");
        for (MBeans movie : movieList) {
            Assertions.assertTrue(movie.getYear() >= 1998 && movie.getYear() <= 2000, "Movie year should be within the range 1998-2000");
        }
    }

    @Test
    public void testGetMoreSourceBeansWithNullYearRange() {
        String title = "The Matrix";
        String yearRange = null;
        List<MBeans> movieList = MovieAPIHandler.getMoreSourceBeans(title, yearRange);

        Assertions.assertNotNull(movieList, "Movie list should not be null");
        Assertions.assertFalse(movieList.isEmpty(), "Movie list should not be empty");
        for (MBeans movie : movieList) {
            // No specific year assertions since yearRange is null
        }
    }

    @Test
    public void testGetMovieListFromAPI() {
        String title = "Inception";
        List<APIBeans> apiList = MovieAPIHandler.getMovieListFromAPI(title);

        Assertions.assertNotNull(apiList, "API list should not be null");
        Assertions.assertFalse(apiList.isEmpty(), "API list should not be empty");
        Assertions.assertEquals(apiList.size(), 10);
    }

    @Test
    public void testGetMovieListFromAPIWithInvalidTitle() {
        String title = "InvalidMovieTitle";
        List<APIBeans> apiList = MovieAPIHandler.getMovieListFromAPI(title);

        Assertions.assertNotNull(apiList, "API list should not be null");
        Assertions.assertTrue(apiList.isEmpty(), "API list should be empty for an invalid title");
    }

    @Test
    public void testGetMovie() {
        String imdbID = "tt1375666"; // IMDB ID for Inception
        MBeans movie = MovieAPIHandler.getMovie(imdbID);

        Assertions.assertNotNull(movie, "Movie should not be null");
        Assertions.assertEquals("Inception", movie.getTitle(), "Movie title should be Inception");
        Assertions.assertEquals(2010, movie.getYear(), "Movie year should be 2010");
    }

    @Test
    public void testGetMovieWithInvalidID() {
        String imdbID = "invalidID";
        MBeans movie = MovieAPIHandler.getMovie(imdbID);

        Assertions.assertNull(movie, "Movie should be null for an invalid IMDB ID");
    }

    @Test
    public void testParseAPITitle() {
        // Mock InputStream for testing (use actual API response for real tests)
        InputStream inputStream = new ByteArrayInputStream("{\"Search\": [{\"Title\": \"Inception\", \"Year\": \"2010\", \"imdbID\": \"tt1375666\"}]}".getBytes());
        List<APIBeans> apiList = MovieAPIHandler.parseAPITitle(inputStream);

        Assertions.assertNotNull(apiList, "API list should not be null");
        Assertions.assertFalse(apiList.isEmpty(), "API list should not be empty");
        Assertions.assertEquals("Inception", apiList.get(0).getTitle(), "Movie title should be Inception");
    }

    @Test
    public void testParseMovieFromAPI() {
        // Mock InputStream for testing (use actual API response for real tests)
        InputStream inputStream = new ByteArrayInputStream("{\"Title\": \"Inception\", \"Year\": \"2010\", \"imdbID\": \"tt1375666\"}".getBytes());
        MBeans movie = MovieAPIHandler.parseMovieFromAPI(inputStream);

        Assertions.assertNotNull(movie, "Movie should not be null");
        Assertions.assertEquals("Inception", movie.getTitle(), "Movie title should be Inception");
        Assertions.assertEquals(2010, movie.getYear(), "Movie year should be 2010");
    }

    @Test
    public void testHandleErrorResponse() {
        // Mock HttpURLConnection for testing
        HttpURLConnection conn = Mockito.mock(HttpURLConnection.class);
        try {
            Mockito.when(conn.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
            InputStream errorStream = new ByteArrayInputStream("Error".getBytes());
            Mockito.when(conn.getErrorStream()).thenReturn(errorStream);

            MovieAPIHandler.handleErrorResponse(conn);
            // Check console output or use logging framework to verify error handling
        } catch (IOException e) {
            Assertions.fail("Exception should not be thrown");
        }
    }
}
