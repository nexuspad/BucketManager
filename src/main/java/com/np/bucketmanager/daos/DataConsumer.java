package com.np.bucketmanager.daos;

@FunctionalInterface
public interface DataConsumer<T> {
    void consume(T t);
}
