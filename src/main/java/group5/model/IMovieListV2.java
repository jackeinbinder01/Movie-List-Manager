package group5.model;

import java.util.List;
import java.util.stream.Stream;

import group5.model.beans.MBeans;
import group5.model.formatters.Formats;

/**
 * Values are unique / no duplicates (based on Boardmovie.equals/equality) It
 * will return values in Case Insensitive ascending order of movies for that
 * returns a list of Movies.
 */
public interface IMovieListV2 {

    /** Get name of this watch list.
     *
     * @return the name of this watch list.
     */
    String getListName();

    /**
     * gets list of movies.
     *
     * @return the list of movieList.
     */
    Stream<MBeans> getMovieList();

    /**
     * clears the list of movie names.
     *
     * Equivalent of remove all.
     *
     */
    void clear();

    /**
     * Counts/returns the number of movies in the list.
     *
     * @return the number of movies in the list.
     */
    int count();

    /**
     * Saves the list of movies to a file.
     *
     * Will overwrite the file if it already exists.
     *
     * @param filename The name of the file to save the list to.
     * @param format The format to save the list in.
     */
    void savemovie(String filename, Formats format);

    /**
     * adds a media to the list.
     *
     * @param media the media to add to watchlist.
     */
    void addToList(MBeans media);

    /**
     * removes a given movie from the list.
     */
    void removeFromList(MBeans media);

    /**
     * Check if the list contains a media.
     *
     * @param media the media to check for.
     * @return true if exist, false otherwise.
     */
    boolean containsMedia(MBeans media);

}
