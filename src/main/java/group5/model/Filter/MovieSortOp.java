package group5.model.Filter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import group5.model.MovieData;
import group5.model.beans.MBeans;

public final class MovieSortOp {

    private MovieSortOp() {
    }

    /**
     * Gets the type of sort needed.
     *
     * @param sortOn
     * @param ascending
     * @return Comparator for comparison
     */
    public static Comparator<MBeans> getSort(MovieData sortOn, boolean ascending) {
        switch (sortOn) {
            case TITLE:
                return ascending ? new TitleAscending() : new TitleDescending();
            case RELEASED:
                return ascending ? new YearAscending() : new YearDescending();
            case MPA:
                return ascending ? new MetascoreAscending() : new MetascoreDescending();
            case GENRE:
                return ascending ? new GenreAscending() : new GenreDescending();
            case RUNTIME:
                return ascending ? new RuntimeAscending() : new RuntimeDescending();
            case DIRECTOR:
                return ascending ? new DirectorAscending() : new DirectorDescending();
            case IMDB:
                return ascending ? new ImdbRatingAscending() : new ImdbRatingDescending();
            case USER:
                return ascending ? new UserRatingAscending() : new UserRatingDescending();
            case HASWATCHED:
                return ascending ? new WatchedAscending() : new WatchedDescending();
            default:
                return new TitleAscending();
        }
    }

    public static Stream<MBeans> sort(List<MBeans> movies, MovieData sortOn, boolean ascending) {
        return movies.stream().sorted(getSort(sortOn, ascending));
    }

    public static class TitleAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return one.getTitle().compareToIgnoreCase(two.getTitle());
        }
    }

    public static class TitleDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return two.getTitle().compareToIgnoreCase(one.getTitle());
        }
    }

    public static class YearAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Integer.compare(one.getYear(), two.getYear());
        }
    }

    public static class YearDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Integer.compare(two.getYear(), one.getYear());
        }
    }

    public static class MetascoreAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(one.getMetascore(), two.getMetascore());
        }
    }

    public static class MetascoreDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(two.getMetascore(), one.getMetascore());
        }
    }

    public static class GenreAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return one.getGenre().compareToIgnoreCase(two.getGenre());
        }
    }

    public static class GenreDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return two.getGenre().compareToIgnoreCase(one.getGenre());
        }
    }

    public static class RuntimeAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Integer.compare(one.getRuntime(), two.getRuntime());
        }
    }

    public static class RuntimeDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Integer.compare(two.getRuntime(), one.getRuntime());
        }
    }

    public static class DirectorAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return one.getDirector().get(0).compareToIgnoreCase(two.getDirector().get(0));
        }
    }

    public static class DirectorDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return two.getDirector().get(0).compareToIgnoreCase(one.getDirector().get(0));
        }
    }

    public static class ImdbRatingAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(one.getImdbRating(), two.getImdbRating());
        }
    }

    public static class ImdbRatingDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(two.getImdbRating(), one.getImdbRating());
        }
    }

    public static class UserRatingAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(one.getMyRating(), two.getMyRating());
        }
    }

    public static class UserRatingDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Double.compare(two.getMyRating(), one.getMyRating());
        }
    }

    public static class WatchedAscending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Boolean.compare(one.getWatched(), two.getWatched());
        }
    }

    public static class WatchedDescending implements Comparator<MBeans> {
        @Override
        public int compare(MBeans one, MBeans two) {
            return Boolean.compare(two.getWatched(), one.getWatched());
        }
    }
}
