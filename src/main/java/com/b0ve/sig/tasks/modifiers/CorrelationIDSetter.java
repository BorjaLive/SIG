package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import java.util.UUID;

/**
 * Sets auto incremental values as correlation ID
 *
 * @author borja
 */
public class CorrelationIDSetter extends CorrelationIDSetterTemplate {

    @Override
    protected void giveCorrelationID(Message m) {
        m.setCorrelationID(UUID.randomUUID());
    }

}
