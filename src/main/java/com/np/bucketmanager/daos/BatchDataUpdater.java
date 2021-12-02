package com.np.bucketmanager.daos;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BatchDataUpdater<T> {
    private final TransactionTemplate transactionTemplate;

    private final int bufferSize;
    private final int batchCommitSize;

    private List<T> dataRecords;
    private AtomicInteger updated;

    private final boolean async;
    private ReentrantLock lock;
    private ExecutorService executorService;

    public BatchDataUpdater(TransactionTemplate transactionTemplate, int bufferSize, int batchCommitSize, boolean async) {
        this.transactionTemplate = transactionTemplate;
        this.bufferSize = bufferSize;
        this.batchCommitSize = batchCommitSize;
        this.async = async;

        if (this.async) {
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(3);
            executorService = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS, queue);
        }
    }

    public abstract void doUpdate(List<T> updates);

    public void add(T data) {
        if (async) {
            try {
                lock.lock();
                dataRecords.add(data);
                if (dataRecords.size() >= bufferSize) {
                    batchUpdate(dataRecords);
                    executorService.submit(() -> {
                        batchUpdate(dataRecords);
                    });
                    dataRecords = new ArrayList<>(this.bufferSize);
                }
            } finally {
                lock.unlock();
            }
        } else {
            dataRecords.add(data);
            if (dataRecords.size() >= bufferSize) {
                batchUpdate(dataRecords);
                dataRecords = new ArrayList<>(this.bufferSize);
            }
        }
    }

    private void batchUpdate(List<T> dataRecords) {
        long start = System.currentTimeMillis();
        try {
            if (transactionTemplate != null) {
                transactionTemplate.execute((transactionStatus) -> {
                    for (int i = 0; i < dataRecords.size(); i += batchCommitSize) {
                        doUpdate(dataRecords.subList(i, Math.min(i + batchCommitSize, dataRecords.size())));
                    }
                    return true;
                });
            } else {
                for (int i = 0; i < dataRecords.size(); i += batchCommitSize) {
                    doUpdate(dataRecords.subList(i, Math.min(i + batchCommitSize, dataRecords.size())));
                }
            }
        } catch (Exception e) {

        } finally {
            updated.addAndGet(dataRecords.size());
            long timeSpent = System.currentTimeMillis() - start;
        }
    }
}
