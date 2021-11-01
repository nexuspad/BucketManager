package com.np.bucketmanager;

import com.np.bucketmanager.state.BucketState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheStoreTest {
    @Autowired
    BucketState bucketState;

    @Test
    void testCacheAop() {
        bucketState.add(null);
    }
}
