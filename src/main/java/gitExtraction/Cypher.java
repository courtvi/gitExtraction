package gitExtraction;

import java.io.*;

public class Cypher {
    static File myFile;
    static FileWriter myWriter;

    Cypher(File myFile) throws IOException {
        
        this.myFile = myFile;
        createFile();
        myWriter = new FileWriter(this.myFile);
        writeHeader();
        newLine();
        close();
    }

    static void newLine() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(myFile));
        out.newLine();
    }


    public static void createFile() {

        myFile = new File ("Cypher.txt");

    }

    public static void writeHeader() throws IOException {

        myWriter = new FileWriter(myFile);
        myWriter.write("Application;technology");
        System.out.println("Writer header");
        myWriter.close();
    }

    public static void writeFile(String string)  throws IOException {

        myWriter = new FileWriter(myFile);
        myWriter.write(string);
        System.out.println("Writer new line");
    }

    public static void close() throws IOException {

        myWriter.close();
    }
}
