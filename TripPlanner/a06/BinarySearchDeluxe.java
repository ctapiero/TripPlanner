
package a06;

import java.util.Comparator;

/**
 * 
 * Provides two Binary Search options. One for the index of the first key in a[]
 * that equals the search key. One for the index of the last key in a[] that
 * equals the search key.
 * 
 * @author Cristian Tapiero
 *
 */
public class BinarySearchDeluxe {
	/**
	 * Returns the index of the first key in a[] that equals the search key, or -1
	 * if no such key.
	 * 
	 * When binary searching a sorted array that contains more than one key equal to
	 * the search key, the client may want to know the index of either the first or
	 * the last such key.
	 * 
	 * @throws java.lang.NullPointerException if any of its arguments is null.
	 * 
	 * @param <Key>
	 * @param a
	 * @param key
	 * @param comparator
	 * @return mid if key is in the Key[]
	 * @return -1 if key is not in the Key[]
	 */
	public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {

		if (comparator == null || key == null || a == null) {
			throw new java.lang.NullPointerException();
		}
		int lo = 0;
		int hi = a.length - 1;
		if (comparator.compare(a[0], key) == 0)
			return 0;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (comparator.compare(key, a[mid]) < 0)
				hi = mid - 1;
			else if (comparator.compare(key, a[mid]) > 0)
				lo = mid + 1;
			else if (comparator.compare(a[mid - 1], a[mid]) == 0)
				hi = mid - 1;
			else
				return mid;
		}
		return -1;
	}

	/**
	 * Returns the index of the last key in a[] that equals the search key, or -1 if
	 * no such key.
	 * 
	 * @throws a java.lang.NullPointerException if any of its arguments is null.
	 * 
	 * @param <Key>
	 * @param a
	 * @param key
	 * @param comparator
	 * @return mid if key is in the Key[]
	 * @return -1 if key is not in the Key[]
	 */
	public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
		if (comparator == null || key == null || a == null) {
			throw new java.lang.NullPointerException();
		}
		int lo = 0;
		int hi = a.length - 1;
		if (comparator.compare(a[hi], key) == 0)
			return hi;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (comparator.compare(key, a[mid]) < 0)
				hi = mid - 1;
			else if (comparator.compare(key, a[mid]) > 0)
				lo = mid + 1;
			else if (comparator.compare(a[mid + 1], a[mid]) == 0)
				lo = mid + 1;
			else
				return mid;
		}
		return -1;
	}
}
