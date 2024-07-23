package group5.controller;

import com.sun.security.jgss.GSSUtil;
import group5.model.IModel;
import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;
import group5.view.IView;

import java.util.List;

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

        String sampleDataPath = "data/samples/source.json";
        List<MBeans> records = MBeansLoader.loadMediasFromFile(sampleDataPath, Formats.JSON);
        view.setMainTableRecords(records.stream());
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

    @Override
    public void showRecordDetails(MBeans record) {
        System.out.println("[Controller] showRecordDetails called");
        view.setDetailsPaneEntry(record);
    }


    @Override
    public void applyFilters() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("[Controller.java] Unimplemented method 'applyFilters'");
    }

    @Override
    public void clearFilters() {
        view.clearFilters();
        // TODO: set the tables in the view to unfiltered
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
