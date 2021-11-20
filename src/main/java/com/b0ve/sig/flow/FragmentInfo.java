package com.b0ve.sig.flow;

import java.util.UUID;

public class FragmentInfo {

    private final UUID fragmentID;
    private final int fragmentSize;

    public FragmentInfo(UUID fragmentID, int fragmentSize) {
        this.fragmentID = fragmentID;
        this.fragmentSize = fragmentSize;
    }

    public UUID getFragmentID() {
        return fragmentID;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    @Override
    public String toString() {
        return "FragmentInfo{" + "ID=" + fragmentID + ", Size=" + fragmentSize + '}';
    }

}
