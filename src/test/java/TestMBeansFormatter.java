import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansFormatter;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;

public class TestMBeansFormatter {

    private MBeans insideOut;
    private MBeans titanic;

    @TempDir
    static Path tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String insideOutImg = "https://m.media-amazon.com/images/M/MV5BOTgxMDQwMDk0OF5BMl5BanBnXkFtZTgwNjU5OTg2NDE@._V1_SX300.jpg";
        insideOut = new MBeans("Inside Out", 2015, "movie", "PG", LocalDate.parse("19 Jun 2015", dtf), 96, List.of("Animation", "Adventure", "Comedy"),
                           List.of("Pete Docter", "Ronnie Del Carmen"), List.of("Pete Docter", "Ronnie Del Carmen", "Meg LeFauve"),
                           List.of("Amy Poehler", "Bill Hader", "Lewis Black"),
                           "After young Riley is uprooted from her Midwest life and moved to San Francisco,"
                           + " her emotions - Joy, Fear, Anger, Disgust and Sadness - conflict on how "
                           + "best to navigate a new city, house, and school.", List.of("English", "Portuguese", "Latvian"), List.of("United States"),
                           "Won 1 Oscar. 99 wins & 118 nominations total", insideOutImg, 94, 8.1, 356921711, "tt2096673", false, -1.0);

        String titanicImg = "https://m.media-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg";
        titanic = new MBeans("Titanic", 1997, "movie", "PG-13", LocalDate.parse("19 Dec 1997", dtf), 195, List.of("Drama", "Romance"),
                           List.of("James Cameron"), List.of("James Cameron"), List.of("Leonardo DiCaprio", "Kate Winslet", "Billy Zane"),
                           "A seventeen-year-old aristocrat falls in love with a kind but poor artist "
                           + "aboard the luxurious, ill-fated R.M.S. Titanic.", List.of("English", "Swedish", "Italian", "French"), List.of("United States", "Mexico"),
                           "Won 11 Oscars. 126 wins & 83 nominations total", titanicImg, 75, 7.9, 674292608, "tt0120338", false, -1.0);
    }

    @Test
    public void testWrite() throws Exception {

        Path tempCSV = tempDir.resolve("test_write.csv");
        Path tempJSON = tempDir.resolve("test_write.json");
        List<MBeans> sampleList = List.of(titanic, insideOut); // Use List to ensure order of writing.

        MBeansFormatter.writeMediasToFile(sampleList, new FileOutputStream(tempCSV.toString()), Formats.CSV);
        MBeansFormatter.writeMediasToFile(sampleList, new FileOutputStream(tempJSON.toString()), Formats.JSON);

        String expectedCSV = Files.readString(Paths.get("data/test/test_load.csv"));
        String expectedJSON = Files.readString(Paths.get("data/test/test_load.json"));
        Set<MBeans> expectedSet = Set.of(insideOut, titanic); // Set to compare reload from written file.

        String actualCSV = Files.readString(tempCSV);
        String actualJSON = Files.readString(tempJSON);
        Set<MBeans> loadedCSV = MBeansLoader.loadMediasFromFile(tempCSV.toString(), Formats.CSV);
        Set<MBeans> loadedJSON = MBeansLoader.loadMediasFromFile(tempJSON.toString(), Formats.JSON);

        assertEquals(expectedCSV, actualCSV);
        assertEquals(expectedJSON, actualJSON);
        assertEquals(expectedSet, loadedCSV);
        assertEquals(expectedSet, loadedJSON);
    }

    @Test
    public void testBadWrite() throws Exception {

        // Test various null cases
        Path tempCSV = tempDir.resolve("test_write.csv");
        Path tempJSON = tempDir.resolve("test_write.json");
        try {
            FileOutputStream fos1 = new FileOutputStream(tempCSV.toString());
            FileOutputStream fos2 = new FileOutputStream(tempJSON.toString());
            MBeansFormatter.writeMediasToFile(null, fos1, Formats.CSV);
            MBeansFormatter.writeMediasToFile(null, fos2, Formats.JSON);
            fos1.close();
            fos2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String actualCSV = Files.readString(tempCSV);
        String actualJSON = Files.readString(tempJSON);

        assertEquals("", actualCSV);
        assertEquals("", actualJSON);
    }
}
