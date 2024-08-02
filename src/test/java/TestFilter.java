
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import group5.model.Filter.FilterHandler;
import group5.model.beans.MBeans;

public class TestFilter {

    private List<MBeans> movies;
    private FilterHandler filterHandler;
    private Stream<MBeans> moviestream;

    @BeforeEach
    public void setUp() {
        movies = new ArrayList<>();

        movies.add(new MBeans("Inception", 2010, "Movie", "PG-13",
                LocalDate.of(2010, 7, 16), 148,
                List.of("Action", "Sci-Fi"),
                List.of("Christopher Nolan"),
                List.of("Christopher Nolan"),
                List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"),
                "A thief who steals corporate secrets...",
                List.of("English"),
                List.of("USA"),
                "Won 4 Oscars.",
                "https://example.com/poster1.jpg",
                74, 8.8, 836800000,
                "tt1375666", false, -1.0));

        movies.add(new MBeans("The Matrix", 1999, "Movie", "R",
                LocalDate.of(1999, 3, 31), 136,
                List.of("Action", "Sci-Fi"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Lana Wachowski", "Lilly Wachowski"),
                List.of("Keanu Reeves", "Laurence Fishburne"),
                "A computer hacker learns from mysterious rebels...",
                List.of("English"),
                List.of("USA"),
                "Won 4 Oscars.",
                "https://example.com/poster2.jpg",
                73, 8.7, 463517383,
                "tt0133093", false, -1.0));

        movies.add(new MBeans("Interstellar", 2014, "Movie", "PG-13",
                LocalDate.of(2014, 11, 7), 169,
                List.of("Adventure", "Drama", "Sci-Fi"),
                List.of("Christopher Nolan"),
                List.of("Jonathan Nolan", "Christopher Nolan"),
                List.of("Matthew McConaughey", "Anne Hathaway"),
                "A team of explorers travel through a wormhole...",
                List.of("English"),
                List.of("USA"),
                "Won 1 Oscar.",
                "https://example.com/poster3.jpg",
                74, 8.6, 701729206,
                "tt0816692", false, -1.0));

        movies.add(new MBeans("The Social Network", 2010, "Movie", "PG-13",
                LocalDate.of(2010, 10, 1), 120,
                List.of("Biography", "Drama"),
                List.of("David Fincher"),
                List.of("Aaron Sorkin"),
                List.of("Jesse Eisenberg", "Andrew Garfield"),
                "The story of Harvard student Mark Zuckerberg...",
                List.of("English"),
                List.of("USA"),
                "Won 3 Oscars.",
                "https://example.com/poster4.jpg",
                76, 7.7, 224920315,
                "tt1285016", false, -1.0));

        movies.add(new MBeans("Pandemic", 2007, "Series", "TV-14",
                LocalDate.of(2007, 1, 25), 120,
                List.of("Drama", "Thriller"),
                List.of("Armand Mastroianni"),
                List.of("Ron McGee", "Ed Napier"),
                List.of("Tiffani Thiessen", "French Stewart"),
                "The bird flu virus spreads through Los Angeles...",
                List.of("English"),
                List.of("USA"),
                "N/A",
                "https://example.com/poster5.jpg",
                45, 5.3, -1,
                "tt0826763", true, -1.0));
        moviestream = movies.stream();
        filterHandler = new FilterHandler();
    }

    @Test
    public void testFilterByTitle() {
        List<MBeans> result = filterHandler.filter(List.of(List.of("title", "==", "Inception")), moviestream).collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    public void testFilterByYear() {
        List<MBeans> result = filterHandler.filter(List.of(List.of("released", "==", "2014")), moviestream).collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals(2014, result.get(0).getYear());
    }

    @Test
    public void testFilterByGenre() {
        List<MBeans> result = filterHandler.filter(List.of(List.of("genre", "==", "Action")), moviestream).collect(Collectors.toList());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(movie -> movie.getGenre().contains("Action")));
    }

    @Test
    public void testFilterByDirector() {
        List<MBeans> result = filterHandler.filter(List.of(List.of("director", "==", "Christopher Nolan")), moviestream).collect(Collectors.toList());
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(movie -> movie.getDirector().contains("Christopher Nolan")));
    }

    @Test
    public void testFilterByWatchedStatus() {
        List<MBeans> result = filterHandler.filter(List.of(List.of("haswatched", "==", "true")), moviestream).collect(Collectors.toList());
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(MBeans::getWatched));
    }

    @Test
    public void testFilterByMultipleCriteria() {
        List<MBeans> result = filterHandler.filter(List.of(
                List.of("genre", "==", "Action"),
                List.of("released", ">", "2009")), moviestream).collect(Collectors.toList());
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }
}
