package com.volmit.firefly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

	public void log(String l)
	{
		System.out.println(l);
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
