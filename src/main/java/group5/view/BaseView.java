package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.stream.Stream;

import static java.awt.Font.PLAIN;

/**
 * This class represents the base view for a Movie List Manager application.
 * It implements the {@link IView} interface and provides a basic UI structure
 * with components for filtering, listing movies, and displaying movie details.
 */
public class BaseView extends JFrame implements IView {
    /**
     * Default application title.
     */
    private static final String APP_TITLE = "Movie List Manager";

    /**
     * The default width of the window in pixels.
     */
    private static final int DEFAULT_WIDTH = 1024;

    /**
     * The default height of the window in pixels.
     */
    private static final int DEFAULT_HEIGHT = 700;

    /**
     * The default font size used in the UI.
     */
    private static final int DEFAULT_FONT_SIZE = 10;

    /**
     * The filter pane, responsible for providing movie filtering options.
     */
    private FilterPane filterPane;

    /**
     * The list pane, responsible for displaying a list of movies.
     */
    private ListPane listPane;

    /**
     * The details pane, responsible for showing detailed information about a selected movie.
     */
    private DetailsPane detailsPane;

    /**
     * Constructor for the base view.
     */
    public BaseView() {
        super(APP_TITLE);

        // Let the backstage crew do their thing
        setVisible(false);

        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating Base Pane
        JPanel basePane = new JPanel();

        // set font for all View components based on 10pt default system font from base pane
        Font systemDefaultFont = new Font(basePane.getFont().getFontName(), PLAIN, DEFAULT_FONT_SIZE);
        AppFont.setAppFont(new FontUIResource(systemDefaultFont));

        basePane.setLayout(new BorderLayout());
        add(basePane);

        // Creating the Functional Panes
        filterPane = new FilterPane();
        listPane = new ListPane();
        detailsPane = new DetailsPane();

        // Attaching the Functional Panes to the Base Pane
        basePane.add(filterPane, BorderLayout.WEST);
        basePane.add(listPane, BorderLayout.CENTER);
        basePane.add(detailsPane, BorderLayout.EAST);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to set the visibility of the filter, list, and details panes.
     */
    @Override
    public void display() {
        filterPane.setVisible(true);
        listPane.setVisible(true);
        detailsPane.setVisible(true);
        this.pack();
        setVisible(true);
    }


    /**
     * {@inheritDoc}
     * <br>
     * Implemented to bind the features to the filter, list, and details panes.
     */
    @Override
    public void bindFeatures(IFeature features) {
        System.out.println("[BaseView] BaseView is binding features...");
        filterPane.bindFeatures(features);
        listPane.bindFeatures(features);
        detailsPane.bindFeatures(features);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to pass the stream of records to the watchlist specified by the index to the list pane.
     */
    public void setUserTableRecords(Stream<MBeans> records, int userListIndex) {
        System.out.println("[BaseView] setUserTableRecords");
        listPane.setUserTable(records, userListIndex);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to create a new user table in the list pane.
     */
    public void addUserTable(String userListName) {
        System.out.println("[BaseView] createUserTable");
        listPane.createUserTableTab(userListName);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to get the active tab in the list pane.
     */
    public int getActiveTab() {
        return listPane.getActiveTab();
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to set the active tab in the list pane.
     */
    @Override
    public void setActiveTab(int tabIdx) {
        listPane.setActiveTab(tabIdx);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to show an JOptionPane dialog.
     */
    @Override
    public void showAlertDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * {@inheritDoc}
     * <br>
     */
    @Override
    public void setDetailsPaneEntry(MBeans record) {
        detailsPane.setMedia(record);
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to clear all selections in the active table in the list pane.
     */
    @Override
    public void clearTableSelection() {
        listPane.getCurrentTable().clearSelection();
    }

    /**
     * {@inheritDoc}
     * <br>
     * Implemented to pass the stream of records to the source table in the list pane.
     */
    @Override
    public void setSourceTableRecords(Stream<MBeans> records, String[] listNames, boolean[][] recordUserListMatrix) {
        System.out.println("[BaseView] setMainTableRecords");
        listPane.setSourceTable(records, listNames, recordUserListMatrix);
    }

    /**
     * {@inheritDoc}
     * <br>
     */
    @Override
    public FilterPane getFilterPane() {
        return filterPane;
    }

    /**
     * {@inheritDoc}
     * <br>
     */
    @Override
    public DetailsPane getDetailsPane() {
        return detailsPane;
    }


}
