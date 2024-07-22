package group5.controller;

import group5.model.IModel;
import group5.view.IView;

/**
 * Controller class for the program.
 */
public class Controller implements IController, IFeature {
    /**
     * The model object representing the movie database.
     */
    IModel model;
    /**
     * The view object representing the user interface.
     */
    IView view;

    /**
     * Constructor for the controller.
     *
     * @param model the model object representing the movie database
     * @param view  the view object representing the user interface
     */
    public Controller(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Main entry point for the controller.
     */
    @Override
    public void go() {
        System.out.println("Controller.go() called");
    }
}
