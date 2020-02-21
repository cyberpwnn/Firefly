package com.volmit.firefly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

public class Files
{
	public String path()
	{
		return new File(".").getAbsolutePath();
	}
	
	public String readProperty(String path, String property)
	{
		try
		{
			File f = new File(path);
			String d = IO.readAll(f);
			
			for(String i : d.split("\\Q\n\\E"))
			{
				if(i.startsWith(property + "="))
				{
					return i.split("\\Q=\\E")[1];
				}
			}
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return "";
	}

	public void archive(String folder, String destination)
	{
		archive(folder, destination, 3);
	}

	public void archive(String folder, String destination, int level)
	{
		System.out.println("Archiving " + new File(folder).getPath());
		ZipUtil.pack(new File(folder), new File(destination), level);
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

	public void rewrite(String file, String find, String replace)
	{
		try
		{
			String f = IO.readAll(new File(file));
			IO.writeAll(new File(file), f.replaceAll("\\Q" + find + "\\E", replace));
			System.out.println("Rewriting " + file + " '" + find + "' -> '" + replace + "'");
		}

		catch(IOException e)
		{
			e.printStackTrace();
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

		else
		{
			System.out.println("Unable to delete, doesnt exist: " + path);
		}
	}

	public void copy(String from, String to)
	{
		File tox = new File(to);
		File fromx = new File(from);

		if(!fromx.exists())
		{
			System.out.println("Doesnt exist for copy " + from);
			return;
		}

		if(fromx.isDirectory())
		{
			if(!tox.exists())
			{
				tox.mkdirs();
			}

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

			else
			{
				System.out.println("Cannot copy to from folder / file " + to);
			}
		}
	}
}
