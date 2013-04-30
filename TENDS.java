/**		GNU - GENERAL PUBLIC LICENCE 
*
*	 TENDS Editor : The text editing and network supporting editor
*		Copyright (C) 2001-2003  Anuarg Patel
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
*    Anurag Patel
*    National Center for Software Technology
*    
*    < anurag@users.ourproject.org >
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.beans.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.net.*;

public class TENDS
		extends JApplet 
		implements ActionListener,WindowListener,HyperlinkListener,
		Runnable,KeyListener,PropertyChangeListener,MouseListener// ,DocumentListener
{
	static JFrame frame;
	static JMenuBar menubar;
	static JMenu mFile,mEdit,mlnf,mFeatures,mHelp,
	       mImport,mEmail,mOptions;
	static JPopupMenu popup;
	static JMenuItem mNew,mOpen,mSearch,mSaveas,mRun,mCompress,
			mExit,mFont,mRecompileTends,mViewLog,mPrint,mEditorSettings,
		   mCut,mCopy,mPaste,mDelete,mFileInfo,mJava,mMotif,mWindows,
           mMac,mAboutNote,mSysinfo,mTSImport,Helpme,mDecomp,mScreenshot,
		   mSendMail,mReadmail,mMailSettings,mMusic,mGenSettings,mpopfontface,mpopcut,mpopcopy,mpoppaste,mpopdelete;
	static JTextArea mainArea;
	static JEditorPane htmleditor;
	static Clipboard clippy;
	static JScrollPane scroller,htmlscroller;
	static JTabbedPane tabpane;
	static JPanel textpane,htmlpane;
	static JFileChooser chooser;
	static JColorChooser colorChooser;
	static String NAME,version;
	static Thread garbageThread;

	static JToolBar texttoolbar,htmltoolbar;
	static JButton textnew,textopen,textsave,textfont,
		textcut,textcopy,textpaste,textdelete,textmail,textabout;
	static JButton htmlgo,htmlopen,htmlhome,htmledit;
	static JLabel htmladdrlabel;
	static ImageIcon thumbnail;
	static JLabel previewlabel;
	static File f;
	static JTextField htmladdress;
	static String logs;
	static JToggleButton cornerbutton;

	/** This is where the control starts from*/
	public static void main(String args[])
	{
		Splash.display();
		NAME="TENDS";
		version="2.1";
		logs="Loading .";
		TENDS anurag=new TENDS();
		frame.addWindowListener(anurag);
		frame.addKeyListener(anurag);
		frame.setJMenuBar(menubar);
		frame.getContentPane().add(tabpane);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		logs+=".";
		//initializing variables so that they are not null


		logs+="\t[ OK ]\n";
		anurag.initialize();
		Splash.frame.dispose();
		logs+="User Interface loaded\n";

		frame.setVisible(true);
		frame.show();
//		System.out.println(javax.swing.plaf.basic.BasicHTML.isHTMLString("<html><h1>HI!!!!! i am TENDS</h1></html>"));
	}//End main method

	/*The constructor of the TENDS Editor*/
	public TENDS()
	{
		splasher("Initializing garbage collector...",15,50);
		garbageThread=new Thread(this);
		garbageThread.start();

		splasher("Initializing frame",20,150);
		frame=new JFrame(NAME+"  "+version+"  (C) Anurag Patel");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width,screenSize.height-40);
		frame.setIconImage((new ImageIcon("images/icon.gif")).getImage());

		logs+=("..");
		chooser = new JFileChooser();
		f=null;
		thumbnail=new ImageIcon("images/icon.gif");
		previewlabel=new JLabel(thumbnail);
		previewlabel.setPreferredSize(new Dimension(120,90));
		previewlabel.setBorder(BorderFactory.createTitledBorder(" Image Preview "));
		chooser.setAccessory(previewlabel);
		chooser.addPropertyChangeListener(this);

		logs+=(".");
		colorChooser=new JColorChooser();
		logs+=(".");

		splasher("Initializing text editor",30,50);
		mainArea=new JTextArea();
		mainArea.setWrapStyleWord(true);
		mainArea.addMouseListener(this);
		htmleditor=new JEditorPane();
        mainArea.setMargin(new Insets(10,10,10,10));

		logs+=(".");
		splasher("Initializing tabbed panes...",32,15);
		htmleditor.addKeyListener(this);
		htmleditor.setEditable(false);
		htmleditor.addHyperlinkListener(this);
		logs+=(".");

		tabpane=new JTabbedPane();

		textpane=new JPanel();
		textpane.addKeyListener(this);
		htmlpane=new JPanel();
		htmlpane.addKeyListener(this);
		logs+=(".");

		textpane.setLayout(new BorderLayout()); 
		textpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scroller=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textpane.add(scroller,BorderLayout.CENTER);

		htmlpane.setLayout(new BorderLayout()); 
 		htmlpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		htmlscroller=new JScrollPane(htmleditor,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		htmlpane.add(htmlscroller,BorderLayout.CENTER);
		logs+=(".");

		tabpane.addTab("TENDS Text Editor",new ImageIcon("images/icon.gif"),textpane,"TENDS Text Editor tab");
		tabpane.addTab("TENDS Web Browser",new ImageIcon("images/web.gif"),htmlpane,"TENDS Web browser");

////////// SETTING THE MARGINS ////////
		cornerbutton=new JToggleButton("cm");
		scroller.setCorner(JScrollPane.UPPER_LEFT_CORNER,cornerbutton);
//		textpane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        scroller.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        //scroller.setCorner(JScrollPane.LOWER_LEFT_CORNER,new JLabel("I"));
        //scroller.setCorner(JScrollPane.UPPER_RIGHT_CORNER,new JLabel("I"));

		scroller.setViewportView(mainArea);
		scroller.setColumnHeaderView(new JLabel("  TENDS"));
		String [] ss={"1","2","3"};
		scroller.setRowHeaderView(new JLabel("1"));

		logs+=(".");
		splasher("Initializing system clipboard...",35,200);
		clippy=getToolkit().getSystemClipboard();

		//initialising the menubars
		splasher("Loading menu bars...",50,150);
		menubar=new JMenuBar();
		mFile=new JMenu("File   ");
		mFile.setMnemonic('F');
		mEdit=new JMenu("Edit  ");
		mEdit.setMnemonic('E');
		mlnf=new JMenu("Look & Feel  ");
		mlnf.setMnemonic('L');

		mFeatures=new JMenu("Features  ");
		mFeatures.setMnemonic('T');
		mImport=new JMenu("TENDS Server");
		mImport.setMnemonic('M');
		mEmail=new JMenu("Email    ");
		mEmail.setMnemonic('m');
		mOptions=new JMenu("Options   ");
		mOptions.setMnemonic('O');
		mHelp=new JMenu("Help  ");
		mHelp.setMnemonic('H');

		popup=new JPopupMenu("TENDS");
		menubar.add(mFile);
		menubar.add(mEdit);
		menubar.add(mlnf);
		menubar.add(mFeatures);
		menubar.add(mEmail);
		menubar.add(mOptions);
		menubar.add(mHelp);

		mFeatures.add(mImport);
		splasher("Loading menu bars...",55,250);

		logs+=(".");
		//implementing actionlisteners
		mNew=mFile.add(new JMenuItem("New ",new ImageIcon("images/new.gif")));
		mNew.addActionListener(this);
		mNew.setMnemonic('N');

		mFile.addSeparator();

		mOpen=mFile.add(new JMenuItem("Open",new ImageIcon("images/open.gif")));
		mOpen.addActionListener(this);
		mOpen.setMnemonic('O');

		mSearch=mFile.add(new JMenuItem("Find",new ImageIcon("images/search.gif")));
		mSearch.addActionListener(this);
		mSearch.setMnemonic('F');

		mSaveas=mFile.add(new JMenuItem("Save",new ImageIcon("images/save.gif")));
		mSaveas.addActionListener(this);
		mSaveas.setMnemonic('S');

		mFile.addSeparator();

		mPrint=mFile.add(new JMenuItem("Print",new ImageIcon("images/printer.gif")));
		mPrint.addActionListener(this);
		mPrint.setMnemonic('P');

		mFile.addSeparator();
		logs+=(".");

		mRun=mFile.add(new JMenuItem("Run ",new ImageIcon("images/run.gif")));
		mRun.addActionListener(this);
		mRun.setMnemonic('R');

		mFile.addSeparator();

		mExit=mFile.add(new JMenuItem("Exit",new ImageIcon("images/exit.gif")));
		mExit.addActionListener(this);
		mExit.setMnemonic('x');

		mFont=mEdit.add(new JMenuItem("Fonts",new ImageIcon("images/fonts.gif")));
		mFont.addActionListener(this);
		mFont.setMnemonic('F');

		mEdit.addSeparator();
		logs+=(".");

		mCut=mEdit.add(new JMenuItem("Cut    (^X)",new ImageIcon("images/cut.gif")));
		mCut.addActionListener(this);
		mCut.setMnemonic('t');

		mCopy=mEdit.add(new JMenuItem("Copy   (^C)",new ImageIcon("images/copy.gif")));
		mCopy.addActionListener(this);
		mCopy.setMnemonic('y');

		mPaste=mEdit.add(new JMenuItem("Paste  (^V)",new ImageIcon("images/paste.gif")));
		mPaste.addActionListener(this);
		mPaste.setMnemonic('P');

		mDelete=mEdit.add(new JMenuItem("Delete   (Del)",new ImageIcon("images/delete.gif")));
		mDelete.addActionListener(this);
		mDelete.setMnemonic('D');
		mEdit.addSeparator();
		logs+=(".");

		mFileInfo=mEdit.add(new JMenuItem("File Information",new ImageIcon("images/fileinfo.gif")));
		mFileInfo.addActionListener(this);
		mFileInfo.setMnemonic('I');

		mJava=mlnf.add(new JMenuItem("Metal / Swing",new ImageIcon("images/")));
		mJava.addActionListener(this);
		mJava.setMnemonic('S');

		mMotif=mlnf.add(new JMenuItem("Motif / CDE",new ImageIcon("images/")));
		mMotif.addActionListener(this);
		mMotif.setMnemonic('M');

		mWindows=mlnf.add(new JMenuItem("Windows ",new ImageIcon("images/")));
		mWindows.addActionListener(this);
		mWindows.setMnemonic('W');

		mMac=mlnf.add(new JMenuItem("Macintosh  ",new ImageIcon("images/")));
		mMac.addActionListener(this);
		mMac.setMnemonic('a');
		//features

		mCompress=mFeatures.add(new JMenuItem("Archive Manager",new ImageIcon("images/zip.gif")));
		mCompress.addActionListener(this);
		mCompress.setMnemonic('A');

		mTSImport=mImport.add(new JMenuItem("Import a file",new ImageIcon("images/tclientsmall.gif")));
		mTSImport.addActionListener(this);
		mTSImport.setMnemonic('I');

		mDecomp=mFeatures.add(new JMenuItem("Decompiler",new ImageIcon("images/decompiler.gif")));
		mDecomp.addActionListener(this);
		mDecomp.setMnemonic('D');

		mSendMail=mEmail.add(new JMenuItem("Compose new mail",new ImageIcon("images/compose.gif")));
		mSendMail.addActionListener(this);
		mSendMail.setMnemonic('m');
		mReadmail=mEmail.add(new JMenuItem("Check mail  ",new ImageIcon("images/newmail.gif")));
		mReadmail.addActionListener(this);
		mReadmail.setMnemonic('C');

		mScreenshot=mFeatures.add(new JMenuItem("Take Screenshot",new ImageIcon("images/camera.gif")));
		mScreenshot.addActionListener(this);
		mScreenshot.setMnemonic('S');
		mMusic=mFeatures.add(new JMenuItem("Music Player",new ImageIcon("images/music.gif")));
		mMusic.addActionListener(this);
		mMusic.setMnemonic('P');

		mGenSettings=mOptions.add(new JMenuItem("General Settings",new ImageIcon("images/settings.gif")));
		mGenSettings.addActionListener(this);
		mGenSettings.setMnemonic('S');
		mEditorSettings=mOptions.add(new JMenuItem("Editor Settings",new ImageIcon("images/fonts.gif")));
		mEditorSettings.addActionListener(this);
		mEditorSettings.setMnemonic('E');
		mMailSettings=mOptions.add(new JMenuItem("Mail Settings",new ImageIcon("images/mail.gif")));
		mMailSettings.addActionListener(this);
		mMailSettings.setMnemonic('M');
		mOptions.addSeparator();
		mRecompileTends=mOptions.add(new JMenuItem("Recompile TENDS",new ImageIcon("images/devel.gif")));
		mRecompileTends.addActionListener(this);
		mRecompileTends.setMnemonic('R');
		
		mOptions.addSeparator();		
		mViewLog=mOptions.add(new JMenuItem("View Log",new ImageIcon("images/log.gif")));
		mViewLog.addActionListener(this);
		mViewLog.setMnemonic('L');

		Helpme=mHelp.add(new JMenuItem("Help Topics",new ImageIcon("images/help.gif")));
		Helpme.addActionListener(this);
		Helpme.setMnemonic('H');

		mHelp.addSeparator();
		
		mAboutNote=mHelp.add(new JMenuItem("About TENDS",new ImageIcon("images/icon.gif")));
		mAboutNote.addActionListener(this);
		mAboutNote.setMnemonic('A');
		mSysinfo=mHelp.add(new JMenuItem("System Info",new ImageIcon("images/sysinfo.gif")));
		mSysinfo.addActionListener(this);
		mSysinfo.setMnemonic('S');

		mpopfontface=popup.add(new JMenuItem("Fonts",new ImageIcon("images/fonts.gif")));
		mpopfontface.addActionListener(this);
		mpopfontface.setMnemonic('F');

		mpopcut=popup.add(new JMenuItem("Cut",new ImageIcon("images/cut.gif")));
		mpopcut.addActionListener(this);
		mpopcut.setMnemonic('C');

		mpopcopy=popup.add(new JMenuItem("Copy",new ImageIcon("images/copy.gif")));
		mpopcopy.addActionListener(this);
		mpopcopy.setMnemonic('y');

		mpoppaste=popup.add(new JMenuItem("Paste",new ImageIcon("images/paste.gif")));
		mpoppaste.addActionListener(this);
		mpoppaste.setMnemonic('P');

		mpopdelete=popup.add(new JMenuItem("Delete",new ImageIcon("images/delete.gif")));
		mpopdelete.addActionListener(this);
		mpopdelete.setMnemonic('D');
		popup.pack();

		logs+=(".");
		splasher("Loading tool bars...",75,100);
	texttoolbar=new JToolBar();
	htmltoolbar=new JToolBar();
	textnew=new JButton(new ImageIcon("images/new.gif"));
	textopen=new JButton(new ImageIcon("images/open.gif"));
	textsave=new JButton(new ImageIcon("images/save.gif"));
	textfont=new JButton(new ImageIcon("images/fonts.gif"));
	textcut=new JButton(new ImageIcon("images/cut.gif"));
	textcopy=new JButton(new ImageIcon("images/copy.gif"));
	textpaste=new JButton(new ImageIcon("images/paste.gif"));
	textdelete=new JButton(new ImageIcon("images/delete.gif"));
	textmail=new JButton(new ImageIcon("images/compose.gif"));
	textabout=new JButton(new ImageIcon("images/icon.gif"));

	htmlgo=new JButton(new ImageIcon("images/go.gif"));
	htmlopen=new JButton(new ImageIcon("images/open.gif"));
	htmladdrlabel=new JLabel("Address : ");
	htmladdress=new JTextField(10);
	htmlhome=new JButton(new ImageIcon("images/home.gif"));
	htmledit=new JButton(new ImageIcon("images/edit.gif"));

	textnew.setSize(new Dimension(27,27));

	textnew.setToolTipText("Create new file");
	textopen.setToolTipText("Open file");
	textsave.setToolTipText("Save this file");
	textfont.setToolTipText("Change text font");
	textcut.setToolTipText("Cut selected text");
	textcopy.setToolTipText("Copy selected text");
	textpaste.setToolTipText("Paste selected text");
	textdelete.setToolTipText("Delete selected text");
	textmail.setToolTipText("Email this file");
	textabout.setToolTipText("About "+NAME);

	htmlopen.setToolTipText("Open an html file");
	htmlgo.setToolTipText("Fetch this address");
	htmladdress.setToolTipText("Enter web address here");
	htmlhome.setToolTipText("Goto your home page");
	htmledit.setToolTipText("Edit this web page");

	textnew.addActionListener(this);
	textsave.addActionListener(this);
	textopen.addActionListener(this);
	textfont.addActionListener(this);
	textcut.addActionListener(this);
	textcopy.addActionListener(this);
	textpaste.addActionListener(this);
	textdelete.addActionListener(this);
	textmail.addActionListener(this);
	textabout.addActionListener(this);

	htmlgo.addActionListener(this);
	htmlopen.addActionListener(this);
	htmladdress.addActionListener(this);
	htmlhome.addActionListener(this);
	htmledit.addActionListener(this);

	textnew.setPreferredSize(new Dimension(26,26));
	textsave.setPreferredSize(new Dimension(26,26));
	textopen.setPreferredSize(new Dimension(26,26));
	textfont.setPreferredSize(new Dimension(26,26));
	textcut.setPreferredSize(new Dimension(26,26));
	textcopy.setPreferredSize(new Dimension(26,26));
	textpaste.setPreferredSize(new Dimension(26,26));
	textdelete.setPreferredSize(new Dimension(26,26));
	textmail.setPreferredSize(new Dimension(26,26));
	textabout.setPreferredSize(new Dimension(26,26));
	htmlopen.setPreferredSize(new Dimension(26,26));
	htmlhome.setPreferredSize(new Dimension(26,26));
	htmledit.setPreferredSize(new Dimension(26,26));

	texttoolbar.add(textnew);
	texttoolbar.add(textopen);
	texttoolbar.add(textsave);
	texttoolbar.addSeparator();
	texttoolbar.add(textfont);
	texttoolbar.addSeparator();
	texttoolbar.add(textcut);
	texttoolbar.add(textcopy);
	texttoolbar.add(textpaste);
	texttoolbar.add(textdelete);
	texttoolbar.addSeparator();
	texttoolbar.add(textmail);
	texttoolbar.addSeparator();
	texttoolbar.addSeparator();
	texttoolbar.addSeparator();
	texttoolbar.add(textabout);

	htmltoolbar.add(htmlopen);
	htmltoolbar.add(htmlhome);
	htmltoolbar.add(htmledit);
	htmltoolbar.addSeparator();
	htmltoolbar.add(htmladdrlabel);
	htmltoolbar.add(htmladdress);
	htmltoolbar.addSeparator();
	htmltoolbar.add(htmlgo);

	textpane.add("North",texttoolbar);
	htmlpane.add("North",htmltoolbar);

	for(int i=76;i<=99;i++)
	splasher("Preparing for display",i,2);

	///////////
	}
