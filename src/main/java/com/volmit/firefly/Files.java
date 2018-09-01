package com.volmit.firefly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Files
{
	public String path()
	{
		return new File(".").getAbsolutePath();
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

	public void send(String host, int port, String username, String password, String dir, ArrayList<String> put, String key, String pass)
	{
		String SFTPHOST = host;
		int SFTPPORT = port;
		String SFTPUSER = username;
		String SFTPPASS = password;
		String SFTPWORKINGDIR = dir;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		System.out.println("Prepare the host information for sftp.");
		try
		{
			JSch jsch = new JSch();
			jsch.addIdentity(key, pass);
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			for(String i : put)
			{
				File f = new File(i);

				if(f.exists() && f.isFile())
				{
					System.out.println("Uploading " + f.getName());
					channelSftp.put(new FileInputStream(f), f.getName());
					System.out.println("Uploaded " + f.getName());
				}

				if(i.startsWith("dir:::"))
				{
					System.out.println("Make Directory: " + i.split(":::")[1]);
					channelSftp.mkdir(i.split(":::")[1]);
					System.out.println("Made Directory: " + i.split(":::")[1]);
				}
			}
		}

		catch(Exception ex)
		{
			System.out.println("Exception found while tranfer the response.");
			ex.printStackTrace();
		}

		finally
		{
			channelSftp.exit();
			System.out.println("sftp Channel exited.");
			channel.disconnect();
			System.out.println("Channel disconnected.");
			session.disconnect();
			System.out.println("Host Session disconnected.");
		}
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
