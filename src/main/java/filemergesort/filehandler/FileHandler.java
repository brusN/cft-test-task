package filemergesort.filehandler;

import filemergesort.exception.DataTransformErrorException;
import filemergesort.exception.FileNotSortedException;
import filemergesort.exception.IllegalFileDataStructException;
import filemergesort.stringtransformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Comparator;

public class FileHandler <T> {
    private final static Logger logger = LoggerFactory.getLogger(FileHandler.class);
    private boolean reachedEOF = false;
    private long curLineNumber = 0;
    private final String fileName;
    private final BufferedReader inputStream;
    private final Transformer<String, T> dataTransformer;
    private final Comparator<T> comparator;
    private T prevElem = null;
    private T curElem;

    public String getFileName() {
        return fileName;
    }

    public T getPrevElem() {
        return prevElem;
    }

    public T getCurElem() {
        return curElem;
    }

    public boolean isReachedEOF() {
        return reachedEOF;
    }

    public long getCurLineNumber() {
        return curLineNumber;
    }

    public FileHandler(String fileName, Transformer<String, T> dataTransformer, Comparator<T> comparator) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        this.fileName = fileName;
        this.dataTransformer = dataTransformer;
        this.comparator = comparator;
        this.curElem = getNextElemFromFile();
        while (this.curElem == null) {
            if (reachedEOF) {
                break;
            }
            this.curElem = getNextElemFromFile();
        }
    }

    private T getNextElemFromFile() throws IOException {
        if (reachedEOF) {
            return null;
        }

        String nextLine = inputStream.readLine();
        ++curLineNumber;

        // If reached EOF
        if (nextLine == null) {
            inputStream.close();
            prevElem = curElem;
            curElem = null;
            reachedEOF = true;
            return null;
        }

        // If line is empty
        if (nextLine.isEmpty()) {
            logger.warn("Found empty line in file " + fileName + " on line " + curLineNumber + ". Line has skipped...");
            return null;
        }

        // If line has spaces
        if (nextLine.contains(" ")) {
            logger.warn("Found spaces in file " + fileName + " on line " + curLineNumber + ". Data element has skipped...");
            return null;
        }

        T value = null;
        try {
            value = dataTransformer.transform(nextLine);
        } catch (DataTransformErrorException e) {
            logger.error(e.getMessage() + " in file " + fileName + " on line " + curLineNumber);
            return null;
        }

        return value;
    }

    public T nextElem() throws IOException, FileNotSortedException, IllegalFileDataStructException {
        T nextElem = getNextElemFromFile();
        while (nextElem == null) {
            if (reachedEOF) {
                return null;
            }
            nextElem = getNextElemFromFile();
        }

        // Comparator defines order of elements. Comparator may has ASCENDED and DESCENDED compare mode
        if (comparator.compare(curElem, nextElem) < 0) {
            throw new FileNotSortedException("File " + fileName + " not sorted on line " + curLineNumber + ". Data element has skipped...");
        }

        prevElem = curElem;
        curElem = nextElem;
        return curElem;
    }
}