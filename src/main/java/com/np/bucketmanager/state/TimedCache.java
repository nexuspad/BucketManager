package com.np.bucketmanager.state;

public interface TimedCache<T> {
    void add(T cacheObject);
    void purgeExpired();
}
