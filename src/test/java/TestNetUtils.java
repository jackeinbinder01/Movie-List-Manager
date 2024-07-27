import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import group5.model.net.NetUtils;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestNetUtils {

    @Test
    public void TestGetMediaDetails() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream stream1 = NetUtils.getMediaDetails("The Matrix", "1999", "movie");
        InputStream stream2 = NetUtils.getMediaDetails("matrix", "", "series");

        String expected1 = Files.readString(Paths.get("data/test/APITheMatrix.json"));;
        String expected2 = Files.readString(Paths.get("data/test/APImatrix.json"));

        String actual1 = objectMapper.readTree(stream1).toPrettyString();
        String actual2 = objectMapper.readTree(stream2).toPrettyString();

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }
}
