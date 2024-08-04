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
 * A class to format the data in different ways.
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

//     /**
//      * Write the data in the specified format.
//      *
//      * @param records the records to write
//      * @param format the format to write the records in
//      * @param out the output stream to write to
//      */
//     public static void write(@Nonnull Collection<DNRecord> records, @Nonnull Formats format,
//             @Nonnull OutputStream out) {

//         switch (format) {
//             case XML:
//                 writeXmlData(records, out);
//                 break;
//             case JSON:
//                 writeJsonData(records, out);
//                 break;
//             case CSV:
//                 writeCSVData(records, out);
//                 break;
//             default:
//                 prettyPrint(records, out);

//         }
//     }

//     private static class TestDataFormatter {

//         /**
//          * main for the tests.
//          *
//          * @param args
//          */
//         public static void main(String[] args) {
//             testPrettyPrint();
//             testPrettySingle();
//         }

//         /**
//          * a test for the pretty print.
//          */
//         public static void testPrettyPrint() {
//             // Create sample DNRecord objects
//             ArrayList<DNRecord> records = new ArrayList<>();
//             records.add(new DNRecord("www.example.com", "93.184.216.34",
//                     "City", "Region", "Country",
//                     "Postal", 1.23, 4.56));

//             // Capture standard output
//             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//             PrintStream printStream = new PrintStream(outputStream);
//             PrintStream originalOut = System.out;
//             System.setOut(printStream);

//             // Call the prettyPrint method
//             DataFormatter.prettyPrint(records, System.out);

//             // Restore standard output
//             System.setOut(originalOut);

//             // Convert printed output to string
//             String printedOutput = outputStream.toString();

//             // Verify the printed output
//             String expectedOutput = "www.example.com\n"
//                     + "             IP: 93.184.216.34\n"
//                     + "       Location: City, Region, Country, Postal\n"
//                     + "    Coordinates: 1.23, 4.56\n";
//             if (printedOutput.equals(expectedOutput)) {
//                 System.out.println("Test passed: prettyPrint");
//             } else {
//                 System.out.println("Test failed: prettyPrint");
//             }
//         }

//         /**
//          * a test for the pretty format.
//          */
//         public static void testPrettySingle() {
//             // Create a sample DNRecord
//             DNRecord record = new DNRecord("www.example.com", "93.184.216.34",
//                     "City", "Region", "Country",
//                     "Postal", 1.23, 4.56);

//             // Capture standard output
//             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//             PrintStream printStream = new PrintStream(outputStream);
//             PrintStream originalOut = System.out;
//             System.setOut(printStream);

//             // Call the prettySingle method
//             DataFormatter.prettySingle(record, System.out);

//             // Restore standard output
//             System.setOut(originalOut);

//             // Convert printed output to string
//             String printedOutput = outputStream.toString();

//             // Verify the printed output
//             String expectedOutput = "www.example.com\n"
//                     + "             IP: 93.184.216.34\n"
//                     + "       Location: City, Region, Country, Postal\n"
//                     + "    Coordinates: 1.23, 4.56\n";
//             if (printedOutput.equals(expectedOutput)) {
//                 System.out.println("Test passed: prettySingle");
//             } else {
//                 System.out.println("Test failed: prettySingle");
//             }
//         }
//     }
}
