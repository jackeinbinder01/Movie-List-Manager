package group5.model.formatters;

import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import group5.model.beans.MBeans;

import java.util.Set;
import java.util.HashSet;

/**
 * Class to load file data into MBeans objects.
 */
public class MBeansLoader {

    /**
     * Private constructor to prevent instantiation.
     */
    private MBeansLoader() {
        // empty
    }

    /**
     * Deserialize data from JSON data format into MBeans objects.
     *
     * @param filename path to the file to read from
     * @return Set of MBeans objects mapped from read file
     */
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

    /**
     * Deserialize data from CSV data format into MBeans objects.
     *
     * @param filename path to the file to read from
     * @return Set of MBeans objects mapped from read file
     */
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
                    records.add(next);
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

    /**
     * Public loader method to be invoked from Model.
     *
     * Only accepts the following formats: JSON, CSV
     *
     * @param filename path to the file to read from
     * @param format the format of the file
     * @return Set of MBeans objects mapped from read file
     */
    public static Set<MBeans> loadMediasFromFile(String filename, Formats format) {
        if (format == Formats.JSON) {
            return loadMediasFromJSON(filename);
        } else if (format == Formats.CSV) {
            return loadMediasFromCSV(filename);
        } else {
            return null;
        }
    }
}
