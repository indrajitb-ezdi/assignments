package threadFixedTotalJob;

import java.io.BufferedWriter;
import java.io.File;
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

public class Logger {
	// Static members
	private static Logger logger = new Logger();
	
	public static Logger getInstance() {
		logger.create();
		return logger;
	}
	public static Logger getInstance(String name) {
		logger.logName = name;
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
	
	private void makeBackup(Path dir) {
		String logPrefix = dir.toString() + "/" + logName;
		
		// Backup older file if it exists
		if(Files.exists(Paths.get(logPrefix + ".log"))) {
			// Count number of files with same prefix
			File _dir = new File(dir.toString());
			String[] listOfFiles = _dir.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(logName);
				}
			});
			int fileCount = listOfFiles.length;
			
			// Backup file by appending new count with file name 
			Path source = Paths.get(logPrefix + ".log");
			Path destination = Paths.get(logPrefix + "-" + fileCount + ".log");
			try {
				Files.copy(source, destination);
			}
			catch(IOException e) {
				System.out.println("Could not create backup of file: " + logPrefix + ".log");
			}
		}
	}
	private Path getPath() {
		Path logDir = FileSystems.getDefault().getPath("logs").toAbsolutePath();
		if(Files.notExists(logDir)) {
			try {
				Files.createDirectories(logDir);
			}
			catch(IOException eIO) {
				System.out.println("Could not create directory: " + logDir);
				eIO.printStackTrace();
			}
		}
		makeBackup(logDir);
		Path logPath = Paths.get(logDir + "/" + logName + ".log");
		return logPath;
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
