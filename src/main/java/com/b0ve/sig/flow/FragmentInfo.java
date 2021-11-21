package com.b0ve.sig.flow;

import java.util.UUID;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class FragmentInfo {

    private final UUID fragmentID;
    private final int fragmentSize;
    private final Document originalDoc;
    private final Node parentNode;

    public FragmentInfo(UUID fragmentID, int fragmentSize, Document originalDoc, Node parentNode) {
        this.fragmentID = fragmentID;
        this.fragmentSize = fragmentSize;
        this.originalDoc = originalDoc;
        this.parentNode = parentNode;
    }
    
    public FragmentInfo(UUID fragmentID, int fragmentSize) {
        this(fragmentID, fragmentSize, null, null);
    }

    public UUID getFragmentID() {
        return fragmentID;
    }

    public int getFragmentSize() {
        return fragmentSize;
    }

    public Document getOriginalDoc() {
        return originalDoc;
    }

    public Node getParentNode() {
        return parentNode;
    }

    @Override
    public String toString() {
        return "FragmentInfo{" + "ID=" + fragmentID + ", Size=" + fragmentSize + '}';
    }

}
