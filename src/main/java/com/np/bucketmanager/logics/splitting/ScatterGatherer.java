package com.np.bucketmanager.logics.splitting;

import com.np.bucketmanager.models.Marble;

import java.util.concurrent.Callable;

public interface ScatterGatherer {
    public Marble process(Scatter scatter, Gatherer gatherer);

    public interface Scatter {
        boolean hasNext();

        Callable<Marble> next();
    }

    public interface Gatherer {
    }
}
