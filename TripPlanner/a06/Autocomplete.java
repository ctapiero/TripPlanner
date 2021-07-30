
package a06;
import java.util.Arrays;
import edu.princeton.cs.algs4.Quick3way;

/**
 * 
 * Implements an immutable data type that provides autocomplete functionality
 * for a given set of string and weights, using Term and BinarySearchDeluxe.
 * 
 * @author Cristian Tapiero + kevin PIneda + Tommy Xa
 *
 */
public class Autocomplete {
	private Term[] terms;

	/**
	 * Initializes the data structure from the given array of terms.
	 * 
	 * @param terms - array of terms to initialize with.
	 */
	public Autocomplete(Term[] terms) {
		if (terms == null)
			throw new NullPointerException("Terms can't be null");
		this.terms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++)
			this.terms[i] = terms[i];
		Quick3way.sort(this.terms);
	}

	/**
	 * 
	 * Returns all terms that start with the given prefix, in descending order of
	 * weight.
	 * 
	 * @param prefix the prefix you wish to search for.
	 * @return an array containing all terms that match the given prefix.
	 */
	public Term[] allMatches(String prefix) {
		if (prefix == null)
			throw new NullPointerException("Prefix can not be null");

		int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix, ""),
				Term.byPrefixOrder(prefix.length()));
		if (firstIndex == -1)
			return new Term[0];
		int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix, ""),
				Term.byPrefixOrder(prefix.length()));
		Term[] matchTerms = new Term[1 + lastIndex - firstIndex];

		for (int i = 0; i < matchTerms.length; i++)
			matchTerms[i] = terms[firstIndex++];

		Arrays.sort(matchTerms, Term.byPrefixOrder(1));

		return matchTerms;
	}

	/**
	 * Returns the number of terms that start with the given prefix.
	 * 
	 * @param prefix the prefix you wish to search for
	 * @return number of terms that start with the given prefix
	 */
	public int numberOfMatches(String prefix) {
		if (prefix == null)
			throw new NullPointerException("Prefix can not be null");
		Term[] matches = allMatches(prefix);
		return matches.length;
	}
}