//Begin : Miscllaneous methods
	public static void splasher(String msg,int percentage,int sleep)
	{
		Splash.progress.setString(msg);
		Splash.progress.setValue(percentage);
		try{
			if(sleep>1 && sleep <= 1000)
				Thread.sleep(sleep);
		}
		catch(Exception e){}
	}
	public static void initialize()
	{
		try{
		if(Service.getSetting("wordwrap","false").equalsIgnoreCase("true"))
			mainArea.setLineWrap(true);
		else
			mainArea.setLineWrap(false);
		if(Service.getSetting("showhiddenfiles","false").equalsIgnoreCase("true"))
			chooser.setFileHidingEnabled(false);
		else
			chooser.setFileHidingEnabled(true);
		}catch(Exception e){}
		try{
		int FR=Integer.parseInt(Service.getSetting("fontcolorred","0"));
		int FG=Integer.parseInt(Service.getSetting("fontcolorgreen","0"));
		int FB=Integer.parseInt(Service.getSetting("fontcolorblue","0"));
		int BR=Integer.parseInt(Service.getSetting("backcolorred","186"));
		int BG=Integer.parseInt(Service.getSetting("backcolorgreen","186"));
		int BB=Integer.parseInt(Service.getSetting("backcolorblue","255"));
		mainArea.setForeground(new Color(FR,FG,FB));
		mainArea.setBackground(new Color(BR,BG,BB));
		}catch(Exception e){logs+=e+"\n";}

		try{
		int fontSize=Integer.parseInt(Service.getSetting("fontsize","15"));
		String fontFace=Service.getSetting("fontface","Monospaced");
		Font f=new Font(fontFace,Font.PLAIN,fontSize);
		mainArea.setFont(f);
		}
		catch(Exception ee){}
	}

	//Open a new File
	public void newFile()
	{
		if (mainArea.getText().length()!= 0)
		{
			int newSaveCh=JOptionPane.showConfirmDialog(frame,"Do you want to Save changes ?","Save file",JOptionPane.YES_NO_OPTION);
			//yes=0 ;no=1
			if(newSaveCh==0)
				saveFile(mainArea.getText(),"");
			mainArea.setText("");
		}
	}
	
	//Exit the  TENDS Editor
	public void exitTENDS()
	{
		int saveCh=JOptionPane.showConfirmDialog(frame,"Do you want to exit TENDS ?","Exit TENDS",JOptionPane.YES_NO_OPTION);
		//yes=0 ;no=1
		if (saveCh==0)
			System.exit(0);
	}
	//Generic method for showing exceptions
	public void showException(String msg,Exception e)
	{
		logs+="### ERROR : "+msg+"\n"+String.valueOf(e)+"\n";
		
		Object[] options={"Dismiss","View log"};
		int choice=JOptionPane.showOptionDialog(frame,msg,"Error", 
			JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null, options, options[0]);
		if(choice==0) return;
		if(choice==1)
			Service.viewLogs();
	}

	public void setUI(String uiname)
	{
		String lfname;
		String metal="javax.swing.plaf.metal.MetalLookAndFeel";
		String motif="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		String win="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		String mac="com.sun.java.swing.plaf.mac.MacLookAndFeel";
		lfname=metal;
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
			SwingUtilities.updateComponentTreeUI(chooser);
		}
		catch(Exception e)
		{
			showException("Error loading Look and Feel",e);
		}
	}

	//Copy selected text into the clipboard
	public void processCopy()
	{
		String selection=mainArea.getSelectedText();
		if (selection==null)return;
		StringSelection clipstring=new StringSelection(selection);
		clippy.setContents(clipstring,clipstring);
	}

	// Cut the selected text into the clipboard
	public void processCut()
	{
		String selection=mainArea.getSelectedText();
		if(selection==null)return;
		StringSelection clipstring=new StringSelection(selection);
		clippy.setContents(clipstring,clipstring);
		mainArea.replaceRange("",mainArea.getSelectionStart(),mainArea.getSelectionEnd());
	}

	//Paste the text from clipboard to editor
	public void processPaste()
	{
		Transferable clipdata=clippy.getContents(TENDS.this);
		try
		{
			String clipstring=(String)clipdata.getTransferData(DataFlavor.stringFlavor);
			mainArea.replaceRange(clipstring,mainArea.getSelectionStart(),mainArea.getSelectionEnd());
		}
		catch(Exception clipbrdExp)
		{
		}
	}

	// delete the selected text
	public void processDelete()
	{
		String selection=mainArea.getSelectedText();
		if(selection==null)return;
		StringSelection clipstring=new StringSelection(selection);
		mainArea.replaceRange("",mainArea.getSelectionStart(),mainArea.getSelectionEnd());
	}

	// Popup some silly file info
	public void giveFileInfo()
	{
		int rows,columns,size,words,lines,tab;
		rows=mainArea.getRows();
		columns=mainArea.getColumns();
		size=mainArea.getText().length();
		StringTokenizer tokens=new StringTokenizer(mainArea.getText());
		words=tokens.countTokens();
		lines=mainArea.getLineCount();
		tab=mainArea.getTabSize();
		String info="File Information"+"\n\nRows Count  = "
		+rows+"\nColumns Count  = "+columns+"\nNumber of Lines  = "+lines
		+"\nFile Size  = "+size+"\nNumber of Words  = "+words
		+"\nTab Size  = "+tab;
		JOptionPane.showMessageDialog(frame,info,"File Information",JOptionPane.INFORMATION_MESSAGE); 
	}

	//This is about the TENDS Editor ;-)
	public static void aboutAnuragsTENDS()
	{
		String aboutMsg=NAME+"  "+version
		+"(C) Anurag Patel \n[terminal_anu@rediffmail.com]\n\n"
		+"BE - Instrumentation\nDepartment of Instrumentation\n"
		+"SIGCOE - Vashi, New Mumbai\n";
		String[] options={" Wow !"};
		int result = JOptionPane.showOptionDialog(frame,aboutMsg,"About Anurag's TENDS",
		JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("images/splash.jpg"),
		options,options[0]);
	}

	// Display welcome message
	public void welcome()
	{
		String features="Text Editor\nImage Viewer\nWeb Browser\nFile Transfer\nDynamic Look & Feel\nFile Compression\nBytecode Decompiler\nEmail (POP3)";
		String aboutMsg=NAME+"   "+version+"\nAn all in one interface for\n\n"+features;
		String[] options={"OK","Start up"};
		int result = JOptionPane.showOptionDialog(frame,aboutMsg,"Welcome to Anurag's TENDS",
		JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("images/java.gif"),
		options,options[0]);
		switch(result)
		{
		case 0:break;
		case 1:openHtml("help/help.html");
				tabpane.setSelectedIndex(1);
				break;
		}
	}

	//This is a common method for all the other methods to fetch a filename
	//from the user
	public static String getFileName(int choice,Frame frm)
	{
		int returnVal=JFileChooser.CANCEL_OPTION;
		String filename;
		String ff="";

		String mode=Service.getSetting("usenativefiledialog","false");
		if(mode.equalsIgnoreCase("true"))
		{
			ff=nativeFileDialog(choice,frm);
		}
		else
		{
		switch (choice)
		{
		case 0:	previewlabel.setIcon(new ImageIcon("images/folder1.png"));
				returnVal=chooser.showOpenDialog(frm);
				break;
		case 1: previewlabel.setIcon(new ImageIcon("images/floppy1.png"));
				returnVal=chooser.showSaveDialog(frm);
				break;
		default: break;
		}
		if(returnVal==JFileChooser.APPROVE_OPTION)
		{
			filename=String.valueOf(chooser.getSelectedFile());
			if(filename.length()!=0)
			{
				ff=filename;
			}
		}
		}
		return ff;
	}
	public static String nativeFileDialog(int choice,Frame frm)
	{
		FileDialog fd=new FileDialog(frame);
		String ff="";
		switch (choice)
		{
		case 0:	
				fd.setMode(FileDialog.LOAD);
				fd.show();
				break;
		case 1: 
				fd.setMode(FileDialog.SAVE);
				fd.show();
				break;
		default: break;
		}
		String dir=fd.getDirectory();
		String filename=fd.getFile();
		if(dir!=null && filename!=null)
			ff=dir+filename;
		return ff;
	}

	//Compress a ASCII file using Adler32(ZIP) algorithm
	public void zipFile()
	{
		JOptionPane.showMessageDialog(frame,"TENDS Archive engine version1.0\n\nPlease select input file\nCAUTION : File should be in ASCII format","File to be compressed",JOptionPane.INFORMATION_MESSAGE);
		String fname=getFileName(0,frame);
		if(fname.length()==0)return;
		JOptionPane.showMessageDialog(frame,"Please provide location for saving archive ","Archive name",JOptionPane.INFORMATION_MESSAGE);
		String zipfl=getFileName(1,frame);
		if(zipfl.length()==0)
		{
			JOptionPane.showMessageDialog(frame,"Invalid archive name : The specified Filename is Invalid", "Invalid archive name", JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			try
			{
			zipfl=zipfl+".zip";
			FileOutputStream fos=new FileOutputStream(zipfl);
			CheckedOutputStream csum=new CheckedOutputStream(fos, new Adler32());
			ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(csum));
			out.setComment("TENDS Zipping Engine  version 1.0 TENDS copyright (C) 2003 Anurag Patel");
			out.setLevel(9);

	        BufferedReader in=new BufferedReader(new FileReader(fname));
		    out.putNextEntry(new ZipEntry(fname));
			int c;
	        while((c = in.read()) != -1) out.write(c);
		    in.close();
		    out.close();
		    String checksum="TENDS Zipping Engine\nThe file was compressed successfully"+("Checksum : "+csum.getChecksum().getValue())+"\nCompression format = ZIP"+"\nFile Compressed = "+fname+"\nDestination directory = "+zipfl;
		    JOptionPane.showMessageDialog(frame,checksum, "File Compressed", JOptionPane.INFORMATION_MESSAGE);
			logs+="Compressed file : "+zipfl+"\n";
			}
			catch(Exception zipE)
			{
				showException("Could not compress the File  :  ",zipE);
			}
		}
	}

	//Save the file passed as parameter
	public void saveFile(Object fileContent,String filename)
	{
		if(filename.length()==0)
			filename=getFileName(1,frame);
		if(filename.length()!=0)
		{
			try
			{
				File f=new File(filename);
				if(!f.exists())
				{
					DataOutputStream dos=new DataOutputStream(new FileOutputStream(filename));
					dos.writeBytes((String)fileContent);
					dos.flush();
					dos.close();
					logs+="Saved file : "+filename+"\n";
				}
				else
				{
					int newSaveCh=JOptionPane.showConfirmDialog(frame,"File already exists! Overwrite this file ?","Overwrite file ?",JOptionPane.YES_NO_OPTION);
					//yes=0 ;no=1
					if(newSaveCh==0)
					{
						f.delete();
						saveFile(fileContent,filename);
					}
				}
			}
			catch (Exception fexp)
			{
				showException("Could not Save File",fexp);
			}
		}
	}

	//Check if the filename corresponds to an image (GIF/JPG/PNG)
	public boolean checkForImage(String fn)
	{
		fn=fn.toLowerCase();
		boolean isImage=false;
		if(fn.endsWith(".gif")||fn.endsWith(".png")||fn.endsWith(".jpeg")||fn.endsWith(".jpg")||fn.endsWith(".jpe"))
		{
			isImage=true;
			openImage(fn);
		}
		return isImage;
	}

	//Check if the filename corresponds to HTML/RTF file
	public boolean checkForHTML(String fn)
	{
		fn=fn.toLowerCase();
		boolean ishtml=false;
		if(fn.endsWith(".htm")||fn.endsWith(".html")||fn.endsWith(".rtf"))
		{
			ishtml=true;
			openHtml(fn);
		}
		return ishtml;
	}

	//Open the user selected file
	public void openFile(String filename)
	{
		if(filename.length()==0)
			filename=getFileName(0,frame);
		if(filename.length()!=0)
		{
			if(!checkForImage(filename)&&!checkForHTML(filename))
			{
			try
			{
				DataInputStream dis=new DataInputStream(new FileInputStream(filename));
				int nob=dis.available();
				byte b[]=new byte[nob];
				dis.read(b,0,nob);
				mainArea.setText(new String(b));
				dis.close();
				logs+="Opened file : "+filename+"\n";
				tabpane.setSelectedIndex(0);
			} 
			catch(IOException oexp)
			{
				showException("Could not open the file...",oexp);
			}
			}
		}
	}

	//  boolean b=javax.swing.plaf.basic.BasicHTML.isHTMLString(String);
	//OPen an HTML file
	public void openHtml(String fn)
	{
		String protocol=fn.substring(0,4);
		try
		{
			File f = new File (fn);
			String s="";
			if(protocol.startsWith("http"))
			{
				openWebPage(fn);
			}
			else
			{
				s = f.getAbsolutePath();
				s = "file:"+s;
			}
			URL url = new URL(s);
//			Document doc = htmleditor.getDocument();
			htmleditor.setPage(url);
			htmladdress.setText(url.toString());
			tabpane.setSelectedIndex(1);
		}
		catch (MalformedURLException e)
		{showException("Malformed URL : "+fn,e);}
		catch (IOException e)
		{
			showException("Cannot open File  HTML File  : "+fn,e);
		}
		catch(Exception e)
		{	showException("Error loading File : "+fn,e);
		}
	}
	//Open a web page
	public void openWebPage(String address)
	{
		try{
		URL url = new URL(address);
		htmladdress.setText(url.toString());
//		Document doc = htmleditor.getDocument();
		htmleditor.setPage(url);
		tabpane.setSelectedIndex(1);
		}
		catch (MalformedURLException e)
		{address="http://"+address;openWebPage(address);}
		catch (IOException e)
		{	showException("Cannot find server : "+address,e);	}
		catch(Exception e)
		{	showException("Error loading URL : "+address,e);}
	}

	//Open an image
	public void openImage(String filename)
	{
		new ImageViewer(filename);
	}
	
	public void printPage()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();
