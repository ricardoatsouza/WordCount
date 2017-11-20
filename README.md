# WordCount

This is a simple command line tool for the Word Count problem.

## To compile

Be sure to have Java 8 and Gradle. Then execute:

```bash
$ gradle build
```

The resulting jar is gonna be under the directory `build/libs/SimpleMapReduce.jar`

## To execute

The documentation is the following:

```bash
Usage: SimpleMapReduce.jar -files <file1,file2,...> [-output (stdout|csv)]
    -files <files>       Name of the files separated by comma.
    -output <arg>        Specify output format: 'stdout' (default) or 'csv'
```

As an example, you can run it with the two example files that are in this repository:

```bash
java -jar build/libs/SimpleMapReduce.jar -files example1.txt,example2.txt -output csv
```