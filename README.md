# MS3-Coding-Challenge
MS3 Coding Challenge

Please open as maven project


pom.xml:
opencsv library added,
sqlite-jdbc driver added

SQLite config: direct write, journaling in memory - increased INSERT command productivity by ~900% (4000ms => 500ms) vs default config.

Assumptions:

1. The program should be simple. no UI, no JPA, no layered architecture.
2. We always have 10 columns for correct CSV record (line)
3. 1st CSV record contains columns names:
  a. CSV record "A,,C,D,E,F,G,H,I,J" contains 10 columns even if its body may be empty
  b. CSV record field containing "," will be double-quoted on input: "A, "B,B", C..." therefor the program should not detect it and make the quotes.

4. We will have 2 input parameters: a. path to the source of CSV file and b. path to output directory that will contain a log file and the bad-data-.csv file
5. The program will not start in a parallel mode so each bad-data-<*>.csv file will be unique.

Detected extra columns:
"HELLOWORLD HAS NOTHING ON ME"
"If you found me Keep up the good work!"
"evilrow"
"extracolumns"
"ImExtra!!!"
=)


update 06/28

added check for input parameters.
slight methods refactoring.
removed not necessary functionality in connect method.
added check for established connection in insert method.
