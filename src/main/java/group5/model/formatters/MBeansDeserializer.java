package group5.model.formatters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Custom Deserializer class for deserializaing data to MBeans fields.
 */
public class MBeansDeserializer {

    /** Nested static class for Deserializing Integer. */
    public static class IntDeserializer extends JsonDeserializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to Integer.
         * In an event of N/A, returns -1.
         *
         * @return Integer value of read string
         */
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String intString = p.getValueAsString(); // Get the value as a string
            if (intString.equals("N/A")) {
                return -1;
            } else {
                return Integer.parseInt(intString);
            }
        }
    }

    /** Nested static class for Deserializing Double. */
    public static class DoubleDeserializer extends JsonDeserializer<Double> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to Double.
         * In an event of N/A, returns -1.0.
         *
         * @return Double value of read string
         */
        @Override
        public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String doubleString = p.getValueAsString(); // Get the value as a string
            if (doubleString.equals("N/A")) {
                return -1.0;
            } else {
                return Double.parseDouble(doubleString);
            }
        }
    }

    /** Nested static class for Deserializing Released data value. */
    public static class DateDeserializer extends JsonDeserializer<LocalDate> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to LocalDate object.
         * In an event of N/A, returns null.
         *
         * @return LocalDate object of read released date
         */
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getValueAsString(); // Get the value as a string
            if (dateString.equals("N/A")) {
                return null;
            } else {
                try {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy");
                    return LocalDate.parse(dateString, format); // Parse to Date
                } catch (Exception e) {
                    throw new IOException("Error parsing date value: " + dateString, e);
                }
            }
        }
    }

    /** Nested static class for Deserializing Runtime value. */
    public static class RuntimeDeserializer extends JsonDeserializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to Integer representing runtime in minutes.
         * In an event of N/A, returns -1.
         *
         * @return Integer representing runtime
         */
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String minutesString = p.getValueAsString(); // Get the value as a string
            if (minutesString.equals("N/A")) {
                return -1;
            }
            String numericValue = minutesString.split(" ")[0]; // Extract numeric part
            int minutes = Integer.parseInt(numericValue); // Parse to integer
            return minutes;
        }
    }

    /** Nested static class for Deserializing List of values. */
    public static class StringListDeserializer extends JsonDeserializer<List<String>> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to List of Strings.
         *
         * @return List of Strings from a list of values read
         */
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String genreString = p.getValueAsString(); // Get the value as a string
            List<String> genreArray = Arrays.asList(genreString.split(", ")); // Split to Array
            return genreArray;
        }
    }

    /** Nested static class for Deserializing Box Office value. */
    public static class BoxOfficeDeserializer extends JsonDeserializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Deserialize read string value to Integer representing BoxOffice value.
         * In an event of N/A, returns -1.
         *
         * @return Integer Representing BoxOffice value
         */
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String boxOfficeString = p.getValueAsString(); // Get the value as a string
            if (boxOfficeString.equals("N/A")) {
                return -1;
            }
            String numericValue = boxOfficeString.replace("$", "").replace(",", ""); // Extract only numeric part
            int boxOffice = Integer.parseInt(numericValue); // Parse to integer
            return boxOffice;
        }
    }
}
