package model.beans;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import model.net.NetUtils;
import model.formatters.Formats;

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

    private static List<MBeans> loadMBeansFromJSON(String filename) {
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

    private static List<MBeans> loadMBeansFromCSV(String filename) {
        try {
            File inFile = new File(filename);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(MBeans.class).withHeader();
            MappingIterator<MBeans> it = mapper.readerFor(MBeans.class).with(schema).readValues(inFile);
            List<MBeans> records = it.readAll();
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MBeans> loadMBeansFromFile(String filename, Formats format) {
        if (format == Formats.JSON) {
            return loadMBeansFromJSON(filename);
        } else if (format == Formats.CSV) {
            return loadMBeansFromCSV(filename);
        } else {
            return null;
        }
    }

    /**
     * Main to test the loader.
     */
    public static void main(String[] args) {
        MBeans media = loadMBeansFromAPI("The Matrix", "1999", "movie");
        System.out.println(media);
        List<MBeans> records = loadMBeansFromFile("./src/test/testing_resources/sample.json", Formats.JSON);
        System.out.println(records);
    }
}