package models;

import java.time.LocalDate;
import java.util.Arrays;

public class Movie {
    private short id;
    private String homePage;
    private String movieName;
    private String[] genres;
    private String overview;
    private String[] cast;
    private String[] originalLanguage;
    private String storyLine;
    private String[] productionCompany;
    private LocalDate releaseDate;
    private String tagLine;
    private float voteAverage;
    private String voteCount;
    private String budgetUsd;
    private String revenue;
    private int runtimeMinutes;
    private String releaseCountry;

    public void write() {
        System.out.println("ID: " + id);
        System.out.println("Home Page: " + homePage);
        System.out.println("Movie Name: " + movieName);
        System.out.println("Genres: " + Arrays.toString(genres));
        System.out.println("Overview: " + overview);
        System.out.println("Cast: " + Arrays.toString(cast));
        System.out.println("Original Language: " + Arrays.toString(originalLanguage));
        System.out.println("Story Line: " + storyLine);
        System.out.println("Production Companies: " + Arrays.toString(productionCompany));
        System.out.println("Release Date: " + releaseDate);
        System.out.println("Tagline: " + tagLine);
        System.out.println("Vote Average: " + voteAverage);
        System.out.println("Vote Count: " + voteCount);
        System.out.println("Budget (USD): " + budgetUsd);
        System.out.println("Revenue: " + revenue);
        System.out.println("Runtime (Minutes): " + runtimeMinutes);
        System.out.println("Release Country: " + releaseCountry);
    }

    public short getId() {
        return id;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getMovieName() {
        return movieName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String[] getCast() {
        return cast;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public String getBudgetUsd() {
        return budgetUsd;
    }

    public String getRevenue() {
        return revenue;
    }

    public String[] getOriginalLanguage() {
        return originalLanguage;
    }

    public String getReleaseCountry() {
        return releaseCountry;
    }

    public String[] getProductionCompany() {
        return productionCompany;
    }

    public void setId(short id) {
        this.id = id;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setCast(String[] cast) {
        this.cast = cast;
    }

    public void setBudgetUsd(String budgetUsd) {
        this.budgetUsd = budgetUsd;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public void setOriginalLanguage(String[] originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setProductionCompany(String[] productionCompany) {
        this.productionCompany = productionCompany;
    }

    public void setReleaseCountry(String releaseCountry) {
        this.releaseCountry = releaseCountry;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }
}
