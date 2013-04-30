/***************************************************************************
 *            Pop3.java
 *
 *  Tue Jul 22 16:01:05 2003
 *  Copyright  2003  anu
 *  anu@localhost.localdomain
 ****************************************************************************/
 /*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;

public class Pop3
	//implements ActionListener, WindowListener
{

		String servermsg="";
		int port=110;
		InetAddress addr;
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		Frame frame=null;

	public static void main(String args[])
	{
		new Pop3();
		return;
	}
	public Pop3()
	{
		try{
		if(login(null,Service.getSetting("userid"),Service.decrypt(Service.getSetting("password"))))
		{
			int ii=getmailstatus();
			if(ii==0)
				return;
			else
				for (int ia=1 ; ia<=ii ; ia++)
					System.out.println(download(ia));
			logout();
		}
		}
		catch(Exception ee)
		{
			System.out.println("ERROR in constructor : "+ee);
			return;
		}
		return;
	}
	public Pop3(int decoy)
	{}

	public boolean login(String Pop3server,String userid,String passwd) throws Exception
	{
		try
		{
			addr=InetAddress.getByName(Pop3server);
			socket=new Socket(addr,port);

			in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

			servermsg=in.readLine();
// Sending User name
			TENDS.logs+="\nLogging into POP3 server ... USER NAME = "+userid+"\n";
			out.write("USER "+userid);
			out.write("\r\n");
			out.flush();
			servermsg=in.readLine();
			if(isok(servermsg)==false)
			{
				Service.showException(frame,"ERROR : "+servermsg+"\nCheck your mail settings",new Exception("Bad Login"));
				socket.close();
				return false;
			}
// Sending Password
			out.write("PASS "+passwd);
			out.write("\r\n");
			out.flush();
			servermsg=in.readLine();
			if(isok(servermsg)==false)
			{
				Service.showException(frame,"ERROR : "+servermsg+"\nCheck your mail settings",new Exception("Bad username or password"));
				socket.close();
				return false;
			}
			return true;
		}
		catch(Exception e)
		{Service.showException(frame,"Error logging into server",e);return false;
		}
	}

	public int getmailstatus()
	{
		try{
		out.write("STAT");
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();
		int n=gethowmanymsg(servermsg);
		return n;
		}
		catch(Exception e)
		{
			Service.showException(frame,"Error getting mail status",e);
			return 0;
		}
	}
	public String download(int n)
	{
		String retval="";
		TENDS.logs+="Downloading mails...\n";
			try{
			out.write("RETR "+String.valueOf(n)+"\r\n");
			out.flush();
			servermsg=in.readLine();
			int thismessagesize=getsize(servermsg);
			char cbuf[]=new char[thismessagesize];
			int bytesread=in.read(cbuf,0,thismessagesize);
			retval=String.valueOf(cbuf);
			}
			catch(Exception eee)
			{
			Service.showException(frame,"Error downloading mail...",eee);
			download(n);
			}
		return retval;
	}
	public boolean logout()
	{
		try{
		System.out.println(servermsg);
		out.write("QUIT");
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();
		in.close();
		out.close();
		socket.close();
		TENDS.logs+="Closing POP3 connection...\n";
		return true;
		}
		catch(Exception e)
		{
			Service.showException(frame,"Error logging out of server...",e);
			return false;
		}
	}

	public int gethowmanymsg(String servermsg)
	{
		java.util.StringTokenizer tokens=new java.util.StringTokenizer(servermsg);
		String a=tokens.nextToken();
		int num=Integer.parseInt(tokens.nextToken());
		System.out.println(num+ " Messages are there");
		return (num);
	}

	public int getsize(String servermsg)
	{
		java.util.StringTokenizer tokens=new java.util.StringTokenizer(servermsg);
		String a=tokens.nextToken();
		int num=Integer.parseInt(tokens.nextToken());
		System.out.println("This message size = "+num);
		return (num);
	}

	public boolean isok(String servermsg)
	{
		boolean answer=false;
		java.util.StringTokenizer tokens=new java.util.StringTokenizer(servermsg);
		String ok=(tokens.nextToken());
		if (ok.equalsIgnoreCase("+OK"))
			answer=true;
		else if(ok.equalsIgnoreCase("-ERR"))
			answer=false;
		return answer;
	}

	
}