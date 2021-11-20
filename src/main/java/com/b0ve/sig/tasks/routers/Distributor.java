package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.condiciones.Checkeable;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Routes messages based on an array of Checkeable conditions.
 *
 * @author borja
 */
public class Distributor extends DistributorTemplate {

    private final Checkeable[] conditions;

    public Distributor(Checkeable[] conditions) {
        super();
        this.conditions = conditions;
    }

    @Override
    protected int check(Message m) throws SIGException {
        int outPin = -1;
        int i = 0;
        while (outPin == -1 && i < conditions.length) {
            if (conditions[i].checkCondition(m)) {
                outPin = i;
            } else {
                i++;
            }
        }
        if (outPin == -1) {
            outPin = nOutputs() - 1;
        }
        return outPin;
    }

}
