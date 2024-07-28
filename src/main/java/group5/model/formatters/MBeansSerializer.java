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

public class MBeansSerializer {

    public static class IntSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ( value == -1 ) {
                gen.writeString("N/A");
            } else {
                gen.writeNumber(value);
            }
        }
    }

    public static class DoubleSerializer extends JsonSerializer<Double> {
        @Override
        public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ( value == -1.0 ) {
                gen.writeString("N/A");
            } else {
                gen.writeNumber(value);
            }
        }
    }

    public static class DateSerializer extends JsonSerializer<LocalDate> {
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

    public static class RuntimeSerializer extends JsonSerializer<Integer> {
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

    public static class StringListSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> genre, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", genre));
        }
    }

    public static class BoxOfficeSerializer extends JsonSerializer<Integer> {
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
