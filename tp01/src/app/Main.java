package app;

import io.SequentialDataIO;
import utils.*;
import parsers.*;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
    public static void main(String[] args) {


        SequentialDataIO io = new SequentialDataIO("res/movies.db");
        //CsvParser.parse("test.csv", io); // carregar o csv para arquivo binario

        /* ExternOrdinate ordenator = new ExternOrdinate(io, 10, 3);
        try {
            ordenator.distribute();
            ordenator.intercalate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } */

        SequentialDataIO other = new SequentialDataIO("dist/int_temp_9.0");
        other.getRegistry((short) 499).getMovie().write();
    }
}