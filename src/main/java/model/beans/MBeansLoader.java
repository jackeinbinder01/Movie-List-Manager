package model.beans;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.net.NetUtils;

public class MBeansLoader {

    private MBeansLoader() {}

    public static MBeans loadMBeansFromAPI(String title, String year, String type) {
        try {
            InputStream inStream = NetUtils.getMediaDetails(title, year, type);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MBeans media = mapper.readValue(inStream, MBeans.class);
            return media;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MBeans> loadMBeansFromFile(String filename) {
        try {
            File inFile = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            List<MBeans> records = new ArrayList<>();
            records = mapper.readValue(inFile, new TypeReference<List<MBeans>>() { });
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Main to test the loader.
     */
    public static void main(String[] args) {
        MBeans media = loadMBeansFromAPI("The Matrix", "1999", "movie");
        System.out.println(media);
        List<MBeans> records = loadMBeansFromFile("data/movies.json");
        System.out.println(records);
    }
}