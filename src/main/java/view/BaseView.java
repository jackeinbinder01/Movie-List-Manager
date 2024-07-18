package view;

import javax.swing.*;

public class BaseView extends JFrame implements IView {
    private final static String APP_TITLE = "App Title";
    public BaseView() {
        super(APP_TITLE);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
