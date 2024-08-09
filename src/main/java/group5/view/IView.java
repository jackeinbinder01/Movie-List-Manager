package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import java.util.stream.Stream;

/**
 * Interface providing methods for managing and displaying data in a view.
 */
public interface IView {

    /**
     * Set or update the record in the details pane.
     *
     * @param record the record to be displayed
     */
    void setDetailsPaneEntry(MBeans record);

    /**
     * Clear any selected entry in the table.
     */
    void clearTableSelection();

    /**
     * Update records for the source table.
     *
     * @param records The stream of MBeans records in the list.
     * @param watchlistNames The array of watchlist names.
     * @param recordWatchlistMatrix The 2D boolean matrix indicating which records belong to each watchlist.
     */
    void setSourceTableRecords(Stream<MBeans> records, String[] watchlistNames, boolean[][] recordWatchlistMatrix);

    /**
     * Update records for a user watchlist.
     *
     * @param records The stream of MBeans records in the list.
     */
    void setUserTableRecords(Stream<MBeans> records, int userListId);

    /**
     * Adds a watchlist to the user table.
     *
     * @param watchlistName The name of the watchlist to add.
     */
    void addUserTable(String watchlistName);

    /**
     * Bind the features to the view.
     *
     * @param features the features to bind
     */
    void bindFeatures(IFeature features);

    /**
     * Returns the filter pane component of the view.
     *
     * @return The FilterPane object representing the filter pane.
     */
    FilterPane getFilterPane();

    /**
     * Returns the details pane component of the view.
     *
     * @return The DetailsPane object representing the details pane.
     */
    DetailsPane getDetailsPane();

    /**
     * Display the view after initialization.
     */
    void display();

    /**
     * Get the active tab index.
     *
     * @return the active tab index
     */
    int getActiveTab();

    /**
     * Set the active tab index.
     *
     * @param tabIdx the index of the tab to set as active
     */
    void setActiveTab(int tabIdx);

    /**
     * Show an alert dialog.
     *
     * @param title the title of the dialog
     * @param message the message to display
     */
    void showAlertDialog(String title, String message);
}
