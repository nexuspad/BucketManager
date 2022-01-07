package com.np.bucketmanager.logics.async;

import com.np.bucketmanager.blockstore.FileStore;
import com.np.bucketmanager.logics.MatchingLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MarbleSorter {
    Logger logger = LoggerFactory.getLogger(MarbleSorter.class);

    @Autowired
    FileStore fileStore;

    @Autowired
    MatchingLogic matchingLogic;

    private AtomicLong counter;

    public void process() {
        List<String> indexes = fileStore.indexes();

        CountDownLatch latch = new CountDownLatch(indexes.size());
        ExecutorService executorServicePool = Executors.newFixedThreadPool(4);

        for (String index : indexes) {
            executorServicePool.submit(() -> {
                processIndex(index);
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Countdown latch is interrupted");
        }
    }

    public void processIndex(String index) {
        fileStore.retrieve(Collections.singleton(index)).forEach(storeObj -> {
            matchingLogic.findMatch(storeObj.getMarble());
            counter.incrementAndGet();
        });
    }
}
