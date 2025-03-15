package io;

import utils.Document;

import java.io.RandomAccessFile;

public interface DataIO {
    default String getPath() { return ""; }

    default void createRegistry(Document document) {};
    default Document getRegistry(short id) { return null; }
    default Document getNextRegistry(RandomAccessFile raf) { return null; };
    default Document deleteRegistry(short id) { return null; }
    default void updateRegistry(short id, Document document) {};
}
