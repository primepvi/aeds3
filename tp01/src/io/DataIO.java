package io;

import utils.Document;

import java.io.RandomAccessFile;

public interface DataIO {
    String getPath();
    void setPath(String path);

    default Document createRegistry(Document document) { return null; };
    default Document getRegistry(short id) { return null; }
    default Document getNextRegistry(RandomAccessFile raf) { return null; };
    default Document deleteRegistry(short id) { return null; }
    default void updateRegistry(short id, Document document) {};
}