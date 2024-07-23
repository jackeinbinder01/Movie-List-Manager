package group5.controller;

import com.sun.security.jgss.GSSUtil;
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

        System.out.println("[Controller] Controller constructor called");
        this.model = model;
        this.view = view;

        // bindFeatures accept an IFeature interface, which is the controller itself
        view.bindFeatures(this);
    }

    @Override
    public void exportListToFile(String filepath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'exportListToFile'");
    }

    @Override
    public void addListFromFile(String filepath) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'addListFromFile'");
    }

    /**
     * Main entry point for the controller.
     */
    @Override
    public void go() {
        System.out.println("[Controller] Controller.go() called");

        view.display();
    }
}
