package simplemapreduce.tmp.wordcount;

public class FileThread implements Runnable {

    private String threadName;
    private String inputFilename;

    public static FileThread createFileThread(String threadName, String inputFilename) {
        return new FileThread(threadName, inputFilename);
    }

    public FileThread(String threadName, String inputFilename) {
        this.threadName = threadName;
        this.inputFilename = inputFilename;
    }

    @Override
    public void run() {

    }

}
