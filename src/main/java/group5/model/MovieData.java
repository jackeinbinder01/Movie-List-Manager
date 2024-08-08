package group5.model;

/**
 * Enum to represent the columns in the movie data.
 */
public enum MovieData {
    /*
     * Enum for working through filtration of list.
     */
    /**
     * the title.
     */
    TITLE("objectname"),
    /**
     * the list number.
     */
    NUMBER("listnumber"),
    /**
     * the genre of a movie.
     */
    GENRE("type"),
    /**
     * the director(s).
     */
    DIRECTOR("whodirectedit"),
    /**
     * the actor(s).
     */
    ACTOR("actorsList"),
    /**
     * the language(s) available.
     */
    LANGUAGE("languageList"),
    /**
     * the writer(s).
     */
    WRITER("writer"),
    /*
     * Enums that are based on double values in the csv file.
     */
    /**
     * the mpa rating.
     */
    MPA("ratingtype"),
    /**
     * the imdb rating.
     */
    IMDB("ratingtype"),
    /**
     * the users rating.
     */
    USER("ratingtype"),
    /*
     * Enums based on whole int values in the csv file.
     */
    /**
     * the year relaeased.
     */
    RELEASED("yearreleased"),
    /**
     * the length in minutes.
     */
    RUNTIME("howlong"),
    /**
     * the money earned.
     */
    BOXOFFICE("boxoffice"),
    /*
     * Enums based on bool.
     */
    /**
     * if the user watched the movie.
     */
    HASWATCHED("hasuserwatched");

    /**
     * stores the original title in the enum.
     */
    private final String columnTitle;

    /**
     * Constructor for the enum.
     *
     * @param columnTitle the name of the column in the CSV file.
     */
    MovieData(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    /**
     * Getter for the column name.
     *
     * @return the name of the column in the CSV file.
     */
    public String getColumnTitle() {
        return columnTitle;
    }

    /**
     * Get the enum from the column name.
     *
     * @param columnName the name of the column in the CSV file.
     * @return the enum that matches the column name.
     */
    public static MovieData fromColumnName(String columnName) {
        for (MovieData col : MovieData.values()) {
            if (col.getColumnTitle().equals(columnName)) {
                return col;
            }
        }
        throw new IllegalArgumentException("No column with name " + columnName);
    }

    /**
     * Get the enum from the enum name.
     *
     * Can use the enum name or the column name. Useful for filters and sorts as
     * they can use both.
     *
     * @param title the name of a column.
     * @return the enum that matches the title.
     */
    public static MovieData fromString(String title) {
        for (MovieData col : MovieData.values()) {
            if (col.name().equalsIgnoreCase(title) || col.getColumnTitle().equalsIgnoreCase(title)) {
                return col;
            }
        }
        throw new IllegalArgumentException("No column with name " + title);
    }
}
