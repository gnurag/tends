/***************************************************************************
 *            Inbox.java
 *
 *  Tue Jul 22 16:02:14 2003
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

class Inbox extends JApplet
	implements ActionListener,WindowListener
{
	static JFrame frame;
	static JEditorPane editor;
	static JScrollPane upperscroller,lowerscroller;
	static JTabbedPane tabpane;
	static JSplitPane splitter;
	static GridBagLayout gb;
	static GridBagConstraints gbc;
	static JTable table;

	public static void main(String[] args)
	{
		Inbox in=new Inbox();
		frame.addWindowListener(in);
	}
	public Inbox()
	{
		frame=new JFrame("Mail Inbox");
		editor=new JEditorPane();
		tabpane=new JTabbedPane();
		gb=new GridBagLayout();
		gbc=new GridBagConstraints();

		table=new JTable(10,5);
		table.setShowGrid(true);
		upperscroller=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		lowerscroller=new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		splitter=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,upperscroller,lowerscroller);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerLocation(120);
		tabpane.addTab("View Inbox",new ImageIcon("images/inbox.png"),splitter,"Mails available on server");

		frame.getContentPane().add(tabpane);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width*2/3,screenSize.height-100);
		int x=((screenSize.width)-(screenSize.width*2/3))/2;
		int y=((screenSize.height)-(screenSize.height-100))/2;
		frame.setLocation(x,y);
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.show();
		downloadmails();
	}
	
	public void downloadmails()
	{
		Pop3 mail=new Pop3(1);
		try
		{
			String username=Service.getSetting("userid");
			String password=Service.decrypt(Service.getSetting("password"));
			mail.frame=frame;
			if(mail.login(null,username,password))
			{
				int i=mail.getmailstatus();
				if(i==0)
					return;
				else
					for (int ia=1 ; ia<=i ; ia++)
					{
						//editor.setText(editor.getText()+"\n___________\n"+mail.download(ia));
						sortmail(mail.download(ia));
					}
				mail.logout();
			}
		}
		catch(Exception ee)
		{
			Service.showException(frame,"Error downloading mails from server",ee);
			return;
		}
		return;
	}
	public void sortmail(String data)
	{
	}
	public String getheader(String data,String header)
	{
		String answer="";
		
		if(header.equalsIgnoreCase("subject"))
		{
			answer="Hi ! man!!!!!!!";
		}
		else if(header.equalsIgnoreCase("from"))
		{answer="billgates@microsoft.com";
		}
		else if(header.equalsIgnoreCase("to"))
		{answer="anurag@localhost";
		}
		else if(header.equalsIgnoreCase("cc"))
		{answer="oasama@alquaida.gov";
		}
		else if(header.equalsIgnoreCase("reply-to"))
		{answer="musharraf@pakistan.gov.pk";
		}
		return answer;
	}

	public void windowClosing(WindowEvent w){System.exit(0);}
	public void windowOpened(WindowEvent w){}
	public void windowClosed(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeactivated(WindowEvent w){}

	public void actionPerformed(ActionEvent e)
	{
	}
}
