package se.nackademin.cinema;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public interface FileHandler {

    List<String> load(Path path);

    void save(Path path, List<String> lines, StandardOpenOption option);

    void save(Path path, List<String> lines, StandardOpenOption... options);
}
