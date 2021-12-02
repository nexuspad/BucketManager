package com.np.bucketmanager.daos;

@FunctionalInterface
public interface DataProducer<T> {
    void produce(DataConsumer<T> dataConsumer);
}
