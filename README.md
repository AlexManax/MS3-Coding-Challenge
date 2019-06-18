# MS3-Coding-Challenge
MS3 Coding Challenge

Please open as maven project


pom.xml:
opencsv library added,
sqlite-jdbc driver added

SQLite config: direct write, journaling in memory - increased INSERT command productivity by ~900% (4000ms => 500ms) vs default config.

Assumptions:
1. We always have 10 columns for correct CSV record (line)
2. 1st CSV record contain columns names
3. CSV record "A,,C,D,E,F,G,H,I,J" contains 10 columns even if its body may be empty
4. CSV record field containing "," will be double-quoted on input : "A, "B,B", C..."
    therefor the program should not detect it and make the quotes.
4. The program should be simple. no UI, no JPA, no layered architecture.
5. We will have 2 input parameters:
    a. path to source of CSV file and
    b. path to output directory that will contain a log file and the bad-data-<timestamp>.csv file
6. The program will not start in parallel mode so each bad-data-<*>.csv file will be unique.
