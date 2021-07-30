
package a06;

import java.util.Comparator;

/**
 * 
 * Creates an immutable data type Term that represents an autocomplete term: a
 * string query and an associated real-valued weight. You must implement the
 * following API, which supports comparing terms by three different orders:
 * lexicographic order by query string (the natural order); in descending order
 * by weight (an alternate order); lexicographic order by query string but using
 * only the first r characters (a family of alternate orderings).
 * 
 * @author Cristian Tapiero + Kevin Pineda + Tommy Xa
 *
 */
public class Term implements Comparable<Term> {
	private final String query;
	private final String weight;

	/**
	 * Initializes a term with the given query and weight.
	 * 
	 * @param query
	 * @param weight
	 */
	public Term(String query, String weight) {
		if (query == null)
			throw new NullPointerException();
		if (weight == null)
			throw new IllegalArgumentException();
		this.query = query;
		this.weight = weight;
	}


	/**
	 * Compares the terms in lexicographic order but using only the first r
	 * characters of each query.
	 * 
	 * @param r
	 * @return prefixOrder
	 */
	public static Comparator<Term> byPrefixOrder(int r) {
		if (r < 0)
			throw new IllegalArgumentException();
		Comparator<Term> prefixOrder = new Comparator<Term>() {
			@Override
			public int compare(Term o1, Term o2) {
				if (r > o1.query.length() || r > o2.query.length()) {
					return o1.query.compareTo(o2.query);
				}
				return o1.query.substring(0, r).compareTo(o2.query.substring(0, r));
			}
		};
		return prefixOrder;
	}

	/**
	 * Compares the terms in lexicographic order by query.
	 * 
	 * @param that
	 * @return 1 if this.query is larger than that query; 0 if this.query is equal
	 *         to that query; -1 if this.query is smaller than that query.
	 */
	public int compareTo(Term that) {
		return this.query.compareTo(that.query);
	}

	/**
	 * Returns a String representation of the term. In the following format: weight,
	 * followed by a tab, followed by the query.
	 * 
	 */
	public String toString() {
		return weight + "\t" + query;
	}
}
