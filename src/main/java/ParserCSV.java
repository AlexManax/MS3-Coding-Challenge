import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserCSV {
    public static void main(String[] args) {
        Date start = new Date();
        DBservice dBservice = new DBservice();
        int countGoodRecords = 0;
        int countBadRecords = 0;
        String csvFilePath = args[1] + "/bad-data-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".csv";
        try {
            Files.createFile(Paths.get(csvFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(args[0])));
             CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(csvFilePath)))
        ) {
            System.out.println(args[1]);
            String[] line;
            boolean triger = true;
            while ((line = reader.readNext()) != null) {
                if (triger) {
                    if (line.length == 10) {
                        dBservice.initDB(line);
                        triger = false;
                        countGoodRecords++;
                    }
                } else if (line.length != 10) {
                    writer.writeNext(line);
                    countBadRecords++;
                } else {
                    dBservice.insert(line);
                    countGoodRecords++;
                }
            }
            dBservice.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            dBservice.closeConnection();
        }
        writeLog(args[1], countGoodRecords, countBadRecords);
        System.out.println(new Date().getTime() - start.getTime());
    }

    private static void writeLog(String arg, int countGood, int countBad) {
        String logFilePath = arg + "/log.log";
        String log = countGood + countBad + " records received\n" + countGood + " records successful\n" + countBad + " records failed\n ****************************************\n";
        try {
            if (Files.exists(Paths.get(logFilePath))) {
                Files.write(Paths.get(logFilePath), log.getBytes(), StandardOpenOption.APPEND);
            } else {
                Files.write(Paths.get(logFilePath), log.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
