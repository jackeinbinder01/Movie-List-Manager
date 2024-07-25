package group5.controller;

import group5.model.IModel;
import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;
import group5.view.IView;

import java.util.List;

/**
 * Controller class for the program.
 */
public class Controller implements IController, IFeature {
    /**
     * The model object representing the movie database.
     */
    IModel model;
    /**
     * The view object representing the user interface.
     */
    IView view;

    /**
     * Constructor for the controller.
     *
     * @param model the model object representing the movie database
     * @param view  the view object representing the user interface
     */
    public Controller(IModel model, IView view) {

        System.out.println("[Controller] Controller constructor called");
        this.model = model;
        this.view = view;

        model.loadSourceData();
        // bindFeatures accept an IFeature interface, which is the controller itself
        view.bindFeatures(this);
        view.setSourceTableRecords(model.getSourceLists());

        // String sampleDataPath = "data/samples/source.json";
        // List<MBeans> records = MBeansLoader.loadMediasFromFile(sampleDataPath, Formats.JSON);

        view.createUserTable("Anything below this is a test");
        view.setUserTableRecords(0, model.getSourceLists().toList().subList(0, 5).stream());
    }

    @Override
    public void exportListToFile(String filepath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'exportListToFile'");
    }

    @Override
    public void addListFromFile(String filepath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'addListFromFile'");
    }

    @Override
    public void showRecordDetails(MBeans record) {
        System.out.println("[Controller] showRecordDetails called");
        view.setDetailsPaneEntry(record);
    }


    @Override
    public void applyFilters() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'applyFilters'");
    }

    /**
     * Clear the filters in the FilterPane.
     */
    @Override
    public void clearFilters() {
        view.clearFilters();
        // TODO: set the tables in the view to unfiltered
    }


    /**
     * Main entry point for the controller.
     */
    @Override
    public void go() {
        System.out.println("[Controller] Controller.go() called");

        view.display();
    }


    public void removeFromWatchList(MBeans mbean, int userListIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'removeMovieFromList'");
        // model.removeFromWatchList(mbean, userListIndex);

    }

    public void addToWatchList(MBeans mbean, int userListIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'addMovieToList'");
        // model.addToWatchList(mbean, userListIndex);
    }

    public void changeRating(MBeans mbean, double rating) {
        model.updateUserRating(mbean, rating);
        // TODO: Check if Views are correctly updated
    }

    public void changeWatchedStatus(MBeans mbean, boolean watched) {
        model.updateWatched(mbean, watched);

        // TODO: Check if Views are correctly updated
    }

}
