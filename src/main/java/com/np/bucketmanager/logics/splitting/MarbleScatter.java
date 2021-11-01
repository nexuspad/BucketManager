package com.np.bucketmanager.logics.splitting;

import com.np.bucketmanager.models.Marble;

import java.util.concurrent.Callable;

public class MarbleScatter implements ScatterGatherer.Scatter {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Callable<Marble> next() {
        return null;
    }
}
