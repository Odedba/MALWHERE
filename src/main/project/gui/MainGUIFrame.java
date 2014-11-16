package main.project.gui;

import java.awt.EventQueue;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.Font;
import java.io.File;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import main.project.MalRunnable;
import main.project.DeobfRunnable;
import main.project.Run;
import java.awt.Color;
import java.awt.Toolkit;

/***
 * This GUI was built using WindowBuilder and requires swt.jar to be included
 * in the file path.
 *
 */
public class MainGUIFrame {

	private JFrame frmWorkshopApplication;
	private final Action exitApp = new ExitAction();
	private final Action openFile = new OpenFileAction();
	private JTextPane outputTxt = new JTextPane();
	private final Action testMiniCode = new TestMiniAction();
	private final Action testObfCode = new TestObfAction();
	private final Action testMalCode = new TestMalAction();
	File file = null;
	private final Action about = new AboutAction();
	/*loading screen will open this folder first*/
	private static final String ROOT_FOLDER_PATH = "./in";
	private final Action generateReport = new GenerateReportAction();
	private final Action urlSearch = new URLSearchAction();
	private JMenu mnNewMenu;
	private JMenu mnSearch;
	private JMenu mnGenerate;
	private JMenuItem mntmURLInspectionReport;
	private final Action urlInspectionReport = new URLInspectionReportAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUIFrame window = new MainGUIFrame();
					window.frmWorkshopApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUIFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWorkshopApplication = new JFrame();
		frmWorkshopApplication.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Yonathan\\workspace\\workshop\\src\\icon.gif"));
		frmWorkshopApplication.setFont(new Font("Dialog", Font.PLAIN, 12));
		frmWorkshopApplication.setTitle("MALWHERE");
		frmWorkshopApplication.setBounds(100, 100, 561, 507);
		frmWorkshopApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmWorkshopApplication.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnFile.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open File");
		mntmOpen.setAction(openFile);
		mntmOpen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnFile.add(mntmOpen);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmNewMenuItem.setAction(exitApp);
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		mnFile.add(mntmNewMenuItem);

