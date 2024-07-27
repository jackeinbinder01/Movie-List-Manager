package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * ListPaneV2 is ....... TODO: add description
 * BorderLayout.CENTER = a JTabbedPane containing the various lists
 * BorderLayout.SOUTH = a JPanel toolbar containing with buttons
 * <p>
 * TabbedPane citation: https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
 */

public class ListPaneV2 extends JPanel {

    private final String MAIN_TAB_NAME = "All Movies";


    JTable sourceTable;
    JButton addListButton;
    JButton exportListButton;
    JTabbedPane tabbedPane;

    MovieTableModel sourceTableModel;

    List<String> userListNames;
    Consumer<MBeans> tableSelectionHandler;
    BiConsumer<MBeans, Integer> removeFromListHandler;
    BiConsumer<MBeans, Integer> addToListHandler;
    BiConsumer<MBeans, Boolean> changeWatchedStatusHandler;
    // BiConsumer<MBeans, Double> changeRatingHandler;


    /**
     * List of movie models for user-defined lists
     * Each user-defined list will have its own model
     * Each tab will own a table with its own model
     */
    List<MovieTableModel> userListModels;

    ListPaneV2() {
        super();

        // Set layout for the list pane
        this.setLayout(new BorderLayout());


        // Setting up the tabbed pane, and adding listener for tab change
        tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        // Create the main table
        createSourceTableTab();
        userListModels = new ArrayList<>();
        userListNames = new ArrayList<>();


        // Create panel for add and export buttons below the table
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addListButton = new JButton("Add list from file");
        exportListButton = new JButton("Export list to file");
        bottomButtonPanel.add(addListButton);
        bottomButtonPanel.add(exportListButton);

        // Add button panel to the bottom of the list panel
        this.add(bottomButtonPanel, BorderLayout.SOUTH);

    }


    public int getActiveTab() {
        return tabbedPane.getSelectedIndex();
    }


