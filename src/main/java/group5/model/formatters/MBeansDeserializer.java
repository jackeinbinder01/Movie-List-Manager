package group5.model.formatters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MBeansDeserializer {

    public static class IntDeserializer extends JsonDeserializer<Integer> {
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

    public static class DoubleDeserializer extends JsonDeserializer<Double> {
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

    public static class DateDeserializer extends JsonDeserializer<LocalDate> {
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

    public static class RuntimeDeserializer extends JsonDeserializer<Integer> {
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

    public static class StringListDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String genreString = p.getValueAsString(); // Get the value as a string
            List<String> genreArray = Arrays.asList(genreString.split(", ")); // Split to Array
            return genreArray;
        }
    }

    public static class BoxOfficeDeserializer extends JsonDeserializer<Integer> {
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
