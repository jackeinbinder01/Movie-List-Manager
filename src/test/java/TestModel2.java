
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import group5.model.Model;

class TestModel2 {

    private Model model;

    @BeforeEach
    void setUp() {
        model = new Model();
    }

    @Test
    void testLoadSourceData() {
        assertNotNull(model.getRecords());
        assertTrue(model.getRecords().count() > 0);
    }

    @Test
    void testCreateNewWatchList() {
        int index = model.createNewWatchList("NewList");
        assertEquals("NewList", model.getUserListName(index));
        assertEquals(1, model.getUserListCount());
    }

    @Test
    void testDeleteWatchList() {
        int index1 = model.createNewWatchList("List1");
        int index2 = model.createNewWatchList("List2");

        assertEquals(2, model.getUserListCount());

        model.deleteWatchList(index1);

        assertEquals(1, model.getUserListCount());
        assertEquals("List2", model.getUserListName(0));
    }

    @Test
    void testLoadWatchListFromFile() throws IOException {
        Path tempFile = Files.createTempFile("test_watchlist", ".json");
        Files.writeString(tempFile, "[]"); // Write an empty JSON array to the file

        int result = model.loadWatchList(tempFile.toString());

        assertEquals(0, result); // Since this is the first watchlist being loaded, it should return index 0
        assertEquals(1, model.getUserListCount());
    }
    // the tests below requires     testImplementation "org.mockito:mockito-inline:3.+"
    // it functions when testImplementation "org.mockito:mockito-core:3.+" is replaced with the above line
    // however more tests fail when the inline function is in use 

//     @Test
//     void testAddToWatchList() {
//         MBeans media = new MBeans();
//         media.setTitle("The Bee Movie");
//         // Mock the MBeansLoader to return a predefined set of MBeans
//         Set<MBeans> mockSourceList = new HashSet<>();
//         mockSourceList.add(media);
//         try (MockedStatic<MBeansLoader> mockLoader = mockStatic(MBeansLoader.class)) {
//             mockLoader.when(() -> MBeansLoader.loadMediasFromFile(anyString(), any())).thenReturn(mockSourceList);
//             model.loadSourceData(); // Load the mocked source data
//             int index = model.createNewWatchList("NewList");
//             model.addToWatchList(media, index);
//             Stream<MBeans> records = model.getRecords(index);
//             assertTrue(records.anyMatch(m -> m.getTitle().equalsIgnoreCase("The Bee Movie")));
//         }
//     }
//     @Test
//     void testFetchMBeans() {
//         // Mock MovieAPIHandler to return a predefined set of MBeans
//         MBeans mockBean = new MBeans();
//         mockBean.setTitle("The Bee Movie");
//         List<MBeans> mockBeans = List.of(mockBean); // Return a List instead of a Set
//         try (MockedStatic<MovieAPIHandler> mockApiHandler = mockStatic(MovieAPIHandler.class)) {
//             mockApiHandler.when(() -> MovieAPIHandler.getMoreSourceBeans(anyString(), anyString())).thenReturn(mockBeans);
//             Set<MBeans> fetchedBeans = model.fetchMBeans("The Bee movie", "2010", "2021");
//             assertNotNull(fetchedBeans);
//             assertTrue(fetchedBeans.contains(mockBean));
//         }
//     }
}
