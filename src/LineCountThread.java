import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineCountThread extends Thread{
    String filename="";
    private int lines;


    public LineCountThread(String filename) {
        this.filename=filename;
        this.lines=0;
    }

    @Override
    public void run() {
        File tempFile=null;
        Scanner fileReader=null;
        try{
            tempFile = new File(this.filename);
            fileReader = new Scanner(tempFile);
            while(fileReader.hasNextLine()){
                this.lines++;
                fileReader.nextLine();
            }
            fileReader.close();
        }catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    @Override
    public State getState() {
        return super.getState();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLines() {

        return lines;
    }

}
