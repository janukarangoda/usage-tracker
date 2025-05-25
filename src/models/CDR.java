package models;

public class CDR {
    public String date;
    public String msisdn;
    public String callType;
    public int duration; // in seconds

    public CDR(String date, String msisdn, String callType, int duration) {
        this.date = date;
        this.msisdn = msisdn;
        this.callType = callType;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CDR{" +
                "date='" + date + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", callType='" + callType + '\'' +
                ", duration=" + duration +
                '}';
    }
}
