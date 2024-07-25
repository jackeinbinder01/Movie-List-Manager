package group5.model;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;

public class Model implements IModel {

    /** List of MBeans representing the source databse list. */
    private Set<MBeans> sourceList;

    /** List of watchLists where each holds a list of reference to source list MBeans. */
    private List<Set<MBeans>> watchLists;

    /**
     * Model class constructor.
     */
    public Model() {
        loadSourceData();
        this.watchLists = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     *
     * Stores the source list in the sourceList field.
     */
    @Override
    public void loadSourceData() {
        System.out.println("Load source database from" + DEFAULT_DATA);
        this.sourceList = MBeansLoader.loadMediasFromFile(DEFAULT_DATA, Formats.JSON);
    }

    /**
     * {@inheritDoc}
     *
     * Create a list of MBeans where each is a reference to source list.
     */
    @Override
    public void loadWatchList(String filename) {
        Set<MBeans> externalList = MBeansLoader.loadMediasFromFile(filename, Formats.JSON);
        // Create a list of sourcelist references by mapping externalList to sourceList
        this.watchLists.add(externalList.stream()
                                        .map(externalBean ->
                                             this.sourceList.stream()
                                                            .filter(localBean -> localBean.equals(externalBean))
                                                            .findFirst()
                                                            .orElse(null))
                                        .collect(Collectors.toSet()));
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
        for (MBeans bean : this.sourceList) {
            if (bean.equals(media)) {
                bean.setWatched(watched);
            }
        }
        // TODO Reflecting changes into actual file after these are updated
    }

    @Override
    public void updateUserRating(MBeans media, double rating) {
        for (MBeans bean : this.sourceList) {
            if (bean.equals(media)) {
                bean.setMyRating(rating);
            }
        }
        // TODO Reflecting changes into actual file after these are updated
    }

    /**
     * Get the object reference of the MBeans that matched given media inside the source list.
     *
     * @param media
     * @return MBeans object reference of the media with the same imdbID
     */
    public MBeans getMatchedObjectFromSource(MBeans media) {
        return this.sourceList.stream()
                              .filter(bean -> bean.equals(media))
                              .findFirst()
                              .orElse(null);
        }

//    @Override
//    public void updateWatchList(MBeans media, int userListId) {
      // TODO Need method signature of what will be passed from Controller
//    public Stream<MBeans> getFiltered() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
//    }

    /**
     * Main method to test the model.
     */
    public static void main(String[] args) {
        Model model = new Model();
        model.loadWatchList("data/samples/watchlist.json");
        Set<MBeans> externalList = MBeansLoader.loadMediasFromFile("data/samples/watchlist.json", Formats.JSON);
        System.out.println("Source");
        for (MBeans bean : model.getSourceLists().collect(Collectors.toList())) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println(bean.getTitle() + "  Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("WatchList");
        for (MBeans bean : model.getWatchLists(0).collect(Collectors.toList())) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println(bean.getTitle() + "  Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("Not reference WatchList");
        for (MBeans bean : externalList) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println(bean.getTitle() + "  Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("\nextList item:\n" + System.identityHashCode(externalList.get(0)));
        System.out.println("\nreturn matched ID:\n" + System.identityHashCode(model.getMatchedObjectFromSource(externalList.get(0))));
        System.out.println("\nextList item:\n" + externalList.get(0));
        System.out.println("\nreturn matched ID Content:\n" + model.getMatchedObjectFromSource(externalList.get(0)));
    }
}
