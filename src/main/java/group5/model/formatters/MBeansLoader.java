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

import group5.model.net.MovieAPIHandler;

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
            if (records.stream().anyMatch(bean -> bean.getID() == null)) {
                return null;
            }
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
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<MBeans> it = mapper.readerFor(MBeans.class)
                    .with(schema)
                    .readValues(inFile);
            Set<MBeans> records = new HashSet<>();
            while (it.hasNext()) {
                MBeans next = it.next();
                if (next.getID() != null) {
                    records.add(it.next());
                } else {
                    return null;
                }
            }

            if (records.isEmpty()) {
                return null;
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
        //Set<MBeans> bean = loadMediasFromFile("./data/test/sample.json", Formats.JSON);
        Set<MBeans> bean = loadMediasFromFile("./bad.csv", Formats.CSV);
        Set<MBeans> bean2 = loadMediasFromFile("./bad.json", Formats.JSON);
        //Set<MBeans> empty = loadMediasFromFile("./data/test/empty.json", Formats.JSON);
        System.out.println(bean);
        System.out.println(bean2);
        //System.out.println(bean2);
        /*MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("test.xml"), Formats.XML);
        MBeansFormatter.writeMediasToFile(empty, new FileOutputStream("testE.xml"), Formats.XML);
        MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("test.json"), Formats.JSON);
        MBeansFormatter.writeMediasToFile(empty, new FileOutputStream("testE.json"), Formats.JSON);
        MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("test.csv"), Formats.CSV);
        MBeansFormatter.writeMediasToFile(empty, new FileOutputStream("testE.csv"), Formats.CSV);
        MBeansFormatter.writeMediasToFile(bean, new FileOutputStream("test.txt"), Formats.PRETTY);
        MBeansFormatter.writeMediasToFile(empty, new FileOutputStream("testE.txt"), Formats.PRETTY);*/
    }
}
