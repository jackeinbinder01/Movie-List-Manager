package group5.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import group5.model.beans.MBeans;

public class MovieList implements IMovieList {

    /**
     * set of board movie.
     */
    private Set<MBeans> movieList;

    // private List<String> returnList;
    /**
     * Constructor for the movieList.
     */
    public MovieList() {
        this.movieList = new HashSet<MBeans>();
        // hashset so there can only be one instance of a movie
        // in list
    }

    /**
     * gets list of movie names return movieList - list.
     *
     * @return the list of movieList.
     */
    @Override
    public List<String> getMovieList() {
        return movieList.stream().map(movieList -> movieList.getTitle())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /**
     * clears the list of movie names.
     *
     */
    @Override
    public void clear() {
        movieList.clear();
    }

    /**
     * returns the number of elements in a list.
     *
     * @return the number of elements in a list.
     */
    @Override
    public int count() {
        return movieList.size();
    }

    /**
     * saves the movie to a file.
     *
     */
    @Override
    public void savemovie(String filename) {
        File saves = new File(filename);
        try {
            FileWriter writer = new FileWriter(saves);
            for (String name : getMovieList()) {
                writer.write(name);
            }
            writer.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * adds a board movie (s) to the list.
     */
    @Override
    public void addToList(String str, Stream<MBeans> filtered) throws IllegalArgumentException {
        List<MBeans> filteredList = filtered.collect(Collectors.toList());

        // add all
        if (str.equalsIgnoreCase(ADD_ALL)) {
            for (MBeans movie : filteredList) {
                movieList.add(movie);
            }
            return;
        }
        // add the range
        if (str.contains("-")) {
            String[] values = str.split("-");

            if (values.length != 2) {
                throw new IllegalArgumentException("Invalid range format");
            }
            try {
                int low = Integer.parseInt(values[0]) - 1;
                int high = Integer.parseInt(values[1]) - 1;

                if (low < 0 || low > high) {
                    throw new IllegalArgumentException("Invalid range values");
                }

                for (int i = high; i >= low; i--) {
                    movieList.add(filteredList.get(i));
                }

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid range values");
            }
            return;
        }
        // add value and if caught see if string
        try {
            int value = Integer.parseInt(str) - 1;
            if (value < 0 || value >= filteredList.size()) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            movieList.add(filteredList.get(value));
            return;
        } catch (NumberFormatException e) {
            // this will be the string of movie name condition
            boolean movieFound = false;
            for (MBeans movie : filteredList) {
                if (movie.getTitle().equalsIgnoreCase(str)) {
                    movieList.add(movie);
                    movieFound = true;
                    break;
                }
            }
            if (!movieFound) {
                // if string not name throw illegal
                throw new IllegalArgumentException("movie name not found");
            }
        }
    }

    /**
     * removes a boardmovie(s) from the list.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {

        if (str.equalsIgnoreCase(ADD_ALL)) {
            // Clear all movies
            movieList.clear();
            return;
        }

        // Handle range removal
        if (str.contains("-")) {
            String[] values = str.split("-");
            if (values.length != 2) {
                throw new IllegalArgumentException("Invalid range format");
            }

            int low = Integer.parseInt(values[0]) - 1;
            int high = Integer.parseInt(values[1]) - 1;
            if (high >= movieList.size()) {
                high = movieList.size() - 1;
            }
            if (low < 0 || low > high) {
                String message = "low is" + low + "high is" + high + "size is" + movieList.size();
                throw new IllegalArgumentException(message);
            }

            // Create a list of movies to remove based on the range
            List<MBeans> moviesToRemove = new ArrayList<>(movieList);

            for (int i = high; i >= low; i--) {
                movieList.remove(moviesToRemove.get(i));
            }
            return;
        }

        // Handle single index removal
        try {
            int value = Integer.parseInt(str) - 1;
            if (value < 0 || value >= movieList.size()) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            // Convert movieList set to list to access by index
            List<MBeans> moviesToRemove = new ArrayList<>(movieList);
            movieList.remove(moviesToRemove.get(value));
        } catch (NumberFormatException e) {
            // Remove by movie name
            boolean movieFound = false;
            for (MBeans movie : movieList) {
                if (movie.getTitle().equals(str)) {
                    movieList.remove(movie);
                    movieFound = true;
                    break;
                }
            }
            if (!movieFound) {
                throw new IllegalArgumentException("movie name not found");
            }
        }
    }

    /**
     * Gets a sample list of movies.
     *
     * @return a list of sample movies.
     */
    public List<MBeans> getSampleMovies() {
        List<MBeans> sampleMovies = new ArrayList<>();
        try {
            sampleMovies.add(new MBeans("Inception", 2010, "Movie", "PG-13", LocalDate.of(2010, 7, 16), 148,
                    "Action, Sci-Fi", List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                    List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                    74, 8.8, "830 million USD", "tt1375666", false, -1.0));

            sampleMovies.add(new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                    "Action, Sci-Fi", List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                    List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                    73, 8.7, "463 million USD", "tt0133093", false, -1.0));

            sampleMovies.add(new MBeans("Interstellar", 2014, "Movie", "PG-13", LocalDate.of(2014, 11, 7), 169,
                    "Adventure, Drama, Sci-Fi", List.of("Christopher Nolan"), List.of("Jonathan Nolan", "Christopher Nolan"),
                    List.of("Matthew McConaughey", "Anne Hathaway"), "A team of explorers travel through a wormhole...",
                    List.of("English"), List.of("USA"), "Won 1 Oscar.", new URL("https://example.com/poster3.jpg"),
                    74, 8.6, "677 million USD", "tt0816692", false, -1.0));

            sampleMovies.add(new MBeans("The Social Network", 2010, "Movie", "PG-13", LocalDate.of(2010, 10, 1), 120,
                    "Biography, Drama", List.of("David Fincher"), List.of("Aaron Sorkin"),
                    List.of("Jesse Eisenberg", "Andrew Garfield"), "The story of Harvard student Mark Zuckerberg...",
                    List.of("English"), List.of("USA"), "Won 3 Oscars.", new URL("https://example.com/poster4.jpg"),
                    76, 7.7, "224 million USD", "tt1285016", false, -1.0));

            sampleMovies.add(new MBeans("Pandemic", 2007, "Series", "TV-14", LocalDate.of(2007, 1, 25), 120,
                    "Drama, Thriller", List.of("Armand Mastroianni"), List.of("Ron McGee", "Ed Napier"),
                    List.of("Tiffani Thiessen", "French Stewart"), "The bird flu virus spreads through Los Angeles...",
                    List.of("English"), List.of("USA"), "N/A", new URL("https://example.com/poster5.jpg"),
                    45, 5.3, "N/A", "tt0826763", false, -1.0));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return sampleMovies;
    }

    /**
     * main for testing.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Create an instance of MovieList
        MovieList movieList = new MovieList();

        // Add some sample movies
        List<MBeans> sampleMovies = movieList.getSampleMovies();
        System.out.println("Initial sample movies added to the list:");
        sampleMovies.forEach(movie -> System.out.println(movie.getTitle()));

        // Test addToList method
        System.out.println("\nAdding movies to the list...");
        movieList.addToList("3", sampleMovies.stream());
        movieList.addToList("Inception", sampleMovies.stream());
        movieList.addToList("4-5", sampleMovies.stream());

        // Print current movie names after additions
        System.out.println("\nCurrent movies in the list:");
        movieList.getMovieList().forEach(System.out::println);

        // Test removeFromList method
        System.out.println("\nRemoving movies from the list...");
        movieList.removeFromList("1-2");
        movieList.removeFromList("1");

        // Print current movie names after removals
        System.out.println("\nMovies remaining in the list after removals:");
        movieList.getMovieList().forEach(System.out::println);

        // Test clear method
        movieList.addToList(ADD_ALL, sampleMovies.stream());
        System.out.println("\nFull movie list. Number of movies: " + movieList.count());

        movieList.clear();
        System.out.println("\nCleared movie list. Number of movies: " + movieList.count());

        // Save current movie list to a file
        movieList.savemovie("movie_list.txt");
    }
}
