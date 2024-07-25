package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static group5.model.formatters.MBeansLoader.loadMBeansFromAPI;

/** Contains and passes filters to controller for selected movie list */
public class FilterPane extends JPanel implements ActionListener, FocusListener {

    /** List of movies. */
    private List<MBeans> movies = new ArrayList<>();

    // Panels
    /** Panel containing filters. */
    private JPanel filterPanel = new JPanel(new GridBagLayout());
    /** Panel containing buttons. */
    private JPanel buttonPanel  = new JPanel(new GridLayout(0, 2));

    // Filters
    /** Title filter. */
    private JTextField titleFilter = new JTextField();
    /** Content Type filter. */
    private JComboBox contentTypeFilter = new JComboBox();
    /** Genre filter. */
    private JComboBox genreFilter = new JComboBox();
    /** MPA Rating filter. */
    private JComboBox mpaRatingFilter = new JComboBox();
    /** Director filter. */
    private JTextField directorFilter = new JTextField();
    /** Actor filter. */
    private JTextField actorFilter = new JTextField();
    /** Writer filter. */
    private JTextField writerFilter = new JTextField();
    /** Language filter. */
    private JComboBox languageFilter = new JComboBox();
    /** Country of Origin filter. */
    private JComboBox countryOfOriginFilter = new JComboBox();

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

    // Range filters min/max placeholders
    /** Min and max of year released data in movies. */
    private String[] releasedRange;
    /** Min and max of IMDB rating data in movies. */
    private String[] imdbRatingRange;
    /** Min and max of Box office earnings data in movies. */
    private String[] boxOfficeRange;

    // Buttons
    /** Apply filters button */
    private JButton applyFilterButton = new JButton("Apply Filters");
    /** Clear filters button */
    private JButton clearFilterButton = new JButton("Clear Filters");

    // GridBagConstraints & grid row int
    /** Class global GridBagConstraints. */
    private GridBagConstraints gbc = new GridBagConstraints();
    /** Filter row to place filters descending using gbc.gridy */
    private int filterRow = 0;

