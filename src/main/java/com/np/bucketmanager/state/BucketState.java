package com.np.bucketmanager.state;

import com.np.bucketmanager.models.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BucketState implements TimedCache<Bucket> {
    Logger logger = LoggerFactory.getLogger(BucketState.class);

    @Override
    @TimedCacheCheck
    public void add(Bucket bucket) {
        logger.info("Add new element to the bucket cache.");
    }

    @Override
    public void purgeExpired() {
        logger.info("Purge the old cache object.");
    }
}
