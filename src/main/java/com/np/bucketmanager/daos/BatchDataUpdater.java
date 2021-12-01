package com.np.bucketmanager.daos;

import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BatchDataUpdater<T> {
    private TransactionTemplate transactionTemplate;
    private ExecutorService executorService;

    private int commitSize;
    private int batchSize;

    private List<T> dataRecords;
    private int updated;

    private boolean async;
    private ReentrantLock lock;

    public BatchDataUpdater(TransactionTemplate transactionTemplate, int commitSize, int batchSize, boolean async) {
        this.transactionTemplate = transactionTemplate;
        this.commitSize = commitSize;
        this.batchSize = batchSize;
        this.async = async;
    }

    public abstract void doUpdate();

    public void add(T data) {
        if (async) {
            
        }
    }

    private void batchUpdate(List<T> dataRecords) {

    }
}