    /** Public constructor. */
    public FilterPane() {
        super(new BorderLayout());

        // set list of movies
        setMoviesSetup();

        // name components to enable switch statements
        setComponentNames();

        // configure gbc
        gbc.insets = new Insets(5, 5, 5, 5);
        updateGBC(null, null, null, null, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

        // add panels
        add(filterPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // add some filters
        addFilter("Title:", titleFilter);
        addFilter("Content Type:", contentTypeFilter);
        addFilter("Genre(s):", genreFilter);
        addFilter("MPA Rating:", mpaRatingFilter);

        // add range filters
        addRangeFilter("Released:", releasedFrom, releasedTo, releasedRange[0], releasedRange[1]);
        addRangeFilter("IMDB Rating:", imdbRatingFrom, imdbRatingTo, imdbRatingRange[0], imdbRatingRange[1]);
        addRangeFilter("Box Office Earnings: (millions)", boxOfficeEarningsFrom, boxOfficeEarningsTo,
                boxOfficeRange[0], boxOfficeRange[1]);

        // add remaining filters
        addFilter("Director(s):", directorFilter);
        addFilter("Actor(s)", actorFilter);
        addFilter("Writer(s)", writerFilter);
        addFilter("Language(s):", languageFilter);
        addFilter("Country of Origin:", countryOfOriginFilter);

        // add buttons
        buttonPanel.add(applyFilterButton);
        buttonPanel.add(clearFilterButton);

        // add action listeners
        titleFilter.addActionListener(this);
        directorFilter.addActionListener(this);
        actorFilter.addActionListener(this);
        writerFilter.addActionListener(this);
        applyFilterButton.addActionListener(this);
        clearFilterButton.addActionListener(this);
    }

    public String getFilteredTitle() {
        return titleFilter.getText();
    }

    public String getFilteredContentType() {
        try {
            return contentTypeFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFilteredGenre() {
        try {
            return genreFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFilteredMpaRating() {
        try {
            return mpaRatingFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFilteredReleasedMin() {
        return releasedFrom.getText();
    }

    public String getFilteredReleasedMax() {
        return releasedTo.getText();

    }

    public String getFilteredImdbRatingMin() {
        return imdbRatingFrom.getText();

    }

    public String getFilteredImdbRatingMax() {
        return imdbRatingTo.getText();

    }

    public String getFilteredBoxOfficeEarningsMin() {
        return formatFromMillions(boxOfficeEarningsFrom.getText());

    }

    public String getFilteredBoxOfficeEarningsMax() {
        return formatFromMillions(boxOfficeEarningsTo.getText());

    }

    public String getFilteredDirectorFilter() {
        return directorFilter.getText();
    }

    public String getFilteredActorFilter() {
        return actorFilter.getText();

    }

    public String getFilteredWriterFilter() {
        return writerFilter.getText();
    }

    public String getFilteredLanguageFilter() {
        try {
            return languageFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFilteredCountryOfOriginFilter() {
        try {
            return countryOfOriginFilter.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * Enables conditions through switch statements instead of if-else blocks.
     */
    private void setComponentNames() {
        // set filter names
        titleFilter.setName("titleFilter");
        contentTypeFilter.setName("contentTypeFilter");
        genreFilter.setName("genreFilter");
        mpaRatingFilter.setName("mpaRatingFilter");

        // set range filter names
        releasedFrom.setName("releasedFrom");
        releasedTo.setName("releasedTo");
        imdbRatingFrom.setName("imdbRatingFrom");
        imdbRatingTo.setName("imdbRatingTo");
        boxOfficeEarningsFrom.setName("boxOfficeEarningsFrom");
        boxOfficeEarningsTo.setName("boxOfficeEarningsTo");

        // set remaining filter names
        directorFilter.setName("directorFilter");
        actorFilter.setName("actorFilter");
        writerFilter.setName("writerFilter");
        languageFilter.setName("languageFilter");
        countryOfOriginFilter.setName("countryOfOriginFilter");
    }

    private void setRangeFilterRanges() {
        // set filter ranges
        releasedRange = getIntFilterRange(MBeans::getYear);
        imdbRatingRange = getDoubleFilterRange(MBeans::getImdbRating);
        boxOfficeRange = getIntFilterRange(MBeans::getBoxOffice);

        // format box office range as US currency
        for (int i = 0; i < boxOfficeRange.length; i++) {
            boxOfficeRange[i] = formatAsCurrency(boxOfficeRange[i]);
        }
    }

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

    private void addLabel(String filterTitle) {
        // update gbc
        updateGBC(0, filterRow, 4, 1, GridBagConstraints.WEST, null);

        // add label
        filterPanel.add(new JLabel(filterTitle), gbc);

        // increment filter row
        filterRow++;
    }

    private void addFilter(String filterTitle, Object filter) {
        // add filter label
        addLabel(filterTitle);

        // configure combo boxes
        if (filter instanceof JComboBox) {
            configureComboBox((JComboBox<String>) filter);
        }

        // update gbc
        updateGBC(0, filterRow, 4, 1, null, GridBagConstraints.HORIZONTAL);

        // add component
        filterPanel.add((Component) filter, gbc);

        // increment filter row
        filterRow++;
    }

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

    private String[] getDoubleFilterRange(ToDoubleFunction<MBeans> fieldFunction) {
        // find max/min
        OptionalDouble maxValue = movies.stream().mapToDouble(fieldFunction).max();
        OptionalDouble minValue = movies.stream().mapToDouble(fieldFunction).min();

        // convert to string
        String maxValueString = maxValue.isPresent() ? Double.toString(maxValue.getAsDouble()) : "No Max";
        String minValueString = minValue.isPresent() ? Double.toString(minValue.getAsDouble()) : "No Min";

        return new String[] {minValueString, maxValueString};
    }

    private String[] getIntFilterRange(ToIntFunction<MBeans> fieldFunction) {
        // find max/min
        OptionalInt maxValue = movies.stream().mapToInt(fieldFunction).max();
        OptionalInt minValue = movies.stream().mapToInt(fieldFunction).min();

        // convert to string
        String maxValueString = maxValue.isPresent() ? Integer.toString(maxValue.getAsInt()) : "No Max";
        String minValueString = minValue.isPresent() ? Integer.toString(minValue.getAsInt()) : "No Min";

        return new String[] {minValueString, maxValueString};
    }

    private String[] getStringFilterRange(Function<MBeans, String> fieldFunction) {
        // find max/min
        OptionalDouble maxValue = movies.stream()
                .map(fieldFunction)
                .mapToDouble(this::customParseDouble)
                .filter(v -> v!= Double.MIN_VALUE)
                .max();

        OptionalDouble minValue = movies.stream()
                .map(fieldFunction)
                .mapToDouble(this::customParseDouble)
                .filter(v -> v!= Double.MIN_VALUE)
                .min();

        // convert to string
        String maxValueString = maxValue.isPresent() ? Double.toString(maxValue.getAsDouble()) : "No Max";
        String minValueString = minValue.isPresent() ? Double.toString(minValue.getAsDouble()) : "No Min";

        return new String[] {minValueString, maxValueString};
    }

    private double customParseDouble(String value) {
        // remove leading dollar sign
        try {
            String processedValue = value.replaceAll("\\D", "");
            return Double.parseDouble(processedValue);
        } catch (NumberFormatException e) {
            // return standard value if exception, which is filtered out in getStringFilterRange()
            return Double.MIN_VALUE;
        }
    }

    private String formatAsCurrency(String value) {
        // set currency formatter to US locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        // convert to string and remove trailing .00
        String currencyStringDouble = currencyFormatter.format(Double.parseDouble(value) / 1_000_000);
        String currencyInMillions = new String(currencyStringDouble + "M");

        return currencyInMillions;
    }

    private String formatFromMillions(String value) {
        // strip $ and M if present
        String processedValue = value.replaceAll("[^0-9.]", "");
        double processedDouble  = Double.parseDouble(processedValue);

        // convert to double from millions and return as String
        Double expandedDouble = processedDouble * 1_000_000;
        return String.format("%.0f", expandedDouble);
    }

    private void addRangeFilter(String filterTitle, JTextField filterFrom,
                                JTextField filterTo, String rangeMin, String rangeMax) {
        // add title to panel
        addLabel(filterTitle);

        // create "From" and "To" labels with italic font
        JLabel fromLabel = new JLabel("From:");
        italicizeFont(fromLabel);
        JLabel toLabel = new JLabel("To:");
        italicizeFont(toLabel);

        // update gbc and add "From" label
        updateGBC(0, filterRow, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE);
        filterPanel.add(fromLabel, gbc);

        // update gbc and add "From" text field
        updateGBC(1, null, null, 1, null, GridBagConstraints.HORIZONTAL);
        // set font italic
        italicizeFont(filterFrom);
        // add min as placeholder
        setPlaceholder(filterFrom, rangeMin);
        // add focus listener and add filter to panel
        filterFrom.addFocusListener(this);
        filterPanel.add(filterFrom, gbc);

        // update gbc and add "To" label
        updateGBC(2, null, null, 0, null, GridBagConstraints.NONE);
        filterPanel.add(toLabel, gbc);

        // update gbc and add "To" text field
        updateGBC(3, null, null, 1, null, GridBagConstraints.HORIZONTAL);
        // set font italic
        italicizeFont(filterTo);
        // add max as placeholder
        setPlaceholder(filterTo, rangeMax);
        // add focus listener and add filter to panel
        filterTo.addFocusListener(this);
        filterPanel.add(filterTo, gbc);

        // increment filter row
        filterRow++;
    }

    private void configureComboBox(JComboBox<String> comboBox) {
        // initialize empty tree sets
        Set<String> uniqueContentType = new TreeSet<>();
        Set<String> uniqueGenres = new TreeSet<>();
        Set<String> uniqueMpaRatings = new TreeSet<>();
        Set<String> uniqueLanguages = new TreeSet<>();
        Set<String> uniqueCountries = new TreeSet<>();

        switch (comboBox.getName()) {
            case "contentTypeFilter":
                // add options to set
                for (MBeans movie : movies) {
                    uniqueContentType.add(movie.getType());
                }
                // add elements to combo box
                for (String contentType : uniqueContentType) {
                    comboBox.addItem(contentType);
                }
            case "genreFilter":
                // add options to set
                for (MBeans movie : movies) {
                    uniqueGenres.add(movie.getGenre());
                }
                // add elements to combo box
                for (String genre : uniqueGenres) {
                    comboBox.addItem(genre);
                }
            case "mpaRatingFilter":
                // add options to set
                for (MBeans movie : movies) {
                    uniqueMpaRatings.add(movie.getRated());
                }
                // add elements to combo box
                for (String mpaRating : uniqueMpaRatings) {
                    comboBox.addItem(mpaRating);
                }
            case "languageFilter":
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
            case "countryOfOriginFilter":
                // add options to set
                for (MBeans movie : movies) {
                    // unpack options from string list
                    for (String country : movie.getCountry()) {
                        uniqueCountries.add(country);
                    }
                }
                // add elements to combo box
                for (String country : uniqueCountries) {
                    comboBox.addItem(country);
                }
        }
        // deselect options in combobox
        comboBox.setSelectedIndex(-1);
    }

    private void setMovies(Stream<MBeans> movies) {
        this.movies = movies.toList();
        // reset filter ranges and clear filter options
        setRangeFilterRanges();
        resetFilterOptions();
    }

    private void setMoviesSetup() {
        MBeans theMatrix;
        MBeans titanic;

        theMatrix = loadMBeansFromAPI("The Matrix", "1999", "movie");
        titanic = loadMBeansFromAPI("Titanic", "1997", "movie");
        movies.add(theMatrix);
        movies.add(titanic);

        setRangeFilterRanges();
    }

    public void resetFilterOptions() {
        // clear options
        titleFilter.setText("");
        contentTypeFilter.setSelectedIndex(-1);
        genreFilter.setSelectedIndex(-1);
        mpaRatingFilter.setSelectedIndex(-1);

        // reset placeholders for ranges
        setPlaceholder(releasedFrom, releasedRange[0]);
        setPlaceholder(releasedTo, releasedRange[1]);
        setPlaceholder(imdbRatingFrom, imdbRatingRange[0]);
        setPlaceholder(imdbRatingTo, imdbRatingRange[1]);
        setPlaceholder(boxOfficeEarningsFrom, boxOfficeRange[0]);
        setPlaceholder(boxOfficeEarningsTo, boxOfficeRange[1]);

        // clear options
        directorFilter.setText("");
        actorFilter.setText("");
        writerFilter.setText("");
        languageFilter.setSelectedIndex(-1);
        countryOfOriginFilter.setSelectedIndex(-1);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // @Wing decide how you want apply/clear message sent to BaseView/controller and I can reconfigure

        if (e.getSource() == applyFilterButton) {
            System.out.println("Filters applied");

            System.out.println("Title: " + getFilteredTitle());
            System.out.println("Content Type: " + getFilteredContentType());
            System.out.println("Genre: " + getFilteredGenre());
            System.out.println("Rating: " + getFilteredMpaRating());
            System.out.println("Released From: " + getFilteredReleasedMin());
            System.out.println("Released To: " + getFilteredReleasedMax());
            System.out.println("IMDB From: " + getFilteredImdbRatingMin());
            System.out.println("IMDB To: " + getFilteredImdbRatingMax());
            System.out.println("Box Office From: " + getFilteredBoxOfficeEarningsMin());
            System.out.println("Box Office To: " + getFilteredBoxOfficeEarningsMax());
            System.out.println("Director: " + getFilteredDirectorFilter());
            System.out.println("Actor: " + getFilteredActorFilter());
            System.out.println("Writer: " + getFilteredWriterFilter());
            System.out.println("Language: " + getFilteredLanguageFilter());
            System.out.println("Country: " + getFilteredCountryOfOriginFilter());

        } else if (e.getSource() == clearFilterButton) {
            System.out.println("Filters cleared");

            resetFilterOptions();
        }
    }

    private void setPlaceholder(JTextField textField, String value) {
        textField.setText(value);
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
        // reset placeholders when focus is lost
        JTextField textField = (JTextField) e.getSource();
        resetPlaceholder(textField);
    }

    public void resetPlaceholder(JTextField textField) {
        switch (textField.getName()) {
            case "releasedFrom":
                if (releasedFrom.getText().isEmpty()) {
                    releasedFrom.setText(releasedRange[0]);
                }
                break;
            case "releasedTo":
                if (releasedTo.getText().isEmpty()) {
                    releasedTo.setText(releasedRange[1]);
                }
                break;
            case "imdbRatingFrom":
                if (imdbRatingFrom.getText().isEmpty()) {
                    imdbRatingFrom.setText(imdbRatingRange[0]);
                }
                break;
            case "imdbRatingTo":
                if (imdbRatingTo.getText().isEmpty()) {
                    imdbRatingTo.setText(imdbRatingRange[1]);
                }
                break;
            case "boxOfficeEarningsFrom":
                if (boxOfficeEarningsFrom.getText().isEmpty()) {
                    boxOfficeEarningsFrom.setText(boxOfficeRange[0]);
                } else {
                    boxOfficeEarningsFrom.setText(
                            formatAsCurrency(formatFromMillions(boxOfficeEarningsFrom.getText())));
                }
                break;
            case "boxOfficeEarningsTo":
                if (boxOfficeEarningsTo.getText().isEmpty()) {
                    boxOfficeEarningsTo.setText(boxOfficeRange[1]);
                } else {
                    boxOfficeEarningsTo.setText(
                            formatAsCurrency(formatFromMillions(boxOfficeEarningsTo.getText())));
                }
                break;
            default:
                System.out.println("Invalid text field passed");
        }
    }

    public void bindFeatures(IFeature features) {

    }

    public static void main(String[] args) {

        /* Keeping this here so you can see how I was viewing the filter pane while building. */

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setMinimumSize(new Dimension(340, 120));

        FilterPane filterPane = new FilterPane();

        JScrollPane scrollPane = new JScrollPane(filterPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame.getContentPane().add(scrollPane);

        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
