package model;

import java.util.stream.Stream;

import model.beans.MBeans;

/**
 * methods used to perform filters on the movie list.
 */
public interface IFilterHandler {

    /**
     * Resets the filters to the original collection of board games.
     *
     * Assumes the results are sorted in ascending order, and that the steam is
     * sorted by the name of the board game.
     *
     * @param filter The filter to apply to the board games.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<MBeans> filter(String filter);

    /**
     * Filters the board games by the passed in text filter. Assumes the results
     * are sorted in ascending order.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<MBeans> filter(String filter, MovieData sortOn);

    /**
     * Filters the board games by the passed in text filter.
     *
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order or
     * descending order.
     * @return A stream of board games that match the filter.
     */
    Stream<MBeans> filter(String filter, MovieData sortOn, boolean ascending);

    /**
     * Resets the collection to have no filters applied.
     */
    void reset();

}
