package com.np.bucketmanager.logics.pubsub;

import com.np.bucketmanager.models.Marble;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;

public class MarbleSorter {
    private static final int MAX_BUFFER_CAPACITY = 1000;

    public Map<Marble, Integer> marbleBucketMapping() {
        return null;
    }

    public void sort() {
        Map<Marble, Integer> marbleBucketMapping = marbleBucketMapping();
        final SubmissionPublisher<Marble> publisher = new SubmissionPublisher<>(ForkJoinPool.commonPool(), MAX_BUFFER_CAPACITY);

        BucketSubscriber bucketSubscriber = new BucketSubscriber();

        publisher.subscribe(bucketSubscriber);

        marbleBucketMapping.forEach((marble, bucket) -> {
            publisher.submit(marble);
        });
    }
}
