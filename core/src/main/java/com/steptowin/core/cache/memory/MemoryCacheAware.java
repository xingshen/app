package com.steptowin.core.cache.memory;

import java.util.Collection;

public  interface MemoryCacheAware<K, V> {
	 boolean put(K paramK, V paramV);

	 V get(K paramK);

	 V remove(K paramK);

	 Collection<K> keys();

	 void clear();
}
