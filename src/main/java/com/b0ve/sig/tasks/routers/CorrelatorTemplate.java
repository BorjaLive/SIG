package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Sincronizes messages.
 *
 * @author borja
 */
public abstract class CorrelatorTemplate extends Task {

    public CorrelatorTemplate() {
        super(0, 0);
    }

    @Override
    public final void process() throws SIGException {
        try {
            //Bloquear las nuevas entradas
            lockPushes();
            try {
                //Buscar en todos los buffers de entrada y organizar por el campo de correlacion
                Map<Object, List<Message>> relations = new HashMap<>();
                for (Iterator<Buffer> iterator = inputs(); iterator.hasNext();) {
                    Buffer input = iterator.next();
                    for (Iterator<Message> iterator1 = input.getIterator(); iterator1.hasNext();) {
                        Message message = iterator1.next();
                        Object correlation = extractCorrelation(message);
                        List<Message> list = relations.get(correlation);
                        if(list == null){
                            list = new ArrayList<>();
                            relations.put(correlation, list);
                        }
                        list.add(message);
                    }
                }
                //Los grupos que esten completos se borran de los buffers de entrada y se envian por los de salida
                for (Map.Entry<Object, List<Message>> relation : relations.entrySet()) {
                    List<Message> list = relation.getValue();
                    if(list.size() == nInputs()){
                        for (ListIterator<Message> iterator = list.listIterator(); iterator.hasNext();) {
                            int i = iterator.nextIndex();
                            Message message = iterator.next();
                            input(i).deleteMessage(message);
                            output(i).push(message);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                //Desbloquear las nuevas entradas
                unlockPushes();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    protected Object extractCorrelation(Message message) throws SIGException {
        return message.getCorrelationID();
    }

    @Override
    public void validate() throws SIGException {
        super.validate();
        if (nInputs() != nOutputs()) {
            throw new SIGException("Configuration exception. Correlator requires the same number of inputs and outputs", null, null);
        }
    }

}
