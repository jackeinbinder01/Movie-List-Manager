package group5.model.formatters;

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

import group5.model.beans.MBeans;
import group5.model.net.NetUtils;
import group5.model.beans.MBeansViews;
import group5.model.formatters.MBeansFormatter;

import java.io.FileOutputStream;

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

    public static List<MBeans> loadWatchListFromJSON(String filename) {
        try {
            File inFile = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<MBeans> records = new ArrayList<>();
            records = mapper.readerWithView(MBeansViews.CompleteView.class)
                            .forType(new TypeReference<List<MBeans>>() { })
                            .readValue(inFile);
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MBeans> loadSourceFromJSON(String filename) {
        try {
            File inFile = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<MBeans> records = new ArrayList<>();
            records = mapper.readerWithView(MBeansViews.PartialView.class)
                            .forType(new TypeReference<List<MBeans>>() { })
                            .readValue(inFile);
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MBeans> loadWatchListFromCSV(String filename) {
        try {
            File inFile = new File(filename);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<MBeans> it = mapper.readerWithView(MBeansViews.CompleteView.class)
                                               .forType(MBeans.class)
                                               .with(schema)
                                               .readValues(inFile);
            List<MBeans> records = it.readAll();
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MBeans> loadWatchListFromFile(String filename, Formats format) {
        if (format == Formats.JSON) {
            return loadWatchListFromJSON(filename);
        } else if (format == Formats.CSV) {
            return loadWatchListFromCSV(filename);
        } else {
            return null;
        }
    }

    /**
     * Main to test the loader.
     */
    public static void main(String[] args) throws Exception {
        List<MBeans> medias = new ArrayList<>();
        MBeans media = loadMBeansFromAPI("The Matrix", "1999", "movie");
        medias.add(loadMBeansFromAPI("Titanic", "", "movie"));
        medias.add(loadMBeansFromAPI("Shrek", "", "movie"));
        medias.add(loadMBeansFromAPI("Shrek 2", "", "movie"));
        medias.add(loadMBeansFromAPI("Shrek 3", "", "movie"));
        medias.add(loadMBeansFromAPI("John Wick", "", "movie"));
        medias.add(loadMBeansFromAPI("Avatar", "", "movie"));
        medias.add(loadMBeansFromAPI("Exodus", "", "movie"));
        medias.add(loadMBeansFromAPI("Equalizer", "", "movie"));
        medias.add(loadMBeansFromAPI("Sharknado", "", "movie"));
        medias.add(loadMBeansFromAPI("Inside out", "", "movie"));
        medias.add(loadMBeansFromAPI("Monsters", "", "movie"));
        medias.add(loadMBeansFromAPI("Spirited Away", "", "movie"));
        System.out.println(media);
        //List<MBeans> records = loadWatchListFromFile("./src/test/testing_resources/sample.json", Formats.JSON);
        //List<MBeans> source = loadSourceFromJSON("./src/test/testing_resources/sample.json");
        //System.out.println(records);
        //System.out.println(source);
        MBeansFormatter.writeSourceToJSON(medias, new FileOutputStream("./data/samples/source.json"));
    }
}