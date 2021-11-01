package com.np.bucketmanager;

import com.np.bucketmanager.state.BucketState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BucketManagerApplication implements CommandLineRunner {

	@Autowired
	BucketState bucketState;

	public static void main(String[] args) {
		SpringApplication.run(BucketManagerApplication.class, args);
	}

	public void run(String...args) {
		bucketState.add(null);
	}
}
