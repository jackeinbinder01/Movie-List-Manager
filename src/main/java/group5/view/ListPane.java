package group5.view;

import group5.controller.ErrorMessage;
import group5.controller.IFeature;
import group5.model.beans.MBeans;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * ListPane is ....... TODO: add description
 * BorderLayout.CENTER = a JTabbedPane containing the various lists
 * BorderLayout.SOUTH = a JPanel toolbar containing with buttons
 * <p>
 * TabbedPane citation: https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
 */

public class ListPane extends JPanel {

    private final String MAIN_TAB_NAME = "All Movies";
    private final String ADD_LIST_BUTTON_TEXT = "Import List";
    private final String DELETE_LIST_BUTTON_TEXT = "Delete List";
    private final String EXPORT_LIST_BUTTON_TEXT = "Export List";
    private final String MAIN_ACTION_BUTTON_TEXT = "+/-";
    private final String WATCHLIST_ACTION_BUTTON_TEXT = "Remove";

    private final String NEW_LIST_POPUP_TITLE = "New Watchlist";
    private final String NEW_LIST_POPUP_PROMPT = "Enter the name of the new watchlist:";

    JTable sourceTable;
    JButton importListButton;
    JButton deleteListButton;
    JButton exportListButton;
    JTabbedPane tabbedPane;

    MovieTableModel sourceTableModel;


    Consumer<MBeans> tableSelectionHandler;
    BiConsumer<MBeans, Integer> removeFromListHandler;
    BiConsumer<MBeans, Integer> addToListHandler;
    TriConsumer<MBeans, Boolean, String> changeWatchedStatusHandler;
    Consumer<String> createListHandler;
    Consumer<Integer> deleteListHandler;
    Consumer<Integer> tabChangeHandler;
    Consumer<String> importListHandler;
    Consumer<String> exportListHandler;

    Boolean SORTING_ENABLED = true;
    Boolean SELECTION_PERMENANCE = true;
    /**
     * List of movie models for user-defined lists
     * Each user-defined list will have its own model
     * Each tab will own a table with its own model
     */
    List<MovieTableModel> watchlistModels;
    List<JTable> watchlistTables;

    ListPane() {
        super();

        // Set layout for the list pane
        this.setLayout(new BorderLayout());

        // Setting up the tabbed pane + tabs to overflow horizontally
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.add(tabbedPane, BorderLayout.CENTER);

        // Create the main table
        createSourceTableTab();
        watchlistModels = new ArrayList<>();
        watchlistTables = new ArrayList<>();


        // Create panel for add and export buttons below the table
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        importListButton = new JButton(ADD_LIST_BUTTON_TEXT);
        exportListButton = new JButton(EXPORT_LIST_BUTTON_TEXT);
        deleteListButton = new JButton(DELETE_LIST_BUTTON_TEXT);
        exportListButton.setEnabled(false);
        deleteListButton.setEnabled(false);
        bottomButtonPanel.add(importListButton);
        bottomButtonPanel.add(importListButton);
        bottomButtonPanel.add(exportListButton);
        bottomButtonPanel.add(deleteListButton);

        // Add button panel to the bottom of the list panel
        this.add(bottomButtonPanel, BorderLayout.SOUTH);

    }

