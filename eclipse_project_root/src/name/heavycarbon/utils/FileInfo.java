package name.heavycarbon.utils;

import java.io.File;
import static name.heavycarbon.checks.BasicChecks.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * A class used to describe where to find and how to read a text file
 * 
 * 2014.01.22 - Created
 ******************************************************************************/

class FileInfo {

    public final static int DEFAULT_BUFFERED_READER_SIZE = 1024;

    public final File fileName;
    public final String encoding;
    public final int bufferedReaderSize;

    public FileInfo(String fileName, String encoding) {
        this(new File(fileName), encoding, DEFAULT_BUFFERED_READER_SIZE);
    }

    public FileInfo(String fileName, String encoding, int bufferedReaderSize) {
        this(new File(fileName), encoding, bufferedReaderSize);
    }

    public FileInfo(File fileName, String encoding) {
        this(fileName, encoding, DEFAULT_BUFFERED_READER_SIZE);
    }

    public FileInfo(File fileName, String encoding, int bufferedReaderSize) {
        checkNotNull(fileName, "fileName");
        checkNotNullAndNotOnlyWhitespace(encoding, "encoding");
        checkLargerOrEqualToZero(bufferedReaderSize, "bufferedReaderSize");
        this.fileName = fileName;
        this.encoding = encoding;
        this.bufferedReaderSize = bufferedReaderSize;
    }
}