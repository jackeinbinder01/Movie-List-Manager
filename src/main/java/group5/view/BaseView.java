package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.stream.Stream;

import static java.awt.Font.PLAIN;

public class BaseView extends JFrame implements IView {
    private final static String APP_TITLE = "Movie List Manager";
    private final static int DEFAULT_WIDTH = 1024;
    private final static int DEFAULT_HEIGHT = 700;
    private final static int DEFAULT_FONT_SIZE = 10;

    FilterPane filterPane;
    ListPane listPane;
    DetailsPane detailsPane;

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

    @Override
    public void setDetailsPaneEntry(MBeans record) {
        detailsPane.setMedia(record);
    }


    @Override
    public void clearTableSelection() {
        listPane.getCurrentTable().clearSelection();
    }

    @Override
    public void setSourceTableRecords(Stream<MBeans> records, String[] userListNames, boolean[][] recordUserListMatrix) {
        System.out.println("[BaseView] setMainTableRecords");
        listPane.setSourceTable(records, userListNames, recordUserListMatrix);
    }

    @Override
    public FilterPane getFilterPane() {
        return filterPane;
    }

    @Override
    public DetailsPane getDetailsPane() {
        return detailsPane;
    }


}
