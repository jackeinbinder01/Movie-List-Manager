package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Contains and passes filters to controller for selected movie list. */
public class FilterPane extends JPanel implements ActionListener, FocusListener {

    /** Default panel width. */
    private final int DEFAULT_WIDTH = 225;
    /** Default panel height. */
    private final int DEFAULT_HEIGHT = 100;

    /** Set of movies. */
    private Set<MBeans> movies = new HashSet<>();
    /** Boolean denoting if the current movies set is the source list. */
    private boolean moviesIsSourceList = false;

    // Panels
    /** Panel containing filters. */
    private JPanel filterPanel = new JPanel(new GridBagLayout());
    /** Panel containing buttons. */
    private JPanel buttonPanel  = new JPanel(new GridLayout(0, 2));

    // Filters
    /** Title filter. */
    private JTextField titleFilter = new JTextField();
    /** Genre filter. */
    private JComboBox<String> genreFilter = new JComboBox();
    /** MPA Rating filter. */
    private JComboBox<String> mpaRatingFilter = new JComboBox();
    /** Director filter. */
    private JTextField directorFilter = new JTextField();
    /** Actor filter. */
    private JTextField actorFilter = new JTextField();
    /** Writer filter. */
    private JTextField writerFilter = new JTextField();
    /** Language filter. */
    private JComboBox<String> languageFilter = new JComboBox();

    // Range filters
    /** Year released min of range. */
    private JTextField releasedFrom = new JTextField();
    /** Year released max of range. */
    private JTextField releasedTo = new JTextField();
    /** IMDB Rating min of range. */
    private JTextField imdbRatingFrom = new JTextField();
    /** IMDB Rating max of range. */
    private JTextField imdbRatingTo = new JTextField();
    /** Box Office Earnings min of range. */
    private JTextField boxOfficeEarningsFrom = new JTextField();
    /** Box Office Earnings max of range. */
    private JTextField boxOfficeEarningsTo = new JTextField();

    /** Set containing all text filters. */
    private Set<JTextField> textFilters = new HashSet<JTextField>();
    /** Set containing all dropdown filters. */
    private Set<JComboBox<String>> dropDownFilters = new HashSet<JComboBox<String>>();
    /** Map containing all range filters and min/maxes. */
    private Map<JTextField, String> rangeFilterMap = new HashMap<>();

    // Buttons
    /** Apply filters button. */
    private JButton applyFiltersButton = new JButton("Apply Filters");
    /** Clear filters button. */
    private JButton clearFiltersButton = new JButton("Clear Filters");

    // GridBagConstraints & grid row int
    /** Class global GridBagConstraints. */
    private GridBagConstraints gbc = new GridBagConstraints();
    /** Filter row to place filters descending using gbc.gridy. */
    private int filterRow = 0;

