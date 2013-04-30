/***************************************************************************
 *            MailComposer.java
 *
 *  Tue Jul 22 16:01:57 2003
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


class MailComposer extends JApplet 
		implements ActionListener,WindowListener//,HyperlinkListener,,KeyListener
{
	static JFrame frame;
	static JMenuBar menubar;
	static JMenu mFile,mEdit,mHelp;
	static JMenuItem mNew,mCLose;
	static JEditorPane editor;
	static JScrollPane scroller;
	static JTabbedPane tabpane;
	static JSplitPane splitter;
	static JLabel lto,lcc,lsub;
	static JTextField txtto,txtcc,txtsub;
	static JButton sendbutton;
	static JPanel upperpanel;
	static GridBagLayout gb;
	static GridBagConstraints gbc;

	public static void main(String[] args) 
	{
		MailComposer m=new MailComposer();
		frame.addWindowListener(m);
	}
	public MailComposer()
	{
		frame=new JFrame("Compose Mail");
		upperpanel=new JPanel();
		editor=new JEditorPane();
		tabpane=new JTabbedPane();
		lto=new JLabel("To: ");
		lcc=new JLabel("Cc: ");
		lsub=new JLabel("Subject: ");
		txtto=new JTextField(20);
		txtcc=new JTextField(20);
		txtsub=new JTextField(20);
		sendbutton=new JButton("",new ImageIcon("images/sendmail.gif"));
		sendbutton.addActionListener(this);
		gb=new GridBagLayout();
		gbc=new GridBagConstraints();

		scroller=new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		upperpanel.setLayout(gb);

		gbc.gridwidth=GridBagConstraints.RELATIVE;
		gb.setConstraints(lto,gbc);
		upperpanel.add(lto);

		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gb.setConstraints(txtto,gbc);
		upperpanel.add(txtto);

		gbc.gridwidth=GridBagConstraints.RELATIVE;
		gb.setConstraints(lcc,gbc);
		upperpanel.add(lcc);
		
		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gb.setConstraints(txtcc,gbc);
		upperpanel.add(txtcc);

		gbc.gridwidth=GridBagConstraints.RELATIVE;
		gb.setConstraints(lsub,gbc);
		upperpanel.add(lsub);

		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gb.setConstraints(txtsub,gbc);
		upperpanel.add(txtsub);

		gbc.gridwidth=GridBagConstraints.REMAINDER;
		gb.setConstraints(sendbutton,gbc);
		upperpanel.add(sendbutton);

		splitter=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,upperpanel,scroller);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerLocation(120);
		tabpane.addTab("Compose your mail.",new ImageIcon("images/mail.png"),splitter,"Compose your mail");

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
	}
	public void settext(String text)
	{
		editor.setText(text);
	}
	public void sendmail()
	{
		if(txtto.getText().length()==0)
		{
			String address=JOptionPane.showInputDialog(frame,"Enter the email address of the recipient");
			txtto.setText(address);
		}
		else
		{
			boolean mailsent=false;
			Mailer m = new Mailer();
			try{
			String displayname=Service.getSetting("displayname");
			String smtpserver=Service.getSetting("smtphost");
			String fromaddress=Service.getSetting("emailaddress");
			mailsent=m.sendMail(smtpserver,fromaddress,displayname,
		txtto.getText(),txtcc.getText(),txtsub.getText(),"x-Mailer: TENDS Mailer 2.1",
		editor.getText());
			if(mailsent)
				frame.dispose();
			else
				frame.show();
		}
			catch(Exception e){}
		}
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
		if (e.getSource()==sendbutton)
		{
			sendmail();
		}
	}
}
