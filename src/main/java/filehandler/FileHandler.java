package filehandler;

import filemergesort.exception.FileNotSortedException;
import filemergesort.exception.IllegalFileDataStructException;
import stringtransformer.Transformer;

import java.io.*;
import java.util.Comparator;

public class FileHandler <T> {
    private long rowNumber = 1;
    private final String fileName;
    private final BufferedReader inputStream;
    private final Transformer<String, T> dataTransformer;
    private final Comparator<T> comparator;
    private T prevElem = null;
    private T curElem = null;

    public FileHandler(String fileName, Transformer<String, T> dataTransformer, Comparator<T> comparator) throws IOException, IllegalFileDataStructException {
        this.inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        this.dataTransformer = dataTransformer;
        this.comparator = comparator;
        this.fileName = fileName;

        String nextLine = inputStream.readLine();
        ++rowNumber;
        if (nextLine.contains(" ")) {
            inputStream.close();
            throw new IllegalFileDataStructException("Found spaces in file! File > " + fileName + " | Row: " + rowNumber);
        }
        curElem = dataTransformer.transform(nextLine);
    }

    public String getFileName() {
        return fileName;
    }

    public T getPrevElem() {
        return prevElem;
    }

    public T getCurElem() {
        return curElem;
    }

    public T readNextElem() throws IOException, FileNotSortedException, IllegalFileDataStructException {
        prevElem = curElem;
        String nextLine = inputStream.readLine();
        if (nextLine == null) {
            inputStream.close();
            curElem = null;
            return null;
        }
        ++rowNumber;
        if (nextLine.contains(" ")) {
            inputStream.close();
            throw new IllegalFileDataStructException("Found spaces in file! File > " + fileName + " | Row: " + rowNumber);
        }
        T nextElem = dataTransformer.transform(nextLine);
        if (comparator.compare(curElem, nextElem) < 0) {
            inputStream.close();
            throw new FileNotSortedException("File with name " + fileName + " not sorted!");
        }
        curElem = nextElem;
        return curElem;
    }
}