import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class LineCountTPool implements Callable<Integer> {

    String filename="";
    public LineCountTPool(String filename) {
        this.filename=filename;
    }

    @Override
    public Integer call() throws Exception {
        File tempFile=null;
        Scanner fileReader=null;
        int lineCounter = 0;
        try{
            tempFile = new File(this.filename);
            fileReader = new Scanner(tempFile);
            while(fileReader.hasNextLine()){
                lineCounter++;
                fileReader.nextLine();
            }
            fileReader.close();
        }catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return lineCounter;
    }
}
