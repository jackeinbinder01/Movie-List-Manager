import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import group5.controller.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import group5.model.IModel;
import group5.model.beans.MBeans;
import group5.view.IView;
import group5.view.FilterPane;
import group5.view.DetailsPane;

import java.util.List;


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
//        String newWatchlistName = "New Watchlist";
//        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
//        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");
//        when(mockModel.getRecords(2)).thenReturn(Stream.empty());
//        when(mockModel.getUserListCount()).thenReturn(3);
//
//        controller.createWatchlist(newWatchlistName);
//
//        verify(mockModel).createNewWatchList(newWatchlistName);
//        verify(mockView).addUserTable(newWatchlistName);
        // verify(mockView).setUserTableRecords(any(), eq(2));
        // verify(mockModel).getRecords(eq(2));

        // Bad Case: Watchlist name already exists
//        newWatchlistName = "Existing List 1";
//        when(mockModel.getUserListCount()).thenReturn(3);
//        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
//        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");
//        when(mockModel.getRecords(2)).thenReturn(Stream.empty());
//
//        controller.createWatchlist(newWatchlistName);
//        verify(mockModel, never()).createNewWatchList(newWatchlistName);
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






}
