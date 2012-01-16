package org.ic.protrade.data.parsers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Ignore;

@Ignore
public class ParserTest {

    protected String getTestString(String filename) {
    
        Scanner scanner;
        String test = "";
    
        try {
            scanner = new Scanner(new FileInputStream(filename));
        
            while (scanner.hasNext()){
            test += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return test;
    }
}