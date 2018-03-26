
public class Note {
	private String pitch;
	private String rhythm;
	
	public Note(String pitch1, String rhythm1){
		pitch=pitch1;
		rhythm=rhythm1;
	}
	
	public Note (Note other){
		pitch=other.getPitch();
		rhythm=other.getRhythm();
	}
	
	public String getPitch(){
		return pitch;
	}
	
	public String getRhythm(){
		return rhythm;
	}
	
	public boolean equals(Note other){
		if(other.getPitch().equals(pitch)
				&&other.getRhythm().equals(rhythm)){
			return true;
		}
		return false;
	}
	public String toString(){
		return pitch+" "+rhythm;
	}
}
