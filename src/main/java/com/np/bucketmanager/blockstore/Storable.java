package com.np.bucketmanager.blockstore;

public interface Storable<O, I, B> {
    I index();
    B block();
    String serialize();
}
