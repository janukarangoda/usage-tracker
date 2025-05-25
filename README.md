# usage-tracker
simple CDR (CSV) reader 
Usage Tracker - Java CLI Application

This command-line application processes Call Detail Records (CDRs) from a CSV file and generates usage reports based on specified commands.

✅ Features Implemented
	1.	Daily Usage Aggregation
	•	Groups data per day and per subscriber (MSISDN)
	•	Outputs both voice call duration and data usage per MSISDN per day
	•	Output: daily-usage-report.json
	2.	Subscriber Usage Query
	•	Filters usage for a specific MSISDN between two dates
	•	Aggregates daily usage into call duration and data usage
	•	Output: subscriber-<msisdn>-usage-report.json
	3.	Top Users by Call Duration
	•	Finds the top 2 MSISDNs with the highest total call time
	•	Output: top-users-report.json

⸻

🏗 Directory Structure

usage-tracker/
├── src/
│   ├── Main.java
│   ├── CDRParser.java
│   ├── ReportGenerator.java
│   └── models/
│       ├── CDR.java
├── lib/
│   └── json.jar         # org.json dependency
├── cdrs.csv             # sample input file
├── out/                 # compiled .class files



⸻

🔧 Setup Instructions

1. Download JSON library

mkdir -p lib
wget https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar -O lib/json.jar

2. Compile the Code

javac -cp lib/json.jar -d out src/models/CDR.java src/CDRParser.java src/ReportGenerator.java src/Main.java

3. Run Commands

Generate Daily Report

java -cp usage-tracker.jar:lib/json.jar Main report cdrs.csv

Query Subscriber Usage

java -cp usage-tracker.jar:lib/json.jar Main query <CDR file> <MSISDN> <from date> <to date>

java -cp usage-tracker.jar:lib/json.jar Main query cdrs.csv 61411111111 2025-05-01 2025-05-03

Get Top 2 Voice Users

java -cp usage-tracker.jar:lib/json.jar Main query cdrs.csv top-users



📤 Sample Input (cdrs.csv)

date,msisdn,call_type,duration
2025-05-01,61411111111,voice,60
2025-05-01,61411111111,data,500
2025-05-02,61411111111,voice,30
2025-05-02,61411111111,data,300
2025-05-03,61411111112,voice,100




