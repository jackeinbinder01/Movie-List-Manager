
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import group5.model.Filter.FilterHandler;
import group5.model.beans.MBeans;

public class TestFilter {

    private Set<MBeans> movies;
    private FilterHandler filterHandler;

    @BeforeEach
    public void setUp() {
        movies = new HashSet<>();
        try {
            movies.add(new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                    "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                    List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                    74, 8.8, 8300000, "tt1375666", false, -1.0));

            movies.add(new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                    "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                    List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                    73, 8.7, 123456789, "tt0133093", false, -1.0));

            movies.add(new MBeans("Interstellar", 2014, "Movie", "PG-13", LocalDate.of(2014, 11, 7), 169,
                    "Adventure, Drama, Sci-Fi", List.of("Christopher Nolan"), List.of("Jonathan Nolan", "Christopher Nolan"),
                    List.of("Matthew McConaughey", "Anne Hathaway"), "A team of explorers travel through a wormhole...",
                    List.of("English"), List.of("USA"), "Won 1 Oscar.", new URL("https://example.com/poster3.jpg"),
                    74, 8.6, 123456789, "tt0816692", false, -1.0));

            movies.add(new MBeans("The Social Network", 2010, "Movie", "PG-13", LocalDate.of(2010, 10, 1), 120,
                    "Biography, Drama", List.of("David Fincher"), List.of("Aaron Sorkin"),
                    List.of("Jesse Eisenberg", "Andrew Garfield"), "The story of Harvard student Mark Zuckerberg...",
                    List.of("English"), List.of("USA"), "Won 3 Oscars.", new URL("https://example.com/poster4.jpg"),
                    76, 7.7, 123456789, "tt1285016", false, -1.0));

            movies.add(new MBeans("Pandemic", 2007, "Series", "TV-14", LocalDate.of(2007, 1, 25), 120,
                    "Drama, Thriller", List.of("Armand Mastroianni"), List.of("Ron McGee", "Ed Napier"),
                    List.of("Tiffani Thiessen", "French Stewart"), "The bird flu virus spreads through Los Angeles...",
                    List.of("English"), List.of("USA"), "N/A", new URL("https://example.com/poster5.jpg"),
                    45, 5.3, -1, "tt0826763", false, -1.0));

            filterHandler = new FilterHandler(movies);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFilterByTitle() {
        List<MBeans> result = filterHandler.filter("title==Inception").collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    public void testFilterByYear() {
        List<MBeans> result = filterHandler.filter("released==2014").collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals(2014, result.get(0).getYear());
    }

    @Test
    public void testFilterByGenre() {
        List<MBeans> result = filterHandler.filter("genre==Action").collect(Collectors.toList());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(movie -> movie.getGenre().contains("Action")));
    }

    @Test
    public void testFilterByDirector() {
        List<MBeans> result = filterHandler.filter("director==Christopher Nolan").collect(Collectors.toList());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(movie -> movie.getDirector().contains("Christopher Nolan")));
    }

    @Test
    public void testFilterByWatchedStatus() {
        List<MBeans> result = filterHandler.filter("haswatched==true").collect(Collectors.toList());
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(MBeans::getWatched));
    }

    @Test
    public void testFilterByMultipleCriteria() {
        List<MBeans> result = filterHandler.filter("genre==Action,released>2010").collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
    }
}
