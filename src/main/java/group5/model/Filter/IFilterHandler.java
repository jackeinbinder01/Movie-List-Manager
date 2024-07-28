package group5.model.Filter;

import java.util.List;
import java.util.stream.Stream;

import group5.model.beans.MBeans;

/**
 * methods used to perform filters on the movie list.
 */
public interface IFilterHandler {

    /**
     * Filters the board movies by the passed in text filter.
     *
     * @param filter The List of lists of filter to apply to the board movie.
     * @param beanStream the stream of beans list. descending order.
     * @return A stream of board movies that match the filter.
     */
    Stream<MBeans> filter(List<List<String>> filter, Stream<MBeans> beanStream);

}
