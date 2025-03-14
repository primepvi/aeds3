package utils;

import models.Movie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BinaryDocument extends Document  {
    private BinaryDocument(Movie movie) {
        super(movie);
    }

    public static BinaryDocument fromDocument(short id, Document document) {
        document.getMovie().setId(id);
        return new BinaryDocument(document.getMovie());
    }

    public static BinaryDocument fromByteArray(short id, byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        DataInputStream data =  new DataInputStream(stream);

        Movie movie = new Movie();
        movie.setId(id);
        movie.setHomePage(data.readUTF());
        movie.setMovieName(data.readUTF());
        movie.setGenres(streamReadArray(data));
        movie.setOverview(data.readUTF());
        movie.setCast(streamReadArray(data));
        movie.setOriginalLanguage(streamReadArray(data));
        movie.setStoryLine(data.readUTF());
        movie.setProductionCompany(streamReadArray(data));
        movie.setReleaseDate(streamReadLocalDate(data));
        movie.setTagLine(data.readUTF());
        movie.setVoteAverage(data.readFloat());
        movie.setVoteCount(data.readUTF());
        movie.setBudgetUsd(data.readUTF());
        movie.setRevenue(data.readUTF());
        movie.setRuntimeMinutes(data.readInt());
        movie.setReleaseCountry(data.readUTF());

        return new BinaryDocument(movie);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(stream);

        data.writeShort(movie.getId());
        data.writeUTF(movie.getHomePage());
        data.writeUTF(movie.getMovieName());
        streamWriteArray(data, movie.getGenres());
        data.writeUTF(movie.getOverview());
        streamWriteArray(data, movie.getCast());
        streamWriteArray(data, movie.getOriginalLanguage());
        data.writeUTF(movie.getStoryLine());
        streamWriteArray(data, movie.getProductionCompany());
        streamWriteLocalDate(data, movie.getReleaseDate());
        data.writeUTF(movie.getTagLine());
        data.writeFloat(movie.getVoteAverage());
        data.writeUTF(movie.getVoteCount());
        data.writeUTF(movie.getBudgetUsd());
        data.writeUTF(movie.getRevenue());
        data.writeInt(movie.getRuntimeMinutes());
        data.writeUTF(movie.getReleaseCountry());

        return stream.toByteArray();
    }

    private static void streamWriteArray(DataOutputStream data, String[] values) throws IOException {
        ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
        DataOutputStream arrayData = new DataOutputStream(arrayStream);

        for (int i = 0; i < values.length; i++) {
            arrayData.writeUTF(values[i]);
            if (i < values.length - 1) arrayData.writeChar('|');
        }

        data.writeInt(arrayData.size());
        data.write(arrayStream.toByteArray());
    }


    private static String[] streamReadArray(DataInputStream data) throws IOException {
        int size = data.readInt();

        ByteArrayInputStream arrayStream = new ByteArrayInputStream(data.readNBytes(size));
        DataInputStream arrayData = new DataInputStream(arrayStream);

        List<String> values = new ArrayList<String>();

        while (arrayData.available() > 0) {
            values.add(arrayData.readUTF());
            if (arrayData.available() > 0) arrayData.readChar();
        }

        return values.toArray(String[]::new);
    }

    private static void streamWriteLocalDate(DataOutputStream data, LocalDate date) throws IOException {
        data.writeUTF(date.toString());
    }

    private static LocalDate streamReadLocalDate(DataInputStream data) throws IOException {
        String value = data.readUTF();
        return LocalDate.parse(value);
    }
}
