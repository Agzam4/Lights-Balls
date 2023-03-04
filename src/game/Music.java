package game;

import java.util.Iterator;

public class Music {

	/*
	 * musicbox
	 * overdrive
	 * guitarlong
	 * pianolong
	 * hit
	 */
	private static final String[] musicTypes = {"musicbox", "pianolong", "overdrive", "hit"};
	private static final int[] musicTime = {370/2, 370, 370, 250};
	private static final int[] musicCount = {1, 2, 1, 1};


	private static final int[][] patterns = {
			{3,1, 2,1,1, 2,1,1, 2,2, 2,2, 2,2, 3,1, 1,1,2},
			//			{3,1, 2,1,1, 2,1,1, 2,2, 2,2, 2,2, 3,1, 1,1,2},
			{4, 4, 4, 4, 4, 4, 4, 4},
			{4, 4, 4, 4, 4, 4, 4, 4},
			{2,2, 1,1,1,1, 2,1,1, 1,1,1,1, 2,2, 1,1,1,1, 2,2, 1,1,1,1}
			//			{2,2, 2,1,1, 2,1,1, 2,2, 2,2, 2,2, 2,2, 1,1,2}
			//			{3,1, 2,1,1, 2,1,1, 2,2, 2,2, 2,2, 3,1, 1,1,2}
	};
	private static boolean[][] patternb;
	private static final int patternSize = 4*8;
	private static  final long bit = 95; // 93.75

	private static final int channels = 4;
	private static final int notesCount = 12;

	private Sound[][] notes;
	private int[][] playNotes;
	private int[] channelPatternId;

	private long wait;
	private int count;

	//	private StringBuilder log;

	public Music() {
		notes = new Sound[channels][notesCount];
		channelPatternId = new int[channels];
		playNotes = new int[channels][notesCount];
		patternb = new boolean[channels][patternSize];

		for (int channel = 0; channel < musicCount.length; channel++) {
			int point = 0;
			for (int i = 0; i < patterns.length; i++) {
				patternb[channel][point] = true;
				point+=patterns[channel][i];
			}
		}
	}

	boolean isLoaded = false;
	int musicLoadedCount;
	
	public void load() {
		System.out.println("Loading music...");
		musicLoadedCount = 0;
		for (int channel = 0; channel < musicCount.length; channel++) {
			for (int i = 0; i < notesCount; i++) {
				notes[channel][i] = new Sound("/sounds/note." + musicTypes[channel] + "." + (i+1) + ".mp3");
				notes[channel][i].setPlayRefresh(0);
				musicLoadedCount++;
			}
		}
		System.out.println("Loading loaded!");
		isLoaded = true;
	}

	public boolean isLoaded() {
		return isLoaded;
	}
	
	public int getMusicLoadedCount() {
		return musicLoadedCount;
	}
	
	public int getNeedLoadMusicCount() {
		return musicCount.length * notesCount;
	}

	boolean isRunning = false;

	public void run() {
		isRunning = true;
		new Thread(() -> {
//			int loop = 0;
//			log = new StringBuilder();
//			for (int ch = 0; ch < channels; ch++) {
//				for (int i = 0; i < notesCount; i++) {
//					log.append("-");
//				}
//				log.append("O");
//			}
			while (isRunning) {
				if(!isLoaded) {
					try {
						Thread.sleep(bit);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				for (int loop = 0; loop < patternSize; loop++) {
					for (int ch = 0; ch < channels; ch++) {
						if(patternb[ch][loop]) {
							int need = musicCount[ch];
							for (int i = notesCount-1; i >= 0; i--) {
								if(playNotes[ch][i] > 0 && need > 0) {
									notes[ch][i].justplay();
									playNotes[ch][i] = 0;
									//								log.append("#");
									//								break;
									need--;
									//								if(need <= 0) break;
								} else {
									//								log.append(" ");
								}
							}
//							for (int i = 0; i < playNotes.length; i++) {
//								if(playNotes[ch][i] > 0) {
//									notes[ch][i].justplay();
//									playNotes[ch][i] = 0;
//									//								break;
//									need--;
//									if(need <= 0) break;
//								}
//							}

//							int max = 0;
//							int maxID = -1;
//							for (int i = notesCount-1; i >= 0; i--) {
//								if(playNotes[ch][i] > max) {
//									max = playNotes[ch][i];
//									maxID = i;
//								}
//							}
//							if(maxID != -1) {
//								notes[ch][maxID].justplay();
//								playNotes[ch][maxID] = 0;
//							}

//							channelPatternId[ch]++;

						}
						//					log.append("|");
					}
					//				log.append("\n");

					//				for (int i = 0; i < playNotes.length; i++) {
					//					if(playNotes[i] > 0) {
					//						notes[i].justplay();
					//						playNotes[i] = 0;
					////						break;
					//					}
					//				}
					//				if(count == -1) {
					//				} else {
					//				}

					try {
						Thread.sleep(bit);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//				loop++;
				//				loop = loop%patternSize;
			}

		}).start();
	}

	//	public StringBuilder getLog() {
	//		return log;
	//	}
	//	
	//	public void clearLog() {
	//		log = new StringBuilder();
	//	}

	public void justplayNote(int ch, double d) {
		int i = (int) (d*notesCount);
		notes[ch][i].justplay();
		playNotes[ch][i] = 0;
	}

	public void playNote(int ch, double d) {
		d%=1;
		playNotes[ch][(int) (d*notesCount)]++;
	}

	public void playNote(int ch, int id) {
		playNotes[ch][id]++;
	}

}
