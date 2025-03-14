package utils;

import io.DataIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class ExternOrdinate {
    public int pathCount;
    public int distributionRegistryCount;
    public DataIO io;

    public ExternOrdinate(DataIO io, int pathCount, int distributionRegistryCount) {
        this.io = io;
        this.pathCount = pathCount;
        this.distributionRegistryCount = distributionRegistryCount;
    }

    public void distribute() throws IOException {
        List<RandomAccessFile> rafs = new ArrayList<>(pathCount);
        for (int i = 0; i < pathCount; i++) {
            String filename = "dist/temp_" + i;
            RandomAccessFile raf = new RandomAccessFile(filename, "rw");
            rafs.add(raf);
        }

        int currentRaf = 0;

        List<BinaryDocument> documents = null;
        RandomAccessFile readBinaryRaf = new RandomAccessFile("res/movies.db", "r");
        readBinaryRaf.readShort();

        do {
            RandomAccessFile raf = rafs.get(currentRaf);

            documents = getOrdinatedRegistryArray(readBinaryRaf);

            for (BinaryDocument doc : documents) {
                byte[] bytes = doc.toByteArray();

                raf.writeInt(bytes.length);
                raf.writeBoolean(false);
                raf.write(bytes);
            }

            currentRaf = (currentRaf + 1) % pathCount;
        } while(documents.size() >= distributionRegistryCount);

        readBinaryRaf.close();

        for (int i = 0; i < pathCount; i++) {
            RandomAccessFile raf = rafs.get(i);
            raf.close();
        }
     }

    public void intercalate() throws IOException {
        List<RandomAccessFile> distributionRafs = new ArrayList<>(pathCount);
        List<RandomAccessFile> intercalationRafs = new ArrayList<>(pathCount);
        List<Short> currentIds = new ArrayList<>(pathCount);

        for (int i = 0; i < pathCount; i++) {
            String distributionFilename = "dist/temp_" + i;
            String intercalationFilename = "dist/int_temp_" + i;

            RandomAccessFile distributionRaf = new RandomAccessFile(distributionFilename, "r");
            RandomAccessFile intercalationRaf = new RandomAccessFile(intercalationFilename, "rw");

            BinaryDocument initialDocument = (BinaryDocument) io.getNextRegistry(distributionRaf);
            if (initialDocument == null) currentIds.add(Short.MAX_VALUE);
            else currentIds.add(initialDocument.getMovie().getId());

            distributionRafs.add(distributionRaf);
            intercalationRafs.add(intercalationRaf);
        }

        int blockSize = distributionRegistryCount;

        for (int i = 0; i < pathCount; i ++) {
            RandomAccessFile raf = intercalationRafs.get(i);
            short minValue = 0;
            int count = 0;

            while (minValue < Short.MAX_VALUE - 1 && count < blockSize * 2) {
                minValue = currentIds.stream()
                        .min((a, b) -> a - b)
                        .orElse(Short.MAX_VALUE);

                if (minValue == Short.MAX_VALUE) break;
                System.out.println("t");

                int index = currentIds.indexOf(minValue);
                BinaryDocument newDocument = (BinaryDocument) io.getNextRegistry(distributionRafs.get(index));
                if (newDocument == null) currentIds.set(index, Short.MAX_VALUE);
                else currentIds.set(index, newDocument.getMovie().getId());

                raf.writeShort(minValue);
                count++;
            }
        }
    }

    private List<BinaryDocument> getOrdinatedRegistryArray(RandomAccessFile raf) {
        List<BinaryDocument> documents = new ArrayList<>(distributionRegistryCount);
        for (int i = 0; i < distributionRegistryCount; i++) {
            BinaryDocument doc = (BinaryDocument) io.getNextRegistry(raf);
            if (doc == null) break;
            documents.add(doc);
        }

        documents.sort((a, b) -> {
            return a.getMovie().getId() - b.getMovie().getId();
        });

        return documents;
    }
}
