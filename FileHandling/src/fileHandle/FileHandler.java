package fileHandle;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
//import java.io.FileOutputStream;
import java.util.*;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.text.SimpleDateFormat;

public class FileHandler {
	Path filePath;
	BufferedWriter writer;
	boolean isFileOpen;
	
	FileHandler(String name) {
		String timestamp = new SimpleDateFormat("dd_MM_yy__HH_mm_ss_SSS").format(new Date());
		name = name + "__" + timestamp + ".log";
		
		Path fileDir = FileSystems.getDefault().getPath("logs").toAbsolutePath();
		if(Files.notExists(fileDir)) {
			try {
				Files.createDirectories(fileDir);
			}
			catch(IOException eIO) {
				System.out.println("Could not create directory: " + fileDir);
				eIO.printStackTrace();
			}
		}
		filePath = Paths.get(fileDir + "/" + name);
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath.toString(), true), StandardCharsets.UTF_8));
			isFileOpen = false;
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file: " + filePath);
			e.printStackTrace();
		}
	}
	
	void write(String line) {
		line = line + "\n";
		try {
//			Files.write(filePath, Arrays.asList(line), StandardCharsets.UTF_8);
			writer.write(line);
		} catch (IOException e) {
			System.out.println("Could not write to file: " + filePath);
			e.printStackTrace();
		}
	}
	void close() {
		try {
			writer.close();
			isFileOpen = true;
		} 
		catch (IOException e) {
			System.out.println("Error closing file: " + filePath);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(Paths.get("abc.txt").toAbsolutePath());
//		Path dir = FileSystems.getDefault().getPath("logs").toAbsolutePath();
//		Path name = Paths.get(dir + "/abc.txt");
//		System.out.println(FileSystems.getDefault().getPath("logs").toAbsolutePath());
//		System.out.println(name.toAbsolutePath());
		FileHandler obj = new FileHandler("testFile");
		obj.write("line 1");
		obj.write("line 2");
		obj.write("line 3");
		obj.close();
	}
}
