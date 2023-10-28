package eriksUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class textEditor implements ActionListener {

	JFrame window;
	JTextArea textArea = new JTextArea();
	JScrollPane textPane = new JScrollPane(textArea);
	JButton done = new JButton("Done");
	JPanel bottomPane = new JPanel();
	JLabel curEditing = new JLabel();

	URL fileURL;

	boolean doneEditing = false;
	Object monitor = new Object();

//	static File tempFile = new File(
//			"L:/Eclipse/Workspace/pkmncard/src/eriksUtil/testFile.txt");

	public textEditor(URL file) {
			
		fileURL = file;

		window = new JFrame("Text editor");
		textArea.setText(readFile(fileURL));
		// textArea.setPreferredSize(new Dimension(700, 1200));
		textPane.setPreferredSize(new Dimension(700, 700));

		window.add(textPane, BorderLayout.CENTER);
		bottomPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPane.add(done);
		window.add(bottomPane, BorderLayout.SOUTH);

		curEditing.setFont(new Font("Serif", Font.BOLD, 20));
		curEditing.setText(fileURL.getPath());
		window.add(curEditing, BorderLayout.NORTH);

		done.addActionListener(this);

		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosing(WindowEvent w) {
				int decision = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit without saving?",
						"wait!", JOptionPane.OK_CANCEL_OPTION);
				if (decision == 0) {
					windowDone();
					window.dispose();
				}
			}

			public void windowClosed(WindowEvent arg0) {
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowOpened(WindowEvent arg0) {
			}

		});
		window.pack();
		window.setVisible(true);

	}

	public String getText() {
		return textArea.getText();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == done) {
			writeFile(fileURL);
			windowDone();
			window.dispose();
//			System.out.println(getText());
		}
	}

	private void writeFile(URL f) {
		/*try {
			Formatter fileWriter = new Formatter(f);

			fileWriter.format("%s", getText());

			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		try{
			PrintWriter writer = new PrintWriter(f.getPath());
			writer.print(getText());
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		

	private String readFile(URL f) {
		String returnString = "";
		/*try {
			Scanner fileScan = new Scanner(f);
			while (fileScan.hasNext()) {
				returnString += String.format("%s\n", fileScan.nextLine());
			}
			fileScan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(f.openStream()));
			while((line = reader.readLine()) != null){
				returnString += line+"\n";
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnString;
	}

	public void waitForWindowIsDone() {
		while (!doneEditing) {
			synchronized (monitor) {
				try {
					monitor.wait();
				} catch (Exception ex) {
				}
			}
		}
	}
	
	private void windowDone(){
		synchronized (monitor) {
			doneEditing = true;
			monitor.notifyAll();
		}
	}

	/*public static void main(String[] args) {
		textEditor t = new textEditor(tempFile);
		t.waitForWindowIsDone();
		System.out.println("window is done!");
	}*/
}
