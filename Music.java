/***************************************************************************
 *            Music.java
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


class Music extends JApplet 
	implements ActionListener, WindowListener//,AudioClip
{
	static JFrame frame;
	static JTabbedPane tabpane;
	static JLabel title;
	static JButton bload,bdelete,bplay,bpause,bstop,bnext,bprev;
	static JProgressBar progress;
	static JPanel panel;
	static JTable table;
	static JScrollPane scroller;
	static JSplitPane splitter;
	static int numberoffiles;
	AudioClip audio;

	public static void main(String[] args)
	{
		Music m=new Music();
		frame.addWindowListener(m);
	}
	public Music()
	{

		frame=new JFrame("TENDS Music Player");
		tabpane=new JTabbedPane();
		panel=new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		table=new JTable(getPlaylistCount()+1,2);
		table.getTableHeader().setReorderingAllowed(true);

		bload = new JButton("Add");
		bdelete=new JButton("Delete");
		bplay=new JButton("Play");
		bpause=new JButton("Pause");
		bstop=new JButton("Stop");
		bnext=new JButton("Next >>");
		bprev=new JButton("<< Prev");
		progress=new JProgressBar();

		panel.setLayout(null);
/////////////////////////////////////////////////
		panel.add(bload);
		panel.add(bdelete);
		panel.add(bprev);
		panel.add(bplay);
		panel.add(bpause);
		panel.add(bstop);
		panel.add(bnext);
		panel.add(progress);

		bload.setBounds(185,5,80,35);
		bload.addActionListener(this);
		bdelete.setBounds(275,5,80,35);
		bdelete.addActionListener(this);
		bprev.setBounds(40,45,80,35);
		bprev.addActionListener(this);
		bplay.setBounds(145,45,80,35);
		bplay.addActionListener(this);
		bpause.setBounds(230,45,80,35);
		bpause.addActionListener(this);
		bstop.setBounds(315,45,80,35);
		bstop.addActionListener(this);
		bnext.setBounds(420,45,80,35);
		bnext.addActionListener(this);
		progress.setBounds(20,85,500,20);

		bdelete.setEnabled(false);
		bpause.setEnabled(false);
		bstop.setEnabled(false);
///////////////////////////////////////////////*/
		scroller=new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		splitter=new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,scroller,panel);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerLocation(220);
		tabpane.addTab("TENDS Music Player",new ImageIcon("images/sound.png"),splitter,"TENDS Music Player");

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


	int getPlaylistCount()
	{
		numberoffiles=0;
		return numberoffiles;
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
		if (o==bload)
		{
			try{
			TENDS.getFileName(0,frame);
			}
			catch(Exception ex)
			{String cmd=JOptionPane.showInputDialog(frame,"Enter the music filename...");
			table.setValueAt(cmd,numberoffiles,1);
			table.addColumn(new TableColumn());
			}
		}
		else if (o==bdelete)
		{
		}
		else if (o==bprev)
		{
		}
		else if (o==bplay)
		{//file://C:\Dump Ground\Sounds\titanic.mid
			try{
			URL url=new URL("file://"+new File("t.mid").getAbsoluteFile().toString());
			//audio=Applet.newAudioClip(url);
			Applet a=new Applet();
			audio=a.getAudioClip(url);
			System.out.println(audio);
			audio.play();
			}
			catch(Exception ee){Service.showException(frame,"ERROR : In music player",ee);}
		}
		else if (o==bpause)
		{
		}
		else if (o==bstop)
		{
		}
		else if (o==bnext)
		{
		}
	}
	public void play(){}
	public void loop(){}
	public void stop(){}
}
