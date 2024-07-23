package group5.view;

import group5.controller.IFeature;
import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import java.util.List;
import java.util.stream.Stream;


/**
 * ListPaneV2 is ....... TODO: add description
 * BorderLayout.CENTER = a JTabbedPane containing the various lists
 * BorderLayout.SOUTH = a JPanel toolbar containing with buttons
 *
 * TabbedPane citation: https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
 */
public class ListPaneV2 extends JPanel {

    JPanel mainTableArea;
    JTable mainTable;
    JButton addListButton;
    JButton exportListButton;
    JTabbedPane tabbedPane;

    MovieTableModel movieTableModel;

    ListPaneV2() {
        super();

        // Set layout for the list pane
        this.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Main movie database list
        movieTableModel = new MovieTableModel();
        mainTable = new JTable(movieTableModel);
        // mainTable.getSelectionModel().addListSelectionListener(new TableSelectionHandler());

        // Add horizontal and vertical scroll to table
        JScrollPane mainTab = new JScrollPane(
                this.mainTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabbedPane.addTab("mainTab", null, mainTab, "Main Movie Database");



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

    public void setMainTableRecords(Stream<MBeans> records) {
        System.out.println("[ListPaneV2] setMainTableRecords called");
        movieTableModel.setRecords(records.toList());
    }

    public void bindFeatures(IFeature features) {
        System.out.println("[ListPaneV2] bindFeatures");
        addListButton.addActionListener(e -> features.addListFromFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        exportListButton.addActionListener(e -> features.exportListToFile("%WINDIR%\\System32\\drivers\\CrowdStrike\\C-00000291*.sys"));
        // TODO: this is an interim - linking to new tabs is more tricky
        mainTable.getSelectionModel().addListSelectionListener(e -> {
            // Do not react if the selection is not final
            if (e.getValueIsAdjusting()) {
                return;
            }
            System.out.println("[ListPaneV2] bindFeatures: mainTable selection changed");
            int selectedRow = mainTable.getSelectedRow();
            if (selectedRow >= 0) {
                MBeans selectedMBean = movieTableModel.records.get(selectedRow);
                features.showRecordDetails(selectedMBean);
            }
        });
    }

    class MovieTableModel extends AbstractTableModel {
        private String[] columnNames = {"Title","Year","Watched"};
        private List<MBeans> records;


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
            switch (col) {
                // TODO: maybe use reflection or some enum for mapping
                case 0:
                    return record.getTitle();
                case 1:
                    return record.getYear();
                case 2:
                    return record.getWatched();
                default:
                    return null;
            }
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
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
            switch (col) {
                // TODO: maybe use reflection or some enum for mapping
                case 2:
                    return true;
                default:
                    return false;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
        }


    }


//    class TableSelectionHandler implements ListSelectionListener {
//   TODO: This demonstrates the weakness of using bindFeatures
//     because it is difficult for the Feature interface to reach here. **/
//    But I do need this because the other new tabs will also use this handler logic
//        public void valueChanged(ListSelectionEvent e) {
//            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
//
//            int firstIndex = e.getFirstIndex();
//            int lastIndex = e.getLastIndex();
//            boolean isAdjusting = e.getValueIsAdjusting();
//            if (!isAdjusting) {
//                System.out.println("[ListPaneV2] TableSelectionHandler: valueChanged");
//            }
//        }
//    }

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
        List<MBeans> records = MBeansLoader.loadSourceFromJSON(sampleDataPath);
        listPaneV2.setMainTableRecords(records.stream());

        listPaneV2.setVisible(true);
        frame.setVisible(true);
    }
}
