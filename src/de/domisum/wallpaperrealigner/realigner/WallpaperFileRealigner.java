package de.domisum.wallpaperrealigner.realigner;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WallpaperFileRealigner
{
	
	// PROPERTIES
	private File file;
	private File baseDir;
	private File outputDir;
	
	// STATUS
	private BufferedImage image;
	private String error = null;
	
	
	// -------
	// CONSTRUCTOR
	// -------
	public WallpaperFileRealigner(File file, File baseDir, File outputDir)
	{
		this.file = file;
		this.baseDir = baseDir;
		this.outputDir = outputDir;
		
		System.out.println("Realigning file '" + file.getAbsolutePath() + "'...");
		
		loadFile();
		realignFile();
		saveFile();
	}
	
	
	// -------
	// FORMATTING
	// -------
	public void loadFile()
	{
		try
		{
			image = ImageIO.read(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		if(image == null)
			error = "The image file could not be loaded";
	}
	
	private void realignFile()
	{
		int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		
		int[] pixelsNew = new int[pixels.length];
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++)
			{
				int pixel = pixels[y * image.getWidth() + x];
				
				int nX = x - 1920;
				if(nX < 0)
					nX += image.getWidth();
				
				pixelsNew[y * image.getWidth() + nX] = pixel;
			}
		
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) bi.getData();
		raster.setDataElements(0, 0, image.getWidth(), image.getHeight(), pixelsNew);
		bi.setData(raster);
		
		image = bi;
	}
	
	private void saveFile()
	{
		if(error != null)
		{
			System.out.println("An error occured while processing this file: " + error);
			return;
		}
		
		File targetFile = new File(outputDir, file.getAbsoluteFile().getAbsolutePath().replace(baseDir.getAbsoluteFile().getAbsolutePath(), ""));
		targetFile.getParentFile().mkdirs();
		
		try
		{
			ImageIO.write(image, "png", targetFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
