package fileSystem;

import java.util.HashMap;

public class CachingSystem {

	private HashMap<String, Long> cache = new HashMap<String, Long>();
	private final long CACHE_DOES_NOT_CONTAIN = -1;

	public void invalidateCache() {
		cache.clear();
	}

	public void addToCache(String fileName, long blockNumber) {
		cache.put(fileName, blockNumber);
	}

	public boolean hasFile(String fileName) {
		return cache.containsKey(fileName);
	}

	public long getBlockNumber(String fileName) {
		if (hasFile(fileName))
			return cache.get(fileName);
		else
			return CACHE_DOES_NOT_CONTAIN;
	}
}
