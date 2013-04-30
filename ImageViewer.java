/***************************************************************************
 *            ImageViewer.java
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

public class ImageViewer
{
	JFrame frame;
	JScrollPane scroller;
	JLabel img;
	public ImageViewer(String image)
	{
		Icon i=new ImageIcon(image);
		frame=new JFrame("TENDS Image Viewer");
		img = new JLabel(i);
		scroller=new JScrollPane(img,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scroller);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(i.getIconWidth()+40,i.getIconHeight()+40);
		int x=((screenSize.width)-(i.getIconWidth()+40))/2;
		int y=((screenSize.height)-(i.getIconHeight()+40))/2;
		if (x < 30 )
			x=30;
		if (y<30)
			y=30;
		frame.setLocation(x,y);
		
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.show();
	}
}
