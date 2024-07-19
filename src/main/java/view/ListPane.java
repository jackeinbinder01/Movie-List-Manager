package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ListPane extends JPanel {

    JPanel listPanel;
    JTable listTable;
    JButton list1;
    JButton list2;
    JButton addList;
    JButton exportList;

    ListPane() {
        super(new BorderLayout());

        // Create list panel
        listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        // Create table with columns
        listTable = new JTable();
        addColumns(listTable);

        // Add horizontal and vertical scroll to table
        JScrollPane scrollPane = new JScrollPane(listTable);
        listPanel.add(scrollPane, BorderLayout.WEST);

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
            "IMDB Rating", "My Rating", "Has Watched", "Add/Remove"};

        // Create table model and set it to the table
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        listTable.setModel(model);
    }

}
