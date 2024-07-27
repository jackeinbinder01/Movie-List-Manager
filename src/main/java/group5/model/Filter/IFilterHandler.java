package group5.model.Filter;

import java.util.stream.Stream;

import group5.model.beans.MBeans;

/**
 * methods used to perform filters on the movie list.
 */
public interface IFilterHandler {

    /**
     * Filters the board movies by the passed in text filter.
     *
     * @param filter The filter to apply to the board movies.
     * @param beanStream the stream of beans list.
     * descending order.
     * @return A stream of board movies that match the filter.
     */
    Stream<MBeans> filter(String  filter, Stream<MBeans> beanStream);

    /**
     * Resets the collection to have no filters applied.
     */
    void reset();

}
