/***************************************************************************
 *            Archiver.java
 *
 *  Tue Jul 22 16:01:22 2003
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
import javax.swing.table.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;
import java.applet.*;


class Archiver extends JApplet 
	implements ActionListener, WindowListener
{
	static JFrame frame;
	static JTabbedPane tabpane;
	static JLabel title;
	static JButton bAdd,bRemove,bCreate,bClose,bExtract;
	static JProgressBar progress;
	static JPanel panel;
	static JList list;
	static JScrollPane scroller;
	static JSplitPane splitter;
	static int numberoffiles;

	public static void main(String[] args)
	{
		Archiver a=new Archiver("a");
		frame.addWindowListener(a);
	}
	public Archiver(String filename)
	{
		frame=new JFrame("TENDS Archiver");
		tabpane=new JTabbedPane();
		panel=new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		list=new JList(getList(filename));

		bAdd = new JButton("Add",new ImageIcon("images/plus.png"));
		bRemove=new JButton("Remove",new ImageIcon("images/minus.png"));
		bCreate=new JButton("Create");
		bClose=new JButton("Close");
		bExtract=new JButton("Extract");
		progress=new JProgressBar();

		panel.setLayout(null);
/////////////////////////////////////////////////
		panel.add(bAdd);
		panel.add(bRemove);
		panel.add(bCreate);
		panel.add(bClose);
		panel.add(bExtract);
		panel.add(progress);

		bAdd.setBounds(125,5,140,35);
		bAdd.addActionListener(this);
		bRemove.setBounds(275,5,140,35);
		bRemove.addActionListener(this);
		bCreate.setBounds(145,45,80,35);
		bCreate.addActionListener(this);
		bClose.setBounds(230,45,80,35);
		bClose.addActionListener(this);
		bExtract.setBounds(315,45,80,35);
		bExtract.addActionListener(this);
		progress.setBounds(20,85,500,20);

		bRemove.setEnabled(false);
		bClose.setEnabled(false);
		bExtract.setEnabled(false);
///////////////////////////////////////////////*/
		scroller=new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitter=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,scroller,panel);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerLocation(220);
		tabpane.addTab("TENDS Archiver",new ImageIcon("images/sound.png"),splitter,"TENDS Music Player");

		frame.getContentPane().add(tabpane);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(540,400);
		int x=((screenSize.width)-(screenSize.width*2/3))/2;
		int y=((screenSize.height)-(screenSize.height-100))/2;
		frame.setLocation(x,y);
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.show();
	}

	String[] getList(String filename)
	{
		String [] ls={"File1","File2","File3"};
		System.out.println(ls);
		return ls;
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
		Object o=e.getSource();
		if (o==bAdd)
		{
		}
		else if (o==bRemove)
		{
		}
		else if (o==bCreate)
		{
			getList("a");
		}
		else if (o==bClose)
		{
			frame.dispose();
		}
		else if (o==bExtract)
		{
		}
	}
}