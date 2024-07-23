package group5.model;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;

public class Model implements IModel {

    /** List of MBeans representing the source databse list. */
    private List<MBeans> sourceList;

    /** List of imdbID String to reference which media from source list is in watch list. */
    private List<String> watchList;

    public Model() {
        loadSourceData();
    }

    @Override
    public void loadSourceData() {
        System.out.println("Load source database from" + DEFAULT_DATA);
        this.sourceList = MBeansLoader.loadMediasFromFile(DEFAULT_DATA, Formats.JSON);
    }

    @Override
    public void loadWatchList(String filename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadWatchList'");
    }

    @Override
    public Stream<MBeans> getSourceLists() {
        return this.sourceList.stream();
    }

    @Override
    public Stream<MBeans> getWatchLists() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWatchLists'");
    }

    @Override
    public void saveWatchList(String filename, Stream<MBeans> watchList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveWatchList'");
    }

    @Override
    public void updateWatched(MBeans media, boolean watched) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateWatched'");
    }

    @Override
    public void updateUserRating(MBeans media, double rating) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserRating'");
    }

//    @Override
//    public Stream<MBeans> getFiltered() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
//    }

    /**
     * Main method to test the model.
     */
    public static void main(String[] args) {
        Model model = new Model();
        System.out.println("Source list:" + model.getSourceLists().collect(Collectors.toList()));
    }
}
