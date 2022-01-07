package com.np.bucketmanager;

import com.np.bucketmanager.daos.SqlQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResourceLoadingTest {
    @Test
    public void findFiles() {
        SqlQueryFactory.getQuery();
    }
}
