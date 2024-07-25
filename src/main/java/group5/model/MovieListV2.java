package group5.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.io.FileOutputStream;

import group5.model.beans.MBeans;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansFormatter;

public class MovieListV2 implements IMovieListV2 {

    /** set of board movie. */
    private Set<MBeans> movieList;

    /** Name of this watch list */
    private String name;


    /**
     * Default Constructor for the movieList.
     *
     * Create empty list.
     */
    public MovieListV2(String name) {
        this.name = name;
        this.movieList = new HashSet<>();
        // hashset so there can only be one instance of a movie
        // in list
    }

    /**
     * Constructor for the movieList.
     *
     * Create from loading a list of movies.
     */
    public MovieListV2(String name, Set<MBeans> movieList) {
        this.name = name;
        this.movieList = movieList;
        // hashset so there can only be one instance of a movie
        // in list
    }

    @Override
    public String getListName() {
        return this.name;
    }

    @Override
    public Stream<MBeans> getMovieList() {
        return this.movieList.stream();
    }


    @Override
    public void clear() {
        this.movieList.clear();
    }

    @Override
    public int count() {
        return this.movieList.size();
    }

    @Override
    public void savemovie(String filename, Formats format) {
        try {
            FileOutputStream saves = new FileOutputStream(filename);
            MBeansFormatter.writeMediasToFile(this.movieList, saves, format);
        } catch (IOException e) {
            System.out.println("Error writing to file");
            e.printStackTrace();
        }
    }

    @Override
    public void addToList(MBeans media) {
        if (!this.movieList.add(media)) {
            System.out.println("Media is already in list");
        }
    }

    @Override
    public void removeFromList(MBeans media) {
        if (!this.movieList.remove(media)) {
            System.out.println("Media not found in list");
        }
    }

    @Override
    public boolean containsMedia(MBeans media) {
        return this.movieList.contains(media);
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
                    List.of("Action", "Sci-Fi"), List.of("Christopher Nolan"), List.of("Christopher Nolan"),
                    List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"), "A thief who steals corporate secrets...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster1.jpg"),
                    74, 8.8, 8300000, "tt1375666", false, -1.0));

            sampleMovies.add(new MBeans("The Matrix", 1999, "Movie", "R", LocalDate.of(1999, 3, 31), 136,
                    List.of("Action", "Sci-Fi"), List.of("Lana Wachowski", "Lilly Wachowski"), List.of("Lana Wachowski", "Lilly Wachowski"),
                    List.of("Keanu Reeves", "Laurence Fishburne"), "A computer hacker learns from mysterious rebels...",
                    List.of("English"), List.of("USA"), "Won 4 Oscars.", new URL("https://example.com/poster2.jpg"),
                    73, 8.7, 123456789, "tt0133093", false, -1.0));

            sampleMovies.add(new MBeans("Interstellar", 2014, "Movie", "PG-13", LocalDate.of(2014, 11, 7), 169,
                    List.of("Adventure", "Drama", "Sci-Fi"), List.of("Christopher Nolan"), List.of("Jonathan Nolan", "Christopher Nolan"),
                    List.of("Matthew McConaughey", "Anne Hathaway"), "A team of explorers travel through a wormhole...",
                    List.of("English"), List.of("USA"), "Won 1 Oscar.", new URL("https://example.com/poster3.jpg"),
                    74, 8.6, 123456789, "tt0816692", false, -1.0));

            sampleMovies.add(new MBeans("The Social Network", 2010, "Movie", "PG-13", LocalDate.of(2010, 10, 1), 120,
                    List.of("Biography", "Drama"), List.of("David Fincher"), List.of("Aaron Sorkin"),
                    List.of("Jesse Eisenberg", "Andrew Garfield"), "The story of Harvard student Mark Zuckerberg...",
                    List.of("English"), List.of("USA"), "Won 3 Oscars.", new URL("https://example.com/poster4.jpg"),
                    76, 7.7, 123456789, "tt1285016", false, -1.0));

            sampleMovies.add(new MBeans("Pandemic", 2007, "Series", "TV-14", LocalDate.of(2007, 1, 25), 120,
                    List.of("Drama", "Thriller"), List.of("Armand Mastroianni"), List.of("Ron McGee", "Ed Napier"),
                    List.of("Tiffani Thiessen", "French Stewart"), "The bird flu virus spreads through Los Angeles...",
                    List.of("English"), List.of("USA"), "N/A", new URL("https://example.com/poster5.jpg"),
                    45, 5.3, -1, "tt0826763", false, -1.0));

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

        movieList.clear();
        System.out.println("\nCleared movie list. Number of movies: " + movieList.count());

        // Save current movie list to a file
        movieList.savemovie("movie_list.txt");
    }
}
