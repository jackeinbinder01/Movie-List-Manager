package group5.model.formatters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import java.util.Collection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import group5.model.beans.MBeans;

/**
 * A class to format the MBeans data in different ways.
 */
public final class MBeansFormatter {

    /**
     * Private constructor to prevent instantiation.
     */
    private MBeansFormatter() {
        // empty
    }

    /**
     * Pretty print the data in a human readable format.
     *
     * @param records the records to print
     * @param out the output stream to write to
     */
     private static void writeMediasToTXT(Collection<MBeans> records, OutputStream out) {
         PrintStream pout = new PrintStream(out); // so i can use println
         int i = 1;
         for (MBeans record : records) {
            String recordStr = record.toString();
            recordStr = recordStr.replace("Title: ", "");
            recordStr = recordStr.replace("-1.0", "N/A");
            recordStr = recordStr.replace("-1", "N/A");
            recordStr = recordStr.replace("\n", "\n    ");
             pout.println(i + ". " + recordStr);
             pout.println();
             i++;
         }
     }

    /**
     * Write the data as XML.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
     private static void writeMediasToXML(Collection<MBeans> records, OutputStream out) {
         XmlMapper mapper = new XmlMapper();
         mapper.enable((SerializationFeature.INDENT_OUTPUT));
         MovieXMLWrapper domain = new MovieXMLWrapper(records);
         try {
             mapper.writeValue(out, domain);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    /**
     * Write the data as JSON.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeMediasToJSON(Collection<MBeans> records, OutputStream out) {
        if (records == null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(out, records);
        } catch (IOException e) {
            System.out.println("Error writing to JSON");
            e.printStackTrace();
        }
    }

    /**
     * Write the data as CSV.
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    private static void writeMediasToCSV(Collection<MBeans> records, OutputStream out) {
        if (records == null) {
            return;
        }
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(MBeans.class).withHeader();
        ObjectWriter csvWriter = mapper.writer(schema);
        try {
            csvWriter.writeValue(out, records);
        } catch (IOException e) {
            System.out.println("Error writing to CSV");
            e.printStackTrace();
        }
    }

    /**
     * Public writer method to be invoked from Model and MovieList.
     *
     * Only accepts the following formats: JSON, CSV, PRETTY, XML
     *
     * @param records the records to write
     * @param out the output stream to write to
     */
    public static void writeMediasToFile(Collection<MBeans> records, OutputStream out, Formats format) {
        switch (format) {
            case JSON:
                writeMediasToJSON(records, out);
                break;
            case CSV:
                writeMediasToCSV(records, out);
                break;
            case PRETTY:
                writeMediasToTXT(records, out);
                break;
            case XML:
                writeMediasToXML(records, out);
                break;
            default:
                System.out.println("Invalid format");
        }
    }
}
