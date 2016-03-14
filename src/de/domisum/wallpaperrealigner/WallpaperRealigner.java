package de.domisum.wallpaperrealigner;

import java.io.File;

import de.domisum.wallpaperrealigner.file.FileSelector;
import de.domisum.wallpaperrealigner.realigner.WallpaperFileRealigner;

public class WallpaperRealigner
{
	
	// -------
	// CONSTRUCTOR
	// -------
	public WallpaperRealigner(String[] args)
	{
		if(args.length <= 1)
		{
			System.err.println("Invalid arguments: Use the file or directory paths of the files to be formatted followed by the target directory");
			return;
		}
		
		String outputDirName = args[args.length - 1];
		
		for(int i = 0; i < args.length - 1; i++)
		{
			String arg = args[i];
			
			System.out.println("Realigning '" + arg + "' ...");
			File file = new File(arg).getAbsoluteFile();
			if(!file.isDirectory())
			{
				System.out.println("The specified path '" + arg + "' is not a folder");
				continue;
			}
			
			File outputDir = new File(file.getParentFile(), outputDirName);
			deleteDirectory(outputDir);
			
			formatDirectory(file, outputDir);
			
			System.out.println("Realigning '" + arg + "' done \n\n\n");
		}
		
		System.out.println("Done");
	}
	
	public static void main(String[] args)
	{
		new WallpaperRealigner(args);
	}
	
	
	// -------
	// FORMATTING
	// -------
	private void formatDirectory(File file, File outputDir)
	{
		FileSelector fileSelector = new FileSelector(file);
		
		for(File f : fileSelector.getFiles())
			new WallpaperFileRealigner(f, file, outputDir);
	}
	
	
	// -------
	// UTIL
	// -------
	public static void deleteDirectory(File dir)
	{
		if(!dir.exists())
			return;
		
		File[] files = dir.listFiles();
		for(File f : files)
		{
			if(f.isFile())
				f.delete();
			else
				deleteDirectory(f);
		}
		
		dir.delete();
	}
	
}
