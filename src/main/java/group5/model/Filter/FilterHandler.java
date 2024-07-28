package group5.model.Filter;

import java.rmi.server.Operation;
import java.util.List;
import java.util.stream.Collectors;
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
    public Stream<MBeans> filter(List<List<String>> filters, Stream<MBeans> beanStream) {
        if (filters == null || filters.isEmpty()) {
            return beanStream;
        }

        List<MBeans> filteredBeans = beanStream.collect(Collectors.toList());
        for (List<String> oneFilter : filters) {
            filteredBeans = makeAndApplySingleFilter(filteredBeans, oneFilter).collect(Collectors.toList());
        }

        return filteredBeans.stream();
    }

    /**
     * used to filter a single instance of the filters above.
     *
     * @param beans list of moviebeans
     * @param filter the list of filter
     * @return a sorted stream
     */
    public static Stream<MBeans> makeAndApplySingleFilter(List<MBeans> beans, List<String> filter) {
        if (beans == null) {
            throw new IllegalArgumentException("makeAndApplySingleFilter - beans is null");
        }
        if (filter == null || filter.size() < 4) {
            throw new IllegalArgumentException("makeAndApplySingleFilter - filter is null or incomplete");
        }

        Operations op = Operations.getOperatorFromStr(filter.get(2));
        String valueOfFilter = filter.get(1);
        MovieData filterOn = MovieData.fromColumnName(filter.get(3));

        return beans.stream().filter(bean -> FilterOperation.getFilter(bean, filterOn, op, valueOfFilter));
    }
}