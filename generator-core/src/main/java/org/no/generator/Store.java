package org.no.generator;

public interface Store {

    Object[] fetch();

    default int size() {
        return fetch().length;
    }

    default Object data(int index) {
        return fetch()[index];
    }

}
