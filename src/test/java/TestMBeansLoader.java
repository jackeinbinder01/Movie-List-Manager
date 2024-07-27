import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;

public class TestMBeansLoader {

    private MBeans insideOut;
    private MBeans titanic;

    @BeforeEach
    public void setUp() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        URL insideOutImg = new URL("https://m.media-amazon.com/images/M/MV5BOTgxMDQwMDk0OF5BMl5BanBnXkFtZTgwNjU5OTg2NDE@._V1_SX300.jpg");
        insideOut = new MBeans("Inside Out", 2015, "movie", "PG", LocalDate.parse("19 Jun 2015", dtf), 96, List.of("Animation", "Adventure", "Comedy"),
                           List.of("Pete Docter", "Ronnie Del Carmen"), List.of("Pete Docter", "Ronnie Del Carmen", "Meg LeFauve"),
                           List.of("Amy Poehler", "Bill Hader", "Lewis Black"),
                           "After young Riley is uprooted from her Midwest life and moved to San Francisco,"
                           + " her emotions - Joy, Fear, Anger, Disgust and Sadness - conflict on how "
                           + "best to navigate a new city, house, and school.", List.of("English", "Portugese", "Latvian"), List.of("United States"),
                           "Won 1 Oscar. 99 wins & 118 nominations total", insideOutImg, 94, 8.1, 356921711, "tt2096673", false, -1.0);

        URL titanicImg = new URL("https://m.media-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg");
        titanic = new MBeans("Titanic", 1997, "movie", "PG-13", LocalDate.parse("19 Dec 1997", dtf), 195, List.of("Drama", "Romance"),
                           List.of("James Cameron"), List.of("James Cameron"), List.of("Leonardo DiCaprio", "Kate Winslet", "Billy Zane"),
                           "A seventeen-year-old aristocrat falls in love with a kind but poor artist "
                           + "aboard the luxurious, ill-fated R.M.S. Titanic.", List.of("English", "Swedish", "Italian", "French"), List.of("United States", "Mexico"),
                           "Won 11 Oscars. 126 wins & 83 nominations total", titanicImg, 75, 7.9, 674292608, "tt0120338", false, -1.0);
    }

    @Test
    public void testLoad() {

        Set<MBeans> expected = Set.of(insideOut, titanic);
        Set<MBeans> loadedJSON = MBeansLoader.loadMediasFromFile("./data/test/test_load.json", Formats.JSON);
        Set<MBeans> loadedCSV = MBeansLoader.loadMediasFromFile("./data/test/test_load.csv", Formats.CSV);
        Set<MBeans> loadedSample = MBeansLoader.loadMediasFromFile("./data/test/sample.json", Formats.JSON);


        assertEquals(expected, loadedCSV);
        assertEquals(expected, loadedJSON);
        assertEquals(loadedCSV, loadedJSON);
        assertNotEquals(loadedSample, loadedCSV);
        assertNotEquals(loadedSample, loadedJSON);

        MBeans loadedSingle = MBeansLoader.loadMediasFromFile("./data/test/titanic.json", Formats.JSON).iterator().next();

        // Assert each element
        assertEquals(titanic.getTitle(), loadedSingle.getTitle());
        assertEquals(titanic.getYear(), loadedSingle.getYear());
        assertEquals(titanic.getType(), loadedSingle.getType());
        assertEquals(titanic.getRated(), loadedSingle.getRated());
        assertEquals(titanic.getReleased(), loadedSingle.getReleased());
        assertEquals(titanic.getRuntime(), loadedSingle.getRuntime());
        assertEquals(titanic.getGenre(), loadedSingle.getGenre());
        assertEquals(titanic.getDirector(), loadedSingle.getDirector());
        assertEquals(titanic.getWriter(), loadedSingle.getWriter());
        assertEquals(titanic.getActors(), loadedSingle.getActors());
        assertEquals(titanic.getPlot(), loadedSingle.getPlot());
        assertEquals(titanic.getLanguage(), loadedSingle.getLanguage());
        assertEquals(titanic.getCountry(), loadedSingle.getCountry());
        assertEquals(titanic.getAwards(), loadedSingle.getAwards());
        assertEquals(titanic.getPoster(), loadedSingle.getPoster());
        assertEquals(titanic.getMetascore(), loadedSingle.getMetascore());
        assertEquals(titanic.getImdbRating(), loadedSingle.getImdbRating());
        assertEquals(titanic.getBoxOffice(), loadedSingle.getBoxOffice());
        assertEquals(titanic.getID(), loadedSingle.getID());
        assertEquals(titanic.getWatched(), loadedSingle.getWatched());
        assertEquals(titanic.getMyRating(), loadedSingle.getMyRating());
    }

    @Test
    public void testNull() {

        // Test various null cases

        Set<MBeans> loaded1 = MBeansLoader.loadMediasFromFile("./data/test/test_load.json", Formats.CSV);  // Mismatch format
        Set<MBeans> loaded2 = MBeansLoader.loadMediasFromFile("./data/test/test_bad.csv", Formats.JSON);  // Wrong file
        Set<MBeans> loaded3 = MBeansLoader.loadMediasFromFile("./data/test/sample.json", Formats.PRETTY); // Unsupported format

        assertNull(loaded1);
        assertNull(loaded2);
        assertNull(loaded3);
    }
}
