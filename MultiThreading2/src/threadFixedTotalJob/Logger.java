package threadFixedTotalJob;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Logger {
	// Static members
	private static Logger logger = new Logger();
	
	public static Logger getInstance() {
		logger.makeArchive();
		logger.create();
		return logger;
	}
	public static Logger getInstance(String name) {
		logger.logName = name;
		logger.makeArchive();
		logger.create();
		return logger;
	}
	

	// Private members
	private String logName;
	private String logPath;
	private BufferedWriter writer;
	
	private Logger() {
		logName = "pc";
		logPath = "";
		writer = null;
	}
	
	private void create() {
		logPath = getPath().toString();
		try {
			FileOutputStream outFile = new FileOutputStream(logPath);
			writer = new BufferedWriter(new OutputStreamWriter(outFile, StandardCharsets.UTF_8));
		}
		catch(FileNotFoundException eFNF) {
			System.out.println("Could not open log file: " + logPath);
		}
	}
	private Path getPath() {
		Path logDir = getDir();
		if(logDir != null) {
			makeBackup(logDir);
			return Paths.get(logDir + "/" + logName + ".log");
		}
		else
			return null;
	}
	private Path getDir() {
		Path logDir = FileSystems.getDefault().getPath("logs").toAbsolutePath();
		if(Files.notExists(logDir)) {
			try {
				Files.createDirectories(logDir);
			}
			catch(IOException eIO) {
				logDir = null;
				System.out.println("Could not create directory: " + logDir);
				eIO.printStackTrace();
			}
		}
		return logDir;
	}
	private void makeBackup(Path dir) {
		String logPrefix = dir.toString() + "/" + logName;
		
		// Backup older file if it exists
		if(Files.exists(Paths.get(logPrefix + ".log"))) {
			// Count number of files with same prefix
			File _dir = new File(dir.toString());
			String[] listOfFiles = _dir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(logName) && name.endsWith(".log");
				}
			});
			
			// Backup file by appending new count with file name 
			Path source = Paths.get(logPrefix + ".log");
			Path destination = Paths.get(logPrefix + "-" + listOfFiles.length + ".log");
			try {
				Files.copy(source, destination);
			}
			catch(IOException e) {
				System.out.println("Could not create backup of file: " + logPrefix + ".log");
			}
		}
	}
	private void makeArchive() {
		Path logDir = getDir();
		if(logDir != null) {
			// Check if there are unzipped log files and zip them into a new archive
			String prefix = logDir.toString() + "/";
			String zipName =  getZipName(logDir);
			String[] unzippedLogs = getUnzippedLogs(logDir);
			if(unzippedLogs.length > 0) {
				try {
					ZipOutputStream zipper = new ZipOutputStream(new FileOutputStream(prefix + zipName));
					for(String name : unzippedLogs) {
						addToZip(zipper, prefix, name, zipName);
						// Delete file after adding to zip
						Files.delete(Paths.get(prefix + name));
					}
					zipper.close();
				}
				catch(FileNotFoundException eFNF) {
					eFNF.printStackTrace();
				}
				catch(IOException eIO) {
					eIO.printStackTrace();
				}
			}
		}
	}
	private String getZipName(Path dir) {
		File _dir = new File(dir.toString());
		String[] zippedFiles = _dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(logName) && name.endsWith(".zip");
			}
		});
		int zipCount = zippedFiles.length;
		return (logName + "-" + (zipCount + 1) + ".zip");
	}
	private String[] getUnzippedLogs(Path dir) {
		File _dir = new File(dir.toString());
		String[] unzippedFiles = _dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(logName) && name.endsWith(".log");
			}
		});
		return unzippedFiles;
	}
	private void addToZip(ZipOutputStream zipper, String prefix, String fileName, String zipName) throws FileNotFoundException, IOException {
		zipName = zipName.replace('.', '_');
		FileInputStream reader = new FileInputStream(new File(prefix + fileName));
		zipper.putNextEntry(new ZipEntry(zipName + "/" + fileName));
		
		// Read file and write into zip
		byte[] bytes = new byte[1024];
		int len;
		while((len = reader.read(bytes)) >= 0)
			zipper.write(bytes, 0, len);
		
		zipper.closeEntry();
		reader.close();
	}
	
	private boolean delete() {
		try {
			writer.close();
			return true;
		}
		catch(IOException eIO) {
			System.out.println("Error closing file" + logPath);
			return false;
		}
	}
	
	
	// Public members
	public boolean close() {
		if(writer != null)
			return delete();
		else
			return true;
	}
	public synchronized void write(String line) {
		line = line + "\n";
		try {
			writer.write(line);
		}
		catch(IOException eIO) {
			System.out.println("Could not write to file: " + logPath);
		}
	}
	public void reset() {
		if(writer != null) {
			if(delete())
				create();
		}
		else
			create();
	}
}
