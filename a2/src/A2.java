import java.util.Arrays;

/* NetIds: mia27, mjs698. Time spent: hh hours, mm minutes. */

/** A collection of static String utility functions.
 * All methods assume that String parameters are non-null.
 *
 * If any method is called with arguments that do not satisfy the Preconditions,
 * the behavior is undefined (it can do anything). You
 * do not have to use assert statements to test preconditions.
 * We will not test with testcases that do not satisfy Preconditions. */
public class A2 {
    /* Wherever possible, prefer library functions to writing your own loops.
     *
     * The more complicated your loops become, the more important it is to
     * explain the logic in comments.
     * 
     * See the JavaHyperText entries for if-statement, while-loop, and for-loop.
     * Use of the break-statement and continue-statement is discouraged but not
     * forbidden. They make loops and programs harder to understand. Usually, 
     * they can be eliminated by restructuring/reorganizing code */

    /**Return either s1 + s2 or s1 - s2, depending on b. If b is true, return
     * the sum, otherwise return the difference. */
    public static int sumDif(boolean b, int s1, int s2) {
        // This method is already implemented; it is here to give you something
        // to test, and to show you different ways of writing return statements.
        if (b) {
            int s;
            s = s1 + s2;
            return s;

            /* equivalently:
             * int s = s1 + s2;
             * return s;
             *
             * or simply: return s1 + s2;
             * */
        }

        // b is false;
        return s1 - s2;
    }

    /** Return true iff (i.e. if and only if) s has an even number of characters
     *  and the two middle characters (if they exist) are the same.
     * Examples:
     * For s = "" return true
     * For s = "bb" return true
     * For s = "bx" return false
     * For s = "124456" return true
     * For s = "123456" return false
     * For s = "111" return false */
    public static boolean isMidSame(String s) {
        // TODO 1. There is no need for a loop. Do not use a loop.
        // This can be done cleanly in three statements.
        // Hint: Follow these Principles:
        // Principle: Avoid unnecessary case analysis
        // Principle: Avoid the same expression in several places.
        // Principle: Keep the structure of the method as simple as possible.
    	int len = s.length();
    	if(len % 2 == 0 && s.charAt((len/2) -1) == s.charAt(len/2)){
    		return true;
    	}
        return false;
    }

    /** Return s but, for each character that is a lower-case letter in a..z,
     * insert the corresponding upper-case letter after it.
     * Examples: for s = "", return ""
     * For s = "b", return "bB"
     * For s = "B", return "B"
     * For s = "$", return "$"
     * For s = "1aab", return "1aAaAbB" 
     * For s = "1a$Bb", return "1aA$BbB" */
    public static String dupSome(String s) {
        // TODO 2.
    	String upper = "";
    	for(int i = 0; i < s.length(); i++){
    		upper += s.substring(i, i+1);
    		if(Character.isLowerCase(s.charAt(i))){
    			upper += Character.toString(s.charAt(i)).toUpperCase();
    		}
    	}
        return upper;
    }

    /** Return s but with each occurrence of a letter in 'b'..'z' replaced by the
     * the previous letter and 'a' replaced by 'z'
     *
     * Examples: prevChar("") = ""
     * prevChar("bcda") = "abcz"
     * prevChar("1a$b") = "1z$a"
     * prevChar("AB") = "AB"
     * prevChar("love") = "knud"
     * prevChar("mpwf") = "love"   */
    public static String prevLetters(String s) {
        // TODO 3. Hint: Follow these Principles:
        // Principle: Avoid unnecessary case analysis like the plague.
        // Principle: Avoid the use of int constants for characters.
        // Principle: Use short names where long mnemonic names are unnecessary.
    	String prev = "";
    	for(int i = 0; i < s.length(); i++){
    		if(s.charAt(i) == 'a'){
    			prev += "z";
    		}
    		else if(Character.isLowerCase(s.charAt(i))){
    			prev += Character.toString((char)(s.charAt(i) - 1));
    		}
    		else{
    			prev += s.substring(i, i+1);
    		}
    	}
        return prev;
    }

