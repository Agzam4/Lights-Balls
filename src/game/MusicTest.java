package game;
public class MusicTest {

	private static int notesCount = 4;
	static Sound[] notes;
	
	public static void main(String[] args) throws InterruptedException {

		notes = new Sound[notesCount];
		int[] playNotes = new int[notesCount];
		for (int i = 0; i < notesCount; i++) {
			notes[i] = new Sound("/sounds/note.overdrive." + (i+1) + ".mp3");
			notes[i].setPlayRefresh(0);
		}
		Thread.sleep(1000);
		
		
		for (int i = 0; i < playNotes.length; i++) {
//			if(playNotes[i] > 0) {
				notes[i].justplay();
				playNotes[i] = 0;
//			}
			
			Thread.sleep(200);
		}

		playNote(0);
		playNote(0);
		playNote(1);
		Thread.sleep(250);
		playNote(2);
		playNote(3);
		Thread.sleep(250);
		playNote(3);
	}
	
	private static void playNote(int i) throws InterruptedException {
		notes[i].justplay();
	}
}
