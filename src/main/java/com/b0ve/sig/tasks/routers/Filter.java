package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.condiciones.Checkeable;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Applies a Checkeable condition to messages.
 * @author borja
 */
public class Filter extends FilterTemplate {

    private final Checkeable condicion;

    public Filter(Checkeable condicion) {
        super();
        this.condicion = condicion;
    }

    @Override
    protected boolean check(Message mensaje) throws SIGException {
        return condicion.checkCondition(mensaje);
    }

}
