package WT_OODP.exception;

/**
 * Thrown when a file read or write operation fails in the data layer.
 * Extends RuntimeException so callers are not forced to handle it at
 * every call-site – the error is caught and reported at the top level.
 */
public class DataFileException extends RuntimeException {

    private final String filePath;

    public DataFileException(String filePath, String message, Throwable cause) {
        super("File operation failed [" + filePath + "]: " + message, cause);
        this.filePath = filePath;
    }

    public DataFileException(String filePath, String message) {
        super("File operation failed [" + filePath + "]: " + message);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
