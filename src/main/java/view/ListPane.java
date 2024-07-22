package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.beans.MBeans;
import model.beans.MBeansLoader;

public class ListPane extends JPanel {

    JPanel listPanel;
    JTable listTable;
    JButton list1;
    JButton list2;
    JButton addList;
    JButton exportList;
    JButton removeCurrentEntry;

    ListPane() {
        super(new BorderLayout());

        // Create list panel
        listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        // Create table with columns
        listTable = new JTable();
        addColumns(listTable);

        // Add horizontal and vertical scroll to table
        JScrollPane scrollPane = new JScrollPane(listTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons
        list1 = new JButton("Watchlist");
        list2 = new JButton("My list 2");
        addList = new JButton("Add list from file");
        exportList = new JButton("Export list to file");

        // Create panel for buttons above the table
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.add(list1);
        topButtonPanel.add(list2);
        topButtonPanel.add(new JButton("+"));

        // Add button panel to the top of the list panel
        listPanel.add(topButtonPanel, BorderLayout.NORTH);

        // Create panel for add and export buttons below the table
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomButtonPanel.add(addList);
        bottomButtonPanel.add(exportList);

        // Add button panel to the bottom of the list panel
        listPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

        // Add list panel to the main panel
        this.add(listPanel, BorderLayout.CENTER);
    }

    private void addColumns(JTable listTable) {
        // Define column names
        String[] columns = {"#", "Title", "Released", "MPA Rating", "Genre", "Runtime", "Director",
            "IMDB Rating", "My Rating", "Has Watched", "Add/Remove", ""}; //  last is for removing an entry
        // these should change to buttons so that on a press it switches a bool
        // and rearranges how the lsit is organized.

        // Create table model and set it to the table
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        listTable.setModel(model);
        fillInTable(listTable);
    }

    private void fillInTable(JTable listTable) {
        // needs to fill in the 
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
        ListPane listPane = new ListPane();
        frame.add(listPane);
        MBeans media = MBeansLoader.loadMBeansFromAPI("The Matrix", "", "");
        frame.setVisible(true);
    }
}
