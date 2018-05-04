package cse210;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.Math;

/**
 * This class provides an utility to calculate the similarity between two arrays
 * of strings
 * 
 * @author Dixing Xu
 * @since 0.1.0
 */
public class SimilarityUtil {

    /**
     * Calculates the cosine similarity of two arrays of strings <b>a &middot; b</b>
     * = cos(<i>theta</i>) = (A * B) / (|A| * |B|)
     * 
     * @param strA: an array of strings
     * @param strB: an array of strings
     * @return double: the numeric float of similarity
     */
    public static double cosineSimilarity(String[] strA, String[] strB) {
        Map<String, Integer> strAWordCountMap = new HashMap<String, Integer>();
        Map<String, Integer> strBWordCountMap = new HashMap<String, Integer>();
        Set<String> uniqueSet = new HashSet<String>();
        Integer temp = null;
        for (String strAWord : strA) {
            temp = strAWordCountMap.get(strAWord);
            if (temp == null) {
                strAWordCountMap.put(strAWord, 1);
                uniqueSet.add(strAWord);
            } else {
                strAWordCountMap.put(strAWord, temp + 1);
            }
        }
        for (String strBWord : strB) {
            temp = strBWordCountMap.get(strBWord);
            if (temp == null) {
                strBWordCountMap.put(strBWord, 1);
                uniqueSet.add(strBWord);
            } else {
                strBWordCountMap.put(strBWord, temp + 1);
            }
        }
        int[] strAVector = new int[uniqueSet.size()];
        int[] strBVector = new int[uniqueSet.size()];
        int index = 0;
        Integer tempCount = 0;
        for (String uniqueWord : uniqueSet) {
            tempCount = strAWordCountMap.get(uniqueWord);
            strAVector[index] = tempCount == null ? 0 : tempCount;
            tempCount = strBWordCountMap.get(uniqueWord);
            strBVector[index] = tempCount == null ? 0 : tempCount;
            index++;
        }
        return cosineIntSimilarity(strAVector, strBVector);
    }

    /**
     * The resulting similarity ranges from −1 meaning exactly opposite, to 1
     * meaning exactly the same, with 0 usually indicating independence, and
     * in-between values indicating intermediate similarity or dissimilarity.
     * 
     * For text matching, the attribute vectors A and B are usually the term
     * frequency vectors of the documents. The cosine similarity can be seen as a
     * method of normalizing document length during comparison.
     * 
     * In the case of information retrieval, the cosine similarity of two documents
     * will range from 0 to 1, since the term frequencies (tf-idf weights) cannot be
     * negative. The angle between two term frequency vectors cannot be greater than
     * 90°.
     * 
     * @param AVector: the first array of integers mapped from strings
     * @param BVector: the second array fo integers mapped from strings
     * @return the numeric similarity between two array of integers
     */
    private static double cosineIntSimilarity(int[] AVector, int[] BVector) {
        if (AVector.length != BVector.length)
            return 1;
        double dotProduct = 0;
        double strANorm = 0;
        double strBNorm = 0;
        for (int i = 0; i < AVector.length; i++) {
            dotProduct += AVector[i] * BVector[i];
            strANorm += Math.pow(AVector[i], 2);
            strBNorm += Math.pow(BVector[i], 2);
        }

        double result = dotProduct / (Math.sqrt(strANorm) * Math.sqrt(strBNorm));
        return result;
    }
}