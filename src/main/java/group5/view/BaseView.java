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
    private final static int DEFAULT_HEIGHT = 600;
    private final static int DEFAULT_FONT_SIZE = 10;

    FilterPane filterPane;
    ListPaneV2 listPane;
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
        listPane = new ListPaneV2();
        detailsPane = new DetailsPane();


        // Attaching the Functional Panes to the Base Pane
        basePane.add(filterPane, BorderLayout.WEST);
        basePane.add(listPane, BorderLayout.CENTER);
        basePane.add(detailsPane, BorderLayout.EAST);

    }


    @Override
    public void display() {
        filterPane.setVisible(true);
        listPane.setVisible(true);
        detailsPane.setVisible(true);
        this.pack();
        setVisible(true);
    }

    @Override
    public void bindFeatures(IFeature features) {
        System.out.println("[BaseView] BaseView is binding features...");
        filterPane.bindFeatures(features);
        listPane.bindFeatures(features);
        detailsPane.bindFeatures(features);
    }

    /**
     * Set the user list pane entries
     * Prompts the view to create a new tab pane if the userListId is not found
     * @param userListIndex the index of the user list
     * @param records the MBeans to set
     */
    public void setUserTableRecords(Stream<MBeans> records, int userListIndex) {
        System.out.println("[BaseView] setUserTableRecords");
        listPane.setUserTable(records, userListIndex);
    }

    public void addUserTable(String userListName) {
        System.out.println("[BaseView] createUserTable");
        listPane.createUserTableTab(userListName);
    }

    public int getActiveTab() {
        return listPane.getActiveTab();
    }

    @Override
    public void setActiveTab(int tabIdx) {
        listPane.setActiveTab(tabIdx);
    }

    @Override
    public void showAlertDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Set (or update) the details pane entry
     *
     * @param record the record to set
     */
    @Override
    public void setDetailsPaneEntry(MBeans record) {
        detailsPane.setMedia(record);
    }

    /**
     * Clear the table selection
     */
    @Override
    public void clearTableSelection() {
        listPane.getCurrentTable().clearSelection();
    }

    /**
     * Set the source table records
     *
     * @param records the records to set
     * @param userListNames list of user-defined list names
     * @param recordUserListMatrix 2D array containing which record is in which user-defined list
     */
    @Override
    public void setSourceTableRecordsV2(Stream<MBeans> records, String[] userListNames, boolean[][] recordUserListMatrix) {
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
