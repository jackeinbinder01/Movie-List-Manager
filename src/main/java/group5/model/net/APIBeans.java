package group5.model.net;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

import group5.model.formatters.MBeansDeserializer;
import group5.model.formatters.MBeansSerializer;

/**
 * Java beans that represents a media object with title, year, and IMDb ID.
 */
@JsonPropertyOrder({"Title", "Year", "imdbID"})
@JsonInclude(Include.ALWAYS)
public class APIBeans implements Serializable {

    /**
     * Holds media title.
     */
    @JsonProperty("Title")
    private String title;

    /**
     * Holds year released of the media.
     */
    @JsonProperty("Year")
    @JsonDeserialize(using = MBeansDeserializer.IntDeserializer.class)
    @JsonSerialize(using = MBeansSerializer.IntSerializer.class)
    private int year;

    /**
     * Holds IMDb ID.
     */
    @JsonProperty("imdbID")
    private String id;

    /**
     * Default/empty constructor.
     */
    public APIBeans() {
    }

    /**
     * Constructor that includes all the values.
     *
     * @param title
     * @param year
     * @param id
     */
    public APIBeans(String title, int year, String id) {
        this.title = title;
        this.year = year;
        this.id = id;
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
     * get the imdbID.
     *
     * @return the imdbID
     */
    public String getID() {
        return id;
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
     * Set the imdbID.
     *
     * @param id the imdbID to set
     */
    public void setID(String id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the media.
     *
     * @return a string representation of the media
     */
    @Override
    public String toString() {
        String result = "";
        result += "Title: " + title + "\n";
        result += "Year: " + year + "\n";
        result += "imdbID: " + id + "\n";
        return result;
    }

    /**
     * Checks if given objects and this object are equal.
     *
     * @param o object to compare
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof APIBeans)) {
            return false;
        }
        APIBeans other = (APIBeans) o;
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
