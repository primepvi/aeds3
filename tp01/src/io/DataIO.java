package io;

import utils.Document;

public interface DataIO {
    default void createRegistry(Document document) {};
    default Document getRegistry(short id) { return null; }
    default Document deleteRegistry(short id) { return null; }
}
