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
import group5.model.MovieData;
import group5.model.beans.MBeans;
import group5.view.IView;
import group5.view.FilterPane;
import group5.view.DetailsPane;

import java.util.Arrays;
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
        when(mockModel.getUserListCount()).thenReturn(2);
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");
        when(mockModel.getRecords(2)).thenReturn(Stream.empty());

        controller.createWatchlist(newWatchlistName);

        verify(mockModel).createNewWatchList(newWatchlistName);
        verify(mockView).addUserTable(newWatchlistName);
        verify(mockView).setUserTableRecords(any(), eq(2));
        verify(mockModel).getRecords(eq(2));

        // Bad Case: Watchlist name already exists
        newWatchlistName = "Existing List 1";
        when(mockModel.getUserListCount()).thenReturn(2);
        when(mockModel.getUserListName(0)).thenReturn("Existing List 1");
        when(mockModel.getUserListName(1)).thenReturn("Existing List 2");
        when(mockModel.getRecords(2)).thenReturn(Stream.empty());

        controller.createWatchlist(newWatchlistName);
        verify(mockModel, never()).createNewWatchList(newWatchlistName);
    }

    @Test
    public void testDeleteWatchlist() {

    }


    @Test
    public void testChangeWatchedStatus() {
        List<MBeans> records = TestData.getTestDataSet1().toList();
        MBeans record = records.get(0);

        // Active Tab: Source Table
        // Caller: DetailsPane
        // Modified Record in DetailsPane?: Yes
        when(mockView.getCurrentTab()).thenReturn(0);
        when(mockModel.getRecords()).thenReturn(records.stream());
        reset(mockView);
        controller.changeWatchedStatusV2(record, true, "DETAILSPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, times(1)).setSourceTableRecordsV2(any(), any(), any());


        // Active Tab: Watchlist Table
        // Caller: ListPane
        // Modified Record in DetailsPane?: Yes
        reset(mockView, mockModel);
        when(mockView.getCurrentTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(record);
        controller.changeWatchedStatusV2(record, false, "LISTPANE");
        verify(mockModel).updateWatched(record, false);
        verify(mockView, times(1)).setDetailsPaneEntry(record);


        // Active Tab: Watchlist Table
        // Caller: DetailsPane
        // Modified Record in DetailsPane?: Yes
        reset(mockView, mockModel);
        when(mockView.getCurrentTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(record);
        controller.changeWatchedStatusV2(record, true, "DETAILSPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, times(1)).setUserTableRecords(any(), eq(0));


        // Active Tab: Source Table
        // Caller: ListPane
        // Modified Record in DetailsPane?: No
        reset(mockView, mockModel);
        when(mockView.getCurrentTab()).thenReturn(1); // active tab = watchlist table
        when(mockModel.getRecords(0)).thenReturn(records.stream());
        when(mockView.getDetailsPane()).thenReturn(mockDetailsPane);
        when(mockDetailsPane.getCurrentMedia()).thenReturn(records.get(2));
        controller.changeWatchedStatusV2(record, true, "LISTPANE");
        verify(mockModel).updateWatched(record, true);
        verify(mockView, never()).setDetailsPaneEntry(any());

    }




}
