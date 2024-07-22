import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import group5.model.beans.MBeans;

public class TestMBeans {

    private MBeans media;

    @BeforeEach
    public void setUp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        URL img = null;
        try {
            img = new URL("https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String matrixDate = "31 Mar 1999";
        media = new MBeans("The Matrix", 1999, "movie", "R", LocalDate.parse(matrixDate, dtf), 136, "Action, Sci-Fi",
                           List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                           List.of("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
                           "When a beautiful stranger leads computer hacker Neo to a forbidding underworld,"
                           + "he discovers the shocking truth--the life he knows is the elaborate deception"
                           + "of an evil cyber-intelligence.", List.of("English"), List.of("United States", "Australia"),
                           "Won 4 Oscars. 42 wins & 52 nominations total", img, 73, 8.7, "$172,076,928", "tt0133093", false, -1.0);
    }

    @Test
    public void testConstructor() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        URL img1 = null;
        URL img2 = null;
        try {
            img1 = new URL("https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg");
            img2 = new URL("https://m.media-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MBeans matrix = new MBeans("The Matrix", 1999, "movie", "R", LocalDate.parse("31 Mar 1999", dtf), 136, "Action, Sci-Fi",
        List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
        List.of("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
        "When a beautiful stranger leads computer hacker Neo to a forbidding underworld,"
        + "he discovers the shocking truth--the life he knows is the elaborate deception"
        + "of an evil cyber-intelligence.", List.of("English"), List.of("United States", "Australia"),
        "Won 4 Oscars. 42 wins & 52 nominations total", img1, 73, 8.7, "$172,076,928", "tt0133093", false, -1.0);
        assertEquals("The Matrix", media.getTitle());

        MBeans titanic = new MBeans("Titanic", 1997, "movie", "PG-13", LocalDate.parse("19 Dec 1997", dtf), 195, "Drama, Romance",
        List.of("James Cameron"), List.of("James Cameron"),
        List.of("Leonardo DiCaprio", "Kate Winslet", "Billy Zane"),
        "A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, "
        + "ill-fated R.M.S. Titanic.", List.of("English", "Swedish", "Italian", "French"), List.of("United States", "Mexico"),
        "Won 11 Oscars. 126 wins & 83 nominations total", img2, 75, 7.9, "$674,292,608", "tt0120338", false, -1.0);

        assertTrue(matrix instanceof MBeans);
        assertEquals(media, matrix);
        assertNotEquals(matrix, titanic);
        assertNotEquals(media, titanic);
    }

    @Test
    public void TestGetters() {
        assertEquals("The Matrix", media.getTitle());
        assertEquals(1999, media.getYear());
        assertEquals("movie", media.getType());
        assertEquals("R", media.getRated());
        assertEquals(LocalDate.parse("31 Mar 1999", DateTimeFormatter.ofPattern("dd MMM yyyy")), media.getReleased());
        assertEquals(136, media.getRuntime());
        assertEquals("Action, Sci-Fi", media.getGenre());
        assertEquals(List.of("Lana Wachowski", "Lilly Wachowski"), media.getDirector());
        assertEquals(List.of("Lana Wachowski", "Lilly Wachowski"), media.getWriter());
        assertEquals(List.of("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"), media.getActors());
        assertEquals("When a beautiful stranger leads computer hacker Neo to a forbidding underworld,"
                     + "he discovers the shocking truth--the life he knows is the elaborate deception"
                     + "of an evil cyber-intelligence.", media.getPlot());
        assertEquals(List.of("English"), media.getLanguage());
        assertEquals(List.of("United States", "Australia"), media.getCountry());
        assertEquals("Won 4 Oscars. 42 wins & 52 nominations total", media.getAwards());
        assertEquals("https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg", media.getPoster().toString());
        assertEquals(73, media.getMetascore());
        assertEquals(8.7, media.getImdbRating());
        assertEquals("$172,076,928", media.getBoxOffice());
        assertEquals("tt0133093", media.getID());
        assertEquals(false, media.getWatched());
        assertEquals(-1.0, media.getMyRating());
    }

    @Test
    public void TestSetters() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        URL img1 = null;
        try {
            img1 = new URL("https://m.media-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        media.setTitle("Titanic");
        media.setYear(1997);
        media.setType("movie");
        media.setRated("PG-13");
        media.setReleased(LocalDate.parse("19 Dec 1997", dtf));
        media.setRuntime(195);
        media.setGenre("Drama, Romance");
        media.setDirector(List.of("James Cameron"));
        media.setWriter(List.of("James Cameron"));
        media.setActors(List.of("Leonardo DiCaprio", "Kate Winslet", "Billy Zane"));
        media.setPlot("A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, "
                     + "ill-fated R.M.S. Titanic.");
        media.setLanguage(List.of("English", "Swedish", "Italian", "French"));
        media.setCountry(List.of("United States", "Mexico"));
        media.setAwards("Won 11 Oscars. 126 wins & 83 nominations total");
        media.setPoster(img1);
        media.setMetascore(75);
        media.setImdbRating(7.9);
        media.setBoxOffice("$674,292,608");
        media.setID("tt0120338");
        media.setWatched(true);
        media.setMyRating(10.0);

        assertEquals("Titanic", media.getTitle());
        assertEquals(1997, media.getYear());
        assertEquals("movie", media.getType());
        assertEquals("PG-13", media.getRated());
        assertEquals(LocalDate.parse("19 Dec 1997", DateTimeFormatter.ofPattern("dd MMM yyyy")), media.getReleased());
        assertEquals(195, media.getRuntime());
        assertEquals("Drama, Romance", media.getGenre());
        assertEquals(List.of("James Cameron"), media.getDirector());
        assertEquals(List.of("James Cameron"), media.getWriter());
        assertEquals(List.of("Leonardo DiCaprio", "Kate Winslet", "Billy Zane"), media.getActors());
        assertEquals("A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, "
                     + "ill-fated R.M.S. Titanic.", media.getPlot());
        assertEquals(List.of("English", "Swedish", "Italian", "French"), media.getLanguage());
        assertEquals(List.of("United States", "Mexico"), media.getCountry());
        assertEquals("Won 11 Oscars. 126 wins & 83 nominations total", media.getAwards());
        assertEquals("https://m.media-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg", media.getPoster().toString());
        assertEquals(75, media.getMetascore());
        assertEquals(7.9, media.getImdbRating());
        assertEquals("$674,292,608", media.getBoxOffice());
        assertEquals("tt0120338", media.getID());
        assertEquals(true, media.getWatched());
        assertEquals(10.0, media.getMyRating());
    }
}
