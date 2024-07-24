package group5.model.Filter;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import group5.model.MovieData;
import group5.model.beans.MBeans;

public class FilterHandler implements IFilterHandler {

    /**
     * a set of movies.
     */
    private Set<MBeans> movie;
    /**
     * stream of movies.
     */
    private Stream<MBeans> movieStream;

    /**
     * constructor for planner.
     *
     * @param movies
     */
    public FilterHandler(Set<MBeans> movie) {
        this.movie = movie;
        this.movieStream = movie.stream();
    }

    /**
     * a filter for movies sorte by name (ascending).
     *
     * @return a sorted stream of the movies.
     */
    @Override
    public Stream<MBeans> filter(String filter) {
        return filter(filter, MovieData.TITLE, true);

    }

    /**
     * a filter on movies sorted by an input (ascending).
     *
     * @return a sorted stream of the movies.
     */
    @Override
    public Stream<MBeans> filter(String filter, MovieData sortOn) {
        return filter(filter, sortOn, true);

    }

    /**
     * a filter on movies sorted by an input ascending if bool is true
     * descending. if false.
     *
     * @return a sorted stream of the movies.
     */
    @Override
    public Stream<MBeans> filter(String filter, MovieData sortOn, boolean ascending) {
        List<MBeans> filteredBeans = movie.stream().toList();
        if (filter != null && !filter.isEmpty()) {
            String[] filters = filter.split(",");
            for (String filterStr : filters) {
                filteredBeans = makeAndApplySingleFilter(filteredBeans, filterStr).toList();

            }

        }
        return filteredBeans.stream();

    }

    /**
     * used to filter a single instance of the filters above.
     *
     * @param filteredmovies
     * @param filter
     * @return a sorted stream
     */
    public static Stream<MBeans> makeAndApplySingleFilter(List<MBeans> filteredmovies, String filter) {
        if (filteredmovies == null) {
            throw new IllegalArgumentException("make and apply - movies is null");

        } else {
            Operations op = Operations.getOperatorFromStr(filter);

            String[] columns = filter.split(op.getOperator());
            String valueOfFilter = columns[1].trim();
            MovieData filterOn = MovieData.fromString(columns[0].toLowerCase().trim());

            Stream<MBeans> stream = filteredmovies.stream().filter(
                    MBeans -> {
                        return FilterOperation.getFilter(MBeans, filterOn, op, valueOfFilter);
                    });

            return stream;
        }
    }

    /**
     * a way to reset the set movies.
     */
    @Override
    public void reset() {
        if (movie != null) {
            movieStream = movie.stream();
        }
    }

}
