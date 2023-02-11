package com.quoteall.ip.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Signal {

    private List<Runnable> funcList = new ArrayList<>();
    private boolean isEmitting = false;

    public void emit() {
        isEmitting = true;
        try {
            funcList.forEach(Runnable::run);
        }
        finally {
            isEmitting = false;
        }
    }

    //NOTE the func should not capture owner
    public <T> void connectWithWeakRef(T owner, Consumer<T> func) {
        //NOTE using weak hash map was a mistake
        //https://stackoverflow.com/questions/8051912/will-a-weakhashmaps-entry-be-collected-if-the-value-contains-the-only-strong-re

        WeakReference<T> weakRef = new WeakReference<>(owner);
        Helper.SimpleBox<Runnable> boxOfRunnable = new Helper.SimpleBox<>(null);
        boxOfRunnable.obj = () -> {
            T currentTarget = weakRef.get();
            if (currentTarget != null) {
                func.accept(currentTarget);
            }
            else {
                this.disconnect(boxOfRunnable.obj);
            }
        };

        this.connect(boxOfRunnable.obj);
    }

    public void connect(Runnable func) {
        this.copyDataWhenEmitting();
        funcList.add(func);
    }

    public void disconnect(Runnable func) {
        this.copyDataWhenEmitting();
        boolean removed = funcList.remove(func);
        assert removed;
    }

    private void copyDataWhenEmitting() {
        if (this.isEmitting) {
            this.funcList = new ArrayList<>(funcList);
        }
    }
}
