/**
 * -----------------------------------------------------------------------
 *     Copyright (C) 2012 LM Ericsson Limited.  All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.ericsson.eniq.events.server.test.stubs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import com.ericsson.eniq.events.server.utils.file.FileOperations;

/**
 * extension of the production class FileOperations
 * when running tests from maven/CI, the flat files are bundled in one of the module jars, so this class
 * provides functionality to read these jar files and find specified files there
 *  
 * @author eemecoy
 *
 */
public class StubbedFileOperations extends FileOperations {

    private static final String FILE_PREFIX = "file:";

    /* (non-Javadoc)
     * @see com.ericsson.eniq.events.server.utils.file.FileHelper#getFilesInPath(java.lang.String)
     */
    @Override
    protected String[] getFilesInPath(final String pathToFiles) {
        if (isJarFile(pathToFiles)) {
            try {
                return getFilesInJar(pathToFiles);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
        return getFilesInFolder(pathToFiles);
    }

    private String[] getFilesInJar(final String jarFile) throws IOException {
        final List<String> filesInJar = new ArrayList<String>();
        JarInputStream jarIn = null;
        try {
            jarIn = getJarInputStream(jarFile);
        } catch (final FileNotFoundException e) {
            jarIn = getJarInputStream(correctJarFileName(jarFile));
        }
        ZipEntry entry = jarIn.getNextJarEntry();
        while (entry != null) {
            filesInJar.add(entry.getName());
            entry = jarIn.getNextJarEntry();
        }
        return filesInJar.toArray(new String[] {});
    }

    private JarInputStream getJarInputStream(final String jarFile) throws IOException, FileNotFoundException {
        return new JarInputStream(new FileInputStream(jarFile));
    }

    private boolean isJarFile(final String pathToFiles) {
        return pathToFiles.endsWith(".jar!");
    }

    private String correctJarFileName(final String jarFile) {
        if (jarFile.startsWith(FILE_PREFIX)) {
            final int indexOfFilePrefix = jarFile.indexOf(FILE_PREFIX) + FILE_PREFIX.length();
            return jarFile.substring(indexOfFilePrefix, jarFile.length() - 1);
        }
        return jarFile;
    }

}
