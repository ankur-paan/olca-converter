package org.openlca.olcatdb.conversion;

import java.io.File;
import org.openlca.olcatdb.packaging.ZipPackager;

/**
 * Wrapper for ES2ToILCDConversion that creates a ZIP package suitable for import into openLCA.
 * This provides an easy way to convert EcoSpold 02 files to ILCD format and package them
 * for direct import into openLCA.
 * 
 * Usage:
 * - Convert EcoSpold 02 file/ZIP to ILCD format
 * - Automatically create ZIP package of output
 * - ZIP package can be directly imported into openLCA
 * 
 * @author OpenLCA Converter Team
 */
public class ES2ToOpenLCAConversion extends ES2ToILCDConversion {
	
	private boolean createZipPackage = true;
	
	/**
	 * Set whether to create a ZIP package after conversion.
	 * Default is true.
	 * 
	 * @param createZip true to create ZIP package, false otherwise
	 */
	public void setCreateZipPackage(boolean createZip) {
		this.createZipPackage = createZip;
	}
	
	@Override
	public void run() {
		// Run the standard ES2 to ILCD conversion
		super.run();
		
		// Create ZIP package if requested
		if (createZipPackage && targetDir != null) {
			try {
				File zipFile = new File(targetDir.getParentFile(), 
					targetDir.getName() + "_openLCA.zip");
				
				logger.info("Creating ZIP package for openLCA import: " + zipFile.getName());
				ZipPackager.packageDirectory(targetDir, zipFile);
				logger.info("ZIP package created successfully: " + zipFile.getAbsolutePath());
				logger.info("This ZIP file can be directly imported into openLCA");
				
			} catch (Exception e) {
				logger.warning("Failed to create ZIP package: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
