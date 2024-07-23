package group5.model;

import group5.model.beans.MBeans;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.List;

public interface IModel {

    public static final String DEFAULT_DATA = "./data/samples/source.json";  // Path to default movie DB here

    void loadSourceData();

    void loadWatchList(String filename);

    Stream<MBeans> getSourceLists();

    Stream<MBeans> getWatchLists(/*, UserListIdentifier userListId*/);

    void saveWatchList(String filename, Stream<MBeans> watchList/*, UserListIdentifier userListId*/);

    void updateWatched(MBeans media, boolean watched);

    void updateUserRating(MBeans media, double rating);

    /*public Stream<MBeans> getMovieList(FilterClass filter) {
        return getMovieList(filter, null);
    }*/

    //Stream<MBeans> getMovieList(FilterClass filter, UserListIdentifier userListId);



}