    public MovieTableModel getActiveTableModel() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab == 0) {
            return sourceTableModel;
        } else {
            return userListModels.get(currentTab - 1);
        }
    }

    private void createTableTab(String name, TableMode tableMode) {
        MovieTableModel targetModel;
        JTable targetTable;
        String tabName = tableMode == TableMode.MAIN ? MAIN_TAB_NAME : name;

        if (tableMode == TableMode.MAIN) {
            // Error checking: there should only be one main table
            if (sourceTableModel != null || sourceTable != null || tabbedPane.getTabCount() > 0) {
                throw new IllegalArgumentException("[ListPaneV2] Error: Main table is already constructed!");
            }
        }
        targetModel = new MovieTableModel(tableMode);
        targetTable = new JTable(targetModel);

        if (tableMode == TableMode.MAIN) {
            sourceTableModel = targetModel;
            sourceTable = targetTable;
        } else {
            userListModels.add(targetModel);
        }

        targetTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        targetTable.getSelectionModel().addListSelectionListener(new MovieListSelectionHandler());

        targetTable.getColumn("ACTION").setCellRenderer(new ButtonRenderer(tableMode));
        targetTable.getColumn("ACTION").setCellEditor(new ButtonEditor(tableMode));
        JScrollPane newScrollPane = new JScrollPane(
                targetTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab(tabName, null, newScrollPane, tabName);
    }


    private void createSourceTableTab() {
        createTableTab(MAIN_TAB_NAME, TableMode.MAIN);
    }

    public void createUserTableTab(String tableName) {
        createTableTab(tableName, TableMode.USER_DEFINED);
    }


    public void setUserTableRecords(Stream<MBeans> recordStream, int userListIndex) {
        System.out.println("[BaseView] setUserTableRecords");
        if (tabbedPane.getTabCount() - 2 < userListIndex) {
            throw new IllegalArgumentException("User-defined list index out of bounds");
        }
        MovieTableModel targetUserListModel = userListModels.get(userListIndex);
        List<MovieTableModelRecord> recordsWithMetadata = new ArrayList<>();
        for (MBeans record : recordStream.toList()) {
            recordsWithMetadata.add(new MovieTableModelRecord(record));
        }
        targetUserListModel.setRecordsWithMetadata(recordsWithMetadata);
    }

    /**
     * Set the records for the main table
     *
     * @param records
     */
    public void setSourceTableRecords(Stream<MBeans> records) {
        System.out.println("[ListPaneV2] setMainTableRecords called");
        sourceTableModel.setRecords(records.toList());
    }


    public void setSourceTableRecordsV2(Stream<MBeans> records, String[] userListNames, boolean[][] userListMetadata) {
        System.out.println("[ListPaneV2] setMainTableRecords called");
        List<MovieTableModelRecord> recordsWithMetadata = new ArrayList<>();
        List<MBeans> recordsList = records.toList();
        for (int i = 0; i < recordsList.size(); i++) {
            recordsWithMetadata.add(new MovieTableModelRecord(recordsList.get(i), userListNames, userListMetadata[i]));
        }
        sourceTableModel.setRecordsWithMetadata(recordsWithMetadata);
    }

    public void bindFeatures(IFeature features) {
        System.out.println("[ListPaneV2] bindFeatures");
        addListButton.addActionListener(e -> features.addListFromFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        exportListButton.addActionListener(e -> features.exportListToFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        tableSelectionHandler = features::showRecordDetails;
        removeFromListHandler = features::removeFromWatchList;
        addToListHandler = features::addToWatchList;
        changeWatchedStatusHandler = features::changeWatchedStatus;
        tabbedPane.addChangeListener(e -> features.handleTabChange(tabbedPane.getSelectedIndex()));
    }


    public void setUserListNames(String[] userListNames) {
        this.userListNames = List.of(userListNames);
    }


    class MovieTableModel extends AbstractTableModel {
        /**
         * Enum for the columns in the table for easy reference in switch statements
         */
        enum COLUMN {
            TITLE,
            YEAR,
            WATCHED,
            ACTION;
            public final int index;

            COLUMN() {
                this.index = this.ordinal();
            }
        }


        private String[] columnNames = {"Title", "Year", "Watched", "ACTION"};
        private List<MBeans> records;
        private List<MovieTableModelRecord> movieTableModelRecords;
        private TableMode tableMode;


        MovieTableModel(TableMode tableMode) {
            this.tableMode = tableMode;
        }

        /**
         * Get the record at the specified row.
         * <br>
         * This method is for self-referencing from the table,
         * so we can do something like: table.getModel().getRecordAt(row) in an action listener
         *
         * @param row the row index
         * @return MBeans the movie record
         */
        public MBeans getRecordAt(int row) {
            return movieTableModelRecords.get(row).getRecord();
        }

        public TableMode getTableMode() {
            return tableMode;
        }

        public void setRecords(List<MBeans> records) {
            this.records = records;
            fireTableDataChanged();
        }

        public void setRecordsWithMetadata(List<MovieTableModelRecord> movieTableModelRecords) {
            this.movieTableModelRecords = movieTableModelRecords;
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (movieTableModelRecords == null) {
                return 0;
            }
            return movieTableModelRecords.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            MovieTableModelRecord movieTableModelRecord = movieTableModelRecords.get(row);
            MBeans record = movieTableModelRecord.getRecord();
            COLUMN column = COLUMN.values()[col];
            switch (column) {
                case TITLE:
                    return record.getTitle();
                case YEAR:
                    return record.getYear();
                case WATCHED:
                    return record.getWatched();
                case ACTION:
                    return movieTableModelRecord;
                default:
                    return null;
            }
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a checkbox.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            // The data/cell address is constant, even when are rearranged onscreen.
            COLUMN column = COLUMN.values()[col];
            switch (column) {
                case WATCHED, ACTION:
                    return true;
                default:
                    return false;
            }
        }

        /*
         * This handles any changes to the table's data.
         * Changes in watched status are handled here.
         */
        public void setValueAt(Object value, int row, int col) {
            // System.out.println("[ListPaneV2] setValueAt: " + value + " at row: " + row + " col: " + col);
            // data[row][col] = value;
            if (col == COLUMN.WATCHED.index) {
                MBeans record = this.getRecordAt(row);
                System.out.println("[ListPaneV2] Setting watched status of " + record.getTitle() + " to " + !record.getWatched());
                // calling the handler to update the Model
                changeWatchedStatusHandler.accept(record, !record.getWatched());
            }
            // ideally, the Model has been updated at this point
            // since the MBeans in this model points to the same MBeans in the Model
            // there is no need to re-set the records for this table model
            fireTableCellUpdated(row, col);
        }


    }


    class MovieListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            boolean isAdjusting = e.getValueIsAdjusting();
            if (!isAdjusting) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    // Find out which indexes are selected.
                    int temp = lsm.getSelectedIndices()[0];
                    tableSelectionHandler.accept(getActiveTableModel().getRecordAt(temp));
                }
            }
        }
    }

    /**
     * ButtonRenderer is a custom TableCellRenderer for rendering a JButton in a JTable
     * This button is only for appearance and does not have any action listener and function
     */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        private TableMode tableMode;

        public ButtonRenderer(TableMode tableMode) {
            this.tableMode = tableMode;
            setOpaque(true);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            switch (this.tableMode) {
                case MAIN:
                    setText("Add/Remove");
                    break;
                case USER_DEFINED:
                    setText("Remove");
                    break;
                default:
                    setText("AN_ERROR_OCCURRED");
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        // private MBeans record;
        private boolean isPushed;
        private TableMode tableMode;
        private JPopupMenu editMenu;
        private List<String> userListNames;
        private MovieTableModelRecord movieTableModelRecord;

        private static Icon tickIcon;

        static {
            // Create an ImageIcon from the PNG file
            ImageIcon imageIcon = new ImageIcon(ButtonEditor.class.getClassLoader().getResource("tick.png"));

            // Optionally, you can scale the image if needed
            Image image = imageIcon.getImage(); // Transform it
            Image scaledImage = image.getScaledInstance(10, 10,  Image.SCALE_SMOOTH); // Scale it to 32x32 pixels
            tickIcon = new ImageIcon(scaledImage); // Transform it back to an ImageIcon
        }


        public ButtonEditor(TableMode tableMode) {
            // This is a workaround, since DefaultCellEditor only accepts JCheckBox, JComboBox or JTextField
            // The JCheckBox is unused and hidden
            super(new JCheckBox());

            button = new JButton();
            this.tableMode = tableMode;


            switch (this.tableMode) {
                case MAIN:
                    label = "Add/Remove";
                    break;
                case USER_DEFINED:
                    label = "Remove";
                    break;
                default:
                    label = "AN_ERROR_OCCURRED";
            }
            button.setOpaque(true);


            button.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    switch (tableMode) {
                        case MAIN:

                            // JOptionPane.showMessageDialog(null, "[ButtonEditor] Adding/removing record \"" + record.getTitle(), "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
                            JPopupMenu editMenu = new JPopupMenu("Edit");

                            String[] userListNames = movieTableModelRecord.getUserListNames();
                            boolean[] userListIndices = movieTableModelRecord.getUserListIndices();

                            for (int i = 0; i < userListNames.length; i++) {
                                JMenuItem item;
                                int idx = i;
                                if (userListIndices[i]) {
                                    item = new JMenuItem(userListNames[i], tickIcon);
                                    item.addActionListener(event -> {
                                        System.out.println("List \"" + userListNames[idx] + "\" clicked");
                                        removeFromListHandler.accept(movieTableModelRecord.getRecord(), idx);
                                    });
                                } else {
                                    item = new JMenuItem(userListNames[i], null);
                                    item.addActionListener(event -> {
                                        System.out.println("List \"" + userListNames[idx] + "\" clicked");
                                        addToListHandler.accept(movieTableModelRecord.getRecord(), idx);
                                    });
                                }
                                editMenu.add(item);
                            }


                            JMenuItem createNewListItem = new JMenuItem("Add To New List");
                            createNewListItem.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println("Create new list clicked");
                                    createNewListItem.setSelected(false);
                                }

                            });
                            editMenu.addSeparator();
                            editMenu.add(createNewListItem);
                            editMenu.show(e.getComponent(), e.getX(), e.getY());
                            break;
                        case USER_DEFINED:
                            int currUserTableIndex = tabbedPane.getSelectedIndex() - 1;
                            // JOptionPane.showMessageDialog(null, "[ButtonEditor] Remove record \"" + record.getTitle() + "\" from UserList " + currUserTableIndex, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
                            removeFromListHandler.accept(movieTableModelRecord.getRecord(), currUserTableIndex);
                            break;
                        default:
                            System.out.println("[ButtonEditor] AN_ERROR_OCCURRED");
                    }
                    fireEditingStopped();
                }
            });

