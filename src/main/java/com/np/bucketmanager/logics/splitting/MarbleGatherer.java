package com.np.bucketmanager.logics.splitting;

import com.np.bucketmanager.models.Marble;

public class MarbleGatherer implements ScatterGatherer.Gatherer {
    @Override
    public boolean needMore() {
        return false;
    }

    @Override
    public void gatherResult(Marble marble) {

    }
}
