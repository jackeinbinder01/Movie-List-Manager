import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import group5.controller.Controller;
import group5.model.MovieData;
import group5.model.filter.Operations;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import group5.model.IModel;
import group5.model.beans.MBeans;
import group5.view.IView;
import group5.view.FilterPane;
import group5.view.DetailsPane;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.stream.Stream;


public class TestController {
    @Mock
    private IModel mockModel;

    @Mock
    private IView mockView;

    @Mock
    private FilterPane mockFilterPane;

    @Mock
    private DetailsPane mockDetailsPane; // Create a mock for DetailsPane

    private Controller controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        controller = new Controller(mockModel, mockView);
    }

    @Test
    public void testCreateWatchlist() {
        // Normal case
        String newWatchlistName = "New Watchlist";
        // There are 2 existing watchlists initially
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");
        when(mockModel.getUserListCount()).thenReturn(2);

        when(mockModel.createNewWatchList(any())).thenReturn(2);

        controller.createWatchlist(newWatchlistName);

        verify(mockModel).createNewWatchList(newWatchlistName);
        verify(mockView).addUserTable(newWatchlistName);

        // Bad Case: Watchlist name already exists
        reset(mockModel, mockView);
        newWatchlistName = "Existing List 1";
        when(mockModel.getUserListCount()).thenReturn(2);
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");

        controller.createWatchlist(newWatchlistName);
        verify(mockModel, never()).createNewWatchList(newWatchlistName);
    }


    @Test
    public void testDeleteWatchlist() {
        // Normal case
        when(mockModel.getUserListCount()).thenReturn(2);
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");

        controller.deleteWatchlist(0);

        verify(mockModel).deleteWatchList(0);
        verify(mockView).setActiveTab(0);

        // Bad Case: Watchlist index out of bounds
        reset(mockModel, mockView);
        when(mockModel.getUserListCount()).thenReturn(2);
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");

        controller.deleteWatchlist(2);
        verify(mockModel, never()).deleteWatchList(2);
    }


    @Test
    public void testExportListToFile() {
        // Current Table: Source Table - exporting is not allowed
        reset(mockView, mockModel);
        when(mockView.getActiveTab()).thenReturn(0);
        controller.exportListToFile("usr/bin/test.csv");
        verify(mockModel, never()).saveWatchList(eq("usr/bin/test.csv"), eq(0)); // TODO: because the functionality is not implemented yet

        // Current Table: Watchlist Table
        reset(mockView, mockModel);
        when(mockView.getActiveTab()).thenReturn(10);
        controller.exportListToFile("usr/bin/test.csv");
        verify(mockModel).saveWatchList(eq("usr/bin/test.csv"), eq(9));
    }

    @Test
    public void testChangeRating() {
        List<MBeans> records = TestData.getTestDataSet1().toList();
        MBeans record = records.get(0);
        controller.changeRating(record, 5);
        verify(mockModel).updateUserRating(record, 5);
    }

    @Test
    public void testChangeWatchedStatus() {
        List<MBeans> records = TestData.getTestDataSet1().toList();
        MBeans record = records.get(0);

        // Active Tab: Source Table
        // Caller: DetailsPane
        // Modified Record in DetailsPane?: Yes
        when(mockView.getActiveTab()).thenReturn(0);
        when(mockModel.getRecords()).thenReturn(records.stream());
        reset(mockView);
        controller.changeWatchedStatus(record, true, "DETAILSPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, times(1)).setSourceTableRecords(any(), any(), any());


        // Active Tab: Watchlist Table
        // Caller: ListPane
        // Modified Record in DetailsPane?: Yes
        reset(mockView, mockModel);
        when(mockView.getActiveTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(record);
        controller.changeWatchedStatus(record, false, "LISTPANE");
        verify(mockModel).updateWatched(record, false);
        verify(mockView, times(1)).setDetailsPaneEntry(record);


        // Active Tab: Watchlist Table
        // Caller: DetailsPane
        // Modified Record in DetailsPane?: Yes
        reset(mockView, mockModel);
        when(mockView.getActiveTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(record);
        controller.changeWatchedStatus(record, true, "DETAILSPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, times(1)).setUserTableRecords(any(), eq(0));


        // Active Tab: Source Table
        // Caller: ListPane
        // Modified Record in DetailsPane?: No
        reset(mockView, mockModel);
        when(mockView.getActiveTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(records.get(2));
        controller.changeWatchedStatus(record, true, "LISTPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, never()).setDetailsPaneEntry(any());

    }

    @Test
    void testImportListFromFile() {

        String filename = "usr/bin/test.csv";

        // Importing a correct file, and this will be the first user watchlist
        reset(mockView, mockModel);
        when(mockModel.loadWatchList(filename)).thenReturn(0);
        when(mockModel.getUserListName(0)).thenReturn("Watchlist 01");

        controller.importListFromFile("usr/bin/test.csv");

        verify(mockView).addUserTable("Watchlist 01");
        verify(mockView).setUserTableRecords(any(), eq(0));
        verify(mockView).setSourceTableRecords(any(), any(), any());
        verify(mockView).setActiveTab(1);


        // Importing an incorrect file
        reset(mockView, mockModel);
        when(mockModel.loadWatchList(filename)).thenReturn(-1);

        controller.importListFromFile("usr/bin/test.csv");

        verify(mockView).showAlertDialog(any(), any());
        verify(mockView, never()).addUserTable("Watchlist 01");
        verify(mockView, never()).setUserTableRecords(any(), eq(0));
        verify(mockView, never()).setUserTableRecords(any(), eq(-1));

    }

    @Test
    void testHandleTableSelection() {
        List<MBeans> records = TestData.getTestDataSet1().toList();
        MBeans record = records.get(0);

        controller.handleTableSelection(record);
        verify(mockView).setDetailsPaneEntry(record);

        reset(mockView);

        controller.handleTableSelection(null);
        verify(mockView, never()).setDetailsPaneEntry(any());
    }


    @Test
    void testHandleTabChange() {
        controller.handleTabChange(0);
        verify(mockView).clearTableSelection();
    }

    @Test
    void testClearFiltersAndReloadRecords() {
        reset(mockView, mockModel, mockFilterPane);
        // Active Tab: Source Table
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        when(mockView.getActiveTab()).thenReturn(0);
        controller.clearFiltersAndReloadRecords();
        verify(mockModel).clearFilter();
        verify(mockView).setSourceTableRecords(any(), any(), any());
        verify(mockView, never()).setUserTableRecords(any(), anyInt());
        verify(mockFilterPane).setMovies(any(), eq(true));

        // Active Tab: User Watchlist Table
        reset(mockView, mockModel, mockFilterPane);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        when(mockView.getActiveTab()).thenReturn(1);
        controller.clearFiltersAndReloadRecords();
        verify(mockModel).clearFilter();
        verify(mockView, never()).setSourceTableRecords(any(), any(), any());
        verify(mockView).setUserTableRecords(any(), eq(0));
        verify(mockFilterPane).setMovies(any());
    }



    @Test
    void testAddToWatchlist() {
        reset(mockModel, mockView);
        MBeans record = TestData.getTestDataSet1().toList().get(0);
        controller.addToWatchlist(record, 0);
        verify(mockModel).addToWatchList(record, 0);
        verify(mockView).setSourceTableRecords(any(), any(), any());
    }

    @Test
    void testRemoveFromWatchlist() {
        MBeans record = TestData.getTestDataSet1().toList().get(0);

        // 1. Remove entry via Source List
        reset(mockModel, mockView, mockFilterPane);
        when(mockView.getActiveTab()).thenReturn(0);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        controller.removeFromWatchlist(record, 1);
        verify(mockModel).removeFromWatchList(record, 1);
        verify(mockModel, never()).clearFilter();
        verify(mockFilterPane, never()).setMovies(any(), anyBoolean());
        verify(mockView).setSourceTableRecords(any(), any(), any());

        // 2. Remove entry via Watchlist
        reset(mockModel, mockView, mockFilterPane);
        when(mockModel.getRecords(anyInt())).thenReturn(TestData.getTestDataSet1());
        when(mockView.getActiveTab()).thenReturn(1);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        controller.removeFromWatchlist(record, 1);
        verify(mockModel).removeFromWatchList(record, 1);
        verify(mockModel, never()).clearFilter();
        verify(mockFilterPane).setMovies(any(), eq(false));
        verify(mockView).setUserTableRecords(any(), eq(1));


        // 3. Remove entry via Watchlist while filtered, and no records left in the filtered view
        reset(mockModel, mockView, mockFilterPane);
        when(mockModel.getRecords(anyInt())).thenReturn(Stream.empty());
        when(mockView.getActiveTab()).thenReturn(1);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        controller.removeFromWatchlist(record, 1);
        verify(mockModel).removeFromWatchList(record, 1);
        verify(mockModel).clearFilter();
        verify(mockFilterPane).clearFilterOptions();
        verify(mockFilterPane).setMovies(any(), eq(false));
        verify(mockView).setUserTableRecords(any(), eq(1));

    }

    @Test
    void testApplyFilters() {
        // 1. Active Tab: Source Table
        reset(mockModel, mockView, mockFilterPane);
        when(mockView.getActiveTab()).thenReturn(0);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        when(mockFilterPane.getFilteredTitle()).thenReturn("Title");
        when(mockFilterPane.getFilteredGenre()).thenReturn("Genre");
        when(mockFilterPane.getFilteredMpaRating()).thenReturn("PG-13");
        when(mockFilterPane.getFilteredReleasedMin()).thenReturn("2000");
        when(mockFilterPane.getFilteredReleasedMax()).thenReturn("2020");
        when(mockFilterPane.getFilteredImdbRatingMin()).thenReturn("5.0");
        when(mockFilterPane.getFilteredImdbRatingMax()).thenReturn("10.0");
        when(mockFilterPane.getFilteredBoxOfficeEarningsMin()).thenReturn("1000");
        when(mockFilterPane.getFilteredBoxOfficeEarningsMax()).thenReturn("100000");
        when(mockFilterPane.getFilteredDirectorFilter()).thenReturn("Director");
        when(mockFilterPane.getFilteredActorFilter()).thenReturn("Actor");
        when(mockFilterPane.getFilteredWriterFilter()).thenReturn("Writer");
        when(mockFilterPane.getFilteredLanguageFilter()).thenReturn("Language");

        controller.applyFilters();
        verify(mockView).clearTableSelection();
        verify(mockModel).addNewMBeans(any(), any());
        verify(mockModel).getRecords(any());
        verify(mockView).setSourceTableRecords(any(), any(), any());
        verify(mockView, never()).setUserTableRecords(any(), anyInt());
        verify(mockFilterPane).setMovies(any(), eq(true));

        // 2. Active Tab: User Watchlist Table
        reset(mockModel, mockView, mockFilterPane);
        when(mockView.getActiveTab()).thenReturn(2);
        when(mockView.getFilterPane()).thenReturn(mockFilterPane);
        when(mockFilterPane.getFilteredTitle()).thenReturn("Title");
        when(mockFilterPane.getFilteredGenre()).thenReturn("Genre");
        when(mockFilterPane.getFilteredMpaRating()).thenReturn("PG-13");
        when(mockFilterPane.getFilteredReleasedMin()).thenReturn("2000");
        when(mockFilterPane.getFilteredReleasedMax()).thenReturn("2020");
        when(mockFilterPane.getFilteredImdbRatingMin()).thenReturn("5.0");
        when(mockFilterPane.getFilteredImdbRatingMax()).thenReturn("10.0");
        when(mockFilterPane.getFilteredBoxOfficeEarningsMin()).thenReturn("1000");
        when(mockFilterPane.getFilteredBoxOfficeEarningsMax()).thenReturn("100000");
        when(mockFilterPane.getFilteredDirectorFilter()).thenReturn("Director");
        when(mockFilterPane.getFilteredActorFilter()).thenReturn("Actor");
        when(mockFilterPane.getFilteredWriterFilter()).thenReturn("Writer");
        when(mockFilterPane.getFilteredLanguageFilter()).thenReturn("Language");

        controller.applyFilters();
        verify(mockView).clearTableSelection();
        verify(mockModel, never()).addNewMBeans(any(), any());
        verify(mockModel).getRecords(eq(2-1), any());
        verify(mockView, never()).setSourceTableRecords(any(), any(), any());
        verify(mockView).setUserTableRecords(any(), eq(2-1));
        verify(mockFilterPane, never()).setMovies(any(), anyBoolean());
    }


    @Test
    void testGo() {
        controller.go();
        verify(mockView).display();
    }

}
