package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;
import group5.model.formatters.Formats;
import group5.model.formatters.MBeansLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

        tabbedPane = new JTabbedPane();

        // Create the main table
        createSourceTableTab();
        userListModels = new ArrayList<>();

        this.add(tabbedPane, BorderLayout.CENTER);

        // Create panel for add and export buttons below the table
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addListButton = new JButton("Add list from file");
        exportListButton = new JButton("Export list to file");
        bottomButtonPanel.add(addListButton);
        bottomButtonPanel.add(exportListButton);

        // Add button panel to the bottom of the list panel
        this.add(bottomButtonPanel, BorderLayout.SOUTH);

    }

    private void createSourceTableTab() {
        sourceTableModel = new MovieTableModel(TableMode.MAIN);
        sourceTable = new JMovieTable(sourceTableModel);
//        sourceTable.getModel().addTableModelListener(e -> {
//            int row = e.getFirstRow();
//            int column = e.getColumn();
//            MovieTableModel model = (MovieTableModel) e.getSource();
//            MBeans record = model.getRecordAt(row);
//            System.out.println("[ListPaneV2] Setting watched status of " + record.getTitle() + " to " + record.getWatched());
//
//        });
        sourceTable.getColumn("ACTION").setCellRenderer(new ButtonRenderer(TableMode.MAIN));
        sourceTable.getColumn("ACTION").setCellEditor(new ButtonEditor(TableMode.MAIN));
        JScrollPane mainTab = new JScrollPane(
                this.sourceTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab(MAIN_TAB_NAME, null, mainTab, MAIN_TAB_NAME);
    }

    public void createUserTableTab(String tabName) {
        MovieTableModel newUserModel = new MovieTableModel(TableMode.USER_DEFINED);
        userListModels.add(newUserModel);
        JTable newUserTable = new JMovieTable(newUserModel);
        newUserTable.getColumn("ACTION").setCellRenderer(new ButtonRenderer(TableMode.USER_DEFINED));
        newUserTable.getColumn("ACTION").setCellEditor(new ButtonEditor(TableMode.USER_DEFINED));
        JScrollPane scrollPane = new JScrollPane(
                newUserTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab(tabName, null, scrollPane, tabName);
    }

    public void setUserTableRecords(int userListIndex, Stream<MBeans> mbeans) {
        System.out.println("[BaseView] setUserTableRecords");
        if (tabbedPane.getTabCount() - 2 < userListIndex) {
            throw new IllegalArgumentException("User list index out of bounds");
        }
        MovieTableModel model = userListModels.get(userListIndex);
        model.setRecords(mbeans.toList());
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

    public void bindFeatures(IFeature features) {
        System.out.println("[ListPaneV2] bindFeatures");
        addListButton.addActionListener(e -> features.addListFromFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        exportListButton.addActionListener(e -> features.exportListToFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        tableSelectionHandler = features::showRecordDetails;
        removeFromListHandler = features::removeFromWatchList;
        addToListHandler = features::addToWatchList;
        changeWatchedStatusHandler = features::changeWatchedStatus;
    }


    class MovieTableModel extends AbstractTableModel {
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
        private TableMode tableMode;


        MovieTableModel(TableMode tableMode) {
            this.tableMode = tableMode;
        }

        /**
         * Get the record at the specified row
         * This method is for self-referencing from the table
         * So we can do something like: table.getModel().getRecordAt(row) in an action listener
         * @param row
         * @return
         */
        public MBeans getRecordAt(int row) {
            return records.get(row);
        }

        public TableMode getTableMode() {
            return tableMode;
        }

        public void setRecords(List<MBeans> records) {
            this.records = records;
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (records == null) {
                return 0;
            }
            return records.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            MBeans record = records.get(row);
            COLUMN column = COLUMN.values()[col];
            switch (column) {
                case TITLE:
                    return record.getTitle();
                case YEAR:
                    return record.getYear();
                case WATCHED:
                    return record.getWatched();
                case ACTION:
                    return record;
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
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
//            switch (col) {
//                // TODO: maybe use reflection or some enum for mapping
//                case 2:
//                    return true;
//                case 3:
//                    return true;
//                default:
//                    return false;
//            }
            return true;
        }

        /*
         * This will be useful for reacting to changes in the DetailPane
         * 1. update the model
         * 2. fireTableDataChanged()
         */
        public void setValueAt(Object value, int row, int col) {
            // System.out.println("[ListPaneV2] setValueAt: " + value + " at row: " + row + " col: " + col);
            // data[row][col] = value;
            if (col == COLUMN.WATCHED.index)
            {
                MBeans record = records.get(row);
                System.out.println("[ListPaneV2] Setting watched status of " + record.getTitle() + " to " + !record.getWatched());
                changeWatchedStatusHandler.accept(record, !record.getWatched());
                // record.setWatched(!record.getWatched());
            }
            fireTableCellUpdated(row, col);
        }


    }

    class JMovieTable extends JTable implements ListSelectionListener /**, TableModelListener **/{
        public JMovieTable(MovieTableModel model) {
            super(model);
            // this.getModel().addTableModelListener(this);
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            // ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            if(!e.getValueIsAdjusting()){
                System.out.println("[ListPaneV2] JMovieTable: selectedRow: " + this.getSelectedRow());
                int selectedRow = this.getSelectedRow();
                if (selectedRow >= 0) {
                    MovieTableModel m = (MovieTableModel) this.getModel();
                    MBeans selectedMBean = m.getRecordAt(selectedRow);
                    tableSelectionHandler.accept(selectedMBean);
                }
            }
        }


//        @Override
//        public void tableChanged(javax.swing.event.TableModelEvent e) {
//            System.out.println("[ListPaneV2] JMovieTable: tableChanged");
//        }
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
        private MBeans record;
        private boolean isPushed;
        private TableMode tableMode;

        public ButtonEditor(TableMode tableMode) {
            // This is a workaround, since DefaultCellEditor only accepts JCheckBox, JComboBox or JTextField
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
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    switch (tableMode) {
                        case MAIN:
                            JOptionPane.showMessageDialog(null, "[ButtonEditor] Pop up a dialogue for adding/removing record \"" + record.getTitle() + "\" from UserList ", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        case USER_DEFINED:
                            int currUserTableIndex = tabbedPane.getSelectedIndex() - 1;
                            JOptionPane.showMessageDialog(null, "[ButtonEditor] Remove record \"" + record.getTitle() + "\" from UserList " + currUserTableIndex, "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        default:
                            System.out.println("[ButtonEditor] AN_ERROR_OCCURRED");
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof MBeans) {
                record = (MBeans) value;
            } else {
                record = null;
                System.out.println("[ButtonEditor] getTableCellEditorComponent: value is not MBeans");
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
        List<MBeans> records = MBeansLoader.loadMediasFromFile(sampleDataPath, Formats.JSON);
        listPaneV2.setSourceTableRecords(records.stream());

        listPaneV2.setVisible(true);
        frame.setVisible(true);
    }
}


enum TableMode {
    MAIN,
    USER_DEFINED
}