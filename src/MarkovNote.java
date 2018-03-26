import java.awt.EventQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfugue.player.ManagedPlayer;
import org.jfugue.player.Player;
import org.apache.commons.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.Font;
import java.awt.Color;

import javax.swing.UIManager;


public class MarkovNote {

	private JFrame frame;
	private JTextField src;
	private JTextField txtOriginal;
	private JTextField txtOutput;
	private JTextField pitchDegree;
	private JTextField rhythmDegree;
	private ManagedPlayer play=new ManagedPlayer();
	private ManagedPlayer play1=new ManagedPlayer();
	private JTextField sampleSize;
	private JTextField txtPitchDegree;
	private JTextField txtRhythmDegree;
	private JTextField txtSampleSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MarkovNote window = new MarkovNote();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MarkovNote() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Markov Note");
		frame.setBounds(100, 100, 525, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel middlePanel = new JPanel ();
	    middlePanel.setBorder ( (Border) new TitledBorder ( new EtchedBorder (), "Display Area" ) );
	    
		src = new JTextField();
		src.setBounds(120, 10, 260, 25);
		src.setColumns(10);
		
		JTextArea OpenFile = new JTextArea();
		OpenFile.setEditable(false);
		OpenFile.setBackground(UIManager.getColor("Button.background"));
		OpenFile.setFont(new Font("Tahoma", Font.PLAIN, 13));
		OpenFile.setBounds(10, 10, 100, 25);
		OpenFile.setText("File src: ");
		
		JButton Browse = new JButton("Browse");
		Browse.setBounds(385, 10, 115, 25);
		Browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser;
				//In response to a button click:
				
				final JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Choose File");
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.setFileFilter(new OpenFileFilter("txt"));
			    //int returnVal = chooser.showOpenDialog(frame);
			    //    
			    if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			      System.out.println("getCurrentDirectory(): " 
			         +  chooser.getCurrentDirectory());
			      System.out.println("getSelectedFile() : " 
			         +  chooser.getSelectedFile());
			      src.setText(chooser.getSelectedFile().toString());
			      }
			    else {
			      System.out.println("No Selection ");
			      }
			    }
		});
		
		
		
		txtOriginal = new JTextField();
		txtOriginal.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtOriginal.setBounds(10, 145, 85, 20);
		txtOriginal.setEditable(false);
		txtOriginal.setText("Original");
		txtOriginal.setColumns(10);
		
		txtOutput = new JTextField();
		txtOutput.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtOutput.setBounds(250, 145, 85, 20);
		txtOutput.setEditable(false);
		txtOutput.setText("Output");
		txtOutput.setColumns(10);
		
		txtPitchDegree = new JTextField();
		txtPitchDegree.setEditable(false);
		txtPitchDegree.setText("Pitch Degree");
		txtPitchDegree.setBounds(230, 45, 90, 20);
		frame.getContentPane().add(txtPitchDegree);
		txtPitchDegree.setColumns(10);
		
		txtRhythmDegree = new JTextField();
		txtRhythmDegree.setEditable(false);
		txtRhythmDegree.setText("Rhythm Degree");
		txtRhythmDegree.setBounds(230, 75, 90, 20);
		frame.getContentPane().add(txtRhythmDegree);
		txtRhythmDegree.setColumns(10);
		
		txtSampleSize = new JTextField();
		txtSampleSize.setEditable(false);
		txtSampleSize.setText("Sample Size");
		txtSampleSize.setBounds(230, 105, 90, 20);
		frame.getContentPane().add(txtSampleSize);
		txtSampleSize.setColumns(10);
		
		JTextArea Description = new JTextArea();
		Description.setEditable(false);
		Description.setWrapStyleWord(true);
		Description.setLineWrap(true);
		Description.setBounds(10, 75, 210, 60);
		frame.getContentPane().add(Description);
		
		
		String[] choices= {"MarkovGeneral","MarkovCombine"};
		JComboBox comboBox = new JComboBox(choices);
		comboBox.setBounds(10, 45, 210, 20);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedChoice = (String) comboBox.getSelectedItem();
				if (selectedChoice.equals("MarkovCombine")){
					txtPitchDegree.setText("Degree");
					txtRhythmDegree.setVisible(false);
					rhythmDegree.setVisible(false);
					Description.setText(
							"Performs the Markov Algorithm on the notes (treating each as a whole)"
							);
				}
				else{
					txtPitchDegree.setText("Pitch Degree");
					txtRhythmDegree.setVisible(true);
					rhythmDegree.setVisible(true);
					Description.setText(
							"Performs the Markov Algorithm on pitch and rhythm separately"
							);
				}
			}
		});
		
		pitchDegree = new JTextField();
		pitchDegree.setBounds(325, 45, 45, 20);
		pitchDegree.setColumns(10);
		
		rhythmDegree = new JTextField();
		rhythmDegree.setBounds(325, 75, 45, 20);
		rhythmDegree.setColumns(10);
		
		sampleSize = new JTextField();
		sampleSize.setBounds(325, 105, 45, 20);
		frame.getContentPane().add(sampleSize);
		sampleSize.setColumns(10);
		
		JTextArea original = new JTextArea();
		original.setLineWrap(true);
		original.setEditable(false);
		original.setLocation(12, 0);
		frame.getContentPane().add(original);
		JScrollPane scrollpane = new JScrollPane(original,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.setBounds(10, 175, 230, 180);
		frame.getContentPane().add(scrollpane);
		
		JTextArea output = new JTextArea();
		frame.getContentPane().add(output);
		JScrollPane scrollpane1 = new JScrollPane(output,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane1.setBounds(250, 175, 230, 180);
		frame.getContentPane().add(scrollpane1);
		
		output.setLineWrap(true);
		output.setEditable(false);
		
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(385, 60, 110, 50);
		btnRun.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (src.getText().trim().equals("")){
					JOptionPane.showMessageDialog(frame,
						    "File source cannot be empty",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				Scanner scanner=new Scanner(System.in);
				try {
					scanner = new Scanner( new File(src.getText()) );
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(frame,
						    "Invalid File Source",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				boolean invalidVariables=false;
				String warning="";
				//check pitch degree
				if (pitchDegree==null||pitchDegree.getText().equals("")){
					invalidVariables=true;
					warning+="Pitch Degree cannot be empty\n";
				}
				else if (!NumberUtils.isNumber(pitchDegree.getText())){
					invalidVariables=true;
					warning+="Invalid Pitch Degree\n";
				}
				else if (Integer.parseInt(pitchDegree.getText())>30){
					invalidVariables=true;
					warning+="Pitch Degree should not be greater than 30\n";
				}
				//check rhythmdegree
				if (rhythmDegree==null||rhythmDegree.getText().equals("")){
					invalidVariables=true;
					warning+="Rhythm Degree cannot be empty\n";
				}
				else if (!NumberUtils.isNumber(rhythmDegree.getText())){
					invalidVariables=true;
					warning+="Invalid Rhythm Degree\n";
				}
				else if (Integer.parseInt(pitchDegree.getText())>30){
					invalidVariables=true;
					warning+="Rhythm Degree should not be greater than 30\n";
				}
				//check samplesize
				if (sampleSize==null||sampleSize.getText().equals("")){
					invalidVariables=true;
					warning+="Sample Size cannot be empty\n";
				}
				else if (!NumberUtils.isNumber(sampleSize.getText())){
					invalidVariables=true;
					warning+="Invalid Sample Size\n";
				}
				else if ((new BigInteger(sampleSize.getText())).
						compareTo(new BigInteger("0"))<=0){
					invalidVariables=true;
					warning+="Sample Size should be greater than 0\n";
				}
				else if ((new BigInteger(sampleSize.getText())).
						compareTo(new BigInteger("200000"))>0){
					invalidVariables=true;
					warning+="Sample Size should not be greater than 200000\n";
				}
				
				if (invalidVariables){
					JOptionPane.showMessageDialog(frame,
						    warning,
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String text = scanner.useDelimiter("//A").next();
				original.setText(text);
				//System.out.println(text);
				scanner.close(); // Put this call in a finally block
				if (!rhythmDegree.isVisible()){
					MarkovNoteCombineGeneral markovComGen=new MarkovNoteCombineGeneral
							(Integer.parseInt(pitchDegree.getText()));
					output.setText(MarkovNoteRunner.runModel
							(markovComGen, text, Integer.parseInt(sampleSize.getText())));
				}
				else{
					MarkovNoteMoreGeneral markovMoreGen=new MarkovNoteMoreGeneral
							(Integer.parseInt(pitchDegree.getText()),
									Integer.parseInt(rhythmDegree.getText()));
					output.setText(MarkovNoteRunner.runModel
							(markovMoreGen, text, Integer.parseInt(sampleSize.getText())));
				}
			}
		});
		
		JButton btnPlay_1 = new JButton("Play");
		btnPlay_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pattern="";
				pattern=original.getText().replaceAll(" ","");
				pattern=pattern.replaceAll("\n"," ");
				Player player=new Player();
				try {
					play.start(player.getSequence(pattern));
				} catch (InvalidMidiDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MidiUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPlay_1.setBounds(10, 365, 60, 25);
		
		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnPause.getText().equals("Pause")){
					play.pause();
					btnPause.setText("Resume");
				}
				else{
					play.resume();
					btnPause.setText("Pause");
				}
			}
		});
		btnPause.setBounds(80, 365, 80, 25);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play.finish();
			}
		});
		btnStop.setBounds(170, 365, 70, 25);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pattern="";
				pattern=output.getText().replaceAll(" ","");
				pattern=pattern.replaceAll("\n"," ");
				Player player=new Player();
				try {
					play1.start(player.getSequence(pattern));
				} catch (InvalidMidiDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MidiUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPlay.setBounds(250, 365, 60, 25);
		
		JButton btnPause_1 = new JButton("Pause");
		btnPause_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnPause_1.getText().equals("Pause")){
					play1.pause();
					btnPause_1.setText("Resume");
				}
				else{
					play1.resume();
					btnPause_1.setText("Pause");
				}
			}
		});
		btnPause_1.setBounds(320, 365, 80, 25);
		
		JButton btnStop_1 = new JButton("Stop");
		btnStop_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play1.finish();
			}
		});
		btnStop_1.setBounds(410, 365, 70, 25);
		
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(scrollpane);
		frame.getContentPane().add(OpenFile);
		frame.getContentPane().add(src);
		frame.getContentPane().add(Browse);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(btnPlay_1);
		frame.getContentPane().add(btnPause);
		frame.getContentPane().add(btnStop);
		frame.getContentPane().add(txtOriginal);
		frame.getContentPane().add(btnPlay);
		frame.getContentPane().add(btnPause_1);
		frame.getContentPane().add(btnStop_1);
		frame.getContentPane().add(pitchDegree);
		frame.getContentPane().add(rhythmDegree);
		frame.getContentPane().add(txtOutput);
		frame.getContentPane().add(btnRun);
		
		
		
		
		
		
		
		
		
		
	}
}
