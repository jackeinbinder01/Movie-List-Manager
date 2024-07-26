package group5.controller;

import group5.model.IModel;
import group5.model.beans.MBeans;
import group5.view.IView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        model.loadWatchList("./data/samples/watchlist.json");
        model.loadWatchList("./data/samples/abc.json");
        model.loadWatchList("./data/samples/avatar.json");
        model.loadWatchList("./data/samples/lol.json");
        model.loadWatchList("./data/samples/titanic_only.json");
        // bindFeatures accept an IFeature interface, which is the controller itself
        view.bindFeatures(this);

        // setup source table records
        // view.setSourceTableRecords(model.getSourceLists());

        view.setSourceTableRecordsV2(model.getSourceLists(), tmpGetUserListNames(), tmpGet2DUserListForRecord());
//        System.out.println("tmpGet2DUserListForRecord()" + tmpGet2DUserListForRecord());
//
//
//        List<MBeans> tmpList = model.getSourceLists().toList();
//        boolean[][] userLists = tmpGet2DUserListForRecord();
//        for (int i = 0; i < tmpList.size(); i++) {
//            for (int j = 0; j < model.getUserListCount(); j++) {
//                System.out.print(tmpList.get(i).getTitle() + " inside " + model.getUserListName(j) + " = " + userLists[i][j] + " ");
//            }
//            System.out.println();
//        }

        // load user watchlists into model and view
        for (int i = 0; i < model.getUserListCount(); i++) {
            view.createUserTable(model.getUserListName(i));
            view.setUserTableRecords(model.getWatchLists(i), i);
        }



    }

    @Override
    public void exportListToFile(String filepath) {
        String currentTab = "\"SourceTable\"";
        if (view.getCurrentTab() > 0) {
            currentTab = "\"UserTable " + (view.getCurrentTab() - 1 + "\"");
        }
        JOptionPane.showMessageDialog(null, "Exporting data from " + currentTab + " to " + filepath);
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
        String titleFilter = view.getFilterPane().getFilteredTitle();
        String contentTypeFilter = view.getFilterPane().getFilteredContentType();
        String genreFilter = view.getFilterPane().getFilteredGenre();
        String mpaRatingFilter = view.getFilterPane().getFilteredMpaRating();
        String releasedMin = view.getFilterPane().getFilteredReleasedMin();
        String releasedMax = view.getFilterPane().getFilteredReleasedMax();
        String imdbRatingMin = view.getFilterPane().getFilteredImdbRatingMin();
        String imdbRatingMax = view.getFilterPane().getFilteredImdbRatingMax();
        String boxOfficeEarningsMin = view.getFilterPane().getFilteredBoxOfficeEarningsMin();
        String boxOfficeEarningsMax = view.getFilterPane().getFilteredBoxOfficeEarningsMax();
        String directorFilter = view.getFilterPane().getFilteredDirectorFilter();
        String actorFilter = view.getFilterPane().getFilteredActorFilter();
        String writerFilter = view.getFilterPane().getFilteredWriterFilter();
        String languageFilter = view.getFilterPane().getFilteredLanguageFilter();
        String countryOfOriginFilter = view.getFilterPane().getFilteredCountryOfOriginFilter();

        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'applyFilters'");
    }

    /**
     * Clear the filters in the FilterPane.
     */
    @Override
    public void clearFilters() {
        view.getFilterPane().resetFilterOptions();
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

    /**
     * Remove a record from the user's watch list.
     * The affected user table in the view will be updated.
     * @param record         the MBean to be removed.
     * @param userListIndex the index in the user's watch list where the MBean is located.
     */
    public void removeFromWatchList(MBeans record, int userListIndex) {
        System.out.println("[Controller] removeFromWatchList called to remove " + record.getTitle() + " from user list index " + userListIndex);
        // Remove the record from the model
        model.removeFromWatchList(record, userListIndex);
        // Update the affected table in the view
        view.setUserTableRecords(model.getWatchLists(userListIndex), userListIndex);
        view.setSourceTableRecordsV2(model.getSourceLists(), tmpGetUserListNames(), tmpGet2DUserListForRecord());

    }

    public void addToWatchList(MBeans record, int userListIndex) {
        System.out.println("[Controller] addToWatchList called to add " + record.getTitle() + " to user list index " + userListIndex);
        model.addToWatchList(record, userListIndex);
        view.setSourceTableRecordsV2(model.getSourceLists(), tmpGetUserListNames(), tmpGet2DUserListForRecord());
        view.setUserTableRecords(model.getWatchLists(userListIndex), userListIndex);

        //throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'addMovieToList'");
    }

    public void changeRating(MBeans record, double rating) {
        model.updateUserRating(record, rating);
        // TODO: Check if Views are correctly updated
    }

    public void changeWatchedStatus(MBeans mbean, boolean watched) {
        model.updateWatched(mbean, watched);

        // TODO: Check if Views are correctly updated
    }

    /**
     * Get the records for the currently active tab.
     *
     * @return a stream of MBeans
     */
    private Stream<MBeans> getRecordsForCurrentTab() {
        int currentTab = view.getCurrentTab();
        if (currentTab == 0) {
            return model.getSourceLists();
        } else if (currentTab > 0) {
            return model.getWatchLists(currentTab - 1); // decrement by 1 to get the user-defined list index
        } else {
            return null;
        }
    }

    public void handleTabChange(int tabIndex) {
        System.out.println("[Controller] Handling event: tab changed to " + tabIndex + " and updating filter pane range");
        view.getFilterPane().setMovies(getRecordsForCurrentTab());
    }



    private boolean[][] tmpGet2DUserListForRecord() {
        boolean[][] result = new boolean[(int)model.getSourceLists().count()][model.getUserListCount()];
        for (int i = 0; i < (int)model.getSourceLists().count(); i++) {
            result[i] = tmpGetUserListForRecord(model.getSourceLists().toList().get(i));
        }
        return result;
    }

    /**
     * Temporary method to get the user list for a record.
     * Rationale - passing down for construction for the watchlist dropbox menu
     */
    private boolean[] tmpGetUserListForRecord(MBeans record) {
        int[] indices = model.getUserListIndicesForRecord(record);
        boolean[] result = new boolean[model.getUserListCount()];
        for (int i = 0; i < model.getUserListCount(); i++) {
            result[i] = false;
        }
        for (int index : indices) {
            result[index] = true;
        }
        return result;
    }
    /**
     * Temporary method to get the user list names.
     * Rationale - passing down for construction for the watchlist dropbox menu
     */
    private String[] tmpGetUserListNames() {
        String[] result = new String[model.getUserListCount()];
        for (int i = 0; i < model.getUserListCount(); i++) {
            result[i] = model.getUserListName(i);
        }
        return result;
    }



}
