package group5.model.Filter;

import java.util.List;
import java.util.stream.Stream;

import group5.model.MovieData;
import group5.model.beans.MBeans;

public class FilterHandler implements IFilterHandler {

    /**
     * constructor for planner.
     *
     * @param movies
     */
    public FilterHandler() {

    }

    /**
     * a filter on movies sorted by an input ascending if bool is true
     * descending. if false.
     *
     * @return a sorted stream of the movies.
     */
    @Override
    public Stream<MBeans> filter(String filter, Stream<MBeans> beanStream) {
        List<MBeans> filteredBeans = beanStream.toList();
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

}
