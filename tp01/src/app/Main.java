package app;

import io.SequentialDataIO;
import parsers.CsvParser;
import utils.*;

public class Main {
    public static void main(String[] args) {
        SequentialDataIO io = new SequentialDataIO("res/movies.db");
        // CsvParser.parse("dataset.csv", io); // carregar o csv para arquivo binario

        BinaryDocument document;
        short i = 1;

        do {
          document = io.getRegistry(i);
          if (document == null) System.out.println(i);
          i++;
        } while (document != null);
    }
}