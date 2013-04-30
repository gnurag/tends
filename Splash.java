/***************************************************************************
 *            Splash.java
 *
 *  Tue Jul 22 16:02:29 2003
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
import java.awt.Toolkit;
import java.awt.Dimension;

class Splash 
{
	static JFrame frame;
	static JLabel status;
	static JPanel panel;
	static JProgressBar progress;
	public static void main(String[] args)
	{
		display();
		progress.setString("I can't do anything. I am just a Splash");
	}
	public static void display()
	{
		frame=new JFrame("Starting TENDS ");
		try{setUI();}catch(Exception e){}

		panel=new JPanel();
		status=new JLabel("Starting TENDS Editor...",new ImageIcon("images/tends.png"),SwingConstants.LEFT);
		progress=new JProgressBar(0,100);
		progress.setBorderPainted(true);
		panel.setLayout(null);
		panel.add(status);
		panel.add(progress);

		progress.setStringPainted(true);
		progress.setString("Loading... please wait");
		progress.setValue(0);

		status.setBounds(5,10,290,40);
		progress.setBounds(5,60,285,30);
		frame.getContentPane().add(panel);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(300,150);
		int x=((screenSize.width)-(300))/2;
		int y=((screenSize.height)-(150))/2;
		frame.setLocation(x,y);
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.show();
	}
	public static void setUI()
	{
		String uiname=Service.getSetting("defaultui","Metal");
		String ui=uiname;
		String metal="javax.swing.plaf.metal.MetalLookAndFeel";
		String motif="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		String win="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		String mac="com.sun.java.swing.plaf.mac.MacLookAndFeel";
		String lfname=metal;
		if(uiname!=null)
			uiname=uiname.toLowerCase();

		if (uiname.startsWith("metal"))
			lfname=metal;
		else if (uiname.startsWith("motif"))
			lfname=motif;
		else if (uiname.startsWith("win"))
			lfname=win;
		else if (uiname.startsWith("mac"))
			lfname=mac;
		try
		{
			UIManager.setLookAndFeel(lfname);
			SwingUtilities.updateComponentTreeUI(frame);
		}
		catch(Exception e)
		{
			Service.showException(frame,"Error loading "+ui+" Look and Feel\n\nTENDS will now assume Metal / Swing",e);
			Service.saveSetting("defaultui","Metal / Swing");
		}
	}
}