//		printJob.setPrintable(this);
		PageFormat pf = printJob.pageDialog(printJob.defaultPage());
		if(true)
		{
			try
			{
				printJob.print();
			}
			catch(Exception ex)
			{
                showException("Cannot print document...",ex);
			}
		}
	}

	//Search for a file using its pattern
	public void searchFile()
	{
		String filename=Service.searchFile();
		if(filename=="."){}
		else{openFile(filename);}
	}

	//Display system information
	public void systemInformation()
	{
		String msg,OS,OSarch,Javaver,zone,JavaInstall,OSver,Graphicsenv,
			   User,userhome,currentDir,vendor;
		OS=System.getProperty("os.name");
		OSver=System.getProperty("os.version");
		OSarch=System.getProperty("os.arch");
		Graphicsenv=System.getProperty("java.awt.graphicsenv");
		User=System.getProperty("user.name");
		userhome=System.getProperty("user.home");
		zone=System.getProperty("user.timezone");
		Javaver=System.getProperty("java.version");
		JavaInstall=System.getProperty("java.home");
		currentDir=System.getProperty("user.dir");
		vendor=System.getProperty("java.vendor");
		msg="Operating System = "+OS+"\nOS Architecture = "+OSarch
		+"\nOS version = "+OSver+"\nGraphics Environment = "+Graphicsenv
		+"\nUser Name = "+User+"\nUser's Home directory = "+userhome
		+"\nJava version = "+Javaver+"\nTime zone = "+zone
		+"\nJava Install directory = "+JavaInstall+"\nCurrent Directory = "+currentDir
		+"\nJava Runtime vendor = "+vendor;
		String[] options={"OK"};
		logs+="System Information :---\n"+msg+"\n";
		int result = JOptionPane.showOptionDialog(frame,msg,"System Information",
		JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("images/compatible.gif"),
		options,options[0]);
	}

	//Import a file from TENDS Server
	public void importFile()
	{
		String IPmsg="Enter IP Address of the TENDS Server";
		String winsmsg="For Windows NT networks you may use WINS names";
		JLabel lblIP=new JLabel(IPmsg);
		JLabel lblwins=new JLabel(winsmsg);
		JTextField txtIP=new JTextField(Service.getSetting("tendsserver","127.0.0.1"));
		JLabel space1=new JLabel("__________________________________________________");
		JLabel lblPort=new JLabel(">>>  This is the PORT Number on which Server runs");

		JSlider SliderPort=new JSlider(5990,6000,5998);
		SliderPort.setEnabled(false);
 		SliderPort.setMajorTickSpacing(2);
 		SliderPort.setMinorTickSpacing(1);
 		SliderPort.setPaintLabels(true);
 		SliderPort.setSnapToTicks(true);

		JLabel space2=new JLabel("__________________________________________________");
		JLabel lblFile=new JLabel(">>>  Enter the Name of file existing on the TENDS Server");
		JTextField txtFile=new JTextField();

		Object[] serverDet=new Object[9];
		serverDet[0]=lblIP;
		serverDet[1]=lblwins;
		serverDet[2]=txtIP;
		serverDet[3]=space1;
		serverDet[4]=lblPort;
		serverDet[5]=SliderPort;
		serverDet[6]=space2;
		serverDet[7]=lblFile;
		serverDet[8]=txtFile;

		String[] options={"Import","Cancel"};
		int result = JOptionPane.showOptionDialog(frame,serverDet,"Import a File from TENDS Server",
		JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,new ImageIcon("images/tserver.png"),
		options,options[0]);

		String impFlnm=txtFile.getText();
		String IPAddress=txtIP.getText();
		int portnumber=SliderPort.getValue();

		if(result==1)return;
		if(result==0)
		{
		if(impFlnm.length()==0||IPAddress.length()==0)
		{
			JOptionPane.showMessageDialog(frame,"Please Enter the Machine's IP & Filename", "Invalid IP or Filename", JOptionPane.WARNING_MESSAGE);
			importFile();
		}
		else
		{
		TENDSLink data=new TENDSLink();
		data.importFileName=impFlnm.trim();
		data.importFileContent="";
		data.success=false;
		try
		{
			Socket toServer=new Socket(IPAddress,portnumber);
			ObjectOutputStream streamToServer=new ObjectOutputStream(toServer.getOutputStream());
			streamToServer.writeObject((TENDSLink)data);
			ObjectInputStream streamFromServer=new ObjectInputStream(toServer.getInputStream());
			logs+="Successfully connected to TENDSServer : "+IPAddress+"\n";
			try
			{
				data=(TENDSLink)streamFromServer.readObject();
				Service.saveSetting("tendsserver",IPAddress);
				processInput(data.importFileContent,data.success);
				logs+="Data synchronization completed\n";
			}
			catch(Exception ee)
			{
				showException("Cannot get response from server",ee);
			}
		}
		catch(Exception e)
		{
			showException(("Cannot find Server  : ( "+IPAddress+" ) running on port ( "+portnumber+" )").toString(),e);
		}
		}
		}
	}

	//Process the imported file
	public void processInput(Object idata,boolean success)
	{
		String matter=String.valueOf(idata);
		if(success)
		{
			mainArea.setText(matter);
			tabpane.setSelectedIndex(0);
			int newSaveCh=JOptionPane.showConfirmDialog(frame,"Do you want to Save the imported file ?","Save Imported File",JOptionPane.YES_NO_OPTION);
			//yes=0 ;no=1
			if(newSaveCh==0)
				saveFile(matter,"");
		}
		else
		{	JOptionPane.showMessageDialog(frame,"Response from Server : Error accessing file","File access error",JOptionPane.ERROR_MESSAGE);
		}
	}

	//Execute a command
	public void runCommand()
	{
		String cmd=JOptionPane.showInputDialog(frame,"Enter Command to run...");
		if(cmd.length()==0||cmd==null)
			return;
		try
		{
			Runtime.getRuntime().exec(cmd);
			logs+="Successfully executed : "+cmd+"\n";
		}
		catch (Exception e)
		{
			showException("Could not run command.",e);
		}/*Make sure the command is accessible from PATH settings*/
	}

	//Decompile a class file
	public void decompileClassFile()
	{
		String f="",jad,actualFile;
		f=getFileName(0,frame);
		if(f==null)return;
		f=f.toLowerCase();
		if(f.endsWith(".class"))
		{
			try
			{
				jad=chooser.getName(chooser.getSelectedFile());
				jad=jad.substring(0,jad.length()-6);
				Runtime.getRuntime().exec("jad "+f);
				logs+="Successfully decompiled : "+f+"\n";
				Thread.sleep(1000);
			}
			catch(Exception e1)
			{
				
				JOptionPane.showMessageDialog(frame,"Decompile Engine not found","Error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			try
			{
				JOptionPane.showMessageDialog(frame,"File  "+f+"  was decompiled successfully","Success",JOptionPane.INFORMATION_MESSAGE);
				f=System.getProperty("user.dir")+"\\"+jad+".jad";
				FileInputStream fis=new FileInputStream(f);
				int nob=fis.available();
				byte b[]=new byte[nob];
				fis.read(b,0,nob);
				mainArea.setText(new String(b));
				actualFile=mainArea.getText().substring(141);
				mainArea.setText("/**  Decompiled by  TENDS  Copyright (C) 2003  Anurag Patel*/"+actualFile);
				fis.close();
				tabpane.setSelectedIndex(0);
				int Ch=JOptionPane.showConfirmDialog(frame,"Do you want to Save the Decompilation ?","Save file",JOptionPane.YES_NO_OPTION);
				if(Ch==0)saveFile(mainArea.getText(),"");
			}
			catch(IOException oexp)
			{
				
				String msg="Could not load decompiled file\nFile ["+f+"] could not be reloaded \n"+oexp;
				JOptionPane.showMessageDialog(frame,msg,"Error",JOptionPane.ERROR_MESSAGE);
			}
			try
			{
				File ff=new File(jad+".jad");
				ff.delete();
			}
			catch(Exception ee){}
		}
		else
		{
			if(f.length()!=0)
				JOptionPane.showMessageDialog(frame,"This File is not a valid Class file","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	//Mail compose UI
	public void composeMail(String text)
	{
		new MailComposer().settext(mainArea.getText());
	}
	//Grab the pixels on the screen
	public void takeScreenshot()
	{
		String filename=getFileName(1,frame);
		if(filename.equalsIgnoreCase(""))
			return;
		else
			filename=filename+".jpg";
		boolean b=Service.captureScreen(filename,frame);
		if(b)
			openFile(filename);
	}
	//Recompile TENDS
	public void recompile()
	{
		int newSaveCh=JOptionPane.showConfirmDialog(frame,"Do you want to recompile TENDS?\nRecompilation can proceed only if JDK is installed","Recompile TENDS",JOptionPane.YES_NO_OPTION);
		//yes=0 ;no=1
		System.out.println(System.getProperty("java.compiler"));
		if(System.getProperty("java.compiler")==null)
		{
			showException("System property \"java.compiler\" is null\nRecompilation can't continue",new Exception("\"java.compiler\" = null\nJDK Compiler not found"));
		}
		else
		{
			Compiler.enable();
			System.out.println(Compiler.compileClasses("Config"));
			System.out.println(Compiler.compileClasses("Service"));
			System.out.println(Compiler.compileClasses("Pop3"));
			System.out.println(Compiler.compileClasses("Music"));
			System.out.println(Compiler.compileClasses("Mailer"));
			System.out.println(Compiler.compileClasses("MailComposer"));
			System.out.println(Compiler.compileClasses("Inbox"));
			System.out.println(Compiler.compileClasses("ImageViewer"));
			System.out.println(Compiler.compileClasses("TENDS"));
			System.out.println(Compiler.compileClasses("*.*"));
			logs+="Successfully recompiled all classes\n";
			try
			{
				Runtime.getRuntime().exec("javac *.java");
			}
			catch(Exception ee)
			{showException("System property \"java.compiler\" is null\nRecompilation can't continue",new Exception("\"java.compiler\" = null\nJDK Compiler not found"));
			}			
		}
	}

	//Begin Window events
 	public void windowClosing(WindowEvent w){exitTENDS();}
	public void windowOpened(WindowEvent w){}
	public void windowClosed(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeactivated(WindowEvent w){}
	//Begin Hyperlink event

	//Property change events to manage the icon status of FIleChooser
	public void propertyChange(PropertyChangeEvent e)
	{
	    String prop = e.getPropertyName();
	    if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
		{
		f = (File) e.getNewValue();
		if(chooser.isShowing())
		{
			loadImage();
			repaint();
		}
	    }
	}
	public void loadImage()
	{
	    if(f != null)
		{
		ImageIcon tmpIcon = new ImageIcon(f.getPath());
		if(tmpIcon.getIconWidth() > 90)
		{
			thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
		}
		else
		{
		    thumbnail = tmpIcon;
		}
	    }
		previewlabel.setIcon(thumbnail);
	}
	public void paint(Graphics g)
	{
		if(thumbnail == null)
		{
			loadImage();
	    }
	    if(thumbnail != null)
		{
			int x = getWidth()/2 - thumbnail.getIconWidth()/2;
			int y = getHeight()/2 - thumbnail.getIconHeight()/2;
			if(y < 0)
			{
		    y = 0;
			}
			if(x < 5)
			{
		    x = 5;
			}
			thumbnail.paintIcon(this, g, x, y);
		}
	}
    
	public void hyperlinkUpdate(HyperlinkEvent he)
	{
		if(he.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			openWebPage(he.getURL().toString());
    }

	public void keyPressed(KeyEvent e )
	{
		boolean thwart=false;
		String tmp=mainArea.getText();
		if(e.getKeyCode()==115&&e.isAltDown())
		{
			thwart=true;
		}
		if(e.getKeyCode()==112)
		{
			openHtml("help/help.html");
			tabpane.setSelectedIndex(1);
		}
		else if(e.getKeyCode()==113)
		{
			tabpane.setSelectedIndex(0);
			if(mainArea.getText().length()!=0)
				saveFile(mainArea.getText(),"");
		}
		else if(e.getKeyCode()==114)
		{
			openFile("");
		}
		else if(e.getKeyCode()==115)
		{
			tabpane.setSelectedIndex(0);
			mainArea.setText(htmleditor.getText());
		}
		else if(e.getKeyCode()==116)
		{
			openWebPage(htmladdress.getText());
		}
		else if(e.getKeyCode()==117)
		{
			tabpane.setSelectedIndex(0);
		}
		else if(e.getKeyCode()==118)
		{
			tabpane.setSelectedIndex(1);
		}
		else if(e.getKeyCode()==119)
		{
			importFile();
		}
		if(thwart)
			mainArea.setText(tmp);
	}

    public void keyReleased( KeyEvent e ){} 
    public void keyTyped( KeyEvent e ){}

	public void mouseClicked(MouseEvent e)
	{ 
		if(SwingUtilities.isRightMouseButton(e))
		{
		popup.show(e.getComponent(),e.getX(),e.getY());
		}
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	public void run()
	{
		while(true)
		{
			try
			{
				garbageThread.sleep(60000);
				logs+=("Freeing unused Memory...");
				System.gc();
				System.runFinalization();
				logs+=("\t[ OK ]");
				logs+=("\t"+Runtime.getRuntime().freeMemory()+" Bytes allocated\n");
			}
			catch(Exception garbage_e)
			{
				logs+=("Garbage collector interupted...\n");
			}
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==mNew||e.getSource()==textnew)
		{
			newFile();
		}
		else if(e.getSource()==mSaveas||e.getSource()==textsave)
		{
			if(mainArea.getText().length()!=0)saveFile(mainArea.getText(),"");
		}
		else if(e.getSource()==mOpen||e.getSource()==textopen||e.getSource()==htmlopen)
		{
			openFile("");
		}
		if(e.getSource()==mPrint)
		{
			printPage();
		}
		if(e.getSource()==mRun)
		{
			runCommand();
		}
		if(e.getSource()==mSearch)
		{
			searchFile();
		}
		else if(e.getSource()==mCompress)
		{
			//zipFile();
			new Archiver("a");
		}
		else if(e.getSource()==mFont||e.getSource()==textfont||e.getSource()==mpopfontface)
		{
			new Config("editor");
		}
		else if(e.getSource()==mCut||e.getSource()==textcut||e.getSource()==mpopcut)
		{
			processCut();
		}
		else if(e.getSource()==mCopy||e.getSource()==textcopy||e.getSource()==mpopcopy)
		{
			processCopy();
		}
		else if(e.getSource()==mPaste||e.getSource()==textpaste||e.getSource()==mpoppaste)
		{
			processPaste();
		}
		else if(e.getSource()==mDelete||e.getSource()==textdelete||e.getSource()==mpopdelete)
		{
			processDelete();
		}
		else if(e.getSource()==mFileInfo)
		{
			giveFileInfo();
		}
		else if(e.getSource()==mAboutNote||e.getSource()==textabout)
		{
			aboutAnuragsTENDS();
		}
		else if(e.getSource()==mSysinfo)
		{
			systemInformation();
		}
		else if(e.getSource()==mExit)
		{
			exitTENDS();
		}
		else if(e.getSource()==mTSImport)
		{
			importFile();
		}
		else if(e.getSource()==mDecomp)
		{
			decompileClassFile();
		}
		else if(e.getSource()==mSendMail||e.getSource()==textmail)
		{
			composeMail(mainArea.getText());
		}
		else if(e.getSource()==htmlgo||e.getSource()==htmladdress)
		{
			openWebPage(htmladdress.getText());
		}
		else if(e.getSource()==htmlhome)
		{
			openWebPage(Service.getSetting("homepage"));
		}
		else if(e.getSource()==htmledit)
		{
			mainArea.setText(htmleditor.getText());
			tabpane.setSelectedIndex(0);
		}
		else if(e.getSource()==mReadmail)
		{
			new Inbox();
		}
		else if(e.getSource()==mMailSettings)
		{
			new Config("mail");
		}
		else if(e.getSource()==mGenSettings)
		{
			new Config("general");
		}
		else if(e.getSource()==mEditorSettings)
		{
			new Config("editor");
		}
		else if(e.getSource()==mRecompileTends)
		{
			recompile();
		}
		else if(e.getSource()==mViewLog)
		{
			Service.viewLogs();
		}
		else if(e.getSource()==mScreenshot)
		{
			previewlabel.setIcon(new ImageIcon("images/camera.gif"));
			takeScreenshot();
		}
		else if(e.getSource()==mMusic)
		{
			new Music();
		}
		else if(e.getSource()==Helpme)
		{
			new Helper();
		}
		else if(e.getSource()==mJava)
		{
			setUI("metal");
		}
		else if(e.getSource()==mMotif)
		{
			setUI("motif");
		}
		else if(e.getSource()==mWindows)
		{
			setUI("windows");
		}
		else if(e.getSource()==mMac)
		{
			setUI("macintosh");
		}
	}
}

class TENDSLink extends Object implements java.io.Serializable
{
	String importFileName;
	Object importFileContent;
	boolean success;
}
