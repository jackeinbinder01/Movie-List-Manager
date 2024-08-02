package group5.view;

import javax.swing.*;

public class ErrorMessanger extends JPanel {

    public static void displayError(JFrame baseView, String title, ErrorMessage errorMessage) {
        JOptionPane.showMessageDialog(baseView, errorMessage.toString(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ErrorMessage errorMessage = ErrorMessage.ERROR;
        displayError(frame, "Error", errorMessage);
    }
}

enum ErrorMessage {

    ERROR("error");
    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
