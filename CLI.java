import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.*;
import static java.nio.file.LinkOption.*;

public class CLI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	private static File currentPath;
	private static String userName = "mohammad"; // To be set on the program
													// start

	private static void cd() {
		cd("/home/" + userName);
	}

	private static boolean relativePath(String path) {
		return path.length() < 5 || !path.substring(0, 5).equals("/home");
	}

	private static String normalizePath(String path) {
		if (path.charAt(0) != '/')
			path = "/" + path;
		if (relativePath(path))
			path = currentPath.getPath() + path;
		return path;
	}

	private static void cd(String path) {
		path = normalizePath(path);
		currentPath = new File(path);
	}

	private static void ls() {
		File[] listOfFilesAndFolders = currentPath.listFiles();
		for (int i = 0; i < listOfFilesAndFolders.length; ++i)
			if (listOfFilesAndFolders[i].isFile())
				System.out.println(ANSI_PURPLE + listOfFilesAndFolders[i].getName() + ANSI_RESET);
			else
				System.out.println(ANSI_CYAN + listOfFilesAndFolders[i].getName() + ANSI_RESET);
	}

	private static void cp(String[] sources, String dest) throws IOException {

		dest = normalizePath(dest);
		for (int i = 0; i < sources.length; ++i) {
			cp(sources[i], dest);
		}
	}

	private static void cp(String sourcePath, String dest) throws IOException {
		dest = normalizePath(dest);
		sourcePath = normalizePath(sourcePath);
		File sourceFile = new File(sourcePath);

		String tmpDest = dest + "/" + sourceFile.getName();
		File destination = new File(tmpDest);

		if (!sourceFile.isDirectory() || !destination.exists())
			Files.copy(sourceFile.toPath(), destination.toPath(), REPLACE_EXISTING, NOFOLLOW_LINKS);
		if (sourceFile.isDirectory()) {
			String[] listOfFiles = FilesToStrings(sourceFile.listFiles());
			cp(listOfFiles, dest + "/" + sourceFile.getName());
		}
	}

	private static String[] FilesToStrings(File[] files) {
		String[] strings = new String[files.length];
		for (int i = 0; i < files.length; ++i)
			strings[i] = files[i].getPath();
		return strings;
	}

	private static void mv(String[] sources, String dest) throws IOException {
		cp(sources, dest);
		for (int i = 0; i < sources.length; i++) {
			rm(sources[i]);
		}
	}

	private static void rm(File targetFile) throws IOException {
		if (targetFile.isDirectory()) {
			File[] listOfFiles = targetFile.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				rm(listOfFiles[i]);
			}
		}

		Files.delete(targetFile.toPath());
	}

	private static void rm(String target) throws IOException {
		target = normalizePath(target);
		File targetFile = new File(target);
		rm(targetFile);
	}

	private static void mkdir(String dir) {
		dir = normalizePath(dir);
		new File(dir).mkdirs();
	}

	private static void cat(String[] targets) throws IOException {
		for (int i = 0; i < targets.length; ++i) {
			targets[i] = normalizePath(targets[i]);
			BufferedReader reader = new BufferedReader(new FileReader(targets[i]));
			String line;
			while ((line = reader.readLine()) != null)
				System.out.println(line);
		}
	}

	private static void rmdir(String target) throws IOException {
		rm(target);
	}

	private static void pwd() {
		System.out.println(currentPath.getAbsolutePath());
	}

	private static void more(String target) throws IOException {
		target = normalizePath(target);
		BufferedReader reader = new BufferedReader(new FileReader(target));
		String line = null, inputLine;
		final int numberOfLines = 32;

		Scanner input = new Scanner(System.in);

		do {
			for (int i = 0; i < numberOfLines; ++i) {
				line = reader.readLine();
				if (line == null)
					System.out.println();
				else
					System.out.println(line);
			}

			inputLine = input.nextLine();

		} while (line != null && (inputLine.length() == 0));
		input.close();
		reader.close();
	}

	private static void less(String target) throws IOException {
		target = normalizePath(target);
		BufferedReader reader = new BufferedReader(new FileReader(target));
		String line = null, inputLine;
		final int numberOfLines = 32;

		Scanner input = new Scanner(System.in);

		do {
			for (int i = 0; i < numberOfLines; ++i) {
				line = reader.readLine();
				if (line == null)
					System.out.println();
				else
					System.out.println(line);
			}

			inputLine = input.nextLine();
			System.out.println(inputLine.length());
		} while (line != null && (inputLine.length() == 0));
		input.close();
		reader.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		

	}

}
