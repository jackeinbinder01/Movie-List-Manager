package group5.controller;

import group5.model.beans.MBeans;

public interface IFeature {

    /**
     * Prompt the view to show the details of the selected entry
     * Designed to be triggered by an action listener in the view upon table selection
     */
    public void handleTableSelection(MBeans record);

    /**
     * Adds a list of MBeans from a specified file.
     * The file format and structure are assumed to be predefined and compatible with the application's requirements.
     *
     * @param filepath the path to the file containing the list of MBeans to be added.
     */
    public void importListFromFile(String filepath);

    /**
     * Exports the current list of MBeans to a specified file.
     * TODO: aad file format as parameter?
     *
     * @param filepath the path where the list of MBeans will be exported.
     */
    public void exportListToFile(String filepath);

    /**
     * Removes a specific MBean from the user's watch list at a specified index.
     * This method is intended for managing user-specific watch lists, allowing customization of tracked MBeans.
     *
     * @param mbean         the MBean to be removed.
     * @param userListIndex the index in the user's watch list where the MBean is located.
     */
    public void removeFromWatchlist(MBeans mbean, int userListIndex);

    /**
     * Adds a specific MBean to the user's watch list at a specified index.
     * This allows users to track specific MBeans of interest and receive updates or notifications.
     *
     * @param mbean         the MBean to be added to the watch list.
     * @param userListIndex the index in the user's watch list where the MBean should be added.
     */
    public void addToWatchlist(MBeans mbean, int userListIndex);

    /**
     * Creates a new watch list with the specified name.
     *
     * @param name the name of the new watch list.
     */
    public void createWatchlist(String name);

    /**
     * Deletes a watch list at the specified index.
     *
     * @param userListIndex the index of the watch list to be deleted.
     */
    public void deleteWatchlist(int userListIndex);

    /**
     * Changes the rating of a specific MBean.
     *
     * @param mbean  the MBean whose rating is to be changed.
     * @param rating the new rating to be assigned to the MBean.
     */
    public void changeRating(MBeans mbean, double rating);

    /**
     * Changes the watched status of a specific MBean.
     *
     * @param record  the MBean whose watched status is to be changed.
     * @param watched the new watched status to be assigned to the MBean.
     */
    public void changeWatchedStatusV2(MBeans record, boolean watched, String caller);

    /**
     * Applies filters to the list of MBeans.
     * This method is intended to filter the displayed list of MBeans based on certain criteria, such as rating or watched status.
     */
    public void applyFilters();

    /**
     * Clears all applied filters and refreshes the views.
     */
    public void clearFiltersAndReloadRecords();


    /**
     * Handles the change of the active tab in the view.
     *
     * @param tabIndex the index of the new active tab
     */
    public void handleTabChange(int tabIndex);


}
