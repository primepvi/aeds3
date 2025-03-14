package parsers;

import io.DataIO;
import utils.Document;

import java.io.BufferedReader;
import java.io.FileReader;

public class CsvParser {
    public static void parse(String filename, DataIO io) {
        try {
            FileReader fileReader = new FileReader("res/" + filename);
            BufferedReader buffReader = new BufferedReader(fileReader);

            String line = buffReader.readLine();
            // String[] headFields = parseLineFields(line);

            while ((line = buffReader.readLine()) != null) {
                String[] lineFields = parseLineFields(line);
                Document document = Document.fromRaw(lineFields);
                io.createRegistry(document);
            }

        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

    }

    private static String[] parseLineFields(String line) {
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        return line.split(regex);
    }
}
