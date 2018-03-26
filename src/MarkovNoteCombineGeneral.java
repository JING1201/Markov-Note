import java.util.ArrayList;
import java.util.Random;


public class MarkovNoteCombineGeneral implements IMarkovModel{
	public ArrayList<Note> notes;
	private Random random;
	private int degree;
	
	public MarkovNoteCombineGeneral(){
		notes=new ArrayList<Note>();
		random=new Random();
		degree=1; //default
	}
	
	public MarkovNoteCombineGeneral(int degree1){
		notes=new ArrayList<Note>();
		random=new Random();
		degree=degree1; 
	}
	
	public void setRandom(int seed){
		random=new Random(seed);
	}
	
	public void setTraining(String text){
		String[] texts=text.split("\n");
		String pitch;
		String rhythm;
		for (int i=0;i<texts.length;i++){
			pitch=texts[i].substring(0,texts[i].indexOf(" "));
			rhythm=texts[i].substring(texts[i].indexOf(" ")+1,texts[i].length());
			//System.out.println(pitch+" "+rhythm);
			notes.add(new Note(pitch, rhythm));
		}
		//DEBUG PURPOSES
		/*String pattern="";
		for (int i=0;i<notes.size();i++){
			pattern+=notes.get(i).getPitch()+notes.get(i).getRhythm();
			pattern+=" ";
		}
		Player player=new Player();
		player.play(pattern);*/
	}
	
	public int indexOfNote(ArrayList<Note> noteSample,Note[] targets,int start){
		boolean flag;
		for (int i=start;i<noteSample.size()-(degree-1);i++){
			flag=true;
			for (int j=0;j<targets.length;j++){
				if (!noteSample.get(i+j).equals(targets[j])){
					flag=false;
					break;
	            }
			}
			if (flag){
				return i;
			}
        }
		return -1;
	}
	
	public ArrayList<Note> getFollow(Note[] keys){
		ArrayList<Note> follows = new ArrayList<Note>();
		int pos=0;
		while (pos<notes.size()){
			int start=indexOfNote(notes,keys,pos);
			if (start==-1){
				break;
			}
			if (start==notes.size()-degree){
				break;
			}
			Note next=notes.get(start+degree);
			follows.add(next);
			pos=start+1;
		}
		//System.out.println(follows);
		return follows;
	}
	
	
	
	public ArrayList<Note> getRandomNotes(int numNotes){
		ArrayList<Note> randomNotes=new ArrayList<Note>();
		int index = random.nextInt(notes.size()-1);
		Note[] keys=new Note[degree];
		for (int i=0;i<keys.length;i++){
			randomNotes.add(notes.get(index+i));
			keys[i]=notes.get(index+i);
		}
		for(int i=0; i < numNotes-1; i++){
            ArrayList<Note> follow = getFollow(keys);
            if (follow.size() == 0) {
                break;
            }
            index = random.nextInt(follow.size());
            Note next=new Note(follow.get(index));
            randomNotes.add(next);
            for (int j=0;j<keys.length-1;j++){
            	keys[j]=keys[j+1];
            }
            keys[keys.length-1]=next;
        }
		
		return randomNotes;
	}
}
