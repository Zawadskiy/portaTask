package fileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileReader {

    public Stream<String> readFile(Path path) {

        Stream<String> lines;

        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lines;
    }
}