    /**
     * Switches the active tab to the specified index
     * @param index the index of the tab to switch to
     */
    public void setActiveTab(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    /**
     * Returns the index of the active tab
     * @return the index of the active tab
     */
    public int getActiveTab() {
        return tabbedPane.getSelectedIndex();
    }


    private JTable getActiveTable() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab == 0) {
            return sourceTable;
        } else {
            return watchlistTables.get(currentTab - 1);
        }
    }

    private MovieTableModel getActiveTableModel() {
        int currentTab = tabbedPane.getSelectedIndex();
        if (currentTab == 0) {
            return sourceTableModel;
        } else {
            return watchlistModels.get(currentTab - 1);
        }
    }


    private void createTableTab(String name, TableMode tableMode) {
        MovieTableModel targetModel;
        JTable targetTable;
        String tabName = tableMode == TableMode.MAIN ? MAIN_TAB_NAME : name;

        if (tableMode == TableMode.MAIN) {
            // Error checking: there should only be one main table
            if (sourceTableModel != null || sourceTable != null || tabbedPane.getTabCount() > 0) {
                throw new IllegalArgumentException("[ListPane] Error: Main table is already constructed!");
            }
        }
        targetModel = new MovieTableModel(tableMode);
        targetTable = new JTable(targetModel);

        // Enable sorting and disable sorting for the action column
        if (SORTING_ENABLED) {
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(targetTable.getModel());
            targetTable.setRowSorter(sorter);
            sorter.setSortable(TableColumn.WATCHLIST.getIndex(), false);
        }

        // Setting the max width for certain columns
        List<Pair<TableColumn, Integer>> columnMaxWidths = List.of(
                Pair.of(TableColumn.YEAR, 50),
                Pair.of(TableColumn.WATCHED, 75),
                Pair.of(TableColumn.WATCHLIST, 100),
                Pair.of(TableColumn.RUNTIME, 75)
        );
        for (Pair<TableColumn, Integer> pair : columnMaxWidths) {
            targetTable.getColumnModel().getColumn(pair.getLeft().getIndex()).setMaxWidth(pair.getRight());
        }

        if (tableMode == TableMode.MAIN) {
            sourceTableModel = targetModel;
            sourceTable = targetTable;
        } else {
            watchlistModels.add(targetModel);
            watchlistTables.add(targetTable);
        }

        targetTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        targetTable.getSelectionModel().addListSelectionListener(new MovieListSelectionHandler());

        targetTable.getColumn(TableColumn.WATCHLIST.getName()).setCellRenderer(new ButtonRenderer(tableMode));
        targetTable.getColumn(TableColumn.WATCHLIST.getName()).setCellEditor(new ButtonEditor(tableMode));
        targetTable.getColumn(TableColumn.RUNTIME.getName()).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                Integer runtime = (Integer) value;
                if (runtime == null || runtime < 0) {
                    setText("N/A");
                    return;
                }
                int hours = runtime / 60;
                int minutes = runtime % 60;
                setText(String.format("%dh %dm", hours, minutes));
            }
        });


        JScrollPane newScrollPane = new JScrollPane(
                targetTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabbedPane.addTab(tabName, null, newScrollPane, tabName);
    }

    public void removeUserTable(int userListId) {
        if (tabbedPane.getTabCount() - 2 < userListId) {
            throw new IllegalArgumentException("User-defined list index out of bounds");
        }
        tabbedPane.remove(userListId + 1);
        watchlistModels.remove(userListId);
        watchlistTables.remove(userListId);
    }

    private void createSourceTableTab() {
        createTableTab(MAIN_TAB_NAME, TableMode.MAIN);
    }

    public void createUserTableTab(String tableName) {
        createTableTab(tableName, TableMode.WATCHLIST);
    }
    
    private void localImportListHandler() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilters = new FileNameExtensionFilter(
                "JSON or CSV (*.json;*.csv)", "json", "csv");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(fileFilters);
        fileChooser.setDialogTitle("Import List");

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            importListHandler.accept(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void localExportListHandler() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Export List");
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML (*.xml)", "xml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON (*.json)", "json"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text (*.txt)", "txt"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String selectedExtension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            String filePath = selectedFile.getAbsolutePath();
            // Check if the file path already has the selected extension
            if (!filePath.toLowerCase().endsWith("." + selectedExtension.toLowerCase())) {
                filePath += "." + selectedExtension;
            }
            exportListHandler.accept(filePath);
        }
    }

    public void setSourceTable(Stream<MBeans> records, String[] watchlistNames, boolean[][] recordWatchlistMatrix) {
        System.out.println("[ListPane] setMainTableRecords called");
        List<MovieTableModelRecord> tmRecords = new ArrayList<>();
        List<MBeans> mBeansList = records.toList();
        for (int i = 0; i < mBeansList.size(); i++) {
            tmRecords.add(new MovieTableModelRecord(mBeansList.get(i), watchlistNames, recordWatchlistMatrix[i]));
        }
        int selectedRow = sourceTable.getSelectedRow();
        sourceTableModel.setMovieTableModelRecords(tmRecords);
        if (SELECTION_PERMENANCE) {
            if (selectedRow >= 0 && selectedRow < sourceTable.getRowCount() && tabbedPane.getSelectedIndex() == 0
                && sourceTable.getRowCount() > 0) {
                sourceTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }


    public void setUserTable(Stream<MBeans> recordStream, int watchlistIndex) {
        if (tabbedPane.getTabCount() - 2 < watchlistIndex) {
            throw new IllegalArgumentException("User-defined list index out of bounds");
        }
        List<MBeans> mBeansList = recordStream.toList();
        MovieTableModel targetUserListModel = watchlistModels.get(watchlistIndex);
        List<MovieTableModelRecord> tmRecords = new ArrayList<>();
        for (MBeans record : mBeansList) {
            tmRecords.add(new MovieTableModelRecord(record));
        }
        int selectedRow = watchlistTables.get(watchlistIndex).getSelectedRow();
        targetUserListModel.setMovieTableModelRecords(tmRecords);
        if (SELECTION_PERMENANCE) {
            if (selectedRow >= 0 && selectedRow < watchlistTables.get(watchlistIndex).getRowCount() &&
                    watchlistTables.get(watchlistIndex).getRowCount() > 0
                && tabbedPane.getSelectedIndex() == watchlistIndex + 1) {
                watchlistTables.get(watchlistIndex).setRowSelectionInterval(selectedRow, selectedRow);
            }
        }
    }

    /**
     * Binds the features from the controller to the view.
     *
     * @param features the features to bind
     */
    public void bindFeatures(IFeature features) {
        System.out.println("[ListPane] bindFeatures");
        importListButton.addActionListener(e -> localImportListHandler());
        importListHandler = features::importListFromFile;
        exportListHandler = features::exportListToFile;
        exportListButton.addActionListener(e -> localExportListHandler());
        tableSelectionHandler = features::handleTableSelection;
        removeFromListHandler = features::removeFromWatchlist;
        addToListHandler = features::addToWatchlist;
        changeWatchedStatusHandler = features::changeWatchedStatus;
        tabChangeHandler = features::handleTabChange;
        tabbedPane.addChangeListener(e -> localTabChangeHandler());
        createListHandler = features::createWatchlist;
        deleteListHandler = features::deleteWatchlist;
        deleteListButton.addActionListener(e -> localDeleteListHandler());
    }

    /**
     * A local handler for tab change event
     * before passing the event to the controller.
     */
    private void localTabChangeHandler() {
        deleteListButton.setEnabled(!(tabbedPane.getSelectedIndex() == 0));
        exportListButton.setEnabled(!(tabbedPane.getSelectedIndex() == 0));
        // tabChangeHandler would clear ViewFilter, ModelFilter, and refresh the records for the new tab
        tabChangeHandler.accept(tabbedPane.getSelectedIndex());
    }

    /**
     * Returns the current table in the tabbed pane
     * @return current JTable in view
     */
    public JTable getCurrentTable() {
        return (JTable) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView();
    }

    private void localDeleteListHandler() {
        // Pop up a dialog to confirm deletion
        int currWatchlistIdx = tabbedPane.getSelectedIndex() - 1;
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this watchlist?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            deleteListHandler.accept(currWatchlistIdx);
            removeUserTable(currWatchlistIdx);
        }
    }


    class MovieTableModel extends AbstractTableModel {
        private String[] columnNames = Arrays.stream(TableColumn.values())
                .map(TableColumn::getName)
                .toArray(String[]::new);

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

        public void setMovieTableModelRecords(List<MovieTableModelRecord> movieTableModelRecords) {
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
            TableColumn column = TableColumn.values()[col];
            switch (column) {
                case TITLE:
                    return record.getTitle();
                case YEAR:
                    return ((Integer) record.getYear()).toString();
                case WATCHED:
                    return record.getWatched();
                case WATCHLIST:
                    return movieTableModelRecord;
                case GENRE:
                    return String.join(", ", record.getGenre());
                case RUNTIME:
                    return record.getRuntime();
                default:
                    return "AN_ERROR_OCCURRED";
            }
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a checkbox.
         */
        public Class getColumnClass(int col) {
            TableColumn column = TableColumn.values()[col];
            switch (column) {
                case WATCHED:
                    return Boolean.class;
                case RUNTIME:
                    return Integer.class;
                case WATCHLIST:
                    return movieTableModelRecords.getClass();
                default:
                    return String.class;
            }
        }

        /*
         * Specifies which columns are editable
         */
        public boolean isCellEditable(int row, int col) {
            // The data/cell address is constant, even when are rearranged onscreen.
            TableColumn column = TableColumn.values()[col];
            switch (column) {
                case WATCHED, WATCHLIST:
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
            // System.out.println("[ListPane] setValueAt: " + value + " at row: " + row + " col: " + col);
            // data[row][col] = value;
            if (col == TableColumn.WATCHED.getIndex()) {
                MBeans record = this.getRecordAt(row);
                System.out.println("[ListPane] Setting watched status of " + record.getTitle() + " to " + !record.getWatched());
                // calling the handler to update the Model
                // changeWatchedStatusHandler.accept(record, !record.getWatched());
                changeWatchedStatusHandler.accept(record, !record.getWatched(), "listPane");
            }
            // ideally, the Model has been updated at this point
            // since the MBeans in this model points to the same MBeans in the Model
            // there is no need to re-set the records for this table model
            fireTableCellUpdated(row, col);
        }


    }

    /**
     * MovieListSelectionHandler is a custom ListSelectionListener for handling table selection events
     */
    class MovieListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            boolean isAdjusting = e.getValueIsAdjusting();
            if (!isAdjusting) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    // Find out which indexes are selected.
                    int tableIndex = lsm.getSelectedIndices()[0];
                    // Convert the table index to the model index, since the table is sorted
                    // Model order is unaffected by sorting in table-level sorting
                    int modelIndex = getActiveTable().convertRowIndexToModel(tableIndex);
                    tableSelectionHandler.accept(getActiveTableModel().getRecordAt(modelIndex));
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
                    setText(MAIN_ACTION_BUTTON_TEXT);
                    break;
                case WATCHLIST:
                    setText(WATCHLIST_ACTION_BUTTON_TEXT);
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
        private MBeans record;
        private boolean isPushed;
        private TableMode tableMode;
        private MovieTableModelRecord movieTableModelRecord;

        private static Icon tickIcon;

        static {
            // Create an ImageIcon from the PNG file
            ImageIcon imageIcon = new ImageIcon(ButtonEditor.class.getClassLoader().getResource("tick.png"));

            // Optionally, you can scale the image if needed
            Image image = imageIcon.getImage(); // Transform it
            Image scaledImage = image.getScaledInstance(10, 10, Image.SCALE_SMOOTH); // Scale it to 32x32 pixels
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
                    label = MAIN_ACTION_BUTTON_TEXT;
                    break;
                case WATCHLIST:
                    label = WATCHLIST_ACTION_BUTTON_TEXT;
                    break;
                default:
                    label = "AN_ERROR_OCCURRED";
            }
            button.setOpaque(true);

            button.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    // the fireEditingStopped() call has to be placed strategically
                    // for source list: after the menu is rendered
                    switch (tableMode) {
                        case MAIN:
                            System.out.println("[ButtonEditor] Adding/removing record \"" + record.getTitle() + "\"" + " to/from watchlist");
                            JPopupMenu editMenu = new JPopupMenu("Edit");

                            String[] userListNames = movieTableModelRecord.getUserListNames();
                            boolean[] userListIndices = movieTableModelRecord.getUserListIndices();

                            for (int i = 0; i < userListNames.length; i++) {
                                JMenuItem item;
                                int idx = i;
                                if (userListIndices[i]) {
                                    item = new JMenuItem(userListNames[i], tickIcon);
                                    item.addActionListener(event -> {
                                        removeFromListHandler.accept(record, idx);
                                    });
                                } else {
                                    item = new JMenuItem(userListNames[i], null);
                                    item.addActionListener(event -> {
                                        addToListHandler.accept(record, idx);
                                    });
                                }
                                editMenu.add(item);
                            }
                            JMenuItem createNewListItem = new JMenuItem("Add To New Watchlist");
                            createNewListItem.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    System.out.println("Create new list clicked");
                                    createNewListItem.setSelected(false);
                                    // create a pop-up dialog to get the name of the new list
                                    String newListName = JOptionPane.showInputDialog(null, NEW_LIST_POPUP_PROMPT, NEW_LIST_POPUP_TITLE, JOptionPane.QUESTION_MESSAGE);
                                    if (newListName != null) {
                                        System.out.println("New list name: " + newListName);
                                        if (newListName.length() > 0) {
                                            for (String list : userListNames) {
                                                if (list.equals(newListName)) {
                                                    JOptionPane.showMessageDialog(
                                                            null,
                                                            ErrorMessage.CREATE_SAME_NAME_WATCHLIST.getErrorMessage(newListName),
                                                            String.valueOf(ErrorMessage.ERROR), JOptionPane.ERROR_MESSAGE);
                                                    return;
                                                }
                                            }
                                            createListHandler.accept(newListName);
                                            addToListHandler.accept(record, userListNames.length);
                                        }
                                    }
                                }

                            });
                            editMenu.addSeparator();
                            editMenu.add(createNewListItem);
                            editMenu.show(e.getComponent(), e.getX(), e.getY());
                            fireEditingStopped(); // called after rendering the menu
                            break;
                        case WATCHLIST:
                            fireEditingStopped(); // called before the entry row is deleted
                            int currUserTableIndex = tabbedPane.getSelectedIndex() - 1;
                            removeFromListHandler.accept(movieTableModelRecord.getRecord(), currUserTableIndex);
                            break;
                        default:
                            fireEditingStopped();
                            System.out.println("[ButtonEditor] AN_ERROR_OCCURRED");
                            break;
                    }

                }
            });

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof MovieTableModelRecord) {
                movieTableModelRecord = (MovieTableModelRecord) value;
                record = movieTableModelRecord.getRecord();
            } else {
                movieTableModelRecord = null;
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
     * Enum class for the table mode. This is used to differentiate between the main table and user-defined lists.
     */
    enum TableMode {
        MAIN,
        WATCHLIST
    }

    /**
     * Enum class for the columns in the table. This is used to define the column order and titles.
     */
    enum TableColumn {
        TITLE("Title"),
        YEAR("Year"),
        GENRE("Genre"),
        RUNTIME("Runtime"),
        WATCHED("Watched"),
        WATCHLIST("Watchlist");

        private final String name;


        TableColumn(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return this.ordinal();
        }
    }

    /**
     * This contains all information needed to render a row in the table.
     * The two UserList fields are required for the sourceList to construct
     * the dropdown menu for watchlist management.
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

        public boolean[] getUserListIndices() {
            return userListIndices;
        }

        public String[] getUserListNames() {
            return userListNames;
        }
    }


}


