package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import java.util.stream.Stream;

public interface IView {


    /**
     * Set the details pane entry
     * TODO: this should ultimately trigger the setMedia function defined in DetailsPane
     * @param mbean the MBean to set
     */
    public void setDetailsPaneEntry(MBeans mbean);

//    /**
//     * Set the list pane entries
//     * @param mbeans the MBeans to set
//     */
//    public void setSourceTableRecords(Stream<MBeans> mbeans);


    public void setSourceTableRecordsV2(Stream<MBeans> mbeans, String[] userListNames, boolean[][] userListMetadata);


    public void setUserTableRecords(Stream<MBeans> records, int userListId);



    public void createUserTable(String userListName);

    public void bindFeatures(IFeature features);


    public FilterPane getFilterPane();

    public void display();

    public int getCurrentTab();
}
