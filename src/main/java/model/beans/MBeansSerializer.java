package model.beans;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MBeansSerializer {

    public static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate = dateFormat.format(date);
            gen.writeString(formattedDate);
        }
    }
    public static class RuntimeSerializer extends JsonSerializer<Integer> {
        @Override
        public void serialize(Integer runtime, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(runtime + " min");
        }
    }

    public static class GenreSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> genre, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", genre));
        }
    }

    public static class DirectorSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> director, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", director));
        }
    }

    public static class WriterSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> writer, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", writer));
        }
    }

    public static class ActorSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> actor, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", actor));
        }
    }

    public static class LanguageSerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> language, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", language));
        }
    }

    public static class CountrySerializer extends JsonSerializer<List<String>> {
        @Override
        public void serialize(List<String> country, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.join(", ", country));
        }
    }
}
