package group5.controller;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        view.setSourceTableRecordsV2(model.getRecords(), getUserListNames(), getRecordUserListMatrix());

        // no filters applied on initialization
        view.getFilterPane().setMovies(model.getRecords());

        // load user-defined list into model and view
        for (int i = 0; i < model.getUserListCount(); i++) {
            view.addUserTable(model.getUserListName(i));
            view.setUserTableRecords(model.getRecords(i), i);
        }

    }


    @Override
    public void deleteWatchlist(int userListIndex) {
        System.out.println("[Controller] deleteWatchlist called to delete user list index " + userListIndex);
    }

    @Override
    public void createWatchlist(String name) {
        String existingLists[] = this.getUserListNames();
        if (Arrays.stream(existingLists).anyMatch(list -> list.equals(name))) {
            System.out.println("[Controller] Error creating new watchlist: \"" + name + "\" already exists");
            return;
        }
        model.createNewWatchList(name);
        int newListIdx = model.getUserListCount() - 1; // the new list would be the last one
        view.addUserTable(name);
        view.setUserTableRecords(model.getRecords(newListIdx), newListIdx);
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
    public void importListFromFile(String filepath) {
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
        List<List<String>> filters = getFilterOptions();
        int currTabIdx = view.getCurrentTab();
        List<MBeans> recordList;
        if (currTabIdx == 0) {
            recordList = model.getRecords(filters).collect(Collectors.toList());
            view.setSourceTableRecordsV2(recordList.stream(), getUserListNames(), getRecordUserListMatrix());
        } else {
            recordList = model.getRecords(currTabIdx - 1, filters).collect(Collectors.toList());
            view.setUserTableRecords(recordList.stream(), currTabIdx - 1);
        }
    }

    /**
     * Clear the filters in the FilterPane.
     */
    @Override
    public void clearFilters() {
        view.getFilterPane().resetFilterOptions();
        view.getFilterPane().clearFilterOptions();
        int currTabIdx = view.getCurrentTab();
        List<MBeans> recordList;
        if (currTabIdx == 0) {
            recordList = model.getRecords().collect(Collectors.toList());
            view.setSourceTableRecordsV2(recordList.stream(), getUserListNames(), getRecordUserListMatrix());
        } else {
            recordList = model.getRecords(currTabIdx - 1).collect(Collectors.toList());
            view.setUserTableRecords(recordList.stream(), currTabIdx - 1);
        }
        view.getFilterPane().setMovies(recordList.stream());
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
     *
     * @param record        the MBean to be removed.
     * @param userListIndex the index in the user's watch list where the MBean is located.
     */
    public void removeFromWatchList(MBeans record, int userListIndex) {
        System.out.println("[Controller] removeFromWatchList called to remove " + record.getTitle() + " from user list index " + userListIndex);
        model.removeFromWatchList(record, userListIndex);
        view.setUserTableRecords(model.getRecords(userListIndex, getFilterOptions()), userListIndex);
        view.setSourceTableRecordsV2(model.getRecords(getFilterOptions()), getUserListNames(), getRecordUserListMatrix());
        // Update the filter pane if the current tab is the affected user list
        if (view.getCurrentTab() -1 == userListIndex) {
            view.getFilterPane().setMovies(model.getRecords(userListIndex, getFilterOptions()));
        }
    }

    public void addToWatchlist(MBeans record, int userListIndex) {
        System.out.println("[Controller] addToWatchList called to add " + record.getTitle() + " to user list index " + userListIndex);
        model.addToWatchList(record, userListIndex);
        view.setSourceTableRecordsV2(model.getRecords(getFilterOptions()), getUserListNames(), getRecordUserListMatrix());
        view.setUserTableRecords(model.getRecords(userListIndex, getFilterOptions()), userListIndex);
        // Since adding to a list is done from the source tab only, there is no need to update the filter pane
    }

    public void changeRating(MBeans record, double rating) {
        model.updateUserRating(record, rating);
        // TODO: Check if Views are correctly updated
    }

    public void changeWatchedStatus(MBeans mbean, boolean watched) {
        model.updateWatched(mbean, watched);
        // TODO: Check if Views are correctly updated
    }



    public void handleTabChange(int tabIndex) {
        System.out.println("[Controller] Handling event: tab changed to " + tabIndex + " and updating filter pane range");
        clearFilters();
//        List<MBeans> recordList;
//
//        view.getFilterPane().resetFilterOptions();
//        view.getFilterPane().clearFilterOptions();
//        int currentTab = view.getCurrentTab();
//        if (currentTab == 0) {
//            recordList = model.getRecords().toList();
//            view.setSourceTableRecordsV2(recordList.stream(), getUserListNames(), getRecordUserListMatrix());
//        } else {
//            recordList = model.getRecords(currentTab - 1).toList(); // decrement by 1 to get the user-defined list index
//            view.setUserTableRecords(recordList.stream(), currentTab - 1);
//        }
//        // no filters applied on tab change because the filter pane is reset
//        view.getFilterPane().setMovies(recordList.stream());
    }


    /**
     * Private helper method to get a boolean matrix representing the user lists for each record.
     * @return a 2D boolean array where each row represents a record and each column represents a user list
     */
    private boolean[][] getRecordUserListMatrix() {
        return model.getRecords()
                .map(record -> {
                    int[] indices = model.getUserListIndicesForRecord(record);
                    boolean[] result = new boolean[model.getUserListCount()];
                    for (int i = 0; i < model.getUserListCount(); i++) {
                        result[i] = Arrays.binarySearch(indices, i) >= 0;
                    }
                    return result;
                })
                .toArray(boolean[][]::new);
    }


    /**
     * Method to get the names of the user lists.
     * Created to assist in constructing the watchlist dropbox menu.
     *
     * @return an array of strings representing the names of the user lists
     */
    private String[] getUserListNames() {
        return IntStream.range(0, model.getUserListCount())
                .mapToObj(model::getUserListName)
                .toArray(String[]::new);
    }




    private List<List<String>> getFilterOptions() {
        FilterPane filterPane = view.getFilterPane();

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
        triples.add(Triple.of(filterPane.getFilteredImdbRatingMin(), Operations.GREATEROREQUAL, MovieData.IMDB));
        triples.add(Triple.of(filterPane.getFilteredImdbRatingMax(), Operations.LESSOREQUAL, MovieData.IMDB));

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
                filter.add(triple.getRight().name());
                filter.add(triple.getMiddle().getOperator());
                filter.add(triple.getLeft());
                System.out.println("[Controller] Adding filter: " + filter);
                filters.add(filter);
            }
        }
        return filters;
    }



}
