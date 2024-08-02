package group5.model.formatters;

import java.io.File;
import java.io.FileOutputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import group5.model.beans.MBeans;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

import group5.model.net.apiFunctionality.MovieAPIHandler;

public class MBeansLoader {

    private MBeansLoader() {
    }

    public static List<MBeans> loadMBeansFromAPI(String title, String year, String type) {
        try {

            return MovieAPIHandler.getMoreSourceBeans(title, year);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Set<MBeans> loadMediasFromJSON(String filename) {
        try {
            File inFile = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Set<MBeans> records = new HashSet<>();
            records = mapper.readValue(inFile, new TypeReference<Set<MBeans>>() {
            });
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Set<MBeans> loadMediasFromCSV(String filename) {
        try {
            File inFile = new File(filename);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<MBeans> it = mapper.readerFor(MBeans.class)
                    .with(schema)
                    .readValues(inFile);
            Set<MBeans> records = new HashSet<>();
            while (it.hasNext()) {
                records.add(it.next());
            }
            return records;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<MBeans> loadMediasFromFile(String filename, Formats format) {
        if (format == Formats.JSON) {
            return loadMediasFromJSON(filename);
        } else if (format == Formats.CSV) {
            return loadMediasFromCSV(filename);
        } else {
            return null;
        }
    }

    /**
     * Main to test the loader.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("START");
        Set<MBeans> bean = loadMediasFromFile("./data/test/empty.json", Formats.JSON);
        System.out.println(bean);
        MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("empty.json"), Formats.JSON);
        MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("empty.csv"), Formats.CSV);
        //System.out.println(loadMBeansFromAPI("level+2","2008",""));
        //Set<MBeans> records = loadMediasFromFile("data/samples/watchlist.json", Formats.JSON);
        //MBeans empty = loadMediasFromFile("data/test/empty.json", Formats.JSON).iterator().next();
        //System.out.println(empty);
        //records.add(empty);
        //MBeansFormatter.writeMediasToFile(records, new FileOutputStream("new_csv.csv"), Formats.CSV);
        //MBeansFormatter.writeMediasToFile(records, new FileOutputStream("new_json.json"), Formats.JSON);
    }
}
