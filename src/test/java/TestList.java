
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import group5.model.MovieList;
import group5.model.beans.MBeans;

public class TestList {

    private MovieList movieList;

    @BeforeEach
    public void setUp() {
        movieList = new MovieList();
    }

    @Test
    public void testAddToListAndGetMovieList() throws MalformedURLException {
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);
        MBeans movie2 = new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                73, 8.7, 123456789, "tt0133093", false, -1.0);

        movieList.addToList("1", Stream.of(movie1, movie2));

        List<String> movies = movieList.getMovieList();
        assertEquals(2, movies.size());
        assertTrue(movies.contains("Inception"));
        assertTrue(movies.contains("The Matrix"));
    }

    @Test
    public void testClear() throws MalformedURLException {
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);

        movieList.addToList("1", Stream.of(movie1));
        movieList.clear();

        List<String> movies = movieList.getMovieList();
        assertTrue(movies.isEmpty());
    }

    @Test
    public void testCount() throws MalformedURLException {
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);
        MBeans movie2 = new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                73, 8.7, 123456789, "tt0133093", false, -1.0);

        movieList.addToList("1", Stream.of(movie1, movie2));

        assertEquals(1, movieList.count());
    }

    @Test
    public void testSaveMovie() throws IOException, MalformedURLException {
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);
        MBeans movie2 = new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                73, 8.7, 123456789, "tt0133093", false, -1.0);

        movieList.addToList("1", Stream.of(movie1, movie2));

        String filename = "test_movies.txt";
        movieList.savemovie(filename);

        File file = new File(filename);
        assertTrue(file.exists());

        BufferedReader reader = new BufferedReader(new FileReader(file));
        assertEquals("Inception", reader.readLine());
        assertEquals("The Matrix", reader.readLine());
        reader.close();

        file.delete();
    }

    @Test
    public void testRemoveFromList() throws MalformedURLException {
        MBeans movie2 = new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                73, 8.7, 123456789, "tt0133093", false, -1.0);
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);

        movieList.addToList("ALL", Stream.of(movie1, movie2));

        movieList.removeFromList("2");

        List<String> movies = movieList.getMovieList();
        assertEquals(1, movies.size());
        assertFalse(movies.contains("Inception"));
        assertTrue(movies.contains("The Matrix"));
    }

    @Test
    public void testAddToListWithInvalidRange() throws MalformedURLException {
        MBeans movie1 = new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                74, 8.8, 8300000, "tt1375666", false, -1.0);

    }
}
