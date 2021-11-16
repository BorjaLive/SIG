package com.b0ve.sig.flow;

public class FragmentInfo {

    private final long fragmentID;
    private final int fragmentSize;

    private static long contador = 0;

    public FragmentInfo(long fragmentID, int fragmentSize) {
        this.fragmentID = fragmentID;
        this.fragmentSize = fragmentSize;
    }

    public long getFragmentID() {
        return fragmentID;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    @Override
    public String toString() {
        return "FragmentInfo{" + "ID=" + fragmentID + ", Size=" + fragmentSize + '}';
    }

    public static long uniqueID() {
        return contador++;
    }

}
