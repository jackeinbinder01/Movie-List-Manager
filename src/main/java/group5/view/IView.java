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
    public void setListPaneEntries(Stream<MBeans> mbeans);

    /**
     * Get the filters parameters from the FilterPane
     * TODO: add an actual return type for the filters
     * TODO: figure out a good data format to represent the filters, should I create a "Filter" helper class?
     */
    public void getFilters();


//    public void setOrCreateMainMovieListTab();
//    public void setOrCreateNewUserListTab();

    public void bindFeatures(IFeature features);

    /**
     * Clear the filters in the FilterPane
     */
    public void clearFilters();


    public void display();
}
