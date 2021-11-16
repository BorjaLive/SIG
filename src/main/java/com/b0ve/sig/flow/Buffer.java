package com.b0ve.sig.flow;

import com.b0ve.sig.tasks.Notifiable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public final class Buffer {

    private final Queue<Message> queue;
    private Queue<Message> tempQueue;
    private final Notifiable in, out;
    private final Semaphore s;

    public Buffer(Notifiable in, Notifiable out) {
        this.in = in;
        this.out = out;
        tempQueue = null;
        queue = new LinkedList<>();
        s = new Semaphore(1);
    }

    public synchronized boolean empty() {
        return this.queue.isEmpty();
    }
    
    public synchronized int size(){
        if(tempQueue == null)
            return queue.size();
        else
            return queue.size()+tempQueue.size();
    }

    /**
     * Returns the element that has been waiting the most in the queue
     *
     * @return
     */
    public synchronized Message retrive() {
        return queue.poll();
    }

    /**
     * Inserts a message in the queue.
     *
     * @param mensaje
     */
    public synchronized void push(Message mensaje) {
        if (tempQueue == null) {
            queue.add(mensaje);
            if (out != null) {
                out.signalInput();
            }
        } else {
            tempQueue.add(mensaje);
        }
    }

    /**
     * Returns an Iterator to walk over all the messages in the queue
     *
     * @return
     */
    public synchronized Iterator<Message> getIterator() {
        return queue.iterator();
    }

    /**
     * Deletes a message from the queue
     *
     * @param mensaje
     */
    public synchronized void deleteMessage(Message mensaje) {
        queue.remove(mensaje);
    }

    /**
     * Blocks new messages from entering in the queue.They are stored in a
     * temporary space for later. Use before iterating over the queue. This
     * methods assures exclusive access to the queue.
     *
     * @throws java.lang.InterruptedException
     */
    public synchronized void lockPushes() throws InterruptedException {
        s.acquire();
        if (tempQueue == null) {
            tempQueue = new LinkedList<>();
        }
    }

    /**
     * Unlocks the queue, new messages can be added. Messages in the temporary
     * space are added in. Use after iterating over the queue
     */
    public synchronized void unlockPushes() {
        if (tempQueue != null) {
            boolean hayCambios = !tempQueue.isEmpty();
            while (!tempQueue.isEmpty()) {
                queue.add(tempQueue.poll());
            }
            tempQueue = null;
            if (hayCambios && out != null) {
                out.signalInput();
            }
            s.release();
        }
    }

    public Notifiable getIn() {
        return in;
    }

    public Notifiable getOut() {
        return out;
    }

    @Override
    public String toString() {
        return "Buffer{" + "mensajes=" + queue + ", reserva=" + tempQueue + '}';
    }
}
