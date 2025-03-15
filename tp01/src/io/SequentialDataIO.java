package io;

import utils.BinaryDocument;
import utils.Document;
import java.io.*;

public class SequentialDataIO implements DataIO {
    private final String path;

    public SequentialDataIO(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void createRegistry(Document document) {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            short lastId = 0;

            if (raf.length() >= Short.BYTES) {
                raf.seek(0);
                lastId = raf.readShort();
            }

            short newLastId = (short) (lastId + 1);

            raf.seek(0);
            raf.writeShort(newLastId);

            BinaryDocument binaryDocument = BinaryDocument.fromDocument(newLastId, document);
            byte[] bytes = binaryDocument.toByteArray();

            raf.seek(raf.length());

            raf.writeInt(bytes.length);
            raf.writeBoolean(false);
            raf.write(bytes);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public BinaryDocument getRegistry(short id) {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.readShort(); // last id

            short currentId = -1;

            do {
                int registrySize = raf.readInt();
                boolean isRegistryDeleted = raf.readBoolean();
                currentId = raf.readShort();

                if (isRegistryDeleted) {
                    raf.skipBytes(registrySize - Short.BYTES);
                    System.out.println(currentId + ":GET Registro Morto.");
                    continue;
                }

                if (currentId == id) {
                    byte[] registryBytes = new byte[registrySize - Short.BYTES];
                    raf.readFully(registryBytes);

                    return BinaryDocument.fromByteArray(currentId, registryBytes);
                }

                raf.skipBytes(registrySize - Short.BYTES);
            } while (raf.getFilePointer() < raf.length());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    public BinaryDocument deleteRegistry(short id) {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.readShort(); // last id

            short currentId = -1;

            do {
                int registrySize = raf.readInt();
                long registryDeletedFlagPos = raf.getFilePointer();

                boolean isRegistryDeleted = raf.readBoolean();
                currentId = raf.readShort();

                if (isRegistryDeleted) {
                    raf.skipBytes(registrySize - Short.BYTES);
                    System.out.println(currentId + ":DELETE Registro Morto.");
                    continue;
                }

                if (currentId == id) {
                    byte[] registryBytes = new byte[registrySize - Short.BYTES];
                    raf.readFully(registryBytes);

                    raf.seek(registryDeletedFlagPos);
                    raf.writeBoolean(true);

                    return BinaryDocument.fromByteArray(currentId, registryBytes);
                }

                raf.skipBytes(registrySize - Short.BYTES);
            } while (raf.getFilePointer() < raf.length());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void updateRegistry(short id, Document document) {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            BinaryDocument oldDocument = deleteRegistry(id);
            if (oldDocument == null) return;

            BinaryDocument binaryDocument = BinaryDocument.fromDocument(oldDocument.getMovie().getId(), document);
            byte[] bytes = binaryDocument.toByteArray();

            raf.seek(raf.length());

            raf.writeInt(bytes.length);
            raf.writeBoolean(false);
            raf.write(bytes);
         } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public BinaryDocument getNextRegistry(RandomAccessFile raf) {
        try {
            short currentId = -1;

            do {
                int registrySize = raf.readInt();
                boolean isRegistryDeleted = raf.readBoolean();
                currentId = raf.readShort();

                if (isRegistryDeleted) {
                    raf.skipBytes(registrySize - Short.BYTES);
                    System.out.println(currentId + ":GET NEXT Registro Morto.");
                    continue;
                }

                byte[] registryBytes = new byte[registrySize - Short.BYTES];

                raf.readFully(registryBytes);

                return BinaryDocument.fromByteArray(currentId, registryBytes);
            } while (raf.getFilePointer() < raf.length());
        } catch (Exception e) {}

        return null;
    }
}

