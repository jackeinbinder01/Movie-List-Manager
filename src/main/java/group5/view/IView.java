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

    /**
     * Set the list pane entries
     * TODO: confirm if this is the correct format for passing
     * @param mbeans the MBeans to set
     */
    public void setSourceTableRecords(Stream<MBeans> mbeans);


    /**
     * Set the user list pane entries
     * Prompts the view to create a new tab pane if the userListId is not found
     * @param userListId the identifier of the user list
     * @param mbeans the MBeans to set
     */
    public void setUserTableRecords(int userListId, Stream<MBeans> mbeans);


    public void createUserTable(String userListName);

    public void bindFeatures(IFeature features);


    public FilterPane getFilterPane();

    public void display();

    public int getCurrentTab();
}
