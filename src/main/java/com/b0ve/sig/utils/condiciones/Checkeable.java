package com.b0ve.sig.utils.condiciones;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.XPathEvaluationException;

/**
 * Requirement for a class to be used with Filter and Distributor tasks.
 * @author borja
 */
public interface Checkeable {

    boolean checkCondition(Message mensaje) throws XPathEvaluationException;
}
