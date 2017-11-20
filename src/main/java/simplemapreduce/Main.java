package simplemapreduce;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wordcount.FileThread;
import wordcount.SharedCache;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;


public class Main {

    private static final String FILENAME_ARGUMENT_NAME = "files";
    private static final String OUTPUT_ARGUMENT_NAME = "output";
    private static final Logger LOGGER = LoggerFactory.getLogger( Main.class.getName() );

    public static void main(final String args[]) {
        final Options options = createOptions();

        try {
            final CommandLine commandLine = parseArguments(args, options);
            final String filenames[] = commandLine.getOptionValues(FILENAME_ARGUMENT_NAME);
            final String outputType = commandLine.getOptionValue(OUTPUT_ARGUMENT_NAME, "stdout");

            LOGGER.info("Simple Map Reduce: Word count");
            LOGGER.info(String.format("Files: \"%s\"", mkString(filenames, ',')));
            LOGGER.info(String.format("Output will be: \"%s\"", outputType));

            final SharedCache sharedCache = new SharedCache();
            final Stream<FileThread> fileThreadStream = Arrays.stream(filenames).map(f -> new FileThread("thread_", f, sharedCache));

            final ExecutorService executorService = Executors.newCachedThreadPool();
            fileThreadStream.forEach(executorService::execute);
            executorService.shutdown();

            executorService.awaitTermination(1, TimeUnit.DAYS); // Just some quite long time

            outputResults(outputType, sharedCache.reduceByWord());

            LOGGER.info("Done.");
        }
        catch (final ParseException parseException) {
            LOGGER.error(parseException.getMessage());
            showHelp(options);

            System.exit(-1);
        }
        catch (final Exception exception) {
            LOGGER.error(exception.getMessage());
            System.exit(-1);
        }
    }

    private static void outputResults(final String outputType, final Map<String, Long> mapCount) throws IOException {
        switch (outputType) {
            case "csv": outputToCsv(mapCount); break;
            default: outputToStdout(mapCount);
        }
    }

    private static void outputToStdout(final Map<String, Long> mapCount) {
        for (final String word : mapCount.keySet()) {
            final long wordCount = mapCount.get(word);
            System.out.println(String.format("%s\t%d", word, wordCount));
        }
    }

    private static void outputToCsv(final Map<String, Long> mapCount) throws IOException {
        final Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.csv"), "UTF-8"));

        bufferedWriter.write("Word,Count\n");
        for (final String word : mapCount.keySet()) {
            final long wordCount = mapCount.get(word);
            bufferedWriter.write(String.format("%s,%d\n", word, wordCount));
        }
        bufferedWriter.close();
    }

    private static String mkString(final String stringArray[], char separator) {
        final StringBuffer buffer = new StringBuffer();

        for (final String aStringArray : stringArray) {
            buffer.append(aStringArray).append(separator);
        }

        return buffer.toString();
    }

    private static Options createOptions() {
        final Options options = new Options();
        options.addOption(OUTPUT_ARGUMENT_NAME, true, "Specify output format: 'stdout' (default) or 'csv'");
        options.addOption(
                OptionBuilder.withLongOpt(FILENAME_ARGUMENT_NAME)
                        .withArgName(FILENAME_ARGUMENT_NAME)
                        .hasArgs()
                        .withValueSeparator(',')
                        .isRequired()
                        .withDescription("Name of the files separated by comma.")
                        .create()
        );

        return options;
    }

    private static void showHelp(final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "SimpleMapReduce.jar -file1 <file1> -file2 <file2> [output]", options );
    }

    private static CommandLine parseArguments(final String arguments[], final Options options) throws ParseException {
        final CommandLineParser parser = new GnuParser();
        return parser.parse(options, arguments);
    }

}
