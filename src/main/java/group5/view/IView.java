package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import java.util.stream.Stream;

public interface IView {

    /**
     * Set or update the record in the details pan
     * @param record the record to be displayed
     */
    public void setDetailsPaneEntry(MBeans record);

    /**
     * Clear any selected entry in the table.
     */
    void clearTableSelection();

    public void setSourceTableRecordsV2(Stream<MBeans> records, String[] watchlistNames, boolean[][] recordWatchlistMatrix);

    public void setUserTableRecords(Stream<MBeans> records, int userListId);

    public void addUserTable(String watchlistName);

    /**
     * Bind the features to the view.
     * @param features the features to bind
     */
    public void bindFeatures(IFeature features);

    public FilterPane getFilterPane();

    public DetailsPane getDetailsPane();

    /**
     * Display the view after initialization.
     */
    public void display();

    /**
     * Get the active tab index.
     * @return the active tab index
     */
    public int getActiveTab();

    /**
     * Set the active tab index.
     * @param tabIdx the index of the tab to set as active
     */
    public void setActiveTab(int tabIdx);

    /**
     * Show an alert dialog.
     * @param title the title of the dialog
     * @param message the message to display
     */
    public void showAlertDialog(String title, String message);
}
