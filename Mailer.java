/***************************************************************************
 *            Mailer.java
 *
 *  Tue Jul 22 16:01:33 2003
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
 


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Mailer implements WindowListener
{
	static JFrame frame;
	static JTabbedPane tabpane;
	static JEditorPane editor;
	static JScrollPane scroller;
	static JToolBar toolbar;
	static JButton closebutton;

	public static void main(String args[])
	{
		Mailer mm=new Mailer();
		frame.addWindowListener(mm);
		try
		{
		String username=Service.getSetting("displayname");
		sendMail("mail.rediffmail.com","anonuser@something.com",username,
		"terminal_anu@rediffmail.com","","User Count","Content-Type: text/html",
		"<h1>Hi! I am "+username+"</h1>");
		}
		catch(Exception e)
		{Service.showException(frame,"Cannot connect to mail server:",e);
		}
	}
	public Mailer()
	{
		frame=new JFrame("Sending mail");
		tabpane=new JTabbedPane();
		editor=new JEditorPane();
		toolbar=new JToolBar();
		closebutton=new JButton("Close Window");
		editor.setEditable(false);
		editor.setText("\tTENDS Mailer\n\n");
		scroller=new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tabpane.addTab("Sending Mail",null,scroller,"tooltiptext");

		toolbar.add(closebutton);
		scroller.add(toolbar);
		frame.getContentPane().add(tabpane);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width/3+100,screenSize.height/3+150);
		int x=((screenSize.width)-(screenSize.width/3+100))/2;
		int y=((screenSize.height)-(screenSize.height/3+150))/2;
		frame.setLocation(x,y);

		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.show();
	}

	public static boolean sendMail(String smtphost,String from,String fromname,
		String to, String cc,String subject, String extraargs,String data)throws Exception
	{
	TENDS.logs+="\nTENDS MAILER : \n";
		boolean noErr=true;
		String recipients=to+" "+cc;
		recipients=recipients.replace(',',' ');
		recipients=recipients.replace(';',' ');
		StringTokenizer tokens=new StringTokenizer(recipients);
		while(tokens.hasMoreTokens()&&noErr)
			noErr=mailit(smtphost,from,fromname,tokens.nextToken(),to,cc,subject,extraargs,data);
		frame.dispose();
		return noErr;
	}

	public static boolean mailit(String smtphost,String from,String fromname,
		String to,String tolist, String cc,String subject, String extraargs,String data)throws Exception
	{
	InetAddress addr;
	Socket socket;
	TENDS.logs+="Sending mail to : "+to+"\n";
	int port=25;
	String servermsg="";
	int errno=0;
	String headerFrom="From: \""+fromname+"\" <"+from+">\r\n";
	String headerSubject="Subject: "+subject+"\r\n";
	String headerTo="To: "+tolist+"\r\n";
	String headerCC="Cc: "+cc+"\r\n";
	
	String finalData=headerFrom + headerTo + headerCC + headerSubject+
			extraargs+"\r\n\r\n"+data;

	try{
	 addr =InetAddress.getByName(smtphost);
	editor.setText(editor.getText()+"SMTP Server = " + addr+"\n");
	 socket =new Socket(addr,port);
	}
	catch(Exception ee)
	{
		Service.showException(frame,"Error connecting to SMTP server : ",ee);
		TENDS.logs+="### ERROR : Connection with SMTP sevrer "+smtphost+" refused\n"+String.valueOf(ee)+"\n";
		return false;
	}
  	try 
	{
		editor.setText(editor.getText()+"Connecting...\nSocket = " + socket);
		BufferedReader in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

		servermsg=in.readLine();
		out.write("RSET");
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();

		out.println("HELO ME");
		servermsg=in.readLine();
		editor.setText(editor.getText()+"\nSending HELO...");

		out.write("MAIL FROM: "+from);
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();
		if(geterrno(servermsg)!=250)
		{
			showerror("From address ["+from+"] is invalid");
			socket.close();
			return false;
		}

		out.write("RCPT TO: "+to);
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();
		if(geterrno(servermsg)!=250)
		{
			showerror("Message not sent.\nServer refused to accept mail for ["+to+"]");
			socket.close();
			return false;
		}

		editor.setText(editor.getText()+"\nUploading Data...");
		out.write("DATA");
		out.write("\r\n");
		out.flush();
		servermsg=in.readLine();
 
		out.write(finalData);
		out.write("\r\n.\r\n");
		out.flush();
		servermsg=in.readLine();

		editor.setText(editor.getText()+"\nData Uploaded...");

		editor.setText(editor.getText()+"\nDisconnecting...");
		out.println("QUIT");
		out.flush();
		out.close();
		in.close();

		editor.setText(editor.getText()+"\n+OK   "+servermsg);
		socket.close();

		Object[] options={"Close"};
		int choice=JOptionPane.showOptionDialog(frame,"Message sent to ["+to+"]","Message sent", 
			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, options, options[0]);
		socket.close();
	return true;
	}
	catch(NullPointerException en)
	{
		Service.showException(frame,"Cannot connect to HOST ["+smtphost+"]\nPlease check you Email configuration\n",en);
		return false;
	}
	catch(Exception ee)
	{
		Service.showException(frame,"Error encountered : ",ee);
		return false;
	}
	}

	public static void showerror(String er)
	{
		Object[] options={"Dismiss"};
		int choice=JOptionPane.showOptionDialog(frame,er,"Errors encountered", 
			JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null, options, options[0]);
	}
	public static int geterrno(String servermsg)
	{
		java.util.StringTokenizer tokens=new java.util.StringTokenizer(servermsg);
		int er=Integer.parseInt(tokens.nextToken());
		return (er);
	}

	public void windowClosing(WindowEvent w){System.exit(0);}
	public void windowOpened(WindowEvent w){}
	public void windowClosed(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeactivated(WindowEvent w){}
}
