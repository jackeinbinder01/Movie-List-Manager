package group5.model.formatters;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

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

//     /**
//      * Pretty print the data in a human readable format.
//      *
//      * @param records the records to print
//      * @param out the output stream to write to
//      */
//     private static void prettyPrint(Collection<DNRecord> records, OutputStream out) {
//         PrintStream pout = new PrintStream(out); // so i can use println
//         for (DNRecord record : records) {
//             prettySingle(record, pout);
//             pout.println();
//         }
//     }

//     /**
//      * Pretty print a single record.
//      *
//      * Let this as an example, so you didn't have to worry about spacing.
//      *
//      * @param record the record to print
//      * @param out the output stream to write to
//      */
//     private static void prettySingle(@Nonnull DNRecord record, @Nonnull PrintStream out) {
//         out.println(record.hostname());
//         out.println("             IP: " + record.ip());
//         out.println("       Location: " + record.city() + ", " + record.region() + ", "
//                 + record.country() + ", " + record.postal());
//         out.println("    Coordinates: " + record.latitude() + ", " + record.longitude());

//     }

//     /**
//      * Write the data as XML.
//      *
//      * @param records the records to write
//      * @param out the output stream to write to
//      */
//     private static void writeXmlData(Collection<DNRecord> records, OutputStream out) {
//         XmlMapper mapper = new XmlMapper();
//         mapper.enable((SerializationFeature.INDENT_OUTPUT));
//         DomainXmlWrapper domain = new DomainXmlWrapper(records);
//         try {
//             mapper.writeValue(out, domain);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }


    public static void writeMediaToJSON(Collection<MBeans> records, OutputStream out) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(out, records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMediaToCSV(Collection<MBeans> records, OutputStream out) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(MBeans.class).withHeader();
        ObjectWriter csvWriter = mapper.writer(schema);
        try {
            csvWriter.writeValue(out, records);
        } catch (IOException e) {
            System.out.println("the error is here");
            e.printStackTrace();
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
