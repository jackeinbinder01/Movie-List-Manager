
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import group5.model.MovieList;
import group5.model.IMovieList;
import group5.model.beans.MBeans;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansLoader;

public class TestMovieList {

    private IMovieList emptyList;
    private IMovieList sampleList;
    private Set<MBeans> sampleMovieList;
    private MBeans matrix;
    private MBeans titanic;

    @TempDir
    static Path tempDir;

    @BeforeEach
    public void setUp() {
        sampleMovieList = MBeansLoader.loadMediasFromFile("./data/test/sample.json", Formats.JSON);
        emptyList = new MovieList("Empty List");
        sampleList = new MovieList("My List 01", sampleMovieList);
        matrix = MBeansLoader.loadMediasFromFile("./data/test/matrix.json", Formats.JSON).iterator().next();
        titanic = MBeansLoader.loadMediasFromFile("./data/test/titanic.json", Formats.JSON).iterator().next();
    }

    @Test
    public void testConstructorGetters() {
        Set<MBeans> sample = MBeansLoader.loadMediasFromFile("./data/test/sample.json", Formats.JSON);
        IMovieList list1 = new MovieList("My List 01");  // Empty
        IMovieList list2 = new MovieList("My List 02", sample);  // With movies

        // Assert List Name
        assertEquals("My List 01", list1.getListName());
        assertEquals("My List 02", list2.getListName());

        // Assert List movies
        assertTrue(list1.getMovieList().collect(Collectors.toSet()).isEmpty());
        assertEquals(sampleMovieList, list2.getMovieList().collect(Collectors.toSet()));
    }

    @Test
    public void testCount() {
        assertEquals(0, emptyList.count());
        assertEquals(2, sampleList.count());
    }

    @Test
    public void testContainsMedia() {
        assertTrue(sampleList.containsMedia(titanic));
        assertFalse(sampleList.containsMedia(matrix));
        assertFalse(emptyList.containsMedia(matrix));
        assertFalse(emptyList.containsMedia(titanic));
    }

    @Test
    public void testAddRemoveClear() {
        emptyList.addToList(matrix);
        emptyList.addToList(titanic);
        sampleList.addToList(matrix);
        sampleList.addToList(titanic);  // Won't add - duplicate

        assertEquals(2, emptyList.count());
        assertEquals(3, sampleList.count());  // only added by 1

        emptyList.removeFromList(matrix);
        sampleList.removeFromList(titanic);
        assertEquals(1, emptyList.count());
        assertEquals(2, sampleList.count());  // only added by 1
        assertFalse(emptyList.containsMedia(matrix));
        assertFalse(sampleList.containsMedia(titanic));

        sampleList.clear();
        assertEquals(0, sampleList.count());
        assertFalse(sampleList.containsMedia(matrix));
        assertFalse(sampleList.containsMedia(titanic));
    }

    @Test
    public void testSaveList() throws Exception{
        Path temp1 = tempDir.resolve("saved_list1.json");
        Path temp2 = tempDir.resolve("saved_list2.json");

        MBeans exodus = MBeansLoader.loadMediasFromFile("./data/test/exodus.json", Formats.JSON).iterator().next();

        emptyList.addToList(exodus);
        emptyList.addToList(titanic);
        emptyList.addToList(matrix);
        sampleList.addToList(matrix);

        emptyList.savemovie(temp1.toString(), Formats.JSON);
        sampleList.savemovie(temp2.toString(), Formats.JSON);

        Set<MBeans> loaded1 = MBeansLoader.loadMediasFromFile(temp1.toString(), Formats.JSON);
        Set<MBeans> loaded2 = MBeansLoader.loadMediasFromFile(temp2.toString(), Formats.JSON);
        assertEquals(loaded1, loaded2);
    }
}
