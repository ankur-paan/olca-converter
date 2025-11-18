package org.openlca.olcatdb.packaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for packaging ILCD folders into ZIP files for easy import into openLCA.
 * 
 * @author OpenLCA Converter Team
 */
public class ZipPackager {

	/**
	 * Creates a ZIP package from a directory.
	 * 
	 * @param sourceDir The directory to package
	 * @param zipFile The output ZIP file
	 * @throws IOException if packaging fails
	 */
	public static void packageDirectory(File sourceDir, File zipFile) throws IOException {
		if (sourceDir == null || !sourceDir.exists() || !sourceDir.isDirectory()) {
			throw new IllegalArgumentException("Source directory must be a valid existing directory");
		}
		
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		try {
			addDirectoryToZip(sourceDir, sourceDir, zos);
		} finally {
			zos.close();
			fos.close();
		}
	}
	
	/**
	 * Recursively adds files from a directory to a ZIP output stream.
	 * 
	 * @param rootDir The root directory (for relative path calculation)
	 * @param sourceDir The current directory being added
	 * @param zos The ZIP output stream
	 * @throws IOException if adding files fails
	 */
	private static void addDirectoryToZip(File rootDir, File sourceDir, ZipOutputStream zos) throws IOException {
		File[] files = sourceDir.listFiles();
		if (files == null) {
			return;
		}
		
		for (File file : files) {
			if (file.isDirectory()) {
				addDirectoryToZip(rootDir, file, zos);
			} else {
				addFileToZip(rootDir, file, zos);
			}
		}
	}
	
	/**
	 * Adds a single file to the ZIP output stream.
	 * 
	 * @param rootDir The root directory (for relative path calculation)
	 * @param file The file to add
	 * @param zos The ZIP output stream
	 * @throws IOException if adding the file fails
	 */
	private static void addFileToZip(File rootDir, File file, ZipOutputStream zos) throws IOException {
		// Calculate relative path
		String relativePath = rootDir.toURI().relativize(file.toURI()).getPath();
		
		ZipEntry zipEntry = new ZipEntry(relativePath);
		zos.putNextEntry(zipEntry);
		
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int length;
		
		try {
			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}
		} finally {
			zos.closeEntry();
			fis.close();
		}
	}
}
