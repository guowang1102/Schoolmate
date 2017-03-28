package com.weiguowang.schoolmate.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 软引用的集合
 * 
 * @author l
 * 
 * @param <K>
 * @param <V>
 */
public class SoftMap<K, V> extends HashMap<K, V> {
	// 核心：将添加到集合中的对象级别降低
	// 如V的级别降低到软引用

	private HashMap<K, SoftValue<K, V>> temp;// V：袋子
	private ReferenceQueue<V> queue;// GC记录回收掉信息的账本，V：装某个手机的袋子。该队里中装的都是“袋子”的引用

	public SoftMap() {
		// 软引用的对象操作
		// Object o=new Object();// o占用内存较多
		// SoftReference sr=new SoftReference(o);

		// ①将占用较多的对象变成软引用对象放到map中
		// ②如果占用较多的对象被GC回收，处理掉空袋子

		temp = new HashMap<K, SoftValue<K, V>>();
		queue = new ReferenceQueue<V>();
	}

	@Override
	public V put(K key, V value) {
		SoftValue<K, V> sr = new SoftValue<K, V>(key, value);
		temp.put(key, sr);
		// return super.put(key, value);
		return null;
	}

	@Override
	public V get(Object key) {
		clearSoftReference();
		SoftValue<K, V> sr = temp.get(key);// 袋子

		// 垃圾回收器清除，则此方法将返回 null
		return sr.get();

		// return super.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		clearSoftReference();
		// 什么才叫真正的含有？
		// 含有袋子不能叫真正的含有，必须从袋子中拿出手机

		SoftValue<K, V> sr = temp.get(key);

		// if(sr.get()!=null) { return true; }else{ return false; }

		if (sr != null)
			return sr.get() != null;
		return false;
		// return temp.containsKey(key);可以使用简化

		// temp.containsKey(key);//是否有袋子
		// return super.containsKey(key);
	}

	/**
	 * 当GC回收掉占用内存较多的对象后，清理空袋子
	 */
	private void clearSoftReference() {
		// 方式一： 轮训temp：检查是否可以获取到手机，如果没有手机，删除
		// 更多的情况下，应用在内存充足的情况下运行，GC没有回收，该轮训=无用功

		// 方式二：谁对空袋子最了解，GC回收，让GC在回收对象的时候，记账（到底偷了那个手机）
		// 翻账本（集合）ReferenceQueue<? super T> q
		// 在创建袋子的时候通过构造方法制定账本

		// 如果存在"一个"立即可用的对象，则从该队列中移除此对象并"返回"。否则此方法立即返回 null。
		SoftValue<K, V> sr = (SoftValue<K, V>) queue.poll();

		while (sr != null) {
			// 在temp中删除该空袋子
			temp.remove(sr.key);// sr.key
			// 还有空袋子
			sr = (SoftValue<K, V>) queue.poll();
		}

	}

	/**
	 * 加强系统提供的袋子
	 * 
	 * @author l
	 * 
	 */
	private class SoftValue<K, V> extends SoftReference<V> {
		private Object key;

		public SoftValue(K key, V value) {
			super(value, (ReferenceQueue<? super V>) queue);
			this.key = key;
		}

	}
}
