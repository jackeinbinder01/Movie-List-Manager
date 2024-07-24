package group5.model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;

public class Model implements IModel {

    /** List of MBeans representing the source databse list. */
    private List<MBeans> sourceList;

    /** List of imdbID String to reference which media from source list is in watch list. */
    /** List of watchLists where each holds a list of reference to source list MBeans. */
    private List<List<MBeans>> watchLists;

    public Model() {
        loadSourceData();
        this.watchLists = new ArrayList<>();
    }

    @Override
    public void loadSourceData() {
        System.out.println("Load source database from" + DEFAULT_DATA);
        this.sourceList = MBeansLoader.loadMediasFromFile(DEFAULT_DATA, Formats.JSON);
    }

    @Override
    public void loadWatchList(String filename) {
        List<MBeans> externalList = MBeansLoader.loadMediasFromFile(filename, Formats.JSON);
        this.watchLists.add(externalList.stream()
                                        .map(externalBean ->
                                             this.sourceList.stream()
                                                            .filter(localBean -> localBean.equals(externalBean))
                                                            .findFirst()
                                                            .orElse(null))
                                        .collect(Collectors.toList()));
    }

    @Override
    public Stream<MBeans> getSourceLists() {
        return this.sourceList.stream();
    }

    @Override
    public Stream<MBeans> getWatchLists(int userListId) {
        return this.watchLists.get(userListId).stream();
    }

    @Override
    public void saveWatchList(String filename, int userListId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveWatchList'");
    }

    @Override
    public void addToWatchList(MBeans media, int userListId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToWatchList'");
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
        //System.out.println("Source list:" + model.getSourceLists().collect(Collectors.toList()));
        model.loadWatchList("data/samples/watchlist.json");
        List<MBeans> externalList = MBeansLoader.loadMediasFromFile("data/samples/watchlist.json", Formats.JSON);
        System.out.println("Source");
        for (MBeans bean : model.getSourceLists().collect(Collectors.toList())) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println("Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("WatchList");
        for (MBeans bean : model.getWatchLists(0).collect(Collectors.toList())) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println("Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("Not reference WatchList");
        for (MBeans bean : externalList) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println("Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
    }
}
