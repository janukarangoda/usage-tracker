import models.CDR;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReportGenerator {

    public static void generateDailyReport(String filePath) throws IOException {
    List<CDR> records = CDRParser.parse(filePath);

    // Key = date + msisdn
    class Usage {
        int voice = 0;
        int data = 0;
    }

    Map<String, Map<String, Usage>> usageMap = new TreeMap<>();

    for (CDR cdr : records) {
        usageMap
            .computeIfAbsent(cdr.date, k -> new HashMap<>())
            .computeIfAbsent(cdr.msisdn, k -> new Usage());

        Usage usage = usageMap.get(cdr.date).get(cdr.msisdn);
        if (cdr.callType.equalsIgnoreCase("voice")) {
            usage.voice += cdr.duration;
        } else if (cdr.callType.equalsIgnoreCase("data")) {
            usage.data += cdr.duration; // assumed duration = kilobytes
        }
    }

    JSONArray output = new JSONArray();

    for (var dateEntry : usageMap.entrySet()) {
        String date = dateEntry.getKey();
        for (var msisdnEntry : dateEntry.getValue().entrySet()) {
            String msisdn = msisdnEntry.getKey();
            Usage usage = msisdnEntry.getValue();

            JSONObject obj = new JSONObject();
            obj.put("date", date);
            obj.put("msisdn", msisdn);
            obj.put("callSeconds", usage.voice);
            obj.put("dataKilobytes", usage.data);

            output.put(obj);
        }
    }

    try (FileWriter file = new FileWriter("daily-usage-report.json")) {
        file.write(output.toString(4));
    }

    System.out.println("✅ Corrected daily-usage-report.json generated.");
}

    public static void generateSubscriberReport(String filePath, String msisdn, String from, String to) throws IOException {
    List<CDR> records = CDRParser.parse(filePath);

    // Group by date for the given MSISDN
    class Usage {
        int voice = 0;
        int data = 0;
    }

    Map<String, Usage> dateUsage = new TreeMap<>();

    for (CDR cdr : records) {
        if (!cdr.msisdn.equals(msisdn)) continue;
        if (cdr.date.compareTo(from) < 0 || cdr.date.compareTo(to) > 0) continue;

        dateUsage.computeIfAbsent(cdr.date, k -> new Usage());
        Usage usage = dateUsage.get(cdr.date);

        if (cdr.callType.equalsIgnoreCase("voice")) {
            usage.voice += cdr.duration;
        } else if (cdr.callType.equalsIgnoreCase("data")) {
            usage.data += cdr.duration;
        }
    }

    JSONArray output = new JSONArray();
    for (Map.Entry<String, Usage> entry : dateUsage.entrySet()) {
        JSONObject obj = new JSONObject();
        obj.put("date", entry.getKey());
        obj.put("callSeconds", entry.getValue().voice);
        obj.put("dataKilobytes", entry.getValue().data);
        output.put(obj);
    }

    try (FileWriter file = new FileWriter("subscriber-" + msisdn + "-usage-report.json")) {
        file.write(output.toString(4));
    }

    System.out.println("✅ subscriber-" + msisdn + "-usage-report.json generated.");
}

    public static void generateTopUsersReport(String filePath) throws IOException {
        List<CDR> records = CDRParser.parse(filePath);
        Map<String, Integer> usageMap = new HashMap<>();

        for (CDR cdr : records) {
            usageMap.merge(cdr.msisdn, cdr.duration, Integer::sum);
        }

        List<Map.Entry<String, Integer>> sorted =
            usageMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(2)
                .collect(Collectors.toList());

        JSONArray topUsers = new JSONArray();
        for (Map.Entry<String, Integer> entry : sorted) {
            JSONObject obj = new JSONObject();
            obj.put("msisdn", entry.getKey());
            obj.put("total_duration", entry.getValue());
            topUsers.put(obj);
        }

        try (FileWriter file = new FileWriter("top-users-report.json")) {
            file.write(topUsers.toString(4));
        }
        System.out.println("✅ top-users-report.json generated.");
    }
}
