package group5.controller;

import group5.model.beans.MBeans;

public interface IFeature {

    /**
     * Prompt the view to show the details of the selected entry
     * (this is meant to be triggered by an action listener in the view upon table selection)
     */
    public void showRecordDetails(MBeans record);


    /**
     * Creates a new user-defined list
     * TODO: This will alter the model to create a new list, the model may have to create a new file depending on implementation
     * TODO: Constructor prompts the IView to create a new Tab Pane
     */
    // public void createNewList(String newListName);


    /**
     * Adds a movie to a user-defined list
     * TODO: should the movie-identifier be a MBeans or that just the movie unique ID would be enough
     */
    // public void addMovieToList(MBeans mbean, String listName);


    public void addListFromFile(String filepath);

    public void exportListToFile(String filepath);


    public void applyFilters();

    public void clearFilters();

}
