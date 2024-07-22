package model;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.management.MBeanAttributeInfo;

import model.beans.MBeans;

public class FilterHandler implements IFilterHandler {

    /**
     * a set of games.
     */
    private Set<MBeans> movie;
    /**
     * stream of games.
     */
    private Stream<MBeans> movieStream;

    /**
     * constructor for planner.
     *
     * @param games
     */
    public Filter(Set<MBeans> movie) {
        this.movie = movie;
    }

    /**
     * a filter for games sorte by name (ascending).
     *
     * @return a sorted stream of the games.
     */
    @Override
    public Stream<MBeans> filter(String filter) {
        return filter(filter, MBeans.getTitle(), true);

    }

    /**
     * a filter on games sorted by an input (ascending).
     *
     * @return a sorted stream of the games.
     */
    @Override
    public Stream<MBeans> filter(String filter, MovieData sortOn) {
        return filter(filter, sortOn, true);

    }

    /**
     * a filter on games sorted by an input ascending if bool is true
     * descending. if false.
     *
     * @return a sorted stream of the games.
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
        return GameSort.sort(filteredBeans, sortOn, ascending);

    }

    /**
     * used to filter a single instance of the filters above.
     *
     * @param filteredGames
     * @param filter
     * @return a sorted stream
     */
    public static Stream<BoardGame> makeAndApplySingleFilter(List<BoardGame> filteredGames, String filter) {
        if (filteredGames == null) {
            throw new IllegalArgumentException("make and apply - games is null");

        } else {
            Operations op = Operations.getOperatorFromStr(filter);

            String[] columns = filter.split(op.getOperator());
            String valueOfFilter = columns[1].trim();
            GameData filterOn = MovieData.fromString(columns[0].toLowerCase().trim());

            Stream<MBeans> stream = filteredGames.stream().filter(
                    BoardGame -> {
                        return Filters.getFilter(BoardGame, filterOn, op, valueOfFilter);
                    });

            return stream;
        }
    }

    /**
     * a way to reset the set games.
     */
    @Override
    public void reset() {
        // WRONG
        if (games != null) {
            gameStream = games.stream();
        }
    }

}

}
