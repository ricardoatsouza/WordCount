package wordcount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wordcount.model.Source;
import wordcount.model.Word;

import java.io.*;
import java.util.Arrays;

public class FileThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger( FileThread.class.getName() );

    private String threadName;
    private String inputFilename;
    private SharedCache sharedCache;

    public FileThread(final String threadName, final String inputFilename, final SharedCache sharedCache) {
        this.threadName = threadName;
        this.inputFilename = inputFilename;
        this.sharedCache = sharedCache;
    }

    @Override
    public void run() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilename), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String words[] = line.trim().toLowerCase().split("[^a-zA-Z']");
                Arrays.stream(words).filter(w -> !w.isEmpty()).forEach( w -> sharedCache.addToMap(new Word(w, new Source(inputFilename, threadName))) );
            }
            bufferedReader.close();
        } catch (final IOException exception) {
            LOGGER.error(String.format("Not possible to read %s: %s", inputFilename, exception.getMessage()));
        }
    }

}
