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
        RandomAccessFile readBinaryRaf = new RandomAccessFile(io.getPath(), "r");
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

            currentRaf = currentRaf + 1 < rafs.size() ? currentRaf + 1 : 0;
        } while(!documents.isEmpty());
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

    public void intercalate() throws IOException {
        List<RandomAccessFile> distributionRafs = new ArrayList<>(pathCount);
        List<RandomAccessFile> intercalationRafs = new ArrayList<>(pathCount);

        int currentBlockSize = distributionRegistryCount;
        int intercalationCount = 0;

        for (int i = 0; i < pathCount; i++) {
            String distributionFilename = "dist/temp_" + i;
            RandomAccessFile distributionRaf = new RandomAccessFile(distributionFilename, "r");
            distributionRafs.add(distributionRaf);
        }

        short lastId = -1;

        while (pathCount - intercalationCount > 0) {
            int currentPathCount = pathCount - intercalationCount;

            for (int i = 0; i < currentPathCount; i++) {
                String intercalationFilename = "dist/int_temp_" + intercalationCount + "." + i;
                RandomAccessFile intercalationRaf = new RandomAccessFile(intercalationFilename, "rw");
                intercalationRafs.add(intercalationRaf);
            };

            if (currentPathCount == 1) {
                RandomAccessFile raf = intercalationRafs.getFirst();
                raf.writeShort((short) -1);
            }

            short currentLastId = handleIntercalate(
                    currentPathCount,
                    currentBlockSize,
                    distributionRafs,
                    intercalationRafs
            );

            if (currentLastId != -1) lastId = currentLastId;
            if (currentPathCount == 1) {
                RandomAccessFile raf = intercalationRafs.getFirst();
                raf.seek(0);
                raf.writeShort(lastId);
            }

            distributionRafs.forEach(raf -> {
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            distributionRafs.clear();

            distributionRafs = new ArrayList<>(currentPathCount);
            intercalationRafs = new ArrayList<>(currentPathCount);

            for (int i = 0; i < currentPathCount; i++) {
                String distributionFilename = "dist/int_temp_" + intercalationCount + "." + i;
                RandomAccessFile distributionRaf = new RandomAccessFile(distributionFilename, "r");
                distributionRafs.add(distributionRaf);
            };

            intercalationCount++;
            currentBlockSize *= 2;
        }
    }

    private short handleIntercalate(
            int currentPathCount,
            int blockSize,
            List<RandomAccessFile> distributionRafs,
            List<RandomAccessFile> intercalationRafs
    ) throws IOException {
        short lastId = -1;

        List<Short> ids = new ArrayList<>(currentPathCount);
        List<BinaryDocument> documents = new ArrayList<>(currentPathCount);

        for (int i= 0; i < currentPathCount; i++) {
            RandomAccessFile distributionRaf = distributionRafs.get(i);
            BinaryDocument initialDocument = (BinaryDocument) io.getNextRegistry(distributionRaf);

            if (initialDocument == null) {
                ids.add(Short.MAX_VALUE);
                documents.add(null);
            } else {
                ids.add(initialDocument.getMovie().getId());
                documents.add(initialDocument);
            }
        }

        int currentIntRaf = 0;
        short minValue = 0;

        while(minValue < Short.MAX_VALUE) {
            RandomAccessFile raf = intercalationRafs.get(currentIntRaf);
            int count = 0;

            while (count < blockSize * 2) {
                minValue = ids.stream()
                        .min((a, b) -> a - b)
                        .orElse(Short.MAX_VALUE);

                int index = ids.indexOf(minValue);

                BinaryDocument minDocument = documents.get(index);

                if (minValue >= Short.MAX_VALUE || minDocument == null) break;

                BinaryDocument newDocument = (BinaryDocument) io.getNextRegistry(distributionRafs.get(index));

                if (newDocument == null) {
                    ids.set(index, Short.MAX_VALUE);
                    documents.set(index, null);
                } else {
                    ids.set(index, newDocument.getMovie().getId());
                    documents.set(index, newDocument);
                }

                byte[] bytes = minDocument.toByteArray();

                raf.writeInt(bytes.length);
                raf.writeBoolean(false);
                raf.write(bytes);

                lastId = minDocument.getMovie().getId();

                count++;
            }

            count = 0;
            currentIntRaf = (currentIntRaf + 1) % intercalationRafs.size();
        }

        return lastId;
    }
}
