package com.gmail.br45entei.util;

import com.gmail.br45entei.enteisSkyblock.main.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** @author Brian_Entei */
public class ResourceUtil {
	
	/** @param file The file whose size will be returned
	 * @return The size of the file, or -1 if the file could not be read */
	public static final long size(File file) {
		try {
			return Files.size(file.toPath());
		} catch(IOException e) {
			return -1;
		}
	}
	
	/** @param in The InputStream to read from
	 * @param file The file to write to
	 * @return The written file, or <b><code>null</code></b> if an I/O error
	 *         occurred */
	public static final File loadResourceAsFile(InputStream in, File file) {
		if(file.isFile() && size(file) > 0L) {
			return file;
		}
		try(FileOutputStream out = new FileOutputStream(file)) {
			int len;
			byte[] b = new byte[4096];
			while((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch(IOException ignored) {
			return null;
		}
		return file;
	}
	
	/** @param path The path to the internal resource to load
	 * @return The resulting InputStream if the resource exists,
	 *         <b><code>null</code></b> otherwise */
	public static final InputStream loadResource(String path) {
		if(!path.startsWith("/")) {
			path = "/" + path;
			if(!path.startsWith("/assets")) {
				path = "/assets" + path;
			}
		}
		try {
			System.out.println("Loading asset: " + path);
			return getResourceAsStream(path);
		} catch(Throwable e) {
			System.err.print("Failed to load asset: " + path);
			System.err.println(e);
			return null;
		}
	}
	
	/** @param path The path to the internal resource to load
	 * @param fileName The name of the file to load
	 * @return The loaded file, or <b><code>null</code></b> if an I/O error
	 *         occurred */
	public static final File loadResourceAsFile(String path, String fileName) {
		return loadResourceAsFile(path, new File(System.getProperty("user.dir")), fileName);
	}
	
	/** @param path The path to the internal resource to load
	 * @param file The file to write to
	 * @return The loaded file, or <b><code>null</code></b> if an I/O error
	 *         occurred */
	public static final File loadResourceAsFile(String path, File file) {
		return loadResourceAsFile(path, file.getParentFile(), file.getName());
	}
	
	/** @param path The path to the internal resource to load
	 * @param folder The parent folder of the file to be loaded
	 * @param fileName The name of the file to load
	 * @return The loaded file, or <b><code>null</code></b> if an I/O error
	 *         occurred */
	public static final File loadResourceAsFile(String path, File folder, String fileName) {
		File file = new File(folder, fileName);
		folder.mkdirs();
		try(InputStream in = loadResource(path); FileOutputStream out = new FileOutputStream(file)) {
			byte[] buf = new byte[4096];
			int read;
			while((read = in.read(buf)) != -1) {
				out.write(buf, 0, read);
			}
			out.flush();
		} catch(NullPointerException | IOException e) {
			System.out.print("Failed to load asset \"" + path + "\" as file: " + folder.getAbsolutePath() + File.separator + fileName);
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	/** @param file The file to read
	 * @return The file's contents as a string using UTF-8 as the charset, or
	 *         <b><code>null</code></b> if an I/O error occurred */
	public static final String readFileToString(File file) {
		return readFileToString(file, StandardCharsets.UTF_8);
	}
	
	/** @param file The file to read
	 * @param charset The charset to use when converting the file's contents to
	 *            string
	 * @return The file's contents as a string, or <b><code>null</code></b> if
	 *         an I/O error occurred */
	public static final String readFileToString(File file, Charset charset) {
		if(file.isFile()) {
			StringBuilder sb = new StringBuilder();
			try(FileInputStream in = new FileInputStream(file)) {
				byte[] buf = new byte[4096];
				int read;
				while((read = in.read(buf)) != -1) {
					sb.append(new String(buf, 0, read, charset));
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}
		return null;
	}
	
	/** @param file The file to write to
	 * @param string The string to write
	 * @return Whether or not writing the data was successful */
	public static final boolean writeStringToFile(File file, String string) {
		return writeStringToFile(file, string, StandardCharsets.UTF_8);
	}
	
	/** @param file The file to write to
	 * @param string The string to write
	 * @param charset The charset to use when writing string data to file
	 * @return Whether or not writing the data was successful */
	public static final boolean writeStringToFile(File file, String string, Charset charset) {
		try(FileOutputStream out = new FileOutputStream(file)) {
			out.write(string.getBytes(charset));
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/** @param path The package path
	 * @return A list of all resources in the given path */
	public static final List<String> getResourceFiles(String path) {
		path = path.startsWith("/") ? path : (path.startsWith("assets") ? path : "/assets/" + path);
		path = path.startsWith("/") ? path : "/" + path;
		List<String> filenames = new ArrayList<>();
		try(InputStream in = getResourceAsStream(path);//
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;
			while((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		} catch(IOException | NullPointerException e) {
			System.err.print("Failed to access package \"" + path + "\": ");
			e.printStackTrace();
		}
		return filenames;
	}
	
	/** @param path The path to the resource
	 * @return An InputStream containing the resource's contents, or
	 *         <b><code>null</code></b> if the resource does not exist */
	public static final InputStream getResourceAsStream(String path) {
		return Main.getResourceAsStream(path);
	}
	
	/** @param path The path
	 * @return The full path */
	public static final String getResourcePathFromShorthand(String path) {
		return path == null ? null : (path.startsWith("/") ? path : (path.toLowerCase().startsWith("assets/") ? "/" + path : "/assets/" + path));//(path.startsWith("/") ? path : "/assets/" + path);
	}
	
	/** @param path The resource path to check
	 * @return Whether or not the resource exists(true if an input stream is
	 *         successfully opened from the resource, false otherwise) */
	public static final boolean doesResourceExist(String path) {
		path = getResourcePathFromShorthand(path);
		try(InputStream closeMe = ResourceUtil.class.getResourceAsStream(path)) {
			if(closeMe != null) {
				return true;
			}
		} catch(IOException ignored) {
		}
		return false;
	}
	
	/** @return The path to the current Java Code source(may be null if
	 *         unavailable) */
	public static final String getPathToCodeSource() {
		String path = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = null;
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
			if(decodedPath.startsWith("/")) {
				decodedPath = decodedPath.substring(1);
			}
			//if(Main.isDebugModeOn()) {
			//	System.out.println("Path to currently running jar file: \"" + decodedPath + "\"");
			//}
		} catch(UnsupportedEncodingException e) {
		}
		return decodedPath;
	}
	
	/** @return The Java code source in file form. */
	public static final File getCodeSource() {
		String pathToCodeSource = ResourceUtil.getPathToCodeSource();
		return pathToCodeSource != null ? new File(pathToCodeSource) : null;
	}
	
	/** @return {@link Boolean#TRUE} if the Java code source is a file,
	 *         {@link Boolean#FALSE} if it is a directory, or null if it could
	 *         not be determined. */
	public static final Boolean isCodeSourceAFile() {
		File codeSource = ResourceUtil.getCodeSource();
		if(codeSource == null || !codeSource.exists()) {
			return null;
		}
		if(codeSource.isDirectory()) {
			return Boolean.FALSE;
		} else if(codeSource.isFile()) {
			return Boolean.TRUE;
		}
		return null;
	}
	
	/** @param pkg The package to browse
	 * @return An ArrayList containing the names of any files/folders in the
	 *         given package. */
	public static final ArrayList<String> getAllFilesInPackage(Package pkg) {
		return ResourceUtil.getAllFilesInPackage(pkg.getName());
	}
	
	/** @param list The String list of file paths
	 * @param directory The Folder to scan
	 * @return The given list with any files contained within the given folder,
	 *         if any */
	public static final ArrayList<String> scanForAndAddFilePathsFromDirectories(ArrayList<String> list, File directory) {
		return scanForAndAddFilePathsFromDirectories(list, directory, directory);
	}
	
	private static final ArrayList<String> scanForAndAddFilePathsFromDirectories(ArrayList<String> list, File directory, final File rootDir) {
		for(String filePath : directory.list()) {
			File file = new File(directory, filePath);
			if(file.exists()) {
				if(file.isFile()) {
					String pathToAdd = filePath;
					if(!directory.equals(rootDir)) {
						pathToAdd = file.getAbsolutePath().substring(rootDir.getAbsolutePath().length()).replace('\\', '/');
						pathToAdd = pathToAdd.startsWith("/") ? pathToAdd.substring(1) : pathToAdd;
					}
					if(!list.contains(pathToAdd)) {
						list.add(pathToAdd);
					}
				}
				if(file.isDirectory()) {
					scanForAndAddFilePathsFromDirectories(list, file, rootDir);
				}
			}
			//if(Main.isDebugModeOn()) {
			//	System.out.println("Current file path: \"" + file.getAbsolutePath() + "\";");
			//}
		}
		return list;
	}
	
	/** @param list The String list of file paths
	 * @param directory The Folder to scan
	 * @return The given list with any files contained within the given folder,
	 *         if any */
	public static final ArrayList<String> scanForAndAddFolderPathsFromDirectories(ArrayList<String> list, File directory) {
		return scanForAndAddFolderPathsFromDirectories(list, directory, directory);
	}
	
	private static final ArrayList<String> scanForAndAddFolderPathsFromDirectories(ArrayList<String> list, File directory, final File rootDir) {
		for(String filePath : directory.list()) {
			File file = new File(directory, filePath);
			if(file.exists()) {
				if(file.isDirectory()) {
					String pathToAdd = filePath;
					if(!directory.equals(rootDir)) {
						pathToAdd = file.getAbsolutePath().substring(rootDir.getAbsolutePath().length()).replace('\\', '/');
						pathToAdd = pathToAdd.startsWith("/") ? pathToAdd.substring(1) : pathToAdd;
					}
					if(!list.contains(pathToAdd)) {
						list.add(pathToAdd);
					}
					scanForAndAddFolderPathsFromDirectories(list, file, rootDir);
				}
			}
			//if(Main.isDebugModeOn()) {
			//	System.out.println("Current file path: \"" + file.getAbsolutePath() + "\";");
			//}
		}
		return list;
	}
	
	/** @param pkgName The package to use
	 * @return All packages in the given package
	 * @throws NullPointerException Thrown if the pkgName is not valid */
	public static final ArrayList<String> getAllPackagesInPackage(String pkgName) throws NullPointerException {
		final ArrayList<String> list = new ArrayList<>();
		File directory = null;
		String fullPath;
		String relPath = pkgName;//.replace('.', '/');
		URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
		if(resource == null) {
			throw new NullPointerException("No resource for " + relPath);
		}
		fullPath = resource.getFile();
		try {
			directory = new File(resource.toURI());
		} catch(URISyntaxException e) {
			throw new RuntimeException(pkgName + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
		} catch(IllegalArgumentException e) {
			directory = null;
		}
		if((directory != null) && directory.exists()) {
			scanForAndAddFolderPathsFromDirectories(list, directory);
			//String[] files = directory.list();
			//for(String file : files) {
			//	list.add(file);
			
			//}
		} else {
			//String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			URL url = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation();
			String jarPath;
			try {
				jarPath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
			} catch(UnsupportedEncodingException e1) {
				throw new RuntimeException("\"" + StandardCharsets.UTF_8.name() + "\" is an invalid charset?!", e1);
			}
			try(JarFile jarFile = new JarFile(jarPath)) {
				Enumeration<JarEntry> entries = jarFile.entries();
				while(entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if(entryName.startsWith(relPath) && (entryName.length() > (relPath + "/").length())) {
						if(entry.isDirectory()) {
							//if(Main.isDebugModeOn()) {
							//	System.out.println("ClassDiscovery: JarEntry: " + entryName);
							//}
							list.add(entryName.replace(relPath, ""));
						}
					}
				}
			} catch(IOException e) {
				System.err.println("Unable to browse over package \"" + jarPath + "\"(fullPath: \"" + fullPath + "\"): " + CodeUtil.throwableToStr(e));
			}
		}
		ArrayList<String> rtrn = new ArrayList<>();
		for(String fileName : list) {
			rtrn.add(fileName.startsWith("/") ? fileName.substring(1) : fileName);
		}
		return rtrn;
	}
	
	/** @param pkgName The name of the package to browse
	 * @return An ArrayList containing the names of any files/folders in the
	 *         given package.
	 * @throws NullPointerException Thrown if the given package does not exist
	 *             or could not be loaded */
	public static final ArrayList<String> getAllFilesInPackage(String pkgName) throws NullPointerException {
		final ArrayList<String> list = new ArrayList<>();
		File directory = null;
		String fullPath;
		String relPath = pkgName;//.replace('.', '/');
		URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
		if(resource == null) {
			throw new NullPointerException("No resource for " + relPath);
		}
		fullPath = resource.getFile();
		try {
			directory = new File(resource.toURI());
		} catch(URISyntaxException e) {
			throw new RuntimeException(pkgName + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
		} catch(IllegalArgumentException e) {
			directory = null;
		}
		if((directory != null) && directory.exists()) {
			scanForAndAddFilePathsFromDirectories(list, directory);
			//String[] files = directory.list();
			//for(String file : files) {
			//	list.add(file);
			
			//}
		} else {
			//String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
			URL url = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation();
			String jarPath;
			try {
				jarPath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
			} catch(UnsupportedEncodingException e1) {
				throw new RuntimeException("\"" + StandardCharsets.UTF_8.name() + "\" is an invalid charset?!", e1);
			}
			try(JarFile jarFile = new JarFile(jarPath)) {
				Enumeration<JarEntry> entries = jarFile.entries();
				while(entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if(entryName.startsWith(relPath) && (entryName.length() > (relPath + "/").length())) {
						//if(Main.isDebugModeOn()) {
						//	System.out.println("ClassDiscovery: JarEntry: " + entryName);
						//}
						list.add(entryName.replace(relPath, ""));
					}
				}
			} catch(IOException e) {
				System.err.println("Unable to browse over package \"" + jarPath + "\"(fullPath: \"" + fullPath + "\"): " + CodeUtil.throwableToStr(e));
			}
		}
		ArrayList<String> rtrn = new ArrayList<>();
		for(String fileName : list) {
			rtrn.add(fileName.startsWith("/") ? fileName.substring(1) : fileName);
		}
		return rtrn;
	}
	
}
