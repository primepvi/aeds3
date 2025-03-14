package utils;

import models.Movie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Document {
    protected final Movie movie;

    protected Document(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public static Document fromRaw(String[] rawFields) {
        Movie movie = new Movie();
        Document document = new Document(movie);

        List<String> fields = Arrays.asList(rawFields);

        String rawHomePage = fields.getFirst();
        String rawMovieName = fields.get(1);
        String rawGenres = fields.get(2);
        String rawOverview = fields.get(3);
        String rawCast = fields.get(4);
        String rawOriginalLanguage = fields.get(5);
        String rawStoryLine = fields.get(6);
        String rawProductionCompany = fields.get(7);
        String rawReleaseDate = fields.get(8);
        String rawTagLine = fields.get(9);
        String rawVoteAverage = fields.get(10);
        String rawVoteCount = fields.get(11);
        String rawBudgetUsd = fields.get(12);
        String rawRevenue = fields.get(13);
        String rawRuntimeMinutes = fields.get(14);
        String rawReleaseCountry = fields.get(15);

        movie.setHomePage(rawHomePage);
        movie.setMovieName(rawMovieName);
        movie.setGenres(arrayFromRaw(rawGenres));
        movie.setOverview(rawOverview);
        movie.setCast(arrayFromRaw(rawCast));
        movie.setOriginalLanguage(arrayFromRaw(rawOriginalLanguage));
        movie.setStoryLine(rawStoryLine);
        movie.setProductionCompany(arrayFromRaw(rawProductionCompany));
        movie.setReleaseDate(dateFromRaw(rawReleaseDate));
        movie.setTagLine(rawTagLine);
        movie.setVoteAverage(floatFromRaw(rawVoteAverage));
        movie.setVoteCount(rawVoteCount);
        movie.setBudgetUsd(rawBudgetUsd);
        movie.setRevenue(rawRevenue);
        movie.setRuntimeMinutes(intFromRaw(rawRuntimeMinutes));
        movie.setReleaseCountry(rawReleaseCountry);

        // movie.write();

        return document;
    }
    private static String[] arrayFromRaw(String raw) {
        String[] slice = raw.split("\"");
        String content = slice.length > 1 ? slice[1] : slice[0];
        String[] values = content.split(",");

        return Arrays.stream(values)
                .map(v -> {
                    String[] strings = v.split("'");
                    return strings.length > 1 ? strings[1] : v;
                })
                .toArray(String[]::new);
    }
    private static LocalDate dateFromRaw(String raw) {
        return LocalDate.parse(raw, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private static float floatFromRaw(String raw) {
        return Float.parseFloat(raw);
    }
    private static int intFromRaw(String raw) {
        return Integer.parseInt(raw.equals("Unknown") ? "0" : raw);
    }

}
