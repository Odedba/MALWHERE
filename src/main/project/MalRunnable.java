package main.project;

import javax.swing.JTextPane;

public class MalRunnable implements Runnable {

    private JTextPane outputTxt;
    
    public MalRunnable(JTextPane outputTxt){
    	this.outputTxt = outputTxt;
    }

    @Override
    public void run() {
    	Run.runMaliciousnessTester(outputTxt);
    }
    
}
