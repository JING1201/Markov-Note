import java.util.ArrayList;



public interface IMarkovModel {
	
    public void setTraining(String text);
    
    public void setRandom(int seed);
    
    public ArrayList<Note> getRandomNotes(int numChars);

}