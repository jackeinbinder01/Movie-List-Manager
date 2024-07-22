package group5.model.beans;


import java.time.LocalDate;
import java.util.List;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import java.time.format.DateTimeFormatter;

/**
 * Java beans that represents a media object with various properties such as title, year, type, etc.
 */
public class MBeans implements java.io.Serializable {


    /** Holds media title. */
    @JsonProperty("Title")
    private String title;

    /** Holds year released of the media. */
    @JsonProperty("Year")
    private int year;

    /** Holds type of media(movies, series). */
    @JsonProperty("Type")
    private String type;

    /** Holds content rating of the media. */
    @JsonProperty("Rated")
    private String rated;

    /** Holds release date of the media. */
    @JsonProperty("Released")
    @JsonDeserialize(using = MBeansDeserializer.DateDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.DateSerializer.class)
    private LocalDate released;

    /** Holds runtime of the media in minutes. */
    @JsonProperty("Runtime")
    @JsonDeserialize(using = MBeansDeserializer.RuntimeDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.RuntimeSerializer.class)
    private int runtime;

    /** Holds genres of the media. */
    @JsonProperty("Genre")
    private String genre;

    /** Holds a list of director name(s). */
    @JsonProperty("Director")
    @JsonDeserialize(using = MBeansDeserializer.DirectorDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.DirectorSerializer.class)
    private List<String> director;

    /** Holds a list of writer name(s). */
    @JsonProperty("Writer")
    @JsonDeserialize(using = MBeansDeserializer.WriterDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.WriterSerializer.class)
    private List<String> writer;

    /** Holds a list of actor name(s). */
    @JsonProperty("Actors")
    @JsonDeserialize(using = MBeansDeserializer.ActorDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.ActorSerializer.class)
    private List<String> actors;

    /** Holds plot of the media. */
    @JsonProperty("Plot")
    private String plot;

    /** Holds a list of language(s) spoken in this media. */
    @JsonProperty("Language")
    @JsonDeserialize(using = MBeansDeserializer.LanguageDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.LanguageSerializer.class)
    private List<String> language;

    /** Holds a list of the country or countries where this media production took place. */
    @JsonProperty("Country")
    @JsonDeserialize(using = MBeansDeserializer.CountryDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.CountrySerializer.class)
    private List<String> country;

    /** Holds the awards and nominatinos of the media. */
    @JsonProperty("Awards")
    private String awards;

    /** Holds URL to the media poster. */
    @JsonProperty("Poster")
    private URL poster;

    /** Holds Metascore rating of the media. */
    @JsonProperty("Metascore")
    private int metascore;

    /** Holds imdbRating of the media. */
    @JsonProperty("imdbRating")
    private double imdbRating;

    /** Holds box office revenues of the media. */
    @JsonProperty("BoxOffice")
    private String boxOffice;

    /** Holds imdb ID */
    @JsonProperty("imdbID")
    private String id;

    /** Holds whether user had watched the media. */
    private boolean watched = false;

    /** Holds personal rating of the media. */
    private double myRating = -1.0;


    /** Default/empty constructor. */
    public MBeans() {}

    /** Constructor that includes all the values
     *
     * @param title
     * @param year
     * @param type
     * @param rated
     * @param released
     * @param runtime
     * @param genre
     * @param director
     * @param writer
     * @param actors
     * @param plot
     * @param language
     * @param country
     * @param awards
     * @param poster
     * @param metascore
     * @param imdbRating
     * @param boxOffice
     * @param id
     * @param watched
     * @param myRating
     */
    public MBeans(String title, int year, String type, String rated, LocalDate released, int runtime, String genre,
            List<String> director, List<String> writer, List<String> actors, String plot, List<String> language,
            List<String> country, String awards, URL poster, int metascore, double imdbRating, String boxOffice,
            String id, boolean watched, double myRating) {
        this.title = title;
        this.year = year;
        this.type = type;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.boxOffice = boxOffice;
        this.id = id;
        this.watched = watched;
        this.myRating = myRating;
    }


    /**
     * get the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * get the year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * get the media type.
     *
     * @return the media type
     */
    public String getType() {
        return type;
    }


    /**
     * get the content rating.
     *
     * @return the content rating
     */
    public String getRated() {
        return rated;
    }

    /**
     * get the date released.
     *
     * @return the date released
     */
    public LocalDate getReleased() {
        return released;
    }

    /**
     * get the runtime.
     *
     * @return the runtime
     */
    public int getRuntime() {
        return runtime;
    }

    /**
     * get the genres.
     *
     * @return the genres
     */
    public String getGenre() {
        return genre;
    }

    /**
     * get the list of director(s).
     *
     * @return the list of director(s)
     */
    public List<String> getDirector() {
        return director;
    }

    /**
     * get the list of writer(s).
     *
     * @return the list of writer(s)
     */
    public List<String> getWriter() {
        return writer;
    }

    /**
     * get the list of actor(s).
     *
     * @return the list of actor(s)
     */
    public List<String> getActors() {
        return actors;
    }

