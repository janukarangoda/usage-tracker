import models.CDR;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CDRParser {

    public static List<CDR> parse(String filePath) throws IOException {
        List<CDR> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header if present
            line = br.readLine();
            if (line != null && line.toLowerCase().contains("msisdn")) {
                // skip header
            } else {
                // first line was not a header
                records.add(parseLine(line));
            }

            while ((line = br.readLine()) != null) {
                records.add(parseLine(line));
            }
        }
        return records;
    }

    private static CDR parseLine(String line) {
        String[] parts = line.split(",");
        String date = parts[0].trim();
        String msisdn = parts[1].trim();
        String callType = parts[2].trim();
        int duration = Integer.parseInt(parts[3].trim());

        return new CDR(date, msisdn, callType, duration);
    }
}
