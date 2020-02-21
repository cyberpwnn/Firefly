package com.volmit.firefly;

import java.io.File;

public class Firefly
{
	public static void main(String[] a)
	{
		if(a.length == 0)
		{
			System.out.println("firefly <script> [(arg1) (arg2) (arg3)]");
			System.exit(0);
		}

		File script = new File(a[0]);

		if(script.exists() && script.isFile())
		{
			try
			{
				Fly f = new Fly(new KList<String>(a).qdel(a[0]).toArray(new String[a.length - 1]));
				f.execute(script.getAbsolutePath());
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}

		else
		{
			System.out.println(script.getName() + " is either a folder or doesnt exist.");
			System.exit(0);
		}
	}
}
