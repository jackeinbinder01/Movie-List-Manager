package group5.controller;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import group5.model.Filter.Operations;
import group5.model.IModel;
import group5.model.MovieData;
import group5.model.beans.MBeans;
import group5.view.FilterPane;
import group5.view.IView;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
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
        model.loadWatchList("./data/samples/watchlist.json");
        model.loadWatchList("./data/samples/abc.json");
        model.loadWatchList("./data/samples/avatar.json");
        model.loadWatchList("./data/samples/lol.json");
        model.loadWatchList("./data/samples/titanic_only.json");
        // bindFeatures accept an IFeature interface, which is the controller itself
        view.bindFeatures(this);

        // setup source table records
        view.setSourceTableRecordsV2(model.getRecords(), tmpGetUserListNames(), tmpGet2DUserListForRecord());

        view.getFilterPane().setMovies(model.getRecords());

        // load user-defined list into model and view
        for (int i = 0; i < model.getUserListCount(); i++) {
            view.addUserTable(model.getUserListName(i));
            view.setUserTableRecords(model.getRecords(i), i);
        }

    }


    @Override
    public void createNewWatchList(String name) {
        String existingLists[] = this.tmpGetUserListNames();
        for (String list : existingLists) {
                if (list.equals(name)) {
                    System.out.println("[Controller] Error creating new watchlist: watchlist with name " + name + " already exists");
                    return;
                }
        }
        model.createNewWatchList(name);
        view.addUserTable(name);
        view.setUserTableRecords(model.getRecords(model.getUserListCount() - 1), model.getUserListCount() - 1);
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
        List<List<String>> filters = constructFilters();
        int currTabIdx = view.getCurrentTab();
        List<MBeans> recordList;
        if (currTabIdx == 0) {
            recordList = model.getRecords(filters).collect(Collectors.toList());
            view.setSourceTableRecordsV2(recordList.stream(), tmpGetUserListNames(), tmpGet2DUserListForRecord());
        } else {
            recordList = model.getRecords(currTabIdx - 1, filters).collect(Collectors.toList());
            view.setUserTableRecords(recordList.stream(), currTabIdx - 1);
        }
        view.getFilterPane().setMovies(recordList.stream());
        // System.out.println("[Controller] applyFilters results: " + recordList.stream().map(MBeans::getTitle).toList());
    }

    /**
     * Clear the filters in the FilterPane.
     */
    @Override
    public void clearFilters() {
        view.getFilterPane().resetFilterOptions();
        int currTabIdx = view.getCurrentTab();
        List<MBeans> recordList;
        if (currTabIdx == 0) {
            recordList = model.getRecords().collect(Collectors.toList());
            view.setSourceTableRecordsV2(recordList.stream(), tmpGetUserListNames(), tmpGet2DUserListForRecord());
        } else {
            recordList = model.getRecords(currTabIdx - 1).collect(Collectors.toList());
            view.setUserTableRecords(recordList.stream(), currTabIdx - 1);
        }
        view.getFilterPane().setMovies(recordList.stream());
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
     *
     * @param record        the MBean to be removed.
     * @param userListIndex the index in the user's watch list where the MBean is located.
     */
    public void removeFromWatchList(MBeans record, int userListIndex) {
        System.out.println("[Controller] removeFromWatchList called to remove " + record.getTitle() + " from user list index " + userListIndex);
        // Remove the record from the model
        model.removeFromWatchList(record, userListIndex);
        // Update the affected table in the view
        view.setUserTableRecords(model.getRecords(userListIndex), userListIndex);
        view.setSourceTableRecordsV2(model.getRecords(), tmpGetUserListNames(), tmpGet2DUserListForRecord());

    }

    public void addToWatchList(MBeans record, int userListIndex) {
        System.out.println("[Controller] addToWatchList called to add " + record.getTitle() + " to user list index " + userListIndex);
        model.addToWatchList(record, userListIndex);
        view.setSourceTableRecordsV2(model.getRecords(), tmpGetUserListNames(), tmpGet2DUserListForRecord());
        view.setUserTableRecords(model.getRecords(userListIndex), userListIndex);

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
            return model.getRecords();
        } else if (currentTab > 0) {
            return model.getRecords(currentTab - 1); // decrement by 1 to get the user-defined list index
        } else {
            return null;
        }
    }

    public void handleTabChange(int tabIndex) {
        System.out.println("[Controller] Handling event: tab changed to " + tabIndex + " and updating filter pane range");
        view.getFilterPane().setMovies(getRecordsForCurrentTab());
    }


    private boolean[][] tmpGet2DUserListForRecord() {
        boolean[][] result = new boolean[(int) model.getRecords().count()][model.getUserListCount()];
        for (int i = 0; i < (int) model.getRecords().count(); i++) {
            result[i] = tmpGetUserListForRecord(model.getRecords().toList().get(i));
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


    private List<List<String>> constructFilters() {
        FilterPane filterPane = view.getFilterPane();

//        String titleValue = view.getFilterPane().getFilteredTitle();
//        String contentTypeValue = view.getFilterPane().getFilteredContentType();
//        String genreValue = view.getFilterPane().getFilteredGenre();
//        String mpaRatingValue = view.getFilterPane().getFilteredMpaRating();
//        String releasedMin = view.getFilterPane().getFilteredReleasedMin();
//        String releasedMax = view.getFilterPane().getFilteredReleasedMax();
//        String imdbRatingMin = view.getFilterPane().getFilteredImdbRatingMin();
//        String imdbRatingMax = view.getFilterPane().getFilteredImdbRatingMax();
//        String boxOfficeEarningsMin = view.getFilterPane().getFilteredBoxOfficeEarningsMin();
//        String boxOfficeEarningsMax = view.getFilterPane().getFilteredBoxOfficeEarningsMax();
//        String directorValue = view.getFilterPane().getFilteredDirectorFilter();
//        String actorValue = view.getFilterPane().getFilteredActorFilter();
//        String writerValue = view.getFilterPane().getFilteredWriterFilter();
//        String languageValue = view.getFilterPane().getFilteredLanguageFilter();


        List<List<String>> filters = new ArrayList<>();
        List<Triple<String, Operations, MovieData>> triples = new ArrayList<>();
        triples.add(Triple.of(filterPane.getFilteredTitle(), Operations.CONTAINS, MovieData.TITLE));
        triples.add(Triple.of(filterPane.getFilteredReleasedMin(), Operations.GREATEROREQUAL, MovieData.RELEASED));
        triples.add(Triple.of(filterPane.getFilteredReleasedMax(), Operations.LESSOREQUAL, MovieData.RELEASED));
        triples.add(Triple.of(filterPane.getFilteredGenre(), Operations.CONTAINS, MovieData.GENRE));

        // FIXME: FilterOperation only filters on the first director in the list
        triples.add(Triple.of(filterPane.getFilteredDirectorFilter(), Operations.CONTAINS, MovieData.DIRECTOR));

        // FIXME: in MovieData.java, MPA, IMDB, and USER share the same columnTitle "ratingtype"
        // FIXME: rendering it unable to retrieve the correct MovieData type for filtering
        // triples.add(Triple.of(filterPane.getFilteredMpaRating(), Operations.EQUALS, MovieData.MPA));
        // triples.add(Triple.of(filterPane.getFilteredImdbRatingMin(), Operations.GREATEROREQUAL, MovieData.IMDB));
        // triples.add(Triple.of(filterPane.getFilteredImdbRatingMax(), Operations.LESSOREQUAL, MovieData.IMDB));

        // For FilterPane:
        // RUNTIME filter is not implemented in the view

        // For Model FilterOperation:
        // BOXOFFICE is not implemented in filtering operations
        // CONTENTTYPE is not implemented in filtering operations
        // ACTOR is not implemented in filtering operations
        // WRITER is not implemented in filtering operations
        // LANGUAGE is not implemented in filtering operations


        for (Triple<String, Operations, MovieData> triple : triples) {
            if (!triple.getLeft().isEmpty()) {
                // System.out.println("[Controller] Triple: " + triple);
                List<String> filter = new ArrayList<>();
                filter.add(null); // not sure what this is for
                filter.add(triple.getLeft());
                filter.add(triple.getMiddle().getOperator());
                filter.add(triple.getRight().getColumnTitle());
                System.out.println("[Controller] Adding filter: " + filter);
                filters.add(filter);
            }
        }
        return filters;
    }



}
