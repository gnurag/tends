/***************************************************************************
 *            Config.java
 *
 *  Tue Jul 22 16:01:43 2003
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

public class Config extends JApplet 
		implements ActionListener,WindowListener
{
	static JFrame frame;
	static JScrollPane scroller;
	static JTabbedPane tabpane;
	static JLabel loutname,loutaddress,louthost,loutport,
			 linuserid,linpasswd,linhost,linport,
			 lfontface,lfontsize,leditorbackcolor,lfontcolor,
			 ldefaultui,ltendsserver,lhomepage;
	static JTextField txttendsserver,txthomepage,
			txtoutname,txtoutaddress,txtouthost,txtoutport,
			txtinuserid,txtinpasswd,txtinhost,txtinport;
	static JCheckBox checkDeleteMsgFromServer,checkShowHiddenFiles,
			checkSetWordWrap,checkUseNativeFileDialog;
	static JComboBox comboui,combofontface,combofontsize;
	static JButton okbutton1,okbutton2,okbutton3,okbutton4,
			cancelbutton1,cancelbutton2,cancelbutton3,cancelbutton4,
			fontcolorbutton,backcolorbutton;
	static JPanel inpanel,outpanel,generalpanel,editorpanel;
	static GridBagLayout gb1,gb2,gb3,gb4;
	static GridBagConstraints gbc1,gbc2,gbc3,gbc4;
	JColorChooser colorchooser;

	public static void main(String[] args)
	{
		Config m=new Config("general");
		frame.addWindowListener(m);
	}
	public Config(String what)
	{
		frame=new JFrame("Configure TENDS");
		generalpanel=new JPanel();
		editorpanel=new JPanel();
		inpanel=new JPanel();
		outpanel=new JPanel();
		tabpane=new JTabbedPane();
//Widgets for General settings dialog
		ldefaultui=new JLabel("Default Look & Feel : ");
		ltendsserver=new JLabel("TENDS Server : ");
		lhomepage=new JLabel("Home Page : ");
		String[] uinames={"Metal / Swing","Motif / CDE","Windows","Macintosh"};
		comboui=new JComboBox(uinames);
		txttendsserver=new JTextField(20);
		txthomepage=new JTextField(20);
		checkShowHiddenFiles=new JCheckBox("Show hidden files on UNIX systems ?");
		checkUseNativeFileDialog=new JCheckBox("Use native file chooser dialog ?");
// Widgets for Editor settings dialog
		lfontface=new JLabel("Default font face : ");

		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String envfonts[] = gEnv.getAvailableFontFamilyNames();
		Vector vector = new Vector();
		for(int i=1;i<envfonts.length;i++)
			vector.addElement(envfonts[i]);
		combofontface = new JComboBox( vector );

		lfontsize=new JLabel("Default font size : ");

		String[] fontsizes={"10","11","12","13","14","15","16","17","18","19","20","25","30","40","50","60","70","80","90","100","150","200"};
		combofontsize=new JComboBox(fontsizes);
		combofontsize.setSelectedItem("15");
		lfontcolor=new JLabel("Default font color");
		fontcolorbutton=new JButton(" Change ");
		fontcolorbutton.setBackground(Color.black);
		fontcolorbutton.setForeground(Color.white);
		fontcolorbutton.addActionListener(this);
		leditorbackcolor=new JLabel("Text editor background color : ");
		backcolorbutton=new JButton(" Change ");
		backcolorbutton.setBackground(new Color(186,186,255));
		backcolorbutton.addActionListener(this);
		checkSetWordWrap=new JCheckBox("Set word wrap ?");
// widgets for POP3 settings dialog
		linuserid=new JLabel("User ID : ");
		linpasswd=new JLabel("Password : ");
		linhost=new JLabel("Mail Server name (POP3): ");
		linport=new JLabel("POP3 port : (Default= 110)");
		checkDeleteMsgFromServer=new JCheckBox("Delete message from server after downloading ?");
		txtinuserid=new JTextField(15);
		txtinpasswd=new JPasswordField(15);
		txtinhost=new JTextField(15);
		txtinport=new JTextField(5);
		txtinport.setText("110");
//widgets for SMTP ssettings dialog
		loutname=new JLabel("Your name : ");
		loutaddress=new JLabel("Email address : ");
		louthost=new JLabel("SMTP Server name : ");
		loutport=new JLabel("SMTP port : (Default= 25)");
		txtoutname=new JTextField(15);
		txtoutaddress=new JTextField(15);
		txtouthost=new JTextField(15);
		txtoutport=new JTextField(5);
		txtoutport.setText("25");

		okbutton1=new JButton("Save");
		okbutton1.addActionListener(this);
		cancelbutton1=new JButton("Cancel");
		cancelbutton1.addActionListener(this);
		okbutton2=new JButton("Save");
		okbutton2.addActionListener(this);
		cancelbutton2=new JButton("Cancel");
		cancelbutton2.addActionListener(this);
		okbutton3=new JButton("Save");
		okbutton3.addActionListener(this);
		cancelbutton3=new JButton("Cancel");
		cancelbutton3.addActionListener(this);
		okbutton4=new JButton("Save");
		okbutton4.addActionListener(this);
		cancelbutton4=new JButton("Cancel");
		cancelbutton4.addActionListener(this);

		gb1=new GridBagLayout();
		gbc1=new GridBagConstraints();
		gb2=new GridBagLayout();
		gbc2=new GridBagConstraints();
		gb3=new GridBagLayout();
		gbc3=new GridBagConstraints();
		gb4=new GridBagLayout();
		gbc4=new GridBagConstraints();
		generalpanel.setLayout(gb1);
		editorpanel.setLayout(gb2);
		inpanel.setLayout(gb3);
		outpanel.setLayout(gb4);
////////////// SETTING UP THE LAYOUT OF COMPONENTS/////////////////////////
		gbc1.gridwidth=GridBagConstraints.RELATIVE;
		gb1.setConstraints(ldefaultui,gbc1);
		generalpanel.add(ldefaultui);
		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(comboui,gbc1);
		generalpanel.add(comboui);

		gbc1.gridwidth=GridBagConstraints.RELATIVE;
		gb1.setConstraints(ltendsserver,gbc1);
		generalpanel.add(ltendsserver);
		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(txttendsserver,gbc1);
		generalpanel.add(txttendsserver);

		gbc1.gridwidth=GridBagConstraints.RELATIVE;
		gb1.setConstraints(lhomepage,gbc1);
		generalpanel.add(lhomepage);
		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(txthomepage,gbc1);
		generalpanel.add(txthomepage);

		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(checkShowHiddenFiles,gbc1);
		generalpanel.add(checkShowHiddenFiles);

		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(checkUseNativeFileDialog,gbc1);
		generalpanel.add(checkUseNativeFileDialog);

		gbc1.gridwidth=GridBagConstraints.RELATIVE;
		gb1.setConstraints(okbutton1,gbc1);
		generalpanel.add(okbutton1);
		gbc1.gridwidth=GridBagConstraints.REMAINDER;
		gb1.setConstraints(cancelbutton1,gbc1);
		generalpanel.add(cancelbutton1);
///////////////////////////////////////////////////////////////////
		gbc2.gridwidth=GridBagConstraints.RELATIVE;
		gb2.setConstraints(lfontface,gbc2);
		editorpanel.add(lfontface);
		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(combofontface,gbc2);
		editorpanel.add(combofontface);

		gbc2.gridwidth=GridBagConstraints.RELATIVE;
		gb2.setConstraints(lfontsize,gbc2);
		editorpanel.add(lfontsize);
		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(combofontsize,gbc2);
		editorpanel.add(combofontsize);

		gbc2.gridwidth=GridBagConstraints.RELATIVE;
		gb2.setConstraints(lfontcolor,gbc2);
		editorpanel.add(lfontcolor);
		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(fontcolorbutton,gbc2);
		editorpanel.add(fontcolorbutton);

		gbc2.gridwidth=GridBagConstraints.RELATIVE;
		gb2.setConstraints(leditorbackcolor,gbc2);
		editorpanel.add(leditorbackcolor);
		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(backcolorbutton,gbc2);
		editorpanel.add(backcolorbutton);

		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(checkSetWordWrap,gbc2);
		editorpanel.add(checkSetWordWrap);

		gbc2.gridwidth=GridBagConstraints.RELATIVE;
		gb2.setConstraints(okbutton2,gbc2);
		editorpanel.add(okbutton2);
		gbc2.gridwidth=GridBagConstraints.REMAINDER;
		gb2.setConstraints(cancelbutton2,gbc2);
		editorpanel.add(cancelbutton2);
///////////////////////////////////////////////////////////////////
		gbc3.gridwidth=GridBagConstraints.RELATIVE;
		gb3.setConstraints(linuserid,gbc3);
		inpanel.add(linuserid);
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(txtinuserid,gbc3);
		inpanel.add(txtinuserid);

		gbc3.gridwidth=GridBagConstraints.RELATIVE;
		gb3.setConstraints(linpasswd,gbc3);
		inpanel.add(linpasswd);
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(txtinpasswd,gbc3);
		inpanel.add(txtinpasswd);

		gbc3.gridwidth=GridBagConstraints.RELATIVE;
		gb3.setConstraints(linhost,gbc3);
		inpanel.add(linhost);
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(txtinhost,gbc3);
		inpanel.add(txtinhost);

		gbc3.gridwidth=GridBagConstraints.RELATIVE;
		gb3.setConstraints(linport,gbc3);
		inpanel.add(linport);
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(txtinport,gbc3);
		inpanel.add(txtinport);
		
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(checkDeleteMsgFromServer,gbc3);
		inpanel.add(checkDeleteMsgFromServer);

		gbc3.gridwidth=GridBagConstraints.RELATIVE;
		gb3.setConstraints(okbutton3,gbc3);
		inpanel.add(okbutton3);
		gbc3.gridwidth=GridBagConstraints.REMAINDER;
		gb3.setConstraints(cancelbutton3,gbc3);
		inpanel.add(cancelbutton3);
///////////////////////////////////////////////////////////////////////////
		gbc4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.setConstraints(loutname,gbc4);
		outpanel.add(loutname);

		gbc4.gridwidth=GridBagConstraints.REMAINDER;
		gb4.setConstraints(txtoutname,gbc4);
		outpanel.add(txtoutname);

		gbc4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.setConstraints(loutaddress,gbc4);
		outpanel.add(loutaddress);

		gbc4.gridwidth=GridBagConstraints.REMAINDER;
		gb4.setConstraints(txtoutaddress,gbc4);
		outpanel.add(txtoutaddress);

		gbc4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.setConstraints(louthost,gbc4);
		outpanel.add(louthost);

		gbc4.gridwidth=GridBagConstraints.REMAINDER;
		gb4.setConstraints(txtouthost,gbc4);
		outpanel.add(txtouthost);

		gbc4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.setConstraints(loutport,gbc4);
		outpanel.add(loutport);

		gbc4.gridwidth=GridBagConstraints.REMAINDER;
		gb4.setConstraints(txtoutport,gbc4);
		outpanel.add(txtoutport);

		gbc4.gridwidth=GridBagConstraints.RELATIVE;
		gb4.setConstraints(okbutton4,gbc4);
		outpanel.add(okbutton4);
		gbc4.gridwidth=GridBagConstraints.REMAINDER;
		gb4.setConstraints(cancelbutton4,gbc4);
		outpanel.add(cancelbutton4);
///////////////////////////////////////////////////////////////////////////
		tabpane.addTab("General",new ImageIcon("images/settings.gif"),generalpanel,"Configure general options for TENDS");
		tabpane.addTab("Editor",new ImageIcon("images/icon.gif"),editorpanel,"Configure options for text editor");
		tabpane.addTab("Incoming Mails",new ImageIcon("images/inmail.gif"),inpanel,"Configure options for incoming mails");
		tabpane.addTab("Outgoing Mails",new ImageIcon("images/outmail.gif"),outpanel,"Configure options for outgoing mails");

		if(what.equalsIgnoreCase("mail"))
			tabpane.setSelectedIndex(2);
		else if(what.equalsIgnoreCase("general"))
			tabpane.setSelectedIndex(0);
		else if(what.equalsIgnoreCase("editor"))
			tabpane.setSelectedIndex(1);

		frame.getContentPane().add(tabpane);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width*2/3,screenSize.height/3+100);

		int x=((screenSize.width)-(screenSize.width*2/3))/2;
		int y=((screenSize.height)-(screenSize.height/3+100))/2;
		frame.setLocation(x,y);
		
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.show();
		loadAllSettings();
	}
	public void loadAllSettings()
	{
		try{
		comboui.setSelectedItem(Service.getSetting("defaultui","Metal / Swing"));
		txttendsserver.setText(Service.getSetting("tendsserver","127.0.0.1"));
		txthomepage.setText(Service.getSetting("homepage","http://www.geocities.com/terminal_anu/index.html"));
		if(Service.getSetting("showhiddenfiles","false").trim().equalsIgnoreCase("true"))
			checkShowHiddenFiles.setSelected(true);
		if(Service.getSetting("usenativefiledialog","false").trim().equalsIgnoreCase("true"))
			checkUseNativeFileDialog.setSelected(true);
//////////////////
		combofontface.setSelectedItem(Service.getSetting("fontface","Monospaced"));
		combofontsize.setSelectedItem(Service.getSetting("fontsize","15"));
		if(Service.getSetting("wordwrap","true").trim().equalsIgnoreCase("true"))
			checkSetWordWrap.setSelected(true);
		try{
			int FR=Integer.parseInt(Service.getSetting("fontcolorred","0"));
			int FG=Integer.parseInt(Service.getSetting("fontcolorgreen","0"));
			int FB=Integer.parseInt(Service.getSetting("fontcolorblue","0"));
			int BR=Integer.parseInt(Service.getSetting("backcolorred","186"));
			int BG=Integer.parseInt(Service.getSetting("backcolorgreen","186"));
			int BB=Integer.parseInt(Service.getSetting("backcolorblue","255"));
			fontcolorbutton.setBackground(new Color(FR,FG,FB));
			backcolorbutton.setBackground(new Color(BR,BG,BB));
		}
		catch(Exception e){}
//////////////////
		txtinuserid.setText(Service.getSetting("userid"," < Enter user ID >"));
		txtinpasswd.setText(Service.decrypt(Service.getSetting("password")));
		txtinhost.setText(Service.getSetting("pop3host"," < Ask ur ISP > "));
		txtinport.setText(Service.getSetting("pop3port","110"));
		if(Service.getSetting("deletemessagefromserver","false").trim().equalsIgnoreCase("true"))
		checkDeleteMsgFromServer.setSelected(true);
//////////////////
		txtoutname.setText(Service.getSetting("displayname"," < Enter ur name >"));
		txtoutaddress.setText(Service.getSetting("emailaddress"," < Enter ur email id >"));
		txtouthost.setText(Service.getSetting("smtphost"," < Ask ur ISP > "));
		txtoutport.setText(Service.getSetting("smtpport","25"));
		}
		catch(Exception e){}
	}
	public void saveAllSettings()
	{
		try{
		Service.saveSetting("defaultui",String.valueOf(comboui.getSelectedItem()));
		Service.saveSetting("tendsserver",txttendsserver.getText().trim());
		Service.saveSetting("homepage",txthomepage.getText().trim());
		if(checkShowHiddenFiles.isSelected())
			Service.saveSetting("showhiddenfiles","true");
		else
			Service.saveSetting("showhiddenfiles","false");
		if(checkUseNativeFileDialog.isSelected())
			Service.saveSetting("usenativefiledialog","true");
		else
			Service.saveSetting("usenativefiledialog","false");
////////////////////
		Service.saveSetting("fontface",String.valueOf(combofontface.getSelectedItem()));
		Service.saveSetting("fontsize",String.valueOf(combofontsize.getSelectedItem()));
		if(checkSetWordWrap.isSelected())
			Service.saveSetting("wordwrap","true");
		else
			Service.saveSetting("wordwrap","false");

		Color c1=fontcolorbutton.getBackground();
		Color c2=backcolorbutton.getBackground();
		Service.saveSetting("fontcolorred",String.valueOf(c1.getRed()));
		Service.saveSetting("fontcolorblue",String.valueOf(c1.getBlue()));
		Service.saveSetting("fontcolorgreen",String.valueOf(c1.getGreen()));
		Service.saveSetting("backcolorred",String.valueOf(c2.getRed()));
		Service.saveSetting("backcolorblue",String.valueOf(c2.getBlue()));
		Service.saveSetting("backcolorgreen",String.valueOf(c2.getGreen()));
////////////////////
		Service.saveSetting("pop3host",txtinhost.getText().trim());
		Service.saveSetting("pop3port",txtinport.getText().trim());
		Service.saveSetting("userid",txtinuserid.getText().trim());
		Service.saveSetting("password",Service.encrypt(txtinpasswd.getText().trim()));
		if(checkDeleteMsgFromServer.isSelected())
			Service.saveSetting("deletemessagefromserver","true");
		else
			Service.saveSetting("deletemessagefromserver","false");
/////////////////////
		Service.saveSetting("smtphost",txtouthost.getText().trim());
		Service.saveSetting("smtpport",txtoutport.getText().trim());
		Service.saveSetting("displayname",txtoutname.getText().trim());
		Service.saveSetting("emailaddress",txtoutaddress.getText().trim());
/////////////////////
		frame.dispose();
		}
		catch(Exception ex)
		{
			Service.showException(frame,"Could not save configuration : ",ex);
		}
		try{TENDS.initialize();}catch(Exception e){}
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
		if(e.getSource()==okbutton1||e.getSource()==okbutton2||e.getSource()==okbutton3||e.getSource()==okbutton4)
		{
			saveAllSettings();
		}
		else if(e.getSource()==cancelbutton1||e.getSource()==cancelbutton2||e.getSource()==cancelbutton3||e.getSource()==cancelbutton4)
		{
			frame.dispose();
		}
		else if(e.getSource()==fontcolorbutton)
		{
			Color c=colorchooser.showDialog(frame,"Choose font color",fontcolorbutton.getBackground());
			if(c!=null)
			fontcolorbutton.setBackground(c);
		}
		else if(e.getSource()==backcolorbutton)
		{
			Color c=colorchooser.showDialog(frame,"Choose background color",backcolorbutton.getBackground());
			if(c!=null)
			backcolorbutton.setBackground(c);
		}
	}
}
