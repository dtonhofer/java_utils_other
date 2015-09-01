package name.heavycarbon.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static name.heavycarbon.checks.BasicChecks.*;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Copyright (c) 2013, q-leap S.A.
 *                     14, rue Aldringen
 *                     L-1118 LUXEMBOURG
 *
 * Released by M-PLIFY S.A. under the MIT License
 * All modifications since still under the MIT License   
 *******************************************************************************
 *******************************************************************************
 * Functions that help in processing resources
 *
 * In particular, some copy from a "reader" into a "string" until EOF is reached.
 * 
 * In Groovy, one could write:
 * 
 *    String fileContents = new File('/path/to/file').getText('UTF-8')
 * 
 * But see also:
 * 
 *    http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
 *
 * 2013.01.XX - Released by M-PLIFY S.A. under the MIT License 
 * 2013.08.XX - First version in Groovy
 * 2014.01.22 - Cleaned up extensively and also generalized
 * 2014.02.02 - Namespace changed from "com.mplify.checkers" to 
 *              "com.example" for some neutrality. 
 ******************************************************************************/

class ResourceHelpGroovy {

	private final static CLASS = ResourceHelpGroovy.class.name
	private final static Logger LOGGER_getStreamFromResource = LoggerFactory.getLogger("${CLASS}.getStreamFromResource")
	private final static Logger LOGGER_getStreamFromFile = LoggerFactory.getLogger("${CLASS}.getStreamFromFile")
	private final static Logger LOGGER_slurpThenCloseReader = LoggerFactory.getLogger("${CLASS}.slurpThenCloseReader")
	private final static Logger LOGGER_slurpIntoListOfLinesThenCloseReader = LoggerFactory.getLogger("${CLASS}.slurpIntoListOfLinesThenCloseReader")

	/**
	 * This qualifies the resource name by the package of the Class "clazz".
	 * If "clazz" is null, default package is assumed.
	 * If the "nonQualifiedResourceNameIn" is null, null will be returned.
	 */

	static String fullyQualifyResourceName(Class clazz, String nonQualifiedResourceName) {
		return fullyQualifyResourceName((Package)(clazz?.getPackage()), nonQualifiedResourceName)
	}

	/**
	 * This qualifies the resource name by the package "packijj".
	 * If "packijj" is null, default package is assumed.
	 * If the "nonQualifiedResourceNameIn" is null, null will be returned.
	 */

	static String fullyQualifyResourceName(Package packijj, String nonQualifiedResourceName) {
		return fullyQualifyResourceName((String)(packijj?.name), nonQualifiedResourceName)
	}

	/**
	 * This qualifies the resource name by the "packijjName", whereby "." is replaced by "/".
	 * If "packijjName" is null, default package is assumed.
	 * If the "nonQualifiedResourceName" is null, null will be returned.
	 */

	static String fullyQualifyResourceName(String packijjName, String nonQualifiedResourceName) {
		if (nonQualifiedResourceName == null) {
			return null;
		}
		String res = nonQualifiedResourceName.trim()
		checkNotNullAndNotEmpty(res,"The passed 'non-qualified resource name' is empty")
		checkTrue(res.indexOf('/') < 0, "The passed 'non-qualified resource name' contains a '/': {}", res);
		if (packijjName == null) {
			return res
		}
		else {
			return packijjName.replace('.', '/') + "/" + res
		}
	}

	/**
	 * Get a binary input stream from a resource. Never returns null.
	 */

	static InputStream getStreamFromResource(String fullyQualifiedResourceName) {
		def logger = LOGGER_getStreamFromResource;
		checkNotNullAndNotOnlyWhitespace(fullyQualifiedResourceName, "fully-qualified resource name")
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader()
		//
		// the following call returns (null) if resource not found (instead of throwing)
		//
		InputStream is = classLoader.getResourceAsStream(fullyQualifiedResourceName.trim())
		checkNotNull(is, "Could not find (fully qualified) resource named '{}'", fullyQualifiedResourceName)
		//
		// If we are here, loading the resource was successful!
		//
		if (logger.isInfoEnabled()) {
			URL url = classLoader.getResource(fullyQualifiedResourceName)
			logger.info("Resource '${fullyQualifiedResourceName}' accessed as '${url}'")
		}
		return is;
	}

	/**
	 * Get a binary input stream from a file. Never returns null.
	 */

	static InputStream getStreamFromFile(File fileName) {
		def logger = LOGGER_getStreamFromFile
		checkNotNull(fileName, "file name")
		checkTrue(fileName.exists(), "The file '{}' does not exist", fileName)
		checkTrue(fileName.isFile(), "The file '{}' is not a 'normal' file", fileName)
		checkTrue(fileName.canRead(), "The file '{}' is not readable", fileName)
		return new FileInputStream(fileName)
	}

	/**
	 * Get a reader yielding characters from a file. Never returns null.
	 */

	static Reader getReader(FileInfo fi) {
		InputStream is = getStreamFromFile(fi.fileName)
		return makeBufferedReader(is, fi.encoding, fi.bufferedReaderSize)
	}

	/**
	 * Get a reader yielding characters from a resource. Never returns null.
	 */

	static Reader getReader(ResourceInfo ri) {
		InputStream is = getStreamFromResource(ri.fullyQualifiedResourceName)
		return makeBufferedReader(is, ri.encoding, ri.bufferedReaderSize)
	}

	/**
	 * Read a String from a Resource given as a fully qualified resource name.
	 */

	static String slurpText(ResourceInfo ri) {
		return slurpThenCloseReader(getReader(ri))
	}

	/**
	 * Read a String from a File. 
	 */

	static String slurpText(FileInfo fi) {
		return slurpThenCloseReader(getReader(fi))
	}

	/**
	 * Slurping the data; returns the lines
	 */

	static List<String> slurpIntoListOfLines(ResourceInfo ri) {
		return slurpIntoListOfLinesThenCloseReader(new LineNumberReader(getReader(ri)))
	}

	/**
	 * Slurping the data; returns the lines
	 */

	static List<String> slurpIntoListOfLines(FileInfo fi) {
		return slurpIntoListOfLinesThenCloseReader(new LineNumberReader(getReader(fi)))
	}

	/**
	 * Helper. Only creates a buffered reader if the passed size is > 0
	 */

	private static Reader makeBufferedReader(InputStream is, String encoding, int bufferedReaderSize) {
		checkNotNull(is,"input stream")
		checkNotNullAndNotOnlyWhitespace(encoding, "encoding")
		// http://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
		Reader res = new InputStreamReader(is, encoding)
		if (bufferedReaderSize > 0) {
			res = new BufferedReader(res,bufferedReaderSize)
		}
		return res
	}

	/**
	 * Helper
	 */

	private static String slurpThenCloseReader(Reader r) {
		Logger logger = LOGGER_slurpThenCloseReader
		try {
			return r.text // Groovy feature to slurp the reader
		} finally {
			try {
				r.close();
			} catch (Exception ignore) {
				logger.warn("While closing reader on top of input stream -- loftily ignoring this!", ignore);
			}
		}
	}

	/**
	 * Helper
	 */

	private static List<String> slurpIntoListOfLinesThenCloseReader(LineNumberReader r) {
		Logger logger = LOGGER_slurpIntoListOfLinesThenCloseReader
		List<String> res = new LinkedList()
		try {
			String line
			while ((line = r.readLine())!=null) {
				res << line
			}
			return res
		} finally {
			try {
				r.close()
			} catch (Exception ignore) {
				logger.warn("While closing line number reader on top of input stream -- loftily ignoring this!", ignore)
			}
		}
	}
}