    /**
     * get the plot.
     *
     * @return the plot
     */
    public String getPlot() {
        return plot;
    }

    /**
     * get the list of language(s).
     *
     * @return the list of language(s)
     */
    public List<String> getLanguage() {
        return language;
    }

    /**
     * get the list of country/countries.
     *
     * @return the list of country/countries
     */
    public List<String> getCountry() {
        return country;
    }

    /**
     * get the awards and nominations.
     *
     * @return the awards and nominations
     */
    public String getAwards() {
        return awards;
    }

    /**
     * get the poster URL.
     *
     * @return the poster URL
     */
    public URL getPoster() {
        return poster;
    }

    /**
     * get the metascore rating.
     *
     * @return the metascore rating
     */
    public int getMetascore() {
        return metascore;
    }

    /**
     * get the imdb rating.
     *
     * @return the imdb rating
     */
    public double getImdbRating() {
        return imdbRating;
    }

    /**
     * get the box office revenue.
     *
     * @return the box office revenue
     */
    public String getBoxOffice() {
        return boxOffice;
    }

    /**
     * get the imdbID.
     *
     * @return the imdbID
     */
    public String getID() {
        return id;
    }

    /**
     * get the watched boolean.
     *
     * @return the watched boolean
     */
    public boolean getWatched() {
        return watched;
    }

    /**
     * get the user rating.
     *
     * @return the user rating
     */
    public double getMyRating() {
        return myRating;
    }



    /**
     * Set the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the year.
     *
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Set the media type.
     *
     * @param type the media type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the content rating.
     *
     * @param rated the content rating to set
     */
    public void setRated(String rated) {
        this.rated = rated;
    }

    /**
     * Set the date released.
     *
     * @param released the date released to set
     */
    public void setReleased(LocalDate released) {
        this.released = released;
    }

    /**
     * Set the runtime.
     *
     * @param runtime the runtime to set
     */
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    /**
     * Set the genre.
     *
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Set the director(s).
     *
     * @param director the director(s) to set
     */
    public void setDirector(List<String> director) {
        this.director = director;
    }

    /**
     * Set the writer(s).
     *
     * @param writer the writer(s) to set
     */
    public void setWriter(List<String> writer) {
        this.writer = writer;
    }

    /**
     * Set the actor(s).
     *
     * @param actor the actor(s) to set
     */
    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    /**
     * Set the plot.
     *
     * @param plot the plot to set
     */
    public void setPlot(String plot) {
        this.plot = plot;
    }

    /**
     * Set the language(s).
     *
     * @param language the language(s) to set
     */
    public void setLanguage(List<String> language) {
        this.language = language;
    }

    /**
     * Set the country/countries.
     *
     * @param country the country/countries to set
     */
    public void setCountry(List<String> country) {
        this.country = country;
    }

    /**
     * Set the awards and nominations.
     *
     * @param awards the awards and nominations to set
     */
    public void setAwards(String awards) {
        this.awards = awards;
    }

    /**
     * Set the poster URL.
     *
     * @param poster the poster URL to set
     */
    public void setPoster(URL poster) {
        this.poster = poster;
    }

    /**
     * Set the metascore.
     *
     * @param metascore the metascore to set
     */
    public void setMetascore(int metascore) {
        this.metascore = metascore;
    }

    /**
     * Set the imdb rating.
     *
     * @param imdbRating the imdb rating to set
     */
    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    /**
     * Set the box office revenue.
     *
     * @param boxOffice the box office revenue to set
     */
    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    /**
     * Set the imdbID.
     *
     * @param boxOffice the imdbID to set
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Set the watched boolean.
     *
     * @param watched the watched boolean to set
     */
    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    /**
     * Set the user rating.
     *
     * @param myRating the user rating to set
     */
    public void setMyRating(double myRating) {
        this.myRating = myRating;
    }

    /**
     * Returns a string representation of the media.
     *
     * @return a string representation of the media
     */
    @Override
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String result = "";
        result += "Title: " + title + "\n";
        result += "Year: " + year + "\n";
        result += "Rated: " + rated + "\n";
        result += "Released: " + dateFormat.format(released) + "\n";
        result += "Runtime: " + runtime + "\n";
        result += "Genre: " + genre + "\n";
        result += "Director: " + director + "\n";
        result += "Writer: " + writer + "\n";
        result += "Actors: " + actors + "\n";
        result += "Plot: " + plot + "\n";
        result += "Language: " + language + "\n";
        result += "Country: " + country + "\n";
        result += "Awards: " + awards + "\n";
        result += "Poster: " + poster + "\n";
        result += "Metascore: " + metascore + "\n";
        result += "imdbRating: " + imdbRating + "\n";
        result += "BoxOffice: " + boxOffice + "\n";
        return result;
    }

    /**
     * Checks if given objects abd this object are equal.
     *
     * @param o object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof MBeans)) {
            return false;
        }
        MBeans other = (MBeans) o;
        return this.getID().equals(other.getID());
    }

    /**
     * Returns the hash code of this object.
     *
     * @return the hash code of this object
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getID());
    }
}