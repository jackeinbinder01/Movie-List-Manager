package model.beans;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MBeansDeserializer {

    public static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getValueAsString(); // Get the value as a string
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
                return format.parse(dateString); // Parse to Date
            } catch (Exception e) {
                throw new IOException("Error parsing date value: " + dateString, e);
            }
        }
    }
    public static class RuntimeDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String minutesString = p.getValueAsString(); // Get the value as a string
            String numericValue = minutesString.split(" ")[0]; // Extract numeric part
            int minutes = Integer.parseInt(numericValue); // Parse to integer
            return minutes + 1; // Example: add 1 to transform 194 to 195
        }
    }

    public static class GenreDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String genreString = p.getValueAsString(); // Get the value as a string
            List<String> genreArray = Arrays.asList(genreString.split(", ")); // Split to Array
            return genreArray; // Example: add 1 to transform 194 to 195
        }
    }

    public static class DirectorDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String directorString = p.getValueAsString(); // Get the value as a string
            List<String> directorArray = Arrays.asList(directorString.split(", ")); // Split to Array
            return directorArray; // Example: add 1 to transform 194 to 195
        }
    }

    public static class WriterDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String writerString = p.getValueAsString(); // Get the value as a string
            List<String> writerArray = Arrays.asList(writerString.split(", ")); // Split to Array
            return writerArray; // Example: add 1 to transform 194 to 195
        }
    }

    public static class ActorDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String actorString = p.getValueAsString(); // Get the value as a string
            List<String> actorArray = Arrays.asList(actorString.split(", ")); // Split to Array
            return actorArray; // Example: add 1 to transform 194 to 195
        }
    }

    public static class LanguageDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String languageString = p.getValueAsString(); // Get the value as a string
            List<String> languageArray = Arrays.asList(languageString.split(", ")); // Split to Array
            return languageArray; // Example: add 1 to transform 194 to 195
        }
    }

    public static class CountryDeserializer extends JsonDeserializer<List<String>> {
        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String countryString = p.getValueAsString(); // Get the value as a string
            List<String> countryArray = Arrays.asList(countryString.split(", ")); // Split to Array
            return countryArray; // Example: add 1 to transform 194 to 195
        }
    }
}
