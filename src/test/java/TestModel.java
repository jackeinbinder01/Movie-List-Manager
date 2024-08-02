import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class TestModel {

    private IModel model;

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

        // TODO: delete watchlist
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

        //TODO test change reflect on file


    }
}
