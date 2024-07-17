package com.programming.uit.javadeveloper.tan;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class ReverseFileReader {
     private List<String> lines;
     private int currentLine;

     public ReverseFileReader(String filePath) throws IOException {
          try {
               lines = Files.readAllLines(Paths.get(filePath));
               currentLine = lines.size() - 1;
          } catch (NoSuchFileException e) {
               System.err.println("The specified file does not exist: " + filePath);
               throw e;
          }
     }

     public String readLine() throws IOException {
          if (currentLine < 0) {
               return null;
          }
          return lines.get(currentLine--);
     }

     public static void main(String[] args) {
          try {
               ReverseFileReader reader = new ReverseFileReader("src/main/java/com/programming/uit/javadeveloper/tan/test.txt");
               String line;
               while ((line = reader.readLine()) != null) {
                    System.out.println(line);
               }
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
