package threadFixedTotalJob;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		Path logPath = Paths.get(logDir + "/" + logName);
		return logPath;
	}
	private void create() {
		logPath = getPath().toString();
		try {
			FileOutputStream outFile = new FileOutputStream(logPath, true);
			writer = new BufferedWriter(new OutputStreamWriter(outFile, StandardCharsets.UTF_8));
		}
		catch(FileNotFoundException eFNF) {
			System.out.println("Could not open log file: " + logPath);
		}
	}
	private void delete() {
		try {
			writer.close();
		}
		catch(IOException eIO) {
			System.out.println("Error closing file" + logPath);
		}
	}
	
	
	// Public members
	public void close() {
		if(writer != null)
			delete();
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
}
