package name.heavycarbon.utils;

public class MakeNullStringManip {

    /**
     * x is null            -> return null
     * x is whitespace only -> return null
     * otherwise, return x, possibly trimmed
     */
    
    public static String makeNullIfEmpty(String x, boolean trimNonEmptyString) {
        if (x == null) {
            return null;
        } else {
            String tx = x.trim();
            if (tx.isEmpty()) {
                return null;
            } else {
                if (trimNonEmptyString) {
                    return tx;
                } else {
                    return x;
                }
            }
        }
    }

    /**
     * x is null            -> return null
     * x is whitespace only -> return null
     * otherwise, return x unmodified (no trimming is done) 
     */
    
    public static String makeNullIfEmpty(String x) {
        return makeNullIfEmpty(x,false); 
    }
    
    /**
     * x is null            -> return null
     * x is exactly ''      -> return null
     * otherwise, return x, keeping any whitespace
     */
    
    public static String makeNullIfExactlyEmpty(String x) {
        if (x == null || x.isEmpty()) {
            return null;
        } else {
            return x;
        }
    }
    
}
