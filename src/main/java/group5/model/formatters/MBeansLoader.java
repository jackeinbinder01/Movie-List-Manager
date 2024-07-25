package group5.model.formatters;

import java.io.File;
import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import group5.model.beans.MBeans;
import group5.model.net.NetUtils;

import java.util.Set;
import java.util.HashSet;

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

    private static Set<MBeans> loadMediasFromJSON(String filename) {
        try {
            File inFile = new File(filename);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Set<MBeans> records = new HashSet<>();
            records = mapper.readValue(inFile, new TypeReference<Set<MBeans>>() { });
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
            while(it.hasNext()) {
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
        Set<MBeans> medias = new HashSet<>();
        Set<MBeans> media = loadMediasFromFile("./data/samples/source.json", Formats.JSON);
        Set<MBeans> mediaSet = loadMediasFromFile("./data/samples/source2.csv", Formats.CSV);
        //System.out.println("OLD\n" + media);
        //System.out.println("SET\n" + mediaSet);
        //List<MBeans> records = loadWatchListFromFile("./src/test/testing_resources/sample.json", Formats.JSON);
        //List<MBeans> source = loadSourceFromJSON("./src/test/testing_resources/sample.json");
        //System.out.println(source);
        //MBeansFormatter.writeMediasToFile(media, new FileOutputStream("./data/samples/source2.csv"), Formats.CSV);

        System.out.println("json");
        for (MBeans bean : media) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println(bean.getTitle() + "  Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
        System.out.println("csv");
        for (MBeans bean : mediaSet) {
            int hashCode = System.identityHashCode(bean); // identityHash to show matching references
            System.out.println(bean.getTitle() + "  Object hash code: " + hashCode + "  Local Hash: " + bean.hashCode());
        }
    }
}