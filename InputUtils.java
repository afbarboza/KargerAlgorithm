package com.learning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputUtils {
    public static BufferedReader reader;
    public static String FILE_PATH = "input.txt";

    public static void initInputReader() {
        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            reader = new BufferedReader(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readNextLine() {
        String line = "";

        try {
            line = reader.readLine();
            if (line != null) {
                return line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static ArrayList<String> parseLineIntoStringList(String line) {
        line = line.trim();
        ArrayList<String> edges = new ArrayList<String>();
        List<String> tokens = Arrays.asList(line.split("\t"));
        for (String s : tokens) {
            s = s.trim();
            edges.add(s);
        }

        return edges;
    }

}
