package MisbachFirenze;

public class MF {
    /**
     * Multiplies two values by the rules of Misbach-Firenze multiplication
     * @param m1 the first multiplicand
     * @param m2 the second multiplicand
     * @return m1 (:)) m2
     */
    public static String multiply(String m1, String m2) {
        StringBuilder product = new StringBuilder();
        char[] m1Chars = m1.replace(":|","u").toCharArray();
        char[] m2Chars = m2.replace(":|","u").toCharArray();
        int indexM1 = 0, indexM2 = 0;
        boolean continueM1 = indexM1 < m1Chars.length, continueM2 = indexM2 < m2Chars.length;
        while(continueM1 || continueM2) {
            if(continueM1) {
                if(continueM2) {
                } else {}
            } else {}
            indexM1++;
            indexM2++;
            continueM1 = indexM1 < m1Chars.length;
            continueM2 = indexM2 < m2Chars.length;
        }
        return null;
    }
}
