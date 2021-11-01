package com.np.bucketmanager.logics;

import com.np.bucketmanager.logics.splitting.ExecutorCompletionServiceScatterGatherer;
import com.np.bucketmanager.logics.splitting.MarbleGatherer;
import com.np.bucketmanager.logics.splitting.MarbleScatter;
import com.np.bucketmanager.logics.splitting.ScatterGatherer;

public class MarbleSortingProcess {
    public void finaMatches() {
        ScatterGatherer sg = new ExecutorCompletionServiceScatterGatherer();
        sg.process(new MarbleScatter(), new MarbleGatherer());
    }
}
