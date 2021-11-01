package com.np.bucketmanager.state;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ZipProvider {
    List<String> zip5Codes = new ArrayList<>();

    public void init() {
        initFromFile();
    }

    private void initFromFile() {

    }

    public List<String> getZip5Codes() {
        return zip5Codes;
    }
}
