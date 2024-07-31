
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import group5.model.beans.MBeans;
import group5.model.net.apiFunctionality.MovieAPIHandler;
import group5.model.net.apiFunctionality.apiBeans;

public class TestMovieAPIHandler {

    @Test
    public void testGetMoreSourceBeansWithSpecificSearch() {
        String title = "The Matrix";
        String yearRange = "1999";
        List<MBeans> movieList = MovieAPIHandler.getMoreSourceBeans(title, yearRange);

        Assertions.assertNotNull(movieList, "Movie list should not be null");
        Assertions.assertFalse(movieList.isEmpty(), "Movie list should not be empty");
        for (MBeans movie : movieList) {

        }
    }

    @Test
    public <apiBeans> void testGetMovieListFromAPI() {
        String title = "Inception";
        List<apiBeans> apiList = (List<apiBeans>) MovieAPIHandler.getMovieListFromAPI(title);

        Assertions.assertNotNull(apiList, "API list should not be null");
        Assertions.assertFalse(apiList.isEmpty(), "API list should not be empty");
        for (apiBeans oneBean : apiList) {
            Assertions.assertEquals("Inception", oneBean.getTitle(), "Movie title should be Inception");
        }
    }

    @Test
    public void testGetMovie() {
        String imdbID = "tt1375666"; // IMDB ID for Inception
        MBeans movie = MovieAPIHandler.getMovie(imdbID);

        Assertions.assertNotNull(movie, "Movie should not be null");
        Assertions.assertEquals("Inception", movie.getTitle(), "Movie title should be Inception");
        Assertions.assertEquals(2010, movie.getYear(), "Movie year should be 2010");
    }
}
