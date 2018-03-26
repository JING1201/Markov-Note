
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;

import edu.duke.FileResource;

import org.jfugue.player.*;

public class MarkovNoteRunner {
	public static String runModel(IMarkovModel markov, String text, int size){ 
        markov.setTraining(text); 
        System.out.println("running with " + markov);
        ArrayList<Note> notes =new ArrayList<Note>();
        for(int k=0; k < 3; k++){ 
            notes= markov.getRandomNotes(size); 
            //printOut(notes); 
        } 
        String output="";
        for (int i=0;i<notes.size();i++){
        	if (i==notes.size()-1){
        		output+=notes.get(i).toString();
        		break;
        	}
        	output+=notes.get(i).toString()+"\n";
        }
        return output;
        //playSound(notes);
    } 
	public static void runModel(IMarkovModel markov, String text){ 
        markov.setTraining(text); 
        //markov.setRandom(seed);
        System.out.println("running with " + markov); 
        ArrayList<Note> notes =new ArrayList<Note>();
        for(int k=0; k < 3; k++){ 
        	notes= markov.getRandomNotes(120);  
            printOut(notes); 
        } 
        playSound (notes);
    }
	
	public static void runMarkov() { 
        FileResource fr = new FileResource(); 
        String st = fr.asString(); 
        MarkovNoteMoreGeneral markovNoteMoreGen = new MarkovNoteMoreGeneral (3,5);
        //runModel (markovNoteMoreGen, st, 120);
        MarkovNoteCombineGeneral markovCombineGen = new MarkovNoteCombineGeneral (3);
        runModel (markovCombineGen, st);
    } 
    
	private static void printOut(ArrayList<Note> notes){
		System.out.println("----------------------------------");
        for(int k=0; k < notes.size(); k++){
            System.out.println(notes.get(k));
        } 
        System.out.println("----------------------------------");
    }
	
	private static void playSound (ArrayList<Note> notes){
		String pattern="";
		for (int i=0;i<notes.size();i++){
			pattern+=notes.get(i).getPitch()+notes.get(i).getRhythm();
			pattern+=" ";
		}
		Player player=new Player();
		ManagedPlayer play=new ManagedPlayer();
		try {
			play.start(player.getSequence(pattern));
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		runMarkov();
	}

}
