package group5.model.Filter;

import java.util.stream.Stream;

import group5.model.MovieData;
import group5.model.beans.MBeans;

/**
 * methods used to perform filters on the movie list.
 */
public interface IFilterHandler {

    /**
     * Resets the filters to the original collection of board movies.
     *
     * Assumes the results are sorted in ascending order, and that the steam is
     * sorted by the name of the board movie.
     *
     * @param filter The filter to apply to the board movies.
     * @return A stream of board movies that match the filter.
     * @see #filter(String, movieData, boolean)
     */
    Stream<MBeans> filter(String filter);

    /**
     * Filters the board movies by the passed in text filter. Assumes the
     * results are sorted in ascending order.
     *
     * @param filter The filter to apply to the board movies.
     * @param sortOn The column to sort the results on.
     * @return A stream of board movies that match the filter.
     * @see #filter(String, movieData, boolean)
     */
    Stream<MBeans> filter(String filter, MovieData sortOn);

    /**
     * Filters the board movies by the passed in text filter.
     *
     * @param filter The filter to apply to the board movies.
     * @param sortOn The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order or
     * descending order.
     * @return A stream of board movies that match the filter.
     */
    Stream<MBeans> filter(String filter, MovieData sortOn, boolean ascending);

    /**
     * Resets the collection to have no filters applied.
     */
    void reset();

}
