package group5.model.formatters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;

/**
 * Custom Serializer class for deserializaing data to MBeans fields.
 */
public class MBeansSerializer {

    /** Nested static class for Serializing Integer. */
    public static class IntSerializer extends JsonSerializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Serialize Integer to string.
         * If given -1, write N/A.
         */
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ( value == -1 ) {
                gen.writeString("N/A");
            } else {
                gen.writeNumber(value);
            }
        }
    }

    /** Nested static class for Serializing Double. */
    public static class DoubleSerializer extends JsonSerializer<Double> {

        /**
         * {@inheritDoc}
         *
         * Serialize Double to string.
         * If given -1.0, write N/A.
         */
        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ( value == -1.0 ) {
                gen.writeString("N/A");
            } else {
                gen.writeNumber(value);
            }
        }
    }

    /** Nested static class for Serializing Released data value. */
    public static class DateSerializer extends JsonSerializer<LocalDate> {

        /**
         * {@inheritDoc}
         *
         * Serialize LocalDate object to string.
         * If given 01/01/1800(very old date to flag null), write N/A.
         */
        @Override
        public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (date.equals(LocalDate.of(1800, 1, 1))) {
                gen.writeString("N/A");
                return;
            } else {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM yyyy");
                String formattedDate = date.format(format);
                gen.writeString(formattedDate);
            }
        }
    }

    /** Nested static class for Serializing Runtime value. */
    public static class RuntimeSerializer extends JsonSerializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Serialize Runtime integer value to string.
         * If given -1, write N/A, else append min after.
         */
        @Override
        public void serialize(Integer runtime, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (runtime == -1) {
                gen.writeString("N/A");
                return;
            } else {
                gen.writeString(runtime + " min");
            }
        }
    }

    /** Nested static class for Serializing List of values. */
    public static class StringListSerializer extends JsonSerializer<List<String>> {

        /**
         * {@inheritDoc}
         *
         * Serialize list of values to string.
         * Use `,` as separator between values.
         */
        @Override
        public void serialize(List<String> genre, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", genre));
        }
    }

    /** Nested static class for Serializing Box Office value. */
    public static class BoxOfficeSerializer extends JsonSerializer<Integer> {

        /**
         * {@inheritDoc}
         *
         * Serialize box office value to string.
         * If given -1, write N/A, else append `$` in front.
         */
        @Override
        public void serialize(Integer boxOffice, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (boxOffice == -1) {
                gen.writeString("N/A");
                return;
            }
            NumberFormat usdFormatter = NumberFormat.getNumberInstance(Locale.US);
            String boxOfficeStr = usdFormatter.format(boxOffice);
            gen.writeString("$" + boxOfficeStr);
        }
    }
}
