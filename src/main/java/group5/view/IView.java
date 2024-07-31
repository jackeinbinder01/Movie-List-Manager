package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import java.util.stream.Stream;

public interface IView {

    /**
     * Set or update the record in the details pane
     *
     * @param record the record to be displayed
     */

    public void setDetailsPaneEntry(MBeans record);


    void clearListSelection();

    public void setSourceTableRecordsV2(Stream<MBeans> records, String[] watchlistNames, boolean[][] recordWatchlistMatrix);

    public void setUserTableRecords(Stream<MBeans> records, int userListId);

    public void addUserTable(String watchlistName);

    public void bindFeatures(IFeature features);

    public FilterPane getFilterPane();

    public DetailsPane getDetailsPane();

    public void display();

    public int getCurrentTab();
}
