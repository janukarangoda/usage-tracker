/*
 * Main.java
 * Entry point for the usage-tracker application
 */

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("  report <cdrs.csv>");
            System.out.println("  query <cdrs.csv> <msisdn> <from_date> <to_date>");
            System.out.println("  query <cdrs.csv> top-users");
            return;
        }

        String command = args[0];
        String filePath = args[1];

        try {
            switch (command) {
                case "report":
                    ReportGenerator.generateDailyReport(filePath);
                    break;
                case "query":
                    if (args.length == 3 && args[2].equals("top-users")) {
                        ReportGenerator.generateTopUsersReport(filePath);
                    } else if (args.length == 5) {
                        String msisdn = args[2];
                        String fromDate = args[3];
                        String toDate = args[4];
                        ReportGenerator.generateSubscriberReport(filePath, msisdn, fromDate, toDate);
                    } else {
                        System.out.println("Invalid query command.");
                    }
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}

