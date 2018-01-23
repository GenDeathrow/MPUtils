package com.gendeathrow.mputils.client.gui.elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;

import com.gendeathrow.mputils.api.client.gui.ScrollWindowBase;
import com.gendeathrow.mputils.api.client.gui.elements.TextScrollWindow;
import com.gendeathrow.mputils.core.MPUtils;

public class TextEditor extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -415292092413398469L;

	
	public JTextArea area = new JTextArea(20,120);
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false;
	
	public ScrollWindowBase gui;
	TextScrollWindow guiTextArea;
	
	public TextEditor(final ScrollWindowBase gui) 
	{
		this.gui = gui;
		
		guiTextArea = (TextScrollWindow) gui.scrollWindow;
		
		this.setTitle("MPUtils Text Editor");
		this.setPreferredSize(new Dimension(800, 400));
		this.setSize(800, 400);
		
		area.setFont(new Font("Monospaced",Font.PLAIN,12));
		area.setLineWrap(true);
		area.setWrapStyleWord(true);

		
	    KeyListener keyListener = new KeyListener()
	    {
			@Override
			public void keyPressed(java.awt.event.KeyEvent arg0) 
			{
				
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent arg0) 
			{
				guiTextArea.setRawData(area.getText());
			}

			@Override
			public void keyTyped(java.awt.event.KeyEvent arg0) 
			{
				
			}
	    };
		area.addKeyListener(keyListener);
		
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll,BorderLayout.CENTER);

		ActionMap m = area.getActionMap();
		Action Cut = m.get(DefaultEditorKit.cutAction);
		Action Copy = m.get(DefaultEditorKit.copyAction);
		Action Paste = m.get(DefaultEditorKit.pasteAction);
		Action ColorBlack = null;
		
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		add(toolPanel,BorderLayout.PAGE_START);
		
		JToolBar colorBar = new JToolBar();
		toolPanel.add(colorBar,BorderLayout.PAGE_START);
		colorBar.setFloatable(false);

		
		JButton cblack = colorBar.add(ActionPicker("Black", "\u00A70", "cblack.gif")), 
				cdarkblue = colorBar.add(ActionPicker("Dark Blue", "\u00A71", "cdarkblue.gif")),
				cdarkgreen = colorBar.add(ActionPicker("Dark Green", "\u00A72", "cdarkgreen.gif")),
				cdarkaqua = colorBar.add(ActionPicker("Dark Aqua", "\u00A73", "cdarkaqua.gif")), 
				cdarkred = colorBar.add(ActionPicker("Dark Red", "\u00A74", "cdarkred.gif")),
				cdarkpurple = colorBar.add(ActionPicker("Dark Purple", "\u00A75", "cdarkpurple.gif")),
				cgold = colorBar.add(ActionPicker("Gold", "\u00A76", "cgold.gif")), 
				cgrey = colorBar.add(ActionPicker("Grey", "\u00A77", "clightgrey.gif")),
				cdarkgrey = colorBar.add(ActionPicker("DarkGrey", "\u00A78", "cdarkgrey.gif")),
				cblue = colorBar.add(ActionPicker("Blue", "\u00A79", "cblue.gif")), 
				cgreen = colorBar.add(ActionPicker("Aqua", "\u00A7b", "caqua.gif")),
				caqua = colorBar.add(ActionPicker("Green", "\u00A7a", "cgreen.gif")),
				cred = colorBar.add(ActionPicker("Red", "\u00A7c", "cred.gif")), 
				clightpurple = colorBar.add(ActionPicker("Light Purple", "\u00A7d", "clightpurple.gif")), 
				cyellow = colorBar.add(ActionPicker("Yellow", "\u00A7e", "cyellow.gif")),
				cwhite = colorBar.add(ActionPicker("White", "\u00A7f", "cwhite.gif"));

		
		JToolBar tool = new JToolBar();
		toolPanel.add(tool,BorderLayout.PAGE_END);
		tool.setFloatable(false);
		
		
		JButton bold = tool.add(ActionPicker("Bold", "\u00A7l", "bold.gif")), 
				strike = tool.add(ActionPicker("Strike", "\u00A7m", "strike.gif")),
				underline = tool.add(ActionPicker("Underline", "\u00A7n", "underline.gif")),
				italic = tool.add(ActionPicker("Italicize", "\u00A7o", "italicize.gif")),
				reset = tool.add(ActionPicker("reset", "\u00A7r", "reset.gif")),
				obfuscated = tool.add(ActionPicker("obfuscated", "\u00A7k", "obfuscated.gif"));
		
		this.pack();
		
		setExtendedState(JFrame.NORMAL);

	}

	public Action ActionPicker(String ID, final String unicode, String image )
	{
		return ActionPicker(ID, unicode, image, null);
	}
	
	public Action ActionPicker(String ID, final String unicode, String image, String hover)
	{
		Action action = new AbstractAction(ID, new ImageIcon(loadResource(image))) 
		{
			private String uni = unicode;
			
			public void actionPerformed(ActionEvent e) 
			{

				String selectedtext = area.getSelectedText();
				
				if(selectedtext != null)
				{
					int start = area.getSelectionStart();
					int end = area.getSelectionEnd();
					
					area.insert("\u00A7r", end);				
					area.insert(uni, start);
					
					area.requestFocus();

				}
				else 
				{
					area.insert(uni, area.getCaretPosition());
				}
				guiTextArea.setRawData(area.getText());
				area.requestFocus();
			}


	};
		return action;
	}
	
	public void setTextArea(TextScrollWindow guitextarea)
	{
		this.guiTextArea = guitextarea;
	}
	
	private URL loadResource(String file)
	{
		return this.getClass().getResource("/assets/" + MPUtils.MODID+ "/textures/editor/"+ file);
	}
}
