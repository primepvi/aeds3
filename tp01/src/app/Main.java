package app;

import io.SequentialDataIO;
import utils.*;

public class Main {
    public static void main(String[] args) {
        SequentialDataIO io = new SequentialDataIO("res/movies.db");
        // CsvParser.parse("dataset.csv", io); // carregar o csv para arquivo binario
        ExternOrdinate ordenator = new ExternOrdinate(io, 2, 4);

        try {
           ordenator.distribute();
           ordenator.intercalate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}