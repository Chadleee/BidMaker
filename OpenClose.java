import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.Scanner;

public class OpenClose{


    public Formatter shiftWriter;
    public Scanner shiftReader;

    public void openReadFile(){
        try {
            String readFile = "Shifts.txt";
            FileReader fr = new FileReader(readFile);
            shiftReader = new Scanner(fr);
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void openReadBidsFile(){
        try {
            String readFile = "Bids.txt";
            FileReader fr = new FileReader(readFile);
            shiftReader = new Scanner(fr);
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void closeReadFile(){
        shiftReader.close();
    }

    public void openWriteFile(){
        try {
            String filename = "Shifts.txt";
            FileWriter fw = new FileWriter(filename, true);
            shiftWriter = new Formatter(fw);
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void openWriteBidsFile(){
        try {
            String filename = "Bids.txt";
            FileWriter fw = new FileWriter(filename, true);
            shiftWriter = new Formatter(fw);
        }
        catch(Exception e){
            System.out.println("Error");
        }
    }

    public void closeWriteFile(){
        shiftWriter.close();
    }

}
