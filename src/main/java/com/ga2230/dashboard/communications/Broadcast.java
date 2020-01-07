package com.ga2230.dashboard.communications;

import java.util.ArrayList;

public class Broadcast<T> {
    private ArrayList<T> history = new ArrayList<>();
    private ArrayList<Listener<T>> listeners = new ArrayList<>();

    public void send(T thing) {
        history.add(thing);
        for (Listener<T> listener : listeners)
            if (listener != null)
                listener.update(thing);
    }

    public void listen(Listener<T> listener) {
        listeners.add(listener);
    }

    public interface Listener<T> {
        void update(T thing);
    }
}
