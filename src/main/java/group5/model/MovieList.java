package group5.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.io.FileOutputStream;

import group5.model.beans.MBeans;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansFormatter;

public class MovieList implements IMovieList {

    /** set of movies. */
    private Set<MBeans> movieList;

    /** Name of this watch list */
    private String name;


    /**
     * Default Constructor for the movieList.
     *
     * Create empty list.
     * @param name name of the list
     */
    public MovieList(String name) {
        this.name = name;
        this.movieList = new HashSet<>();
        // hashset so there can only be one instance of a movie
        // in list
    }

    /**
     * Constructor for the movieList.
     *
     * Create from loading a list of movies.
     * @param name name of the list
     * @param movieList list of movies
     */
    public MovieList(String name, Set<MBeans> movieList) {
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
    public void saveMovie(String filename, Formats format) {
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
}
