package group5.model;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.nio.file.FileSystems;

import group5.model.beans.MBeans;
import group5.model.formatters.MBeansLoader;
import group5.model.formatters.Formats;
import group5.model.IMovieListV2;

public class Model implements IModel {

    /** List of MBeans representing the source databse list. */
    private Set<MBeans> sourceList;

    /** List of watchLists where each holds a list of reference to source list MBeans. */
    private List<IMovieListV2> watchLists;

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
     * Create a set of MBeans where each item is a reference to same MBeans in source list.
     * Pass set to MovieListV2 constructor to create a new watch list.
     * Add the new watch list to the watchLists list.
     *
     * @param filename The file to load the watch list from.
     */
    @Override
    public void loadWatchList(String filename) {
        int lastSeparator = Math.max(filename.lastIndexOf("\\"), filename.lastIndexOf("/"));
        int lastDot = filename.lastIndexOf('.');
        String name = filename.substring(lastSeparator + 1, lastDot);
        Set<MBeans> externalList = MBeansLoader.loadMediasFromFile(filename, Formats.JSON);
        // Create a list of sourcelist references by mapping externalList to sourceList
        Set<MBeans> mapped = externalList.stream()
                                        .map(externalBean ->
                                            this.sourceList.stream()
                                                            .filter(localBean -> localBean.equals(externalBean))
                                                            .findFirst()
                                                            .orElse(null))
                                        .collect(Collectors.toSet());
        IMovieListV2 watchList = new MovieListV2(name, mapped);
        this.watchLists.add(watchList);
    }

    @Override
    public Stream<MBeans> getSourceLists() {
        return this.sourceList.stream();
    }

    @Override
    public Stream<MBeans> getWatchLists(int userListId) {
        return this.watchLists.get(userListId).getMovieList();
    }

    @Override
    public void saveWatchList(String filename, int userListId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveWatchList'");
    }

    /**
     * {@inheritDoc}
     *
     * Add the media to the watch list by adding a reference to the media in the source list.
     */
    @Override
    public void addToWatchList(MBeans media, int userListId) {
        MBeans sourceMedia = this.getMatchedObjectFromSource(media);
        this.watchLists.get(userListId).addToList(sourceMedia);
    }

    @Override
    public void removeFromWatchList(MBeans media, int userListId) {
        this.watchLists.get(userListId).removeFromList(media);
    }

    @Override
    public void updateWatched(MBeans media, boolean watched) {
        this.getMatchedObjectFromSource(media).setWatched(watched);
        // TODO Reflecting changes into actual file after these are updated
    }

    @Override
    public void updateUserRating(MBeans media, double rating) {
        this.getMatchedObjectFromSource(media).setMyRating(rating);
        // TODO Reflecting changes into actual file after these are updated
    }

	@Override
	public String getUserListName(int userListId) {
		return this.watchLists.get(userListId).getListName();
	}

	@Override
	public int getUserListCount() {
		return this.watchLists.size();
	}

	@Override
	public int[] getUserListIndicesForRecord(MBeans record) {
		int[] indices = IntStream.range(0, this.watchLists.size())
                                 .filter(i -> this.watchLists.get(i).containsMedia(record))
                                 .toArray();
        return indices;
	}

	@Override
	public void setUserListIndicesForRecord(MBeans record, int[] userListIndices) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setUserListIndicesForRecird'");
	}

//    @Override
//    public void updateWatchList(MBeans media, int userListId) {
      // TODO Need method signature of what will be passed from Controller
//    public Stream<MBeans> getFiltered() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
//    }


    /**
     * Get the object reference of the MBeans that matched given media inside the source list.
     *
     * @param media
     * @return MBeans object reference of the media with the same imdbID
     */
    private MBeans getMatchedObjectFromSource(MBeans media) {
        return this.sourceList.stream()
                              .filter(bean -> bean.equals(media))
                              .findFirst()
                              .orElse(null);
    }


    /**
     * Main method to test the model.
     */
    public static void main(String[] args) {
        Model model = new Model();
        model.loadWatchList("data/samples/watchlist.json");
        model.loadWatchList("data/samples/source.json");
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
    }


}
