package group5;

import group5.controller.Controller;
import group5.controller.IController;
import group5.model.IModel;
import group5.model.Model;
import group5.view.BaseView;
import group5.view.IView;

/**
 * Main driver for the program.
 * Supports both CLI and GUI modes based on input arguments.
 */
public class MovieListManager {

    /**
     * Private constructor to prevent instantiation.
     */
    private MovieListManager() {
        // empty
    }

    /**
     * Main entry point for the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO: do we need argument parsing at all?
        // TODO: question: how are user lists stored locally?
        IModel model = new Model();
        IView view = new BaseView();
        IController controller = new Controller(model, view);
        controller.go();

    }



}