    /** Precondition: s and s1 are not null.
     * Return true iff s contains at least two occurrences of s1.
     *  Examples: For s = "" and s1 = "", return false
     *            For s = "a" and s1 = "", return true. This is weird. Bhe
     *               the empty string occurs before and after each character!
     *            For s = "abc" and s1 = "", return true
     *            For s = "abbb" and s2 = "c", return false.
     *            For s = "abbb" and s2 = "ab", return false.
     *            For s = "abbbabc" and s2 = "ab", return true. 
     */
    public static boolean atLeastTwo(String s, String s1) {
        // TODO 4 Do not use a loop or recursion. Instead, look through the
        // methods of class String and see how you can tell that the first
        // and last occurrences of s1 in s are different. Be sure you handle
        // correctly the case that s1 does not occur in s.
        // Hint: Follow this Principle:
        // Principle: Be aware of efficiency considerations.
        //  Note that a call like s.indexOf(s1) may take time proportional to the
        // length of string s.  If s contains 1,000 characters and s1 contains 5, then
        // about 9996 tests may have to be made in the worst case. So you don't want
        // to have the same method call executed several times. Even the call of
        // contains in the code below is wasteful.
        //         
        //      if (s.contains(s1)) {
        //         int k= s.indexOf(s1);
        //                ...
        //      }
    	if(s.indexOf(s1) != s.lastIndexOf(s1)){
    		return true;
    	}
        return false;
    }

    /** Return true iff s and u are anagrams of each other. An anagram of
     * a string is another string that has the same characters, but possibly
     * in a different order. Note that 'a' and 'A' are considered different
     * characters and that the space is a character.
     *
     * Examples: For s = "noon", u = "noon", return true.
     *           For s = "mary", u = "army", return true.
     *           For s = "tom marvolo riddle", u = "i am lordvoldemort", return true.
     *           For s = "tommarvoloriddle", u = "i am lordvoldemort", return false.
     *           For s = "hello", u = "world", return false.  */
    public static boolean areAnagrams(String s, String u) {
        // TODO 5
        /* Do not use a loop or recursion! This is tricky but can be
         * done in a few lines. Hint: how can a sequence of characters be
         * uniquely ordered? You might need to first convert the string to an
         * array of characters and then use a function in class Arrays
         * (http://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html). */
    	char[] charS = s.toCharArray();
    	char[] charU = u.toCharArray();
    	Arrays.sort(charS);
    	Arrays.sort(charU);
    	return Arrays.equals(charS, charU);
    }

    /** Return true iff s is x + x + ... + x  where x = s[0..n-1].
     * That is, s consists of its initial string of n characters
     * catenated together a number of times.
     * Precondition: n > 0
     * Examples:
     * isX("x", 1) is true
     * isX("x", 2) is false
     * isX("bbbbbb", 1") is true        isX("bbbbbb", 2") is true
     * isX("bbbbbb", 3") is true        isX("bbbbbb", 4") is false
     * isX("xyzxyz", 1") is false       isX("xyxyxyxy", 2") is true
     * isX(s, s.length()) is true if s.length() > 0. */
    public static boolean isX(String s, int n) {
        // TODO 6. Hint: Follow this Principle:
        //  Make the structure of a loop reflect the structure of the data it processes.
        if(s.length() % n != 0)
        	return false;
    	for(int i = n; i <= s.length() - n; i += n) {
        	if(!s.substring(0,n).equals(s.substring(i, i + n)));
        		return false;
        }
        return true;
    }

    /** Return the shortest substring x of s such that s = x + x + ⋯ + x.
     * Examples:
     * For s = "" return ""
     * For s = "xxxxxxxxx" return "x"
     * For s = "xyxyxyxy" return "xy"
     * For s = "hellohellohello" return "hello"
     * For s = "hellohelloworld" return "hellohelloworld"
     * For s = "hellohel" return "hellohel" */
    public static String shorten(String s) {
        // TODO 7.
        // To implement this one, start testing for the shortest
        // substring to have length 1, then 2, then 3, and stop when
        // it meets the criterium. And, to make each of those tests,
        // use the previous method isX.
        // Note that isX(s, s.length())  is always true (s != "").

        // invariant: s is not x + x + ... + x for
        // any prefix x of s of length < h.
        for(int i = s.length(); i > 0; i--){
        	
        }
        return "";
    }
}
