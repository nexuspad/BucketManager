package com.np.bucketmanager.blockstore;

import java.util.Set;
import java.util.stream.Stream;

public abstract class BlockStore<O, I> {
    public abstract void destroy();
    public abstract void close();

    public abstract void write(StoreObj obj);
    public abstract Stream<StoreObj> retrieve(Set<I> indexes);
}
