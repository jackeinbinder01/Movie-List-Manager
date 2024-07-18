package view;

import javax.swing.*;
import java.awt.*;

public class BaseView extends JFrame implements IView {
    private final static String APP_TITLE = "App Title";
    private final static int DEFAULT_WIDTH = 1024;
    private final static int DEFAULT_HEIGHT = 600;



    public BaseView() {
        super(APP_TITLE);

        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Creating Base Pane
//        JPanel basePane = new JPanel();
//        basePane.setLayout(new BorderLayout());
//        add(basePane);

         // Creating the Functional Panes
//         JPanel filterPane = new FilterPane();
//         JPanel listPane = new ListPane();
//         JPanel detailsPane = new DetailsPane();


        // Attaching the Functional Panes to the Base Pane
//        basePane.add(filterPane, BorderLayout.WEST);
//        basePane.add(listPane, BorderLayout.CENTER);
//        basePane.add(detailsPane, BorderLayout.EAST);

    }



}