//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    }
//                }
//            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof MovieTableModelRecord) {
                movieTableModelRecord = (MovieTableModelRecord) value;
            } else {
                movieTableModelRecord = null;
                System.out.println("[ButtonEditor] getTableCellEditorComponent: value is not MovieTableModelRecord");
                // TODO: throw an exception
            }
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // System.out.println("[getCellEditorValue] " + record.getTitle() + " clicked");
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }


    /**
     * Main method to test the DetailsPane.
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        ListPaneV2 listPaneV2 = new ListPaneV2();
        frame.add(listPaneV2);

        String sampleDataPath = "data/samples/source.json";
        Set<MBeans> records = MBeansLoader.loadMediasFromFile(sampleDataPath, Formats.JSON);
        listPaneV2.setSourceTableRecords(records.stream());

        listPaneV2.setVisible(true);
        frame.setVisible(true);
    }


    /**
     * Idea: What about AbstractMovieTableModel -> SourceTableModel and UserTableModel extends AbstractMovieTableModel
     */
    enum TableMode {
        MAIN,
        USER_DEFINED
    }

    /**
     * This contains all information needed to render a row in the table.
     * The UserList fields are required for the sourceList to construct
     * the dropdown menu for watchlist management.
     * Admittedly, this looks counter-intuitive for now.
     * Makes use of composition pattern instead of inheritance with MBeans
     * because we want to retain the original MBeans reference instead of copying and constructing new objects.
     */
    class MovieTableModelRecord {
        private MBeans record;
        private boolean[] userListIndices = null;
        private String[] userListNames = null;

        public MovieTableModelRecord(MBeans record, String[] userListNames, boolean[] userListIndices) {
            this.record = record;
            this.userListNames = userListNames;
            this.userListIndices = userListIndices;
        }
        public MovieTableModelRecord(MBeans record) {
            this.record = record;
        }

        public MBeans getRecord() {
            return record;
        }
        public MBeans getMBeans() {
            return record;
        }
        public boolean[] getUserListIndices() {
            return userListIndices;
        }
        public String[] getUserListNames() {
            return userListNames;
        }
    }


}


