package entrypoint;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wordcount.FileThread;
import wordcount.SharedCache;
import wordcount.model.WordOutput;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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
            final Stream<FileThread> fileThreadStream = Arrays.stream(filenames).map(f -> new FileThread("thread_" + f, f, sharedCache));

            final ExecutorService executorService = Executors.newCachedThreadPool();
            fileThreadStream.forEach(executorService::execute);
            executorService.shutdown();

            executorService.awaitTermination(1, TimeUnit.DAYS); // Just some quite long time

            final Map<WordOutput, Long> reducedByWord = sharedCache.reduceByWord();
            final Map<WordOutput, Long> reducedByWordAndFilename = sharedCache.reduceByWordAndFilename();

            outputResults(outputType, filenames, reducedByWord, reducedByWordAndFilename);

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

    private static String trasformOutput(final String filenames[], final Map<WordOutput, Long> mapCountReducedByWord, final Map<WordOutput, Long> mapCountReducedByWordAndFilename, final char sep) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Set<WordOutput> wordSet = mapCountReducedByWord.keySet();

        stringBuilder.append(String.format("Word%cTotal%c", sep, sep));
        for (final String f : filenames) {
            stringBuilder.append(String.format("%s%c", f, sep));
        }
        stringBuilder.append("\n");

        for (final WordOutput wordOutput : wordSet) {
            stringBuilder.append(String.format("%s%c%d%c", wordOutput.word, sep, mapCountReducedByWord.get(wordOutput), sep));

            for (final String f : filenames) {
                stringBuilder.append(String.format("%d%c", mapCountReducedByWordAndFilename.getOrDefault(new WordOutput(wordOutput.word, f), 0L), sep));
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(String.valueOf(sep)));
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private static void outputResults(final String outputType, final String filenames[], final Map<WordOutput, Long> mapCountReducedByWord, final Map<WordOutput, Long> mapCountReducedByWordAndFilename) throws IOException {
        switch (outputType) {
            case "csv": outputToCsv(trasformOutput(filenames, mapCountReducedByWord, mapCountReducedByWordAndFilename, ',')); break;
            default: outputToStdout(trasformOutput(filenames, mapCountReducedByWord, mapCountReducedByWordAndFilename, '\t'));
        }
    }

    private static void outputToStdout(final String output) {
        System.out.print(output);
    }

    private static void outputToCsv(final String output) throws IOException {
        final Writer bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.csv"), "UTF-8"));
        bufferedWriter.write(output);
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
        formatter.printHelp( "SimpleMapReduce.jar -files <file1,file2,...> [-output (stdout|csv)]", options );
    }

    private static CommandLine parseArguments(final String arguments[], final Options options) throws ParseException {
        final CommandLineParser parser = new GnuParser();
        return parser.parse(options, arguments);
    }

}
