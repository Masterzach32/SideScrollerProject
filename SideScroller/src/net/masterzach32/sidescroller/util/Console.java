package net.masterzach32.sidescroller.util;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Console extends WindowAdapter implements WindowListener, ActionListener, Runnable {
	
	private JFrame frame;
	private JTextArea textArea;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin = new PipedInputStream();
	private final PipedInputStream pin2 = new PipedInputStream();

	Thread errorThrower; // just for testing (Throws an Exception at this Console
	
	public Console() {
		// create all components and add them
		frame = new JFrame("SideScroller Console");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (screenSize.width/2), (int) (screenSize.height/2));
		frame.setBounds(0, 0, frameSize.width, frameSize.height);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JButton button = new JButton("Clear");
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
		
		frame.addWindowListener(this);
		button.addActionListener(this);
		
		try {
			PipedOutputStream pout = new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout, true)); 
		} catch (IOException io) {
			textArea.append("Couldn't redirect STDOUT to this console\n" + io.getMessage());
		} catch (SecurityException se) {
			textArea.append("Couldn't redirect STDOUT to this console\n" + se.getMessage());
	    } 

		try	{
			PipedOutputStream pout2 = new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2, true));
		} catch (IOException io) {
			textArea.append("Couldn't redirect STDERR to this console\n" + io.getMessage());
		} catch (SecurityException se) {
			textArea.append("Couldn't redirect STDERR to this console\n" + se.getMessage());
	    } 		
			
		quit = false; // signals the Threads that they should exit
				
		// Starting two seperate threads to read from the PipedInputStreams
		reader = new Thread(this);
		reader.setDaemon(true);	
		reader.start();	
		//
		reader2 = new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();
	}
	
	public synchronized void windowClosed(WindowEvent evt) {
		quit = true;
		this.notifyAll(); // stop all threads
		try { 
			reader.join(1000);
			pin.close();   
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		try { 
			reader2.join(1000);
			pin2.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
		
	public synchronized void windowClosing(WindowEvent evt) {
		frame.setVisible(false); // default behaviour of JFrame	
	}
	
	public synchronized void actionPerformed(ActionEvent evt) {
		textArea.setText("Cleared\n");
	}

	public synchronized void run() {
		try {			
			while (Thread.currentThread() == reader) {
				try { 
					this.wait(100);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				
				if (pin.available() != 0) {
					String input = this.readLine(pin);
					textArea.append(input);
				}
				if (quit) return;
			}
		
			while (Thread.currentThread() == reader2) {
				try { 
					this.wait(100);
				} catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				
				if (pin2.available() != 0) {
					String input = this.readLine(pin2);
					textArea.append(input);
				}
				if (quit) return;
			}			
		} catch (Exception e) {
			textArea.append("\nConsole reports an Internal error.");
			textArea.append("The error is: " + e);
			e.printStackTrace();
		}
		
		// just for testing (Throw a Nullpointer after 1 second)
		if (Thread.currentThread() == errorThrower) {
			try { 
				this.wait(1000); 
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			throw new NullPointerException("Application test: throwing an NullPointerException. It should arrive at the console");
		}

	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException {
		String input = "";
		do {
			int available = in.available();
			if (available == 0) break;
			byte b[] = new byte[available];
			in.read(b);
			input = input + new String(b, 0, b.length);														
		} while(!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
		return input;
	}
	
	public void setVisible(boolean v) {
		frame.setVisible(v);
	}
}