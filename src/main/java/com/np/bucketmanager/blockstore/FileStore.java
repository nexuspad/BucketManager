package com.np.bucketmanager.blockstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileStore extends BlockStore<StoreObj, String> {
    Logger logger = LoggerFactory.getLogger(FileStore.class);

    private final String DATA_FOLDER = "/Users/renliu/dev/tmp";
    private final int WRITE_BUFFER_SIZE = 100;

    private final Map<String, Lock> blockLocks = new ConcurrentHashMap<>();
    private final Map<String, List<StoreObj>> blockWriteBuffers = new ConcurrentHashMap<>();

    public static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void write(StoreObj obj) {
        String block = obj.block();
        if (!blockWriteBuffers.containsKey(block)) {
            blockWriteBuffers.put(block, new ArrayList<>());
        }

        Lock lock = blockLocks.get(block);
        if (lock == null) {
            lock = new ReentrantLock();
            blockLocks.put(block, lock);
        }

        lock.lock();

        try {
            blockWriteBuffers.get(block).add(obj);
            counter.incrementAndGet();
            if (blockWriteBuffers.get(block).size() > WRITE_BUFFER_SIZE) {
                try {
                    flush(block);
                } catch (Exception e) {
                    logger.info("Buffer flush for block {} was unsuccessful.", block);
                }
            }
        } finally {
            lock.unlock();
        }

    }

    private void flush(String block) {
        logger.info("Flush writing to block {}", block);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(blockFilePath(block), true))) {
            Iterator<StoreObj> iterator = blockWriteBuffers.get(block).iterator();
            while (iterator.hasNext()) {
                try {
                    writer.append(iterator.next().serialize());
                    writer.append(System.lineSeparator());
                    iterator.remove();
                } catch (IOException e) {
                    logger.error("Error writing to file", e);
                }
            }

        } catch (IOException e) {
            logger.error("Error opening file", e);
            throw new RuntimeException();
        }
    }

    public void flushAndFinish() {
        blockWriteBuffers.forEach((block, objects) -> {
            logger.info("Final flush block {} count {}", block, objects.size());
            flush(block);
        });
        blockLocks.clear();
    }

    public List<String> indexes() {
        return null;
    }

    @Override
    public Stream<StoreObj> retrieve(Set<String> indexes) {
        Set<String> blocks = indexes.stream().map(StoreObj::indexToBlock).collect(Collectors.toSet());
        Stream<StoreObj> combinedStream = Stream.empty();
        List<Stream<StoreObj>> streamList = new ArrayList<>();
        blocks.forEach(b -> {
            Path path = Paths.get(blockFilePath(b));
            try {
                streamList.add(Files.lines(path).map(StoreObj::fromString).filter(obj -> indexes.contains(obj.index())));
            } catch (IOException e) {
                logger.error("Error reading block file into stream: {}", path.getFileName());
            }
        });

        for (Stream<StoreObj> s : streamList) {
            combinedStream = Stream.concat(combinedStream, s);
        }

        return combinedStream;
    }

    private String blockFilePath(String block) {
        return DATA_FOLDER + "/" + "B" + block;
    }

    @Override
    public void destroy() {
        logger.info("All files will be removed in {}", DATA_FOLDER);
        File dir = new File(DATA_FOLDER);
        if (dir.exists() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    @Override
    public void close() {
    }
}
