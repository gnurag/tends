/***************************************************************************
 *            Helper.java
 *
 *  Tue Jul 22 16:00:54 2003
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
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import java.io.*;
import java.util.*;
import java.net.*;

class Helper implements HyperlinkListener
{
	JEditorPane helparea=new JEditorPane();
	JFrame frame=new JFrame("TENDS Help Desk");
	JScrollPane	scroller=new JScrollPane(helparea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	public static void main(String[] ar)
	{
		new Helper();
	}
	public Helper()
	{
		helparea.setEditable(false);
		helparea.addHyperlinkListener(this);

		frame.getContentPane().add(scroller);
		helparea.setBorder(BorderFactory.createTitledBorder(" TENDS Helpdesk "));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width*2/3+100,screenSize.height*2/3+100);
		int x=((screenSize.width)-(screenSize.width*2/3+100))/2;
		int y=((screenSize.height)-(screenSize.height*2/3+100))/2;
		frame.setLocation(x,y);

		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.show();
		loadfile("help/help.html");
	}

	public void hyperlinkUpdate(HyperlinkEvent he)
	{
		if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			loadfile(he.getURL().toString());
    }
	public void loadfile(String file)
	{
	try{
		File f=new File(file);
		String helpfile="file:"+f.getAbsolutePath();
		URL url = new URL(helpfile);
		helparea.setPage(url);
	}
	catch(Exception e)
	{
		Service.showException(frame,"Error with help viewer.",e);
	}
	}
}