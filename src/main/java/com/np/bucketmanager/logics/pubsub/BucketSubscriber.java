package com.np.bucketmanager.logics.pubsub;

import com.np.bucketmanager.models.Marble;

import java.util.concurrent.Flow;

public class BucketSubscriber implements Flow.Subscriber<Marble> {
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Marble item) {
        // Add the item to bucket
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {

    }
}
