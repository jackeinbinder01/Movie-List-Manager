package group5.model.Filter;

import java.util.List;
import java.util.stream.Stream;

import group5.model.MovieData;
import group5.model.beans.MBeans;

public class MovieSort {

    private MovieSort() {
        // Private constructor to prevent instantiation
    }

    public static Stream<MBeans> sort(List<MBeans> filteredList, MovieData sortOn, boolean ascending) {
        return filteredList.stream().sorted(MovieSortOp.getSort(sortOn, ascending));
    }
}
