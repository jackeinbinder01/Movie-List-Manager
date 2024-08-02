import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.io.FileOutputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import group5.model.beans.MBeans;
import group5.model.IModel;
import group5.model.Model;
import group5.model.IMovieList;
import group5.model.MovieList;
import group5.model.Filter.IFilterHandler;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansLoader;
import group5.model.net.apiFunctionality.MovieAPIHandler;

public class TestModel {

    private IModel model;

    @TempDir
    static Path tempDir;

    @BeforeEach
    public void setUp() {
        model = new Model();
    }

    @Test
    public void testSourceData() {
        // Test data init from model constructor
        Set<MBeans> expected = MBeansLoader.loadMediasFromFile(IModel.DEFAULT_DATA, Formats.JSON);

        assertEquals(expected, model.getRecords().collect(Collectors.toSet()));
    }

    @Test
    public void testWatchLists() {
        Path tempSave = tempDir.resolve("test_save.json");
        // Assert empty watchlist upon creation
        assertEquals(0, model.getUserListCount());
        // Test empty watch list creation
        int watchList01 = model.createNewWatchList("Test Watch List");
        assertTrue(watchList01 == 0);
        assertEquals("Test Watch List", model.getUserListName(watchList01));
        assertTrue(model.getRecords(watchList01).count() == 0);

        // Test non empty watch list loading
        int watchList02 = model.loadWatchList("./data/test/platoon.json");
        Set<MBeans> expected = MBeansLoader.loadMediasFromFile("./data/test/platoon.json", Formats.JSON);

        assertTrue(watchList02 == 1);
        assertEquals("platoon", model.getUserListName(watchList02));
        assertEquals(expected, model.getRecords(watchList02).collect(Collectors.toSet()));

        assertEquals(2, model.getUserListCount());

        // Save watchlist
        model.saveWatchList(tempSave.toString(), watchList02);
        assertEquals(expected, MBeansLoader.loadMediasFromFile(tempSave.toString(), Formats.JSON));
        // Delete watchlist
        model.deleteWatchList(watchList02);
        assertTrue(model.getUserListCount() == 1);

        // Clean up
        File file01 = new File("./data/watchlist/Test Watch List.json");
        File file02 = new File("./data/watchlist/platoon.json");
        file01.delete();
        file02.delete();

    }

    @Test
    public void testGetFiltered() {
        int watchList01 = model.loadWatchList("./data/test/platoon.json");
        int watchList02 = model.loadWatchList("./data/test/test_load.json");

        List<List<String>> filters01 = List.of(List.of("title", "~", "platoon"));
        Set<MBeans> expected1 = model.getRecords(watchList01).collect(Collectors.toSet());
        assertEquals(expected1, model.getRecords(filters01).collect(Collectors.toSet()));

        List<List<String>> filters02 = List.of(List.of("title", "~", "titanic"));
        Set<MBeans> expected2 = model.getRecords(watchList02, filters02).collect(Collectors.toSet());
        assertEquals(expected2, model.getRecords().collect(Collectors.toSet()));

        // Clean up
        File file01 = new File("./data/watchlist/platoon.json");
        File file02 = new File("./data/watchlist/test_load.json");
        file01.delete();
        file02.delete();

    }

    @Test
    public void addRemoveItem() {
        int watchList01 = model.loadWatchList("./data/test/platoon.json");
        int watchList02 = model.loadWatchList("./data/test/test_load.json");

        MBeans sHolmes = model.getRecords(List.of(List.of("title", "~", "Sherlock Holmes"))).iterator().next();
        MBeans titanic = model.getRecords(List.of(List.of("title", "~", "titanic"))).iterator().next();
        model.clearFilter(); // Clear Filter
        Set<MBeans> expected1 = model.getRecords(watchList01).collect(Collectors.toSet());
        Set<MBeans> expected2 = model.getRecords(watchList02).collect(Collectors.toSet());
        expected1.add(sHolmes);
        expected2.add(sHolmes);
        expected2.remove(titanic);

        model.addToWatchList(sHolmes, watchList01);
        model.addToWatchList(sHolmes, watchList02);
        model.removeFromWatchList(titanic, watchList02);

        assertEquals(expected1, model.getRecords(watchList01).collect(Collectors.toSet()));
        assertEquals(expected2, model.getRecords(watchList02).collect(Collectors.toSet()));

        // Test changes reflect onto file
        assertEquals(expected1, MBeansLoader.loadMediasFromFile("./data/watchlist/platoon.json", Formats.JSON));
        assertEquals(expected2, MBeansLoader.loadMediasFromFile("./data/watchlist/test_load.json", Formats.JSON));

        // Clean up
        File file01 = new File("./data/watchlist/platoon.json");
        File file02 = new File("./data/watchlist/test_load.json");
        file01.delete();
        file02.delete();
    }

    @Test
    public void testExtractFilter() {
        List<List<String>> filters01 = List.of(List.of("title", "~", "platoon"), List.of("released", "==", "2000"), List.of("released", "=", "2005"));
        List<List<String>> filters02 = List.of(List.of("title", "~", "holmes"), List.of("released", "==", "1800"),
                                               List.of("genre", "=", "action"), List.of("director", "=", "me"),
                                               List.of("actor", "=", "you"), List.of("released", "=", "2024"));

        Map<String, String> mapFilter01 = model.extractFilterValues(filters01);
        Map<String, String> mapFilter02 = model.extractFilterValues(filters02);
        assertEquals("platoon", mapFilter01.get("title"));
        assertEquals("2000", mapFilter01.get("year1"));
        assertEquals("2005", mapFilter01.get("year2"));
        assertEquals("holmes", mapFilter02.get("title"));
        assertEquals("1800", mapFilter02.get("year1"));
        assertEquals("2024", mapFilter02.get("year2"));
    }

    @Test
    public void testFetchandAddFromAPI() {
        Set<MBeans> source = model.getRecords().collect(Collectors.toSet());
        MBeans aot = MovieAPIHandler.getMovie("tt22408766");
        MBeans aotc = MovieAPIHandler.getMovie("tt12415546");
        source.add(aot);
        source.add(aotc);
        model.addNewMBeans(List.of(List.of("title", "~", "titan"), List.of("released", "==", "2020"), List.of("released", "=", "2022")), null);
        assertEquals(source, model.getRecords().collect(Collectors.toSet()));

        // Clean up
        File file01 = new File("./data/source/source.json");
        file01.delete();
        // Reload backup un-altered version of source into folder
        model.loadSourceData();
    }

    @Test
    public void testUpdate() {
        MBeans platoon = MBeansLoader.loadMediasFromFile("./data/test/platoon.json", Formats.JSON).iterator().next(); // Get platoon movie from file
        int watchList01 = model.loadWatchList("./data/test/platoon.json");  // Use this as reference to check changes.

        model.updateUserRating(platoon, 9.9);
        model.updateWatched(platoon, true);
        MBeans appPlatoon = model.getRecords(watchList01).collect(Collectors.toSet()).iterator().next();
        assertEquals(9.9, appPlatoon.getMyRating());
        assertEquals(true, appPlatoon.getWatched());

        // Clean up
        File file01 = new File("./data/source/source.json");
        File file02 = new File("./data/watchlist/platoon.json");
        file01.delete();
        file02.delete();
        // Reload backup un-altered version of source into folder
        model.loadSourceData();

    }
}
