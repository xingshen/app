package com.steptowin.core.cache.memory;

import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 
 * 类描述：使用LRU策略来管理内存<br>
 * 创建日期 2015年8月26日 下午3:26:23 <br>
 * 修改记录： <br>
 * 
 * @author zg <br>
 */
public class LruMemoryCacheString implements MemoryCacheAware<String,String> {
	private final LinkedHashMap<String, String> map;
	private final int maxSize;
	private int size;

	public LruMemoryCacheString(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap(0, 0.75F, true);
	}

	public final String get(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			if (!map.containsKey(key))
				return null;
			return this.map.get(key);
		}
	}

	public final boolean put(String key, String value) {
		if ((key == null) || (value == null)) {
			Log.w("warn", "bitmap存储的key 或者value 为 null");
			// throw new NullPointerException("key == null || value == null");
			return false;
		}

		synchronized (this) {
			this.size += sizeOf(value);
			String previous =  this.map.put(key, value);
			if (previous != null) {
				this.size -= sizeOf(previous);
			}
		}

		trimToSize(this.maxSize);
		return true;
	}

	private void trimToSize(int maxSize) {
		while (true)
			synchronized (this) {
				if ((this.size < 0)
						|| ((this.map.isEmpty()) && (this.size != 0))) {
					throw new IllegalStateException(getClass().getName()
							+ ".sizeOf() is reporting inconsistent results!");
				}

				if ((this.size > maxSize) && (!this.map.isEmpty())) {
					Iterator<Entry<String, String>> iterator = this.map
							.entrySet().iterator();
					Log.i("info", "trimToSize...........");
					if (iterator.hasNext()) {
						Entry toEvict = (Entry) this.map.entrySet()
								.iterator().next();
						if (toEvict == null) {
							break;
						}
						String key = (String) toEvict.getKey();
						String value = remove(key);
						this.size -= sizeOf(value);
					} else {
						break;
					}
				} else {
					break;
				}
			}
	}

	public final String remove(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}

		synchronized (this) {
			String previous =  this.map.remove(key);
			if (previous != null) {
				this.size -= sizeOf(previous);
			}
			return previous;
		}
	}

	public Collection<String> keys() {
		synchronized (this) {
			return new HashSet(this.map.keySet());
		}
	}

	public void clear() {
		trimToSize(-1);
	}

	private int sizeOf(String value) {
		return value.getBytes().length;
	}

	public final synchronized String toString() {
		return String.format("LruCache[maxSize=%d]",
				new Object[] { Integer.valueOf(this.maxSize) });
	}
}
