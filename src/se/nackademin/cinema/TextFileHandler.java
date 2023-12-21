package se.nackademin.cinema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class TextFileHandler implements FileHandler {

    @Override
    public List<String> load(Path path) {
        List<String> lines;
        try (var reader = Files.newBufferedReader(path)) {
            lines = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error while loading from the file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return lines;
    }

    @Override
    public void save(Path path, List<String> lines, StandardOpenOption option) {
        save(path, lines, new StandardOpenOption[]{option});
    }

    @Override
    public void save(Path path, List<String> lines, StandardOpenOption... options) {
        try (var writer = Files.newBufferedWriter(path, options)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
