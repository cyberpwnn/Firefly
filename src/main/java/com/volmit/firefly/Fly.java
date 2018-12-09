package com.volmit.firefly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Fly
{
	public String[] parameters;

	public Fly(String[] parameters)
	{
		this.parameters = parameters;
	}

	public String getParameter(int index)
	{
		return parameters[index];
	}

	public void changeVersion(String var, String javaFile)
	{
		try
		{
			String c = "";
			BufferedReader bu = new BufferedReader(new FileReader(new File(javaFile)));
			String l = "";

			while((l = bu.readLine()) != null)
			{
				String v = l + "\n";

				if(v.trim().contains("public static final String " + var))
				{
					String cv = v.split("\\Q\"\\E")[1];
					int d = cv.contains(".") ? cv.split("\\.").length : 0;
					System.out.println("Found " + d + " dots.");
					if(d > 0)
					{
						Integer cver = Integer.valueOf(cv.split("\\.")[d - 1]) + 1;
						v = "	public static final String " + var + " = \"";

						for(int i = 0; i < cv.split("\\.").length - 1; i++)
						{
							System.out.println(cv.split("\\.")[i]);
							v += cv.split("\\.")[i] + ".";
						}

						v += cver + "\";\n";
					}
				}

				c += v;
			}

			bu.close();
			PrintWriter pw = new PrintWriter(new FileWriter(new File(javaFile)));
			pw.println(c);
			pw.close();
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void log(String l)
	{
		System.out.println(l);
	}

	public ArrayList<String> newStringList()
	{
		return new ArrayList<String>();
	}

	public Process runProcess(ArrayList<String> args, String name)
	{
		try
		{
			Process p = buildProcess(args).start();
			monitorProcess(p, name);
			return p;
		}

		catch(Exception e)
		{

		}

		return null;
	}

	public Process monitorProcess(Process ps, String name)
	{
		new StreamGobbler(ps.getInputStream(), name).start();
		new StreamGobbler(ps.getErrorStream(), "ERROR: " + name).start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("DESTROYING CHILD PROCESS");
				ps.destroy();

				try
				{
					ps.waitFor();
				}

				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}));
		return ps;
	}

	public ProcessBuilder buildProcess(ArrayList<String> args)
	{
		ProcessBuilder pb = new ProcessBuilder(args);
		return pb;
	}

	public Thread executeThread(String script)
	{
		Thread t = new Thread(() ->
		{
			try
			{
				execute(script);
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		});

		return t;
	}

	public void execute(String script) throws ScriptException, IOException
	{
		File f = new File(script);
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.put("Fly", this);
		engine.put("FS", new Files());
		Object r = engine.eval(process(read(f)));

		if(r != null)
		{
			System.out.println(f.getName() + " returned with " + r.toString());
		}
	}

	private String process(String read)
	{
		read = "load(\"nashorn:mozilla_compat.js\");\n" + read;

		return read;
	}

	private String read(File f) throws IOException
	{
		BufferedReader bu = new BufferedReader(new FileReader(f));
		String c = "";
		String l = "";

		while((l = bu.readLine()) != null)
		{
			c += l + "\n";
		}

		bu.close();
		return c;
	}
}
