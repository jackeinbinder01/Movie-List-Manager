package group5.model;

import group5.model.beans.MBeans;

import java.util.List;

public interface IModel {

    public static final String DEFAULT_DATA = "";  // Path to default movie DB here

    List<MBeans> loadSourceData();

    List<MBeans> loadWatchList(String filename);

    void saveWatchList(String filename, List<MBeans> watchList);


}
