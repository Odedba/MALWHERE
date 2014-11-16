package main.project;

import javax.swing.JMenuItem;
import javax.swing.JTextPane;

public class DeobfRunnable implements Runnable {
	
	private JTextPane outputTxt;
	private JMenuItem mntmURLInspectionReport;
	
	public DeobfRunnable(JTextPane outputTxt, JMenuItem mntmURLInspectionReport){
    	this.outputTxt = outputTxt;
    	this.mntmURLInspectionReport = mntmURLInspectionReport;
    	
    }

	@Override
	public void run() {
		Run.runDeobfuscatorAnalysis(outputTxt, mntmURLInspectionReport);
	}

}
