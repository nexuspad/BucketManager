package com.np.bucketmanager.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvReader<T> {
    Logger logger = LoggerFactory.getLogger(CsvReader.class);

    private Stream<T> dataStream;

    public CsvReader(String filePath, Function<String, T> mapper) {
        try {
            Path path = Paths.get(filePath);

            Stream<String> lines = Files.lines(path);
            dataStream = lines.map(mapper);

        } catch (InvalidPathException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<T> getDataStream() {
        return dataStream;
    }
}
