import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.jfugue.player.*;

public class MarkovNoteMoreGeneral implements IMarkovModel{
	public ArrayList<Note> notes;
	private Random random;
	private int pitchDegree;
	private int rhythmDegree;
	
	public MarkovNoteMoreGeneral(){
		notes=new ArrayList<Note>();
		random=new Random();
		pitchDegree=1; //default goes to MarkovNote1
		rhythmDegree=1;
	}
	
	public MarkovNoteMoreGeneral(int degree){
		notes=new ArrayList<Note>();
		random=new Random();
		pitchDegree=degree;
		rhythmDegree=degree;
	}
	
	public MarkovNoteMoreGeneral(int pitch, int rhythm){
		notes=new ArrayList<Note>();
		random=new Random();
		pitchDegree=pitch;
		rhythmDegree=rhythm;
	}
	
	public void setRandom(int seed){
		random=new Random(seed);
	}
	
	public void setTraining(String text){
		text=text.trim();
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
	
	public int indexOfPitch(ArrayList<Note> noteSample,String[] pitches,int start){
		boolean flag;
		for (int i=start;i<noteSample.size()-(pitchDegree-1);i++){
			flag=true;
			for (int j=0;j<pitches.length;j++){
				if (!noteSample.get(i+j).getPitch().equals(pitches[j])){
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
	
	public int indexOfRhythm(ArrayList<Note> noteSample,String[] rhythms,int start){
		boolean flag;
		for (int i=start;i<noteSample.size()-(rhythmDegree-1);i++){
			flag=true;
			for (int j=0;j<rhythms.length;j++){
				if (!noteSample.get(i+j).getRhythm().equals(rhythms[j])){
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
	
	public ArrayList<String> getFollowPitch(String[] keyPitches){
		ArrayList<String> follows = new ArrayList<String>();
		int pos=0;
		while (pos<notes.size()){
			int start=indexOfPitch(notes,keyPitches,pos);
			if (start==-1){
				break;
			}
			if (start>=notes.size()-pitchDegree){
				break;
			}
			String next=notes.get(start+pitchDegree).getPitch();
			follows.add(next);
			pos=start+1;
		}
		//System.out.println("Pitch: "+Arrays.toString(keyPitches)+follows);
		return follows;
	}
	
	public ArrayList<String> getFollowRhythm(String[] keyRhythms){
		ArrayList<String> follows = new ArrayList<String>();
		int pos=0;
		while (pos<notes.size()){
			int start=indexOfRhythm(notes,keyRhythms,pos);
			if (start==-1){
				break;
			}
			if (start>=notes.size()-rhythmDegree){
				break;
			}
			String next=notes.get(start+rhythmDegree).getRhythm();
			follows.add(next);
			pos=start+1;
		}
		//System.out.println("Rhythm: "+Arrays.toString(keyRhythms)+follows);
		return follows;
	}
	
	public ArrayList<Note> getRandomNotes(int numNotes){
		ArrayList<Note> randomNotes=new ArrayList<Note>();
		int pitchIndex = random.nextInt(notes.size()-(pitchDegree-1));
		int rhythmIndex = random.nextInt(notes.size()-(rhythmDegree-1));
		
		String[] keyPitches=new String[pitchDegree];
		String[] keyRhythms=new String[rhythmDegree];
		
		//THEY HAVE DIFFERENT LENGTHS
		for (int i=0;i<keyPitches.length;i++){
			keyPitches[i]=notes.get(pitchIndex+i).getPitch();
		}
		for (int i=0;i<keyRhythms.length;i++){
			keyRhythms[i]=notes.get(rhythmIndex+i).getRhythm();
		}
		
		if (pitchDegree<rhythmDegree){
			for (int i=0;i<keyPitches.length;i++){
				randomNotes.add(new Note(notes.get(pitchIndex+i).getPitch(),
						notes.get(rhythmIndex+i).getRhythm()));
			}
			//start to get pitchFollow
			for (int i=0;i<rhythmDegree-pitchDegree;i++){
				ArrayList<String> followPitch = getFollowPitch(keyPitches);
				int index1 = random.nextInt(followPitch.size());
				Note next=new Note(followPitch.get(index1),
						notes.get(rhythmIndex+pitchDegree+i).getRhythm());
				randomNotes.add(next);
				for (int j=0;j<keyPitches.length-1;j++){
	            	keyPitches[j]=keyPitches[j+1];
	            }
	            //System.out.println("check");
	            keyPitches[keyPitches.length-1]=next.getPitch();
			}
		}
		
		else if (rhythmDegree<pitchDegree){
			for (int i=0;i<keyRhythms.length;i++){
				randomNotes.add(new Note(notes.get(pitchIndex+i).getPitch(),
						notes.get(rhythmIndex+i).getRhythm()));
			}
			//start to get rhythmFollow
			for (int i=0;i<pitchDegree-rhythmDegree;i++){
				ArrayList<String> followRhythm = getFollowRhythm(keyRhythms);
				int index1 = random.nextInt(followRhythm.size());
				Note next=new Note(notes.get(pitchIndex+rhythmDegree+i).getPitch(),
						followRhythm.get(index1));
				randomNotes.add(next);
				for (int j=0;j<keyRhythms.length-1;j++){
	            	keyRhythms[j]=keyRhythms[j+1];
	            }
	            //System.out.println("check");
	            keyRhythms[keyRhythms.length-1]=next.getRhythm();
			}
		}
		
		for(int i=0; i < numNotes-1; i++){
            ArrayList<String> followPitch = getFollowPitch(keyPitches);
            ArrayList<String> followRhythm = getFollowRhythm(keyRhythms);
            if (followPitch.size() == 0||followRhythm.size()==0) {
                break;
            }
            int index1 = random.nextInt(followPitch.size());
            int index2 = random.nextInt(followRhythm.size());
            Note next=new Note(followPitch.get(index1),followRhythm.get(index2));
            randomNotes.add(next);
            
            //System.out.println("check");
            
            for (int j=0;j<keyPitches.length-1;j++){
            	keyPitches[j]=keyPitches[j+1];
            }
            for (int j=0;j<keyRhythms.length-1;j++){
            	keyRhythms[j]=keyRhythms[j+1];
            }
            //System.out.println("check");
            keyPitches[keyPitches.length-1]=next.getPitch();
            keyRhythms[keyRhythms.length-1]=next.getRhythm();
        }
		
		return randomNotes;
	}
}
