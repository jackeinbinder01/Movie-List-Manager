package group5.model;

import group5.model.beans.MBeans;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.List;

public interface IModel {

    public static final String DEFAULT_DATA = "";  // Path to default movie DB here

    void loadSourceData();

    void loadWatchList();

    Stream<MBeans> getSourceLists();

    Stream<MBeans> getWatchLists(String filename);

    void saveWatchList(String filename, Stream<MBeans> watchList);

    /*public Stream<MBeans> getMovieList(FilterClass filter) {
        return getMovieList(filter, null);
    }*/

    //Stream<MBeans> getMovieList(FilterClass filter, UserListIdentifier userListId);



}
