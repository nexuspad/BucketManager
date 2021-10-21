package com.np.bucketmanager;

import com.np.bucketmanager.blockstore.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootTest
class BlockstoreTests {

	@Autowired
	FileStore fileStore;

	@Test
	void retrieve() {
		Set<String> indexes = new HashSet<>();
		indexes.add("17950");
		indexes.add("41495");
		fileStore.retrieve(indexes).forEach(obj -> {
			System.out.println(obj.serialize());
		});
	}

	@Test
	void createFileStore() throws ExecutionException, InterruptedException {
		fileStore.destroy();

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		List<Future<String>> futures = new ArrayList<>();
		for (int i=0; i<3; i++) {
			System.out.println("Start the thread " + i);
			futures.add(executorService.submit(() -> {
				for (int j=0; j<5000; j++) {
					fileStore.write(new StoreObj(random()));
				}
				return "success";
			}));
		}

		for (Future<String> future : futures) {
			System.out.println("Thread result: " + future.get());
		}

		fileStore.flushAndFinish();
		System.out.println(FileStore.counter);
	}

	Duns random() {
		Duns d = new Duns();
		Random rand = new Random();
		d.setLoc(padLeft(String.valueOf(rand.nextInt(1000000000)), 9));
		rand = new Random();
		d.setZip(padLeft(String.valueOf(rand.nextInt(50000)), 5));
		d.setName("test");
		return d;
	}

	String padLeft(String s, int n) {
		return String.format("%" + n + "s", s).replace(' ', '0');
	}
}
