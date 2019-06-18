import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserCSV {
    public static void main(String[] args) { //args receiving 2 params: path to CSV file and path to output directory
        Date start = new Date();//Used for time test
        DBservice dBservice = new DBservice();//Used for access to DB methods
        int countGoodRecords = 0;
        int countBadRecords = 0;
        String csvFilePath = args[1] + "/bad-data-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";//Defined path to log CSV file
        try {
            Files.createFile(Paths.get(csvFilePath)); //Created the log CSV file, assumed it does not exist, even thought it will be catches
        } catch (IOException e) {
            e.printStackTrace();
        }
        //try with resources is great closing procedure for java 7+
        //Cover the FR/FW to Buffer to optimize reading/writing
        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(args[0])));
             CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(csvFilePath)))
        ) {
            String[] line; //used to save fields from a record
            boolean trigger = true; //trigger is used one time (decided to use it in order to optimize the speed, probably not the best solution)

            while ((line = reader.readNext()) != null) { //started the file reader cycle
                if (trigger) { //one time use only for defining first record with names of columns
                    if (line.length == 10) {//double check if it's a good record
                        dBservice.initDB(line); //creates DB table
                        trigger = false;
                        countGoodRecords++; //adjust counter
                    }
                } else if (line.length != 10) { //check if record contains not 10 columns
                    writer.writeNext(line); //write record to file
                    countBadRecords++; //adjust counter
                } else { // only good records
                    dBservice.insert(line); //insert in DB the record
                    countGoodRecords++; //adjust counter
                }
            }
            dBservice.closeConnection(); //close DB connection. Unusual solution. Decided to do it in order to optimize insert operations, It will not initialize the connection on each insert operation.
        } catch (Exception e) {
            e.printStackTrace();
            dBservice.closeConnection(); //close DB connection if something is wrong =)
        }
        writeLog(args[1], countGoodRecords, countBadRecords); //it writes the log in file
        System.out.println(new Date().getTime() - start.getTime());
    }

    private static void writeLog(String arg, int countGood, int countBad) {
        String logFilePath = arg + "/log.log";
        String log = countGood + countBad + " records received\n" + countGood + " records successful\n"
                        + countBad + " records failed\n ****************************************\n";
        try {
            if (Files.exists(Paths.get(logFilePath))) {
                Files.write(Paths.get(logFilePath), log.getBytes(), StandardOpenOption.APPEND); //append if file exist
            } else {
                Files.write(Paths.get(logFilePath), log.getBytes(), StandardOpenOption.CREATE); //create if file is not exist
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
