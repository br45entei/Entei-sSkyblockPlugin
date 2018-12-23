package com.gmail.br45entei.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileDeleteStrategy;

/** @author br45e */
@SuppressWarnings("javadoc")
public class SkyblockServer {
	
	public static final void main(String[] args) {
		File wrkDir = new File(System.getProperty("user.dir"));
        ProcessBuilder pb = new ProcessBuilder("java.exe", "-Xms1024m", "-Xmx2048m", "-DIReallyKnowWhatIAmDoingISwear", "-jar", wrkDir.getAbsolutePath() + File.separator + "spigot-1.12.2.jar");
        pb.inheritIO();
        wrkDir.mkdirs();
        pb.directory(wrkDir);
        File plugins = new File(wrkDir, "plugins");
        plugins.mkdirs();
        File exportedSkyblock = new File(wrkDir, File.separator + "plugins" + File.separator + "Entei's Skyblock.jar");
        if(!exportedSkyblock.isFile()) {
        	System.err.println(exportedSkyblock.getAbsolutePath());
            System.err.print("Failed to locate built skyblock plugin!");
            System.exit(-1);
            return;
        }
		File dest = new File(plugins, "Entei's Skyblock.jar");
		boolean deleteDest = false;
		for(String arg : args) {
			if(arg.equalsIgnoreCase("deleteExistingPlugin")) {
				deleteDest = true;
				break;
			}
		}
		if(deleteDest) {
			FileDeleteStrategy.FORCE.deleteQuietly(dest);
		}
		if(!copy(exportedSkyblock, dest) || !dest.isFile()) {
			System.err.println("Failed to copy built skyblock plugin to server plugins folder!");
			System.exit(-1);
			return;
		}
		if(!copy(new File("E:\\Java\\Minecraft\\Plugins\\Bukkit\\1.12.2\\worldedit-bukkit-6.1.7.3.jar"), new File(plugins, "worldedit-bukkit-6.1.7.3.jar"))) {
			System.err.println("Failed to copy worldedit plugin to server plugins folder!");
			System.exit(-1);
			return;
		}
		if(!copy(new File("E:\\Java\\Minecraft\\Plugins\\Bukkit\\1.12.2\\worldguard-6.2.1.jar"), new File(plugins, "worldguard-6.2.1.jar"))) {
			System.err.println("Failed to copy worldguard plugin to server plugins folder!");
			System.exit(-1);
			return;
		}
		Process p;
		try {
			p = pb.start();
		} catch(IOException e) {
			System.err.print("Failed to start test skyblock server: ");
			e.printStackTrace(System.err);
			System.exit(-1);
			return;
		}
		while(p.isAlive()) {
			try {
				Thread.sleep(10L);
			} catch(InterruptedException ignored) {
			}
		}
		System.out.println("Exiting with status: " + p.exitValue());
		System.out.flush();
		System.exit(p.exitValue());
	}
	
	public static final boolean copy(File source, File dest) {
		try {
			if(!dest.isFile() || Files.size(dest.toPath()) == 0) {
				try(FileInputStream in = new FileInputStream(source); FileOutputStream out = new FileOutputStream(dest)) {
					byte[] buf = new byte[4096];
					int read;
					while((read = in.read(buf)) != -1) {
						out.write(buf, 0, read);
					}
					out.flush();
				} catch(IOException e) {
					System.err.print("Failed to copy file: ");
					e.printStackTrace(System.err);
					System.exit(-1);
					return false;
				}
			}
			return true;
		} catch(IOException e) {
			System.err.print("Failed to read destination file attributes: ");
			e.printStackTrace(System.err);
			System.exit(-1);
		}
		return false;
	}
	
}
