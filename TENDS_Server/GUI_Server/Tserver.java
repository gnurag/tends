
/**  TENDS Server : The Server for a remote TENDS Client
*    
*		Copyright (C) 2002  Anurag Patel
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
*	 See the readme for detailed list of features
*	 See licence.txt for licence information
*/


import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TENDSLink extends Object implements java.io.Serializable
{
	String importFileName;
	Object importFileContent;
	boolean success;
}

class Tserver extends JApplet implements Runnable,WindowListener,ActionListener
{
	static JFrame frame;
	static JScrollPane sp;
	static JTextArea txtLogs;
	static JMenuBar mb;
	static JMenu mServer;
	static JMenuItem mExit,mSave;
	ServerSocket server;
	Socket fromClient;
	Thread serverThread;

	public Tserver()
	{
	}
	public Tserver(int port)
	{
		startGUI();
		try
		{
			server=new ServerSocket(port);
			serverThread=new Thread(this);
			serverThread.start();
			txtLogs.setText("\n      TENDS Server  GUI version 1.0\n");
			txtLogs.setText(txtLogs.getText()+"\n  Server started successfully...\n  "+server+"\n\n\tWaiting for Client requests...");
		}
		catch(Exception e)
		{
			txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot start the Server   Port 5998 already open...\n"+e);
		}
	}

	public static void main(String args[])
	{
		new Tserver(5998);
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
			txtLogs.setText(txtLogs.getText()+"\n  Cannot listen to the client ...  \n"+e);
		}
	}
	public void startGUI()
	{
		frame=new JFrame("TENDS Server");
		frame.addWindowListener(new Tserver());
		txtLogs=new JTextArea(200,300);
		txtLogs.setEditable(false);
		sp=new JScrollPane(txtLogs,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(sp);
//Loading the menus mStartServer,mExit,mSave;
		mb=new JMenuBar();
		frame.setJMenuBar(mb);
		mServer=new JMenu("Server  ");
		mServer.setMnemonic('S');
		mb.add(mServer);
		mSave=mServer.add(new JMenuItem("Save Log"));
		mSave.addActionListener(this);
		mSave.setMnemonic('S');
		mExit=mServer.add(new JMenuItem("Stop Server and Exit"));
		mExit.addActionListener(this);
		mExit.setMnemonic('x');

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width-(screenSize.width/2)+150,screenSize.height-60);
		int x=((screenSize.width)-(screenSize.width/2)-150)/2;
		int y=((screenSize.height)-(screenSize.height-60))/2;
		frame.setLocation(x,y);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.show();
	}

/////////// ActionListener
	public void actionPerformed(ActionEvent e)
	{
		Object o=e.getSource();
		if(o==mSave)
		{
			String logfile=JOptionPane.showInputDialog(frame,"Enter file name to save as ...");
			if(logfile.length()!=0)
			{	try{
				RandomAccessFile raf=new RandomAccessFile(logfile,"rw");
				raf.writeBytes(txtLogs.getText());
				raf.close();
				txtLogs.setText(txtLogs.getText()+"\n\nSAVED LOG TO  : "+logfile);
				}
				catch(Exception ex){			JOptionPane.showMessageDialog(frame,"Can't save log file","Error",JOptionPane.ERROR_MESSAGE);}
			}
		}
		else if(o==mExit)
		{
			System.exit(0);
		}
	}
 	public void windowClosing(WindowEvent w){System.exit(0);}
	public void windowOpened(WindowEvent w){}
	public void windowClosed(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeactivated(WindowEvent w){}
	

}

class Connect extends Tserver
{
	Tserver parent=new Tserver();
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
			parent.txtLogs.setText(txtLogs.getText()+"\n________________________________________________________________________________\n\tA Client connected ...\n"+inFromClient);
			try
			{
				data=(TENDSLink)streamFromClient.readObject();
			}
			catch(InvalidClassException e)
			{
				parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot serialize the applicant class : "+e);
			}
			catch(NotSerializableException e)
			{
				parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : The object is not serializable : "+e);
			}
			catch(IOException e)
			{
				parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot read from the client stream : "+e);
			}
			parent.txtLogs.setText(txtLogs.getText()+"\n  Requested file = "+data.importFileName);
			data.importFileContent=processFile(data.importFileName);
				try
				{
					streamToClient=new ObjectOutputStream(inFromClient.getOutputStream());
					streamToClient.writeObject((TENDSLink)data);
					streamToClient.close();
					parent.txtLogs.setText(txtLogs.getText()+"\n  Client served and disconnected...");
				}
				catch(Exception e)
				{
					parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot send response to the client");
					parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Reason   ...\n"+e);
				}
		}
		catch (Exception e)
		{
			parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot get the client stream : "+e);
			parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Reason   : "+e);
		}
	}
	
	public String processFile(String str)
	{
		String ans="";
		File f=new File(str);
		if(f.exists())
		{
			try
			{
				DataInputStream dis=new DataInputStream(new FileInputStream(str));
				int nob=dis.available();
				byte b[]=new byte[nob];
				dis.read(b,0,nob);
				dis.close();
				ans=new String(b);
				data.success=true;
			}
			catch(Exception fe)
			{
				parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Cannot open the requested file : "+str);
				parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Reason  : \n"+fe);
				ans="###ERROR :     File not found on the Server      ###";
				data.success=false;
			}
		}
		else
		{
			parent.txtLogs.setText(txtLogs.getText()+"\n### ERROR : Specified file not found");
			ans="###ERROR :      "+str+" does not exist     ###";
		}
		return(ans);
	}
}
