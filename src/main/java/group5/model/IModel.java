package group5.model;

import group5.model.beans.MBeans;
import group5.model.formatters.Formats;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.List;

/**
 * Interface for the Model component of the application.
 */
public interface IModel {

    /** String file path to default database location. */
    public static final String DEFAULT_DATA = "./data/samples/source.json";  // Path to default movie DB here

    /**
     * Load the source data from the default location.
     */
    void loadSourceData();

    /**
     * Load a watch list from a file.
     *
     * @param filename The file to load the watch list from.
     */
    void loadWatchList(String filename);

    /**
     * Get the source list of media as stream.
     *
     * @return stream of MBeans representing the source list.
     */
    Stream<MBeans> getSourceLists();

    /**
     * Get the watch list as stream.
     *
     * @param userListId The identifier of the watch list to get.
     * @return stream of MBeans representing the source list.
     */
    Stream<MBeans> getWatchLists(int userListId/*, UserListIdentifier userListId*/);

    /**
     * Save the watch list to a file.
     *
     * @param filename string of file path to save the watch list to.
     * @param userListId The identifer of the watch list to save.
     */
    void saveWatchList(String filename, int userListId/*, UserListIdentifier userListId*/);

    /**
     * Add a media to the watch list.
     *
     * @param media The media to add to the watch list.
     * @param userListId The identifier of the watch list to add the media to.
     */
    void addToWatchList(MBeans media, int userListId);

    /**
     * Remove a media from the watch list.
     *
     * @param media The media to remove from the watch list.
     * @param userListId The identifier of the watch list to add the media to.
     */
    void removeFromWatchList(MBeans media, int userListId);

    /**
     * Update user watched status of a media.
     *
     * @param media The media to remove from the watch list.
     * @param watched Boolean to set the watched status of the media.
     */
    void updateWatched(MBeans media, boolean watched);

    /**
     * Update user rating of a media.
     *
     * @param media The media to remove from the watch list.
     * @param rating value to set the user rating of the media.
     */
    void updateUserRating(MBeans media, double rating);

    /**
     * Get the name of watch list.
     *
     * @return the name of watch list
     */
    String getUserListName(int userListId);

    /**
     * Get the amount of user created watch list.
     *
     * @return the amount of user created watch list
     */
    int getUserListCount();

    /**
     * Get the list of user created watch list index.
     *
     * @param record The record to get the watch list indices for.
     * @return the list of user created watch list
     */
    int[] getUserListIndicesForRecord(MBeans record);

    /**
     * Add record to multiple watchlist based on indices.
     *
     * @param record The record to add to the watchlist.
     * @param userListIndices The indices of the watchlist to add the record to.
     */
    void setUserListIndicesForRecird(MBeans record, int[] userListIndices);




    /*public Stream<MBeans> getMovieList(FilterClass filter) {
        return getMovieList(filter, null);
    }*/

    //Stream<MBeans> getMovieList(FilterClass filter, UserListIdentifier userListId);



}
