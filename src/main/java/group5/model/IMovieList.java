package group5.model;

import java.util.List;
import java.util.stream.Stream;

import group5.model.beans.MBeans;

/**
 * Values are unique / no duplicates (based on Boardmovie.equals/equality) It
 * will return values in Case Insensitive ascending order of movies for that
 * returns a list of Movies.
 */
public interface IMovieList {

    /**
     * Default key word to use to add or remove an entire filter from/to the
     * list.
     */
    String ADD_ALL = "all";

    /**
     * Gets the contents of a list, as list of names (Strings) in ascending
     * order ignoring case.
     *
     * @return the list of movie names in ascending order ignoring case.
     */
    List<String> getMovieList();

    /**
     * Removes all movies in the list (clears it out completely).
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
     * The contents of the file will be each movie name on a new line. It will
     * overwrite the file if it already exists.
     *
     * Saves them in the same order as getmovieNames.
     *
     * @param filename The name of the file to save the list to.
     */
    void savemovie(String filename);

    /**
     * Adds a movie or movies to the list.
     *
     * @param str the string to parse and add movies to the list.
     * @param filtered the filtered list to use as a basis for adding.
     * @throws IllegalArgumentException if the string is not valid.
     */
    void addToList(String str, Stream<MBeans> filtered) throws IllegalArgumentException;

    /**
     * Removes a movie or movies from the list.
     *
     * If a single name is specified, that takes priority. However, it could
     * also use a number such as 1 which would indicate movie 1 from the current
     * movies list should be removed. A range can also be specified to remove
     * multiple movies.
     *
     * If all is provided, then clear should be called.
     *
     * If any part of the string is not valid, an IllegalArgumentException
     * should be thrown. Such as ranges being out of range, or none of the
     * results doing anything.
     *
     * @param str The string to parse and remove movies from the list.
     * @throws IllegalArgumentException If the string is not valid.
     *
     */
    void removeFromList(String str) throws IllegalArgumentException;

}
