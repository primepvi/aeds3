import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Parser {
    public static void parse(String filename) {
        try {
            FileReader fileReader = new FileReader("res/" + filename);
            BufferedReader buffReader = new BufferedReader(fileReader);

            String line = buffReader.readLine();
            String[] headFields = parseLineFields(line);

            while ((line = buffReader.readLine()) != null) {
                // String[] lineFields = parseLineFields(line);
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
