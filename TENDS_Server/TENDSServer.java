
/**  TENDS Server : The Server for a remote TENDS Client
*    
*				Copyright (C) 2002  Anurag Patel
*
*    This program is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program; if not, write to the Free Software
*    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*
*	 See Server_readme.txt for detailed list of features
*	 See licence.txt for licence information
*/


import java.net.*;
import java.io.*;

class TENDSLink extends Object implements java.io.Serializable
{
	String importFileName;
	String importFileContent;
}

class TENDSServer implements Runnable
{
	ServerSocket server;
	Socket fromClient;
	Thread serverThread;

	public TENDSServer(int port)
	{
		try
		{
			server=new ServerSocket(port);
			serverThread=new Thread(this);
			serverThread.start();
			System.out.println("Server started successfully...\n"+server+"\n\n\t\tWaiting for Client requests...");
		}
		catch(Exception e)
		{
			System.out.println("\nCannot start the Server   Port 5998 already open...\n"+e);
		}
	}

	public static void main(String args[])
	{
		new TENDSServer(5998);
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				fromClient=server.accept();
				Connect con=new Connect(fromClient);
				System.gc();
			}
		}
		catch (Exception e)
		{
			System.out.println("Cannot listen to the client ...  "+e);
		}
	}
}

class Connect
{
	TENDSLink data;
	ObjectInputStream streamFromClient;
	ObjectOutputStream streamToClient;
	FileInputStream fis;
	int nob;
	byte b[];
	public Connect(Socket inFromClient)
	{
		try
		{
			streamFromClient=new ObjectInputStream(inFromClient.getInputStream());
			System.out.println("________________________________________________________________________________\n\t\tA Client connected ...\n"+inFromClient);
			try
			{
				data=(TENDSLink)streamFromClient.readObject();
			}
			catch(InvalidClassException e)
			{
				System.out.println("Cannot serialize the applicant class : "+e);
			}
			catch(NotSerializableException e)
			{
				System.out.println("The object is not serializable : "+e);
			}
			catch(IOException e)
			{
				System.out.println("Cannot read from the client stream : "+e);
			}
			System.out.println("Requested file = "+data.importFileName);
			data.importFileContent=processFile(data.importFileName);
				try
				{
					streamToClient=new ObjectOutputStream(inFromClient.getOutputStream());
					streamToClient.writeObject((TENDSLink)data);
					streamToClient.close();
					System.out.println("Client served and disconnected...");
				}
				catch(Exception e)
				{
					System.out.println("Cannot send response to the client");
					System.out.println("Reason   ...\n"+e);
				}
		}
		catch (Exception e)
		{
			System.out.println("Cannot get the client stream : "+e);
			System.out.println("Reason   : "+e);
		}
	}
	
	public String processFile(String str)
	{
		String ans="";
		if(str.length()==0 || str==null)
		{
			System.out.println("No file specified by the client");
			ans="###ERROR :      No File specified       ###";
		}
		else if(str.length()!=0)
		{
			try
			{
				fis=new FileInputStream(str);
				nob=fis.available();
				b=new byte[nob];
				fis.read(b,0,nob);
				ans=(new String(b));
				fis.close();
			}
			catch(Exception fe)
			{
				System.out.println("Cannot open the requested file : "+str);
				System.out.println("Reason  : \n"+fe);
				ans="###ERROR :     File not found on the Server      ###";
			}
		}
		return(ans);
	}
}