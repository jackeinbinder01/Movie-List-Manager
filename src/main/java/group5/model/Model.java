package group5.model;

import java.util.List;
import java.util.stream.Stream;

import group5.model.beans.MBeans;

public class Model implements IModel {

    @Override
    public void loadSourceData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadSourceData'");
    }

    @Override
    public void loadWatchList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadWatchList'");
    }

    @Override
    public Stream<MBeans> getSourceLists() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSourceLists'");
    }

    @Override
    public Stream<MBeans> getWatchLists(String filename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWatchLists'");
    }

    @Override
    public void saveWatchList(String filename, Stream<MBeans> watchList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveWatchList'");
    }

//    @Override
//    public Stream<MBeans> getFiltered() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'getFiltered'");
//    }



}
