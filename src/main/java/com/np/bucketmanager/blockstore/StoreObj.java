package com.np.bucketmanager.blockstore;

import com.np.bucketmanager.models.Marble;

public class StoreObj implements Storable<Marble, String, String> {
    private final Marble marble;

    public StoreObj(Marble marble) {
        this.marble = marble;
    }

    public static StoreObj fromString(String str) {
        String[] fields = str.split("\\|");
        Marble d = new Marble();
        d.setLoc(fields[0]);
        d.setName(fields[1]);
        d.setZip(fields[2]);
        return new StoreObj(d);
    }

    public static String indexToBlock(String index) {
        return index.substring(0, 2);
    }

    @Override
    public String index() {
        return marble.getZip();
    }

    @Override
    public String block() {
        return indexToBlock(marble.getZip());
    }

    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(marble.getLoc()).append("|").append(marble.getName()).append("|").append(marble.getZip());
        return sb.toString();
    }

    public Marble getMarble() {
        return marble;
    }
}
