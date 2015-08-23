package name.heavycarbon.utils;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 *******************************************************************************
 * An long which can be modified in called methods (thus it implements a 
 * pass-modify-return value)
 * 
 * TODO: Java has AtomicLong; use that!
 * 
 * 2009.06.02 - Added set()
 ******************************************************************************/

@Deprecated
public class MutableLong {

    private long value;

    public MutableLong(long init) {
        value = init;
    }

    public synchronized void inc() {
        inc(1);
    }

    public synchronized void dec() {
        dec(1);
    }

    public synchronized void inc(long x) {
        value += x;
    }

    public synchronized void dec(long x) {
        value -= x;
    }

    public synchronized void set(long x) {
        value = x;
    }

    public synchronized long get() {
        return value;
    }

    @Override
    public synchronized String toString() {
        return Long.toString(value);
    }
}
