package simplemapreduce.tmp.wordcount.model;

public class Source {
    public String filename;
    public String processedByThread;
    public String creationDatetime;

    public Source(String filename, String processedByThread, String creationDatetime) {
        this.filename = filename;
        this.processedByThread = processedByThread;
        this.creationDatetime = creationDatetime;
    }
}
