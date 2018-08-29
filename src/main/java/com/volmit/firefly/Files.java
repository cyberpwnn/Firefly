package com.volmit.firefly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

public class Files
{
	public String path()
	{
		return new File(".").getAbsolutePath();
	}

	public boolean has(String path)
	{
		return new File(path).exists();
	}

	public void mkdirs(String path)
	{
		new File(path).mkdirs();
	}

	public void mkdir(String path)
	{
		new File(path).mkdirs();
	}

	public void printfile(String file, String data)
	{
		printfile(file, data, false);
	}

	public void printfile(String file, String data, boolean append)
	{
		File f = new File(file);

		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(f, append));
			pw.println(data);
			pw.close();
		}

		catch(Exception e)
		{

		}
	}

	public void touch(String path)
	{
		new File(path).getParentFile().mkdirs();

		try
		{
			new File(path).createNewFile();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void delete(String path)
	{
		File f = new File(path);
		if(f.exists())
		{
			System.out.println("Deleting " + (f.isDirectory() ? "directory" : "file") + ": " + f.getPath());

			if(f.isDirectory())
			{
				for(File i : f.listFiles())
				{
					delete(i.getAbsolutePath());
				}
			}

			f.delete();
		}
	}

	public void copy(String from, String to)
	{
		File tox = new File(to);
		File fromx = new File(from);

		if(!fromx.exists())
		{
			return;
		}

		if(fromx.isDirectory())
		{
			if(!tox.exists())
			{
				tox.mkdirs();

				try
				{
					System.out.println("Copying directory: " + new File(from).getPath() + " to " + new File(to).getPath());
					FileUtils.copyDirectory(new File(from), new File(to));
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		else
		{
			if(!tox.exists() || tox.isFile())
			{
				try
				{
					if(to.endsWith("/") || to.endsWith("\\"))
					{
						System.out.println("Copying file: " + new File(from).getPath() + " into directory " + new File(to).getPath());
						FileUtils.copyFileToDirectory(new File(from), new File(to));
					}

					else
					{
						System.out.println("Copying file: " + new File(from).getPath() + " to " + new File(to).getPath());
						FileUtils.copyFile(new File(from), new File(to));
					}
				}

				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