    /** Public constructor. */
    public FilterPane() {
        super(new BorderLayout());

        // set component size
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        // name components to enable switch statements
        setComponentNames();

        // configure gbc
        gbc.insets = new Insets(5, 4, 5, 4);
        updateGBC(null, null, null, null, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

        // add border padding to button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 5, 4));
        // add panels
        add(filterPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // add filters
        addFilter(FilterLabels.TITLE.getFilterLabel(), titleFilter);
        addFilter(FilterLabels.GENRE.getFilterLabel(), genreFilter);
        addFilter(FilterLabels.MPA_RATING.getFilterLabel(), mpaRatingFilter);
        // add range filters
        addRangeFilter(FilterLabels.RELEASED.getFilterLabel(), releasedFrom, releasedTo);
        addRangeFilter(FilterLabels.IMDB_RATING.getFilterLabel(), imdbRatingFrom, imdbRatingTo);
        addRangeFilter(FilterLabels.BOX_OFFICE_EARNINGS.getFilterLabel(), boxOfficeEarningsFrom, boxOfficeEarningsTo);
        // add remaining filters
        addFilter(FilterLabels.DIRECTOR.getFilterLabel(), directorFilter);
        addFilter(FilterLabels.ACTOR.getFilterLabel(), actorFilter);
        addFilter(FilterLabels.WRITER.getFilterLabel(), writerFilter);
        addFilter(FilterLabels.LANGUAGE.getFilterLabel(), languageFilter);

        // add vertical scroll bar
        JScrollPane scrollPane = new JScrollPane(filterPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
        add(scrollPane);

        // add buttons
        buttonPanel.add(applyFiltersButton);
        buttonPanel.add(clearFiltersButton);

        // add action listeners
        titleFilter.addActionListener(this);
        directorFilter.addActionListener(this);
        actorFilter.addActionListener(this);
        writerFilter.addActionListener(this);
        applyFiltersButton.addActionListener(this);
        clearFiltersButton.addActionListener(this);
    }

    /* Getters -------------------------------------------------------------------------------------------------------*/
    /**
     * Returns user entry in title filter.
     *
     * @return movie title entered by user
     */
    public String getFilteredTitle() {
        return titleFilter.getText();
    }

    /**
     * Returns user selection in genre filter.
     *
     * @return genre selected by user
     */
    public String getFilteredGenre() {
        try {
            return genreFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * Returns user selection in MPA rating filter.
     *
     * @return MPA rating selected by user
     */
    public String getFilteredMpaRating() {
        try {
            return mpaRatingFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * Returns the lower bound user entry in date released filter.
     *
     * @return lower bound release of date range entered by user
     */
    public String getFilteredReleasedMin() {
        return releasedFrom.getText();
    }

    /**
     * Returns the upper bound user entry in date released filter.
     *
     * @return upper bound release of date range entered by user
     */
    public String getFilteredReleasedMax() {
        return releasedTo.getText();
    }

    /**
     * Returns the lower bound user entry in the IMDB Rating filter.
     *
     * @return lower bound of the IMDB Rating range entered by user
     */
    public String getFilteredImdbRatingMin() {
        return imdbRatingFrom.getText();
    }

    /**
     * Returns the upper bound user entry in the IMDB Rating filter.
     *
     * @return upper bound of the IMDB Rating range entered by user
     */
    public String getFilteredImdbRatingMax() {
        return imdbRatingTo.getText();
    }

    /**
     * Returns the lower bound user entry in the Box Office Earnings filter.
     *
     * @return lower bound of the Box Office Earnings range entered by user
     */
    public String getFilteredBoxOfficeEarningsMin() {
        try {
            return boxOfficeEarningsFrom.getText().equalsIgnoreCase("N/A")
                    ? boxOfficeEarningsFrom.getText() : formatFromMillions(boxOfficeEarningsFrom.getText());
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * Returns the upper bound user entry in the Box Office Earnings filter.
     *
     * @return upper bound of the Box Office Earnings range entered by user
     */
    public String getFilteredBoxOfficeEarningsMax() {
        try {
            return boxOfficeEarningsTo.getText().equalsIgnoreCase("N/A") ?
                    boxOfficeEarningsTo.getText() : formatFromMillions(boxOfficeEarningsTo.getText());
        } catch (NumberFormatException e) {
            return "";
        }
    }

    /**
     * Returns user entry in the director filter.
     *
     * @return director name entered by user
     */
    public String getFilteredDirectorFilter() {
        return directorFilter.getText();
    }

    /**
     * Returns user entry in the actor filter.
     *
     * @return actor name entered by user
     */
    public String getFilteredActorFilter() {
        return actorFilter.getText();

    }

    /**
     * Returns user entry in the writer filter.
     *
     * @return writer name entered by user
     */
    public String getFilteredWriterFilter() {
        return writerFilter.getText();
    }

    /**
     * Returns user selection in the language filter.
     *
     * @return language selected by user
     */
    public String getFilteredLanguageFilter() {
        try {
            return languageFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    /* FilterPane Setup Methods --------------------------------------------------------------------------------------*/
    /**
     * Sets the movies set of this FilterPane instance based on the MBeans in an input Stream.
     * Defaults isSourceList to false, denoting that the stream passed does not represent the initial movie data source.
     *
     * @param movies a Stream of movies to replace the current movies set
     */
    public void setMovies(Stream<MBeans> movies) {
        setMovies(movies, false);
    }

    /**
     * Sets the movies set of this FilterPane instance based on the MBeans in an input Stream.
     * Resets filter ranges and options based on MBeans in the new movies set.
     *
     * @param movies a Stream of movies to replace the current movies set
     * @param isSourceList boolean denoting if the list passed is the initial movie data source
     */
    public void setMovies(Stream<MBeans> movies, boolean isSourceList) {
        Set<MBeans> moviesSet = movies.collect(Collectors.toSet());
        if (!moviesSet.isEmpty()) {
            // set movies set to the movies set collected from the input stream
            this.movies = moviesSet;
            if (!moviesIsSourceList && isSourceList) {
                // stream passed represents the source list
                moviesIsSourceList = true;
                // clear placeholders
                refreshPlaceholders("clear");
            }
            // refresh options in dropdown filters
            resetComboBoxOptions();

            // if new movies set is not the source list, refresh range filer placeholders
            if (!isSourceList) {
                // stream passed does not represent the source list
                moviesIsSourceList = false;
                // refresh range filter min/max
                setRangeFilterRanges();
                // update range filter placeholders
                refreshPlaceholders("update");
            }
        }
    }

    /**
     * Sets component names to enable conditions through switch statements.
     */
    private void setComponentNames() {
        // set filter names
        titleFilter.setName(FiltersEnum.TITLE.getFilterName());
        genreFilter.setName(FiltersEnum.GENRE.getFilterName());
        mpaRatingFilter.setName(FiltersEnum.MPA_RATING.getFilterName());

        // set range filter names
        releasedFrom.setName(FiltersEnum.RELEASED_FROM.getFilterName());
        releasedTo.setName(FiltersEnum.RELEASED_TO.getFilterName());
        imdbRatingFrom.setName(FiltersEnum.IMDB_RATING_FROM.getFilterName());
        imdbRatingTo.setName(FiltersEnum.IMDB_RATING_TO.getFilterName());
        boxOfficeEarningsFrom.setName(FiltersEnum.BOX_OFFICE_EARNINGS_FROM.getFilterName());
        boxOfficeEarningsTo.setName(FiltersEnum.BOX_OFFICE_EARNINGS_TO.getFilterName());

        // set remaining filter names
        directorFilter.setName(FiltersEnum.DIRECTOR.getFilterName());
        actorFilter.setName(FiltersEnum.ACTOR.getFilterName());
        writerFilter.setName(FiltersEnum.WRITER.getFilterName());
        languageFilter.setName(FiltersEnum.LANGUAGE.getFilterName());
    }

    /**
     * Sets the min and max of each range filter based on the MBeans in the movies set.
     */
    private void setRangeFilterRanges() {
        // set filter ranges
        getIntFilterRange(MBeans::getYear, releasedFrom, releasedTo);
        getDoubleFilterRange(MBeans::getImdbRating, imdbRatingFrom, imdbRatingTo);
        getIntFilterRange(MBeans::getBoxOffice, boxOfficeEarningsFrom, boxOfficeEarningsTo);

        // format box office earnings min/max in millions of dollars
        rangeFilterMap.put(boxOfficeEarningsFrom,
                formatAsCurrency(rangeFilterMap.get(boxOfficeEarningsFrom), "min"));
        rangeFilterMap.put(boxOfficeEarningsTo,
                formatAsCurrency(rangeFilterMap.get(boxOfficeEarningsTo), "max"));
    }

    /**
     * Updates FilterPane's GridBagContraints to account for the placement of filters and labels.
     *
     * @param x gbc.gridx
     * @param y gbc.gridy
     * @param width gbc.gridwidth
     * @param weightx gbc.weightx
     * @param anchor gbc.anchor
     * @param fill gbc.fill
     */
    private void updateGBC(Integer x, Integer y, Integer width, Integer weightx,
                           Integer anchor, Integer fill) {
        // change gbc only if param is not null
        gbc.gridx = (x != null) ? x : gbc.gridx;
        gbc.gridy = (y != null) ? y : gbc.gridy;
        gbc.gridwidth = (width != null) ? width : gbc.gridwidth;
        gbc.weightx = (weightx != null) ? weightx : gbc.weightx;
        gbc.anchor = (anchor != null) ? anchor : gbc.anchor;
        gbc.fill = (fill != null) ? fill : gbc.fill;
    }

    /**
     * Adds a JLabel to the FilterPane.
     *
     * @param filterTitle the test displayed by the label
     */
    private void addLabel(String filterTitle) {
        // update gbc
        updateGBC(0, filterRow, 4, 1, GridBagConstraints.WEST, null);

        // add label
        filterPanel.add(new JLabel(filterTitle), gbc);

        // increment filter row
        filterRow++;
    }

    /**
     * Adds filters (JTextFields and JComboBoxes) to the FilterPane.
     *
     * @param filterTitle the text displayed above the filter
     * @param filter the filter object
     */
    private void addFilter(String filterTitle, Object filter) {
        // add filter label
        addLabel(filterTitle);

        // configure combo boxes
        if (filter instanceof JComboBox) {
            configureComboBox((JComboBox<String>) filter);
        } else if (filter instanceof JTextField) {
            textFilters.add((JTextField) filter);
        }

        // update gbc
        updateGBC(0, filterRow, 4, 1, null, GridBagConstraints.HORIZONTAL);

        // add component
        filterPanel.add((Component) filter, gbc);

        // increment filter row
        filterRow++;
    }

    /**
     * Adds a filter to the FilterPane where users enter the min and max of a range of values.
     *
     * @param filterTitle the String of text displayed above the filter
     * @param filterFrom a JTextField where users enter the minimum of the range
     * @param filterTo a JTextField where users enter the maximum of the range
     */
    private void addRangeFilter(String filterTitle, JTextField filterFrom, JTextField filterTo) {
        // add title to panel
        addLabel(filterTitle);

        // create "From" and "To" labels with italic font
        JLabel fromLabel = new JLabel(FilterLabels.FROM.getFilterLabel());
        italicizeFont(fromLabel);
        JLabel toLabel = new JLabel(FilterLabels.TO.getFilterLabel());
        italicizeFont(toLabel);

        // update gbc and add "From" label
        updateGBC(0, filterRow, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        filterPanel.add(fromLabel, gbc);

        // update gbc and add "From" text field
        updateGBC(1, null, null, 1, null, GridBagConstraints.HORIZONTAL);
        // set font italic
        italicizeFont(filterFrom);
        // add min as placeholder
        setPlaceholder(filterFrom, rangeFilterMap.get(filterFrom));
        // add focus listener and add filter to panel
        filterFrom.setColumns(6);
        filterFrom.addFocusListener(this);
        filterPanel.add(filterFrom, gbc);
        // add filter to map of range filters
        rangeFilterMap.put(filterFrom, "");

        // update gbc and add "To" label
        updateGBC(2, null, null, 0, null, GridBagConstraints.NONE);
        filterPanel.add(toLabel, gbc);

        // update gbc and add "To" text field
        updateGBC(3, null, null, 1, null, GridBagConstraints.HORIZONTAL);
        // set font italic
        italicizeFont(filterTo);
        // add max as placeholder
        setPlaceholder(filterTo, rangeFilterMap.get(filterTo));
        // add focus listener and add filter to panel
        filterTo.addFocusListener(this);
        filterTo.setColumns(6);
        filterPanel.add(filterTo, gbc);
        // add filter to map of range filters
        rangeFilterMap.put(filterTo, "");

        // increment filter row
        filterRow++;
    }

    /**
     * Sets placeholder text in a JTextField to the String value passed as a param.
     *
     * @param textField the JTextField receiving the placeholder text
     * @param value a String containing the placeholder text
     */
    private void setPlaceholder(JTextField textField, String value) {
        textField.setText(value);
    }

    /* Helper Methods ------------------------------------------------------------------------------------------------*/
    /**
     * Italicizes the font in the object passed.
     *
     * @param object the object with text to italicize
     */
    private void italicizeFont(Object object) {
        // italicize JLabel text
        if (object instanceof JLabel) {
            JLabel label = (JLabel) object;
            Font font = label.getFont();
            Font italicFont = font.deriveFont(Font.ITALIC);
            label.setFont(italicFont);
        }

        // italicize JTextField text
        if (object instanceof JTextField) {
            JTextField textField = (JTextField) object;
            Font font = textField.getFont();
            Font italicFont = font.deriveFont(Font.ITALIC);
            textField.setFont(italicFont);
        }
    }

    /**
     * Finds and maps the minimum and maximum values in a double returning getter from a stream of the movies set.
     *
     * @param fieldFunction the double returning getter method
     * @param from JTextField for range filter minimum
     * @param to JTextField for range filter maximum
     */
    private void getDoubleFilterRange(ToDoubleFunction<MBeans> fieldFunction, JTextField from, JTextField to) {
        // find max/min
        OptionalDouble maxValue = movies.stream().mapToDouble(fieldFunction).max();
        OptionalDouble minValue = movies.stream().mapToDouble(fieldFunction).min();

        // convert to string
        String maxValueString = maxValue.isPresent() ? Double.toString(maxValue.getAsDouble()) : "No Max";
        String minValueString = minValue.isPresent() ? Double.toString(minValue.getAsDouble()) : "No Min";

        // map min/max values to corresponding JTextFields
        rangeFilterMap.put(from, minValueString);
        rangeFilterMap.put(to, maxValueString);
    }

    /**
     * Finds and maps the minimum and maximum values in an int returning getter from a stream of the movies set.
     *
     * @param fieldFunction the int returning getter method
     * @param from JTextField for range filter minimum
     * @param to JTextField for range filter maximum
     */
    private void getIntFilterRange(ToIntFunction<MBeans> fieldFunction, JTextField from, JTextField to) {
        // find max/min
        OptionalInt minValue = movies.stream().mapToInt(fieldFunction).min();
        OptionalInt maxValue = movies.stream().mapToInt(fieldFunction).max();


        // convert to string
        String minValueString = minValue.isPresent() ? Integer.toString(minValue.getAsInt()) : "No Min";
        String maxValueString = maxValue.isPresent() ? Integer.toString(maxValue.getAsInt()) : "No Max";

        if (Integer.parseInt(minValueString) < 0) {
            minValueString = "N/A";
        }
        if (Integer.parseInt(maxValueString) < 0) {
            maxValueString = "N/A";
        }

        rangeFilterMap.put(from, minValueString);
        rangeFilterMap.put(to, maxValueString);
    }

    /**
     * Returns a string formatted as US currency (in millions of dollars) from a double parsable String.
     *
     * @param value a String representing a dollar value
     * @param minOrMax denotes if the dollar value passed is a range minimum or maximum for rounding purposes
     * @return a String containing the dollar value of the input param formatted as US currency in millions of dollars
     */
    private String formatAsCurrency(String value, String minOrMax) {

        if (value.equalsIgnoreCase("N/A")) {
            return value;
        }

        // set currency formatter to US locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        // convert to string and remove trailing .00
        double valueInMillions = Double.parseDouble(value) / 1_000_000;
        String stringValue = "";

        // Handle rounding down for min value and up for max value
        if (minOrMax.equals("min")) {
            double valueRoundedDown = Math.floor(valueInMillions * 100) / 100;
            stringValue = currencyFormatter.format(valueRoundedDown);
        } else if (minOrMax.equals("max")) {
            double valueRoundedUp = Math.ceil(valueInMillions * 100) / 100;
            stringValue = currencyFormatter.format(valueRoundedUp);
        }

        String currencyInMillions = new String(stringValue + "M");

        return currencyInMillions;

    }

    /**
     * Converts and returns a String containing a dollar value in millions of dollars to a String representing that
     * same value as an unformatted number.
     *
     * @param value a String representing a dollar value in millions of dollars
     * @return a String representing the input param dollar value as an unformatted number
     */
    private String formatFromMillions(String value) {
        // strip $ and M if present
        String processedValue = value.replaceAll("[^0-9.]", "");
        double processedDouble  = Double.parseDouble(processedValue);

        // convert to double from millions and return as String
        Double expandedDouble = processedDouble * 1_000_000;
        return String.format("%.0f", expandedDouble);
    }

    /**
     * Sets the options inside a JComboBox (dropdown) filter based on unique MBean attributes.
     *
     * @param comboBox the dropdown filter in which to add options
     */
    private void configureComboBox(JComboBox<String> comboBox) {
        // initialize empty tree sets
        Set<String> uniqueGenres = new TreeSet<>();
        Set<String> uniqueMpaRatings = new TreeSet<>();
        Set<String> uniqueLanguages = new TreeSet<>();

        FiltersEnum filter = getFilterByEnum(comboBox.getName());

        switch (filter) {
            case GENRE:
                // add options to set
                for (MBeans movie : movies) {
                    for (String genre : movie.getGenre()) {
                        uniqueGenres.add(genre);
                    }
                }
                // add elements to combo box
                for (String genre : uniqueGenres) {
                    comboBox.addItem(genre);
                }
                break;
            case MPA_RATING:
                // add options to set
                for (MBeans movie : movies) {
                    uniqueMpaRatings.add(movie.getRated());
                }
                // add elements to combo box
                for (String mpaRating : uniqueMpaRatings) {
                    comboBox.addItem(mpaRating);
                }
                break;
            case LANGUAGE:
                // add options to set
                for (MBeans movie : movies) {
                    // unpack options from string list
                    for (String language : movie.getLanguage()) {
                        uniqueLanguages.add(language);
                    }
                }
                // add elements to combo box
                for (String language : uniqueLanguages) {
                    comboBox.addItem(language);
                }
                break;
            default:
                System.out.println("Invalid text field passed");
        }
        // deselect options in combobox
        comboBox.setSelectedIndex(-1);
        // add combobox to set of dropdown filters
        dropDownFilters.add(comboBox);
    }

    /**
     * Purges filter text and drop down filter options for all filters in the FilterPane.
     */
    public void clearFilterOptions() {
        for (JComboBox filter : dropDownFilters) {
            filter.removeAllItems();
        }
        for (JTextField filter : textFilters) {
            filter.setText("");
        }
        for (JTextField filter : rangeFilterMap.keySet()) {
            filter.setText("");
        }
    }

    /**
     * Resets all JComboBox options in the FilterPane based on the FilterPane's movies set.
     */
    public void resetComboBoxOptions() {
        for (JComboBox filter : dropDownFilters) {
            // store selected option in combo box
            Object selectedItem = filter.getSelectedItem();
            // remove options from combo box
            filter.removeAllItems();
            // reconfigure combobox
            configureComboBox(filter);
            // select stored option in combo box
            filter.setSelectedItem(selectedItem);
        }
    }

    /**
     * Purges text in text filters set.
     */
    public void resetTextFilters() {
        for (JTextField filter : textFilters) {
            filter.setText("");
        }
    }

    /**
     * Clears all filters in the FilterPane of user selections.
     */
    public void resetFilterOptions() {
        // reset options in dropdown filters
        resetComboBoxOptions();
        resetTextFilters();

        // reset placeholders for ranges
        for (JTextField filter : rangeFilterMap.keySet()) {
            setPlaceholder(filter, rangeFilterMap.get(filter));
        }
    }

    /**
     * Clears or updates placeholder text from range filters based on ranges in the FilterPane's movies set.
     *
     * @param updateOrClearPlaceholders String denoting if the method should clear or update placeholders
     */
    public void refreshPlaceholders(String updateOrClearPlaceholders) {
        if (updateOrClearPlaceholders.equalsIgnoreCase("update")) {
            // update range filter placeholders to reflect new ranges
            for (JTextField filter : rangeFilterMap.keySet()) {
                filter.setText(rangeFilterMap.get(filter));
            }
        } else if (updateOrClearPlaceholders.equalsIgnoreCase("clear")) {
            // clear range filter placeholders
            for (JTextField filter : rangeFilterMap.keySet()) {
                filter.setText("");
            }
        }
    }

    /**
     * Resets the placeholder text in a JTextField to the min and max of the initial range.
     *
     * @param textField the JTextField to reset its placeholder
     */
    public void resetPlaceholder(JTextField textField) {
        FiltersEnum filter = getFilterByEnum(textField.getName());
        switch (filter) {
            case RELEASED_FROM:
                if (releasedFrom.getText().isEmpty()) {
                    releasedFrom.setText(rangeFilterMap.get(releasedFrom));
                }
                break;
            case RELEASED_TO:
                if (releasedTo.getText().isEmpty()) {
                    releasedTo.setText(rangeFilterMap.get(releasedTo));
                }
                break;
            case IMDB_RATING_FROM:
                if (imdbRatingFrom.getText().isEmpty()) {
                    imdbRatingFrom.setText(rangeFilterMap.get(imdbRatingFrom));
                }
                break;
            case IMDB_RATING_TO:
                if (imdbRatingTo.getText().isEmpty()) {
                    imdbRatingTo.setText(rangeFilterMap.get(imdbRatingTo));
                }
                break;
            case BOX_OFFICE_EARNINGS_FROM:
                if (boxOfficeEarningsFrom.getText().isEmpty() || boxOfficeEarningsFrom.getText().startsWith("-")) {
                    boxOfficeEarningsFrom.setText(rangeFilterMap.get(boxOfficeEarningsFrom));
                } else if (boxOfficeEarningsFrom.getText().equalsIgnoreCase("N/A")) {
                    boxOfficeEarningsFrom.setText("N/A");
                } else {
                    reformatBoxOfficeEarnings(boxOfficeEarningsFrom);
                }
                break;
            case BOX_OFFICE_EARNINGS_TO:
                if (boxOfficeEarningsTo.getText().isEmpty() || boxOfficeEarningsTo.getText().startsWith("-")) {
                    boxOfficeEarningsTo.setText(rangeFilterMap.get(boxOfficeEarningsTo));
                } else if (boxOfficeEarningsTo.getText().equalsIgnoreCase("N/A")) {
                    boxOfficeEarningsTo.setText("N/A");
                } else {
                    reformatBoxOfficeEarnings(boxOfficeEarningsTo);
                }
                break;
            default:
                System.out.println("Invalid text field passed");
        }
    }

    /**
     * Reformats text in box office earnings range filter to dollars in millions.
     *
     * @param boxOfficeEarnings JTextField containing box office earnings minimum or maximum
     */
    public void reformatBoxOfficeEarnings(JTextField boxOfficeEarnings) {
        FiltersEnum filter = getFilterByEnum(boxOfficeEarnings.getName());
        switch (filter) {
            case BOX_OFFICE_EARNINGS_FROM:
                if (!boxOfficeEarnings.getText().isEmpty()) {
                    String processedBoxOfficeEarningsMin = boxOfficeEarningsFrom.getText()
                            .replaceAll("[^0-9.]", "");
                    try {
                        double boxOfficeEarningsMinDouble = Double.parseDouble(processedBoxOfficeEarningsMin);
                        boxOfficeEarningsFrom.setText(
                                formatAsCurrency(formatFromMillions(boxOfficeEarningsFrom.getText()), "min"));
                    } catch (NumberFormatException e) {
                        setPlaceholder(boxOfficeEarningsFrom, rangeFilterMap.get(boxOfficeEarningsFrom));
                    }
                }
                break;
            case BOX_OFFICE_EARNINGS_TO:
                if (!boxOfficeEarnings.getText().isEmpty()) {
                    String processedBoxOfficeEarningsMax = boxOfficeEarningsTo.getText()
                            .replaceAll("[^0-9.]", "");
                    try {
                        double boxOfficeEarningsMaxDouble = Double.parseDouble(processedBoxOfficeEarningsMax);
                        boxOfficeEarningsTo.setText(
                                formatAsCurrency(formatFromMillions(boxOfficeEarningsTo.getText()), "max"));
                    } catch (NumberFormatException e) {
                        setPlaceholder(boxOfficeEarningsTo, rangeFilterMap.get(boxOfficeEarningsTo));
                    }
                }
                break;
            default:
                System.out.println("Invalid text field passed");
        }
    }

    /**
     * Returns the FiltersEnum enum associated with a filter name passed as a String.
     *
     * @param filter the String name of a filter in the FilterPane
     * @return the FiltersEnum enum associated with the String filter
     */
    private FiltersEnum getFilterByEnum(String filter) {
        for (FiltersEnum filters : FiltersEnum.values()) {
            if (filters.getFilterName().equalsIgnoreCase(filter)) {
                return filters;
            }
        }
        String error = String.format("Unsupported filter: '%s'", filter);
        System.out.println(error);
        return null;
    }

    /* Actions -------------------------------------------------------------------------------------------------------*/
    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == applyFiltersButton) {
            System.out.println("[FilterPane] - Filters applied");
        } else if (e.getSource() == clearFiltersButton) {
            System.out.println("[FilterPane] - Filters cleared");
        }
    }

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param e the event to be processed
     */
    @Override
    public void focusGained(FocusEvent e) {
        // clear text when focus is gained
        JTextField textField = (JTextField) e.getSource();
        textField.setText("");
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param e the event to be processed
     */
    @Override
    public void focusLost(FocusEvent e) {
        // reset placeholders when focus is lost, only if current set of movies is not the source list
        JTextField textField = (JTextField) e.getSource();
        if (!moviesIsSourceList) {
            resetPlaceholder(textField);
        } else {
            reformatBoxOfficeEarnings(textField);
        }
    }

    /**
     * Triggers actions outside the FilterPane from event actions performed by FilterPane components.
     *
     * @param features action tiggered by the FilterPane
     */
    public void bindFeatures(IFeature features) {
        applyFiltersButton.addActionListener(e -> features.applyFilters());
        clearFiltersButton.addActionListener(e -> features.clearFiltersAndReloadRecords());
    }

    /* enums ---------------------------------------------------------------------------------------------------------*/
    /** Encapsulates the name of a FilterPane filter in an enum. */
    enum FiltersEnum {

        /** FiltersEnums. */
        TITLE("titleFilter"), GENRE("genreFilter"), MPA_RATING("mparatingFilter"),
        RELEASED_FROM("releasedFromFilter"), RELEASED_TO("releasedToFilter"),
        IMDB_RATING_FROM("imdbRatingFromFilter"), IMDB_RATING_TO("imdbRatingToFilter"),
        BOX_OFFICE_EARNINGS_FROM("boxOfficeEarningsFromFilter"),
        BOX_OFFICE_EARNINGS_TO("boxOfficeEarningsToFilter"),
        DIRECTOR("directorFilter"), ACTOR("actorFilter"),
        WRITER("writerFilter"), LANGUAGE("languageFilter");

        /** String representing the name of a FilterPane filter. */
        private final String filterName;

        /** Public constructor.
         *
         * @param filterName the filter name associated with the FiltersEnum
         */
        FiltersEnum(String filterName) {
            this.filterName = filterName;
        }

        /**
         * Returns the String, representing a filter name, associated with a FiltersEnum enum.
         *
         * @return String, representing a filter name, associated with a FiltersEnum enum
         */
        public String getFilterName() {
            return filterName;
        }
    }

    /** Encapsulates the text in a JLabel, representing a FilterPane filter title, in an enum. */
    enum FilterLabels {

        /** FilterLabels Enums. */
        TITLE("Title:"), GENRE("Genre:"), MPA_RATING("MPA Rating:"),
        RELEASED("Year Released:"), IMDB_RATING("IMDB Rating:"),
        BOX_OFFICE_EARNINGS("Box Office Earnings: ($ millions)"), DIRECTOR("Director:"), ACTOR("Actor:"),
        WRITER("Writer:"), LANGUAGE("Language:"), FROM("From: "), TO("To: ");

        /** String representing the text in a JLabel above a FilterPane filter. */
        private final String filterLabel;

        /** Public constructor.
         *
         * @param filterLabel the filter label associated with the FilterLabels enum
         */
        FilterLabels(String filterLabel) {
            this.filterLabel = filterLabel;
        }

        /**
         * Returns the String, representing a filter title, associated with a FilterLabels enum.
         *
         * @return String, representing a filter title, associated with a FilterLabels enum
         */
        public String getFilterLabel() {
            return filterLabel;
        }
    }
}