		mnNewMenu = new JMenu("Run");
		mnNewMenu.setEnabled(false);
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnNewMenu);

		JMenuItem mntmTest = new JMenuItem("Minified Code");
		mntmTest.setAction(testMiniCode);
		mntmTest.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnNewMenu.add(mntmTest);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Obfuscated Code");
		mntmNewMenuItem_1.setAction(testObfCode);
		mntmNewMenuItem_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmMaliciousCode = new JMenuItem("Malicious Code");
		mntmMaliciousCode.setAction(testMalCode);
		mntmMaliciousCode.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnNewMenu.add(mntmMaliciousCode);

		mnSearch = new JMenu("Search");
		mnSearch.setEnabled(false);
		mnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnSearch);

		JMenuItem mntmCodeForUrls = new JMenuItem("Code For URLs");
		mntmCodeForUrls.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmCodeForUrls.setAction(urlSearch);
		mnSearch.add(mntmCodeForUrls);

		mnGenerate = new JMenu("Generate");
		mnGenerate.setEnabled(false);
		mnGenerate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnGenerate);

		JMenuItem mntmGenerateDeobfuscatorResult = new JMenuItem("Deobfuscator Report");
		mnGenerate.add(mntmGenerateDeobfuscatorResult);
		mntmGenerateDeobfuscatorResult.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmGenerateDeobfuscatorResult.setAction(generateReport);

		mntmURLInspectionReport = new JMenuItem("URL Inspection Report");
		mntmURLInspectionReport.setAction(urlInspectionReport);
		mntmURLInspectionReport.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmURLInspectionReport.setEnabled(false);
		mnGenerate.add(mntmURLInspectionReport);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setAction(about);
		mntmAbout.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnHelp.add(mntmAbout);
		frmWorkshopApplication.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		frmWorkshopApplication.getContentPane().add(scrollPane);
		outputTxt.setBackground(Color.LIGHT_GRAY);
		outputTxt.setEditable(false);
		outputTxt.setFont(new Font("Consolas", Font.PLAIN, 15));

		scrollPane.setViewportView(outputTxt);
	}

	private class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ExitAction() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	private class OpenFileAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OpenFileAction() {
			putValue(NAME, "Open File");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(ROOT_FOLDER_PATH);
			int returnVal = fc.showOpenDialog(fc);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try {
					Run.readFile(file);
					outputTxt.setText("File "+file.getName()+" loaded successfully.");
					mnNewMenu.setEnabled(true);
					mnSearch.setEnabled(true);
					mnGenerate.setEnabled(true);
					mntmURLInspectionReport.setEnabled(false);
				} catch (Exception e1) {
					outputTxt.setText("Error reading file...");
				}
			} 
		}
	}
	private class TestMiniAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public TestMiniAction() {
			putValue(NAME, "Test For Minified Code");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				outputTxt.setText(Run.runMinifiedTester());
			}
		}
	}
	private class TestObfAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public TestObfAction() {
			putValue(NAME, "Test For Obfuscated Code");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				outputTxt.setText(Run.runObfuscationTester());
			}
		}
	}
	private class TestMalAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public TestMalAction() {
			putValue(NAME, "Test For Malicious Code");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				outputTxt.setText(Run.getMessage());
				MalRunnable runnable = new MalRunnable(outputTxt);
				Thread t = new Thread(runnable);
				t.start();
			}
		}
	}

	private class AboutAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AboutAction() {
			putValue(NAME, "About");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			JEditorPane text = new JEditorPane("text/html","<b>This application was developed for:</b><br>"
					+ "\"Workshop in compile-time techniques for detecting Javascript exploits\",<br>"
					+ "by Yonathan Elbaz and Idan Misgav."

					+ "<p><b>This application Includes software developed by:</b><br>"
					+ "<ul><li>Einar Lielmanis, <a href=\"github.com/beautify-web/js-beautify\">github.com/beautify-web/js-beautify</a></li>"
					+ "<li>Kanishka Dilshan, <a href=\"github.com/kdkanishka/Virustotal-Public-API-V2.0-Client\">github.com/kdkanishka/Virustotal-Public-API-V2.0-Client</a></li>"
					+ "<li>Google Closure Tools, <a href=\"developers.google.com/closure/compiler/\">developers.google.com/closure/compiler/</a></li>"
					+ "<li>Mozilla Developer Network - Project Rhino, <a href=\"developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino\">developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino</a></li>"
					+ "<li>HTMLUnit, <a href=\"htmlunit.sourceforge.net/\">htmlunit.sourceforge.net/</a></li>"
					+ "<li>javatuples, <a href=\"www.javatuples.org/\">www.javatuples.org/</a></li>"
					+ "<li>LIBSVM by Chih-Chung Chang and Chih-Jen Lin, <a href=\"www.csie.ntu.edu.tw/~cjlin/libsvm/\">www.csie.ntu.edu.tw/~cjlin/libsvm/</a></li></ul></p>"
					+ "<p><b>Also inculded is a JavaScript malware detection implementation based on the paper:</b></p>"
					+ "\"A Static Malicious Javascript Detection Using SVM\"'<br>"
					+ "by WANG Wei-Hong, LV Yin-Jun, CHEN Hui-Bing and FANG Zhao-Lin,<br>"
					+ "<b>And concepts borrowed from the paper:</b><br>"
					+ "\"Prophiler: A Fast Filter for the Large-Scale Detection of Malicious Web Pages\",<br>"
					+ "by Davide Canali, Marco Cova, Giovanni Vigna and Christopher Kruegel.</p>");
			JOptionPane.showMessageDialog(null,text,"About This Application",JOptionPane.PLAIN_MESSAGE);
		}
	}
	private class GenerateReportAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public GenerateReportAction() {
			putValue(NAME, "Deobfuscator Report");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				outputTxt.setText(Run.getMessage());
				DeobfRunnable runnable = new DeobfRunnable(outputTxt, mntmURLInspectionReport);
				Thread t = new Thread(runnable);
				t.start();
			}
		}
	}
	private class URLSearchAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public URLSearchAction() {
			putValue(NAME, "Code For URLs");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				Run.runURLSearch(outputTxt, mntmURLInspectionReport);
			}
		}
	}
	private class URLInspectionReportAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public URLInspectionReportAction() {
			putValue(NAME, "URL Inspection Report");
			putValue(SHORT_DESCRIPTION, "");
		}
		public void actionPerformed(ActionEvent e) {
			if(file == null){
				outputTxt.setText("Please open a file to run tests...");
			}
			else{
				Run.runURLInspection(outputTxt);
			}
		}
	}
}
