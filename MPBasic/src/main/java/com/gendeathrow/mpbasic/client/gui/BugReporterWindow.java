package com.gendeathrow.mpbasic.client.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.lwjgl.opengl.Display;

import com.gendeathrow.mpbasic.core.MPBSettings;
import com.gendeathrow.mputils.core.MPUtils;
import com.gendeathrow.mputils.utils.MPFileUtils;
import com.gendeathrow.mputils.utils.MPInfo;
import com.gendeathrow.mputils.utils.Tools;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class BugReporterWindow 
{

	public JFrame frame;
	public JTextField textField;
	
	private final JFileChooser fc = new JFileChooser();
	
	
	private JTextField titlefield;
	private JTextArea descriptiontextArea;
	private List<String> list;
	private JCheckBox gistCheckBox;
	private JTextArea resultTextArea;
	private JTextArea generatedReport;
	private JTextField emailField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		BugReporterWindow window = new BugReporterWindow();
		window.frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public BugReporterWindow() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
        
        list = Lists.newArrayList(new String[] {
        		String.format("%s %dbit", new Object[]{System.getProperty("java.version"), Integer.valueOf(Minecraft.getMinecraft().isJava64bit() ? 64 : 32)}), 
        		String.format("% 2d%% %03d/%03dMB", new Object[]{Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i))}), 
        		String.format("% 2d%% %03dMB", new Object[]{Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j))}), 
        		"", 
        		String.format("%s", new Object[]{OpenGlHelper.getCpu()}), 
        		"", 
        		String.format("%dx%d (%s)", new Object[]{Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GlStateManager.glGetString(7936)}), 
        		GlStateManager.glGetString(7937), 
        		GlStateManager.glGetString(7938)});

		
		frame = new JFrame();
		frame.setBounds(100, 100, 654, 577);
		
		final JPanel panelForum = new JPanel();
		panelForum.setVisible(false);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		frame.getContentPane().add(panelForum, "name_286846318537830");
		panelForum.setLayout(null);
		
		final JPanel panelOption = new JPanel();
		frame.getContentPane().add(panelOption, "name_286846332259221");
		panelOption.setLayout(null);
		
		final JPanel panelResult = new JPanel();
		frame.getContentPane().add(panelResult, "name_289038900546144");
		panelResult.setLayout(null);
		
		
		
		JButton cpclip = new JButton("Copy to Clipboard");
		cpclip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Tools.CopytoClipbard(generatedReport.getText());
			}
		});
		
		cpclip.setBounds(234, 368, 184, 23);
		panelOption.add(cpclip);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(56, 37, 529, 320);
		panelOption.add(scrollPane);
		
		generatedReport = new JTextArea();
		generatedReport.setEditable(false);
		generatedReport.setLineWrap(true);
		scrollPane.setViewportView(generatedReport);
		
		JButton btnNewButton = new JButton("Go to Issue Tracker");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				goToHttp(MPBSettings.issuetrackerURL);
			}
		});
		btnNewButton.setBounds(224, 491, 216, 23);
		panelOption.add(btnNewButton);
		
		JTextArea txtrCopyReport = new JTextArea();
		txtrCopyReport.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtrCopyReport.setText("1.Click \"Copy Report to Clipboard\"\r\n\r\n2. Click \"Go to Issue Tracker\"\r\n\r\n3. Follow instructions for pasing your Issue.");
		txtrCopyReport.setBackground(SystemColor.activeCaptionBorder);
		txtrCopyReport.setEditable(false);
		txtrCopyReport.setLineWrap(true);
		txtrCopyReport.setBounds(224, 402, 263, 82);
		panelOption.add(txtrCopyReport);
			
		JLabel label = new JLabel("Title:");
		label.setBounds(10, 151, 46, 14);
		panelForum.add(label);
		
		JLabel label_1 = new JLabel("Description:");
		label_1.setBounds(10, 176, 62, 14);
		panelForum.add(label_1);
		
		JLabel igntext = new JLabel("IGN: "+ Minecraft.getMinecraft().getSession().getUsername());
		igntext.setBounds(10, 11, 271, 14);
		panelForum.add(igntext);
		
		JLabel forgeversiontext = new JLabel("Forge Verison: "+ ForgeVersion.getVersion());
		forgeversiontext.setBounds(10, 36, 271, 14);
		panelForum.add(forgeversiontext);
		
		JLabel packversiontext = new JLabel("Pack Version: "+ MPInfo.version);
		packversiontext.setBounds(10, 61, 271, 14);
		panelForum.add(packversiontext);
		
		JLabel ostext = new JLabel("OS: "+System.getProperty("os.name"));
		ostext.setBounds(308, 11, 328, 14);
		panelForum.add(ostext);
		
		JLabel modsloaded = new JLabel("Mods Loaded: "+Loader.instance().getActiveModList().size());
		modsloaded.setBounds(308, 36, 328, 14);
		panelForum.add(modsloaded);
		
		titlefield = new JTextField();
		titlefield.setText("Add Title");
		titlefield.setColumns(10);
		titlefield.setBounds(40, 148, 337, 20);
		panelForum.add(titlefield);
		
		JScrollPane desciptionpane = new JScrollPane();
		desciptionpane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		desciptionpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		desciptionpane.setBounds(10, 201, 598, 186);
		panelForum.add(desciptionpane);
		
		descriptiontextArea = new JTextArea();
		descriptiontextArea.setLineWrap(true);
		desciptionpane.setViewportView(descriptiontextArea);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 138, 586, 2);
		panelForum.add(separator);
		
		JButton button = new JButton("Next");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if(MPBSettings.sendJsonData)
				{
					sendData();
					panelResult.setVisible(true);
				}
				else
				{
					createReport();
					panelOption.setVisible(true);
				}
				panelForum.setVisible(false);
			}
		});
		button.setBounds(344, 502, 91, 23);
		panelForum.add(button);
		
		JButton button_1 = new JButton("Cancel");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				frame.dispose();
			}
		});
		button_1.setBounds(163, 502, 91, 23);
		panelForum.add(button_1);
		
		final JLabel nofiletext = new JLabel("No File");
		Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		Border border = BorderFactory.createLineBorder(Color.WHITE);
		nofiletext.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
		nofiletext.setBounds(151, 398, 445, 23);
		panelForum.add(nofiletext);
		
		JButton selectlog = new JButton("Select Crash Log");
		selectlog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				fc.setCurrentDirectory(new File("crash-reports"));
				
				 int returnVal = fc.showOpenDialog(fc);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            
			        	File file = fc.getSelectedFile();
			        	nofiletext.setText(fc.getSelectedFile().getName());
			        	
			            //This is where a real application would open the file.
			        } else 
			        {
			        	
			        }
			}
		});
		selectlog.setBounds(20, 398, 121, 23);
		panelForum.add(selectlog);
		
		JLabel lblJava = new JLabel("Java: "+ list.get(0));
		lblJava.setBounds(10, 88, 271, 14);
		panelForum.add(lblJava);
		
		JLabel lblNewLabel = new JLabel("Mem: "+ list.get(1));
		lblNewLabel.setBounds(308, 61, 328, 14);
		panelForum.add(lblNewLabel);
		
		JLabel lblAllocated = new JLabel("Allocated: "+ list.get(2));
		lblAllocated.setBounds(308, 88, 328, 14);
		panelForum.add(lblAllocated);
		
		JLabel lblCpu = new JLabel("CPU: "+  list.get(4));
		lblCpu.setBounds(10, 113, 271, 14);
		panelForum.add(lblCpu);
		
		JLabel lblDisplay = new JLabel("Display: "+ list.get(7));
		lblDisplay.setBounds(308, 113, 328, 14);
		panelForum.add(lblDisplay);
		
		gistCheckBox = new JCheckBox("Create Gist Link for Log");
		gistCheckBox.setSelected(MPBSettings.crashlogsToGist);
		gistCheckBox.setActionCommand("createGist");
		gistCheckBox.setBounds(151, 428, 226, 23);
		
		if(MPBSettings.sendJsonData)
		{
			gistCheckBox.setSelected(false);
			gistCheckBox.setEnabled(false);
		}
		panelForum.add(gistCheckBox);
	
		
		emailField = new JTextField();
		emailField.setText("example@example.com");
		emailField.setBounds(151, 458, 290, 20);
		panelForum.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblEnmail = new JLabel("Contact Info:");
		lblEnmail.setBounds(69, 461, 72, 14);
		panelForum.add(lblEnmail);
		
		if(!MPBSettings.sendJsonData || !MPBSettings.collectEmails)
		{
			emailField.setVisible(false);
			lblEnmail.setVisible(false);
		}
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 4, 4);
		panelResult.add(scrollPane_1);
		
		resultTextArea = new JTextArea();
		resultTextArea.setBackground(SystemColor.activeCaptionBorder);
		resultTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		resultTextArea.setEditable(false);
		resultTextArea.setLineWrap(true);
		resultTextArea.setBounds(67, 47, 511, 370);
		panelResult.add(resultTextArea);
		
		JButton thankyoubutton = new JButton("Thank You");
		thankyoubutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispose();
			}
		});
		thankyoubutton.setBounds(233, 461, 177, 23);
		panelResult.add(thankyoubutton);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	
    private static long bytesToMb(long bytes)
    {
        return bytes / 1024L / 1024L;
    }
	
	public void sendData()
	{
		JsonObject postData = new JsonObject();
		
		postData.addProperty("title", this.titlefield.getText());
		postData.addProperty("desc", this.descriptiontextArea.getText());
		postData.addProperty("ign", Minecraft.getMinecraft().getSession().getUsername());
		postData.addProperty("email", emailField.getText());
		postData.addProperty("forgeVersion", ForgeVersion.getVersion());
		postData.addProperty("mcVersion", MinecraftForge.MC_VERSION);
		postData.addProperty("mpVersion", MPInfo.version);
		postData.addProperty("os", System.getProperty("os.name"));
		postData.addProperty("java",list.get(0));
		postData.addProperty("cpu", list.get(4));
		postData.addProperty("gpu", list.get(7));
		postData.addProperty("mem", list.get(1));
		postData.addProperty("allocated", list.get(2));
		postData.addProperty("modsLoaded",  Loader.instance().getActiveModList().size());
		
		if(fc.getSelectedFile() != null && fc.getSelectedFile().exists())
		{
			try 
			{
				postData.addProperty("crashLogFile", fc.getSelectedFile().getName());
				String data = MPFileUtils.readFile(fc.getSelectedFile().getPath());
				if(MPBSettings.crashlogsToGist && gistCheckBox.isSelected())
				{
					data = Tools.createGist(fc.getSelectedFile().getName(), MPFileUtils.readFile(fc.getSelectedFile().getPath()), this.titlefield.getText());
				}
				postData.addProperty("crashLog", data);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		try 
		{
			handleRecievedData(Tools.sendJsonHttpPost(MPBSettings.issuetrackerURL, postData));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void handleRecievedData(String returnHttpPost) 
	{
		Gson gson = new Gson();
		
		JsonObject jsonData = gson.fromJson(returnHttpPost, JsonObject.class);
		
		String formatedReturn = "";
		
		if(jsonData.has("timestamp"))
		{
			long timestamp = jsonData.get("timestamp").getAsLong();
			
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.US);
			Date today = new Date();
			formatedReturn = "Recieved on "+formatter.format(today) +"\n\n";
			
		}
		
		if(jsonData.has("message"))
		{
			formatedReturn += jsonData.get("message").getAsString() + "\n\n";
		}
		
		if(jsonData.has("gistLink"))
		{
			formatedReturn += "Gist Link: "+ jsonData.get("gistLink") + "\n\n";
		}
		
		
		resultTextArea.setText(formatedReturn);
	}
	
	public void createReport()
	{
		String title = this.titlefield.getText();
		String desc = this.descriptiontextArea.getText();
		String ign = Minecraft.getMinecraft().getSession().getUsername();
		String forgeVersion = ForgeVersion.getVersion();
		String mcVersion= MinecraftForge.MC_VERSION;
		String mpVersion = MPInfo.version;
		String os = System.getProperty("os.name");
		String java = list.get(0);
		String cpu = list.get(4);
		String gpu = list.get(7);
		String mem = list.get(1);
		String allocated = list.get(2);
		String modsLoaded = ""+ Loader.instance().getActiveModList().size();
		
		String data = "No CrashLog";
		if(fc.getSelectedFile() != null && fc.getSelectedFile().exists())
		{
			try 
			{
				data = MPFileUtils.readFile(fc.getSelectedFile().getPath());
				if(MPBSettings.crashlogsToGist && gistCheckBox.isSelected())
				{
					data = Tools.createGist(fc.getSelectedFile().getName(), MPFileUtils.readFile(fc.getSelectedFile().getPath()), this.titlefield.getText());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String output = "=======Pack Info======= \n";
		output += "\n";	
		output += "IGN: "+ ign +"\n";
		output += "Pack Version: "+ mpVersion +"\n";
		output += "MC Version: "+ mcVersion +"\n";
		output += "Forge Version: "+ forgeVersion +"\n";
		output += "Mods Loaded: "+ modsLoaded +"\n";
		output += "\n";
		
		output += "=======System Info======= \n";
		output += "\n";
		output += "Operating System: "+ os +"\n";
		output += "Java: "+ java +"\n";
		output += "CPU: "+ cpu +"\n";
		output += "Display: "+ gpu +"\n";
		output += "Mem: "+ mem +"\n";
		output += "Allocated: "+ allocated +"\n"; 
			
		output += "\n";
		output += "======= Issue ======= \n \n";
		
		output += "Title: "+ title + "\n \n";
		output += "--------------------------- \n";
		output += "Description: "+ desc  +"\n";
		output += "\n\n";
		
		output += "=======CrashLog======= \n";
		
		output += data+"\n"; 
		
		
		
		this.generatedReport.setText(output);
		
	}
	
	
	private static void goToHttp(String url)
	{
		try
		{
			Class oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
			oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {new URI(url)});
		}	
		catch (Throwable throwable)
		{
			MPUtils.logger.error("Couldn\'t open link", throwable);
		}
	}
}
