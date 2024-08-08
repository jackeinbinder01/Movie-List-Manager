package group5;

import group5.controller.Controller;
import group5.controller.IController;
import group5.model.IModel;
import group5.model.Model;
import group5.view.BaseView;
import group5.view.IView;

/**
 * Main driver for the program.
 */
public final class MovieListManager {

    /**
     * Private constructor to prevent instantiation.
     */
    private MovieListManager() {

    }

    /**
     * Main entry point for the program.
     *
     * @param args command line arguments, ignored in this implementation
     */
    public static void main(String[] args) {
        IModel model = new Model();
        IView view = new BaseView();
        IController controller = new Controller(model, view);
        controller.go();
    }
}
