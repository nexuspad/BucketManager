package com.np.bucketmanager.logics.splitting;

import com.np.bucketmanager.models.Marble;

import java.util.concurrent.Callable;

public interface ScatterGatherer {
    Marble process(Scatter scatter, Gatherer gatherer);

    interface Scatter {
        boolean hasNext();

        Callable<Marble> next();
    }

    interface Gatherer {
    }
}
