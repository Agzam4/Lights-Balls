package game;

import java.io.IOException;

public class Updates {

	public static final int defHp = 1; // 1
	public static final int defShieldTime = 0; // 0
	public static final int defSize = 5; // 20
	public static final int defDamage = 5; // 5

	public static int stage = 0;
	public static int $ = 0; // 25

	private static int[] defUpdates = {defHp, defShieldTime, defSize, defDamage};
	private static int[] updates = {defHp, defShieldTime, defSize, defDamage};

	public static void save() {
		System.out.println("Saving updates");
		Saving.writeObject(updates, "updates.updates");
		Saving.writeObject($, "updates.s");
		Saving.writeObject(maxObj, "maxObj.settings");
	}
	
	public static void load() {
		try {
			updates = (int[]) Saving.readObject("updates.updates");
		} catch (ClassNotFoundException | IOException e) {
			updates = new int[] {defHp, defShieldTime, defSize, defDamage};
		}
		try {
			$ = (int) Saving.readObject("updates.s");
		} catch (ClassNotFoundException | IOException e) {
			$ = 25;
		}
//		$ = 4225;
		try {
			maxObj = (int) Saving.readObject("maxObj.settings");
		} catch (ClassNotFoundException | IOException e) {
			maxObj = 1_000;
		}
	}
	
	
	public static final int UPDATE_HP = 0;
	public static final int UPDATE_TIME = 1;
	public static final int UPDATE_SIZE = 2;
	public static final int UPDATE_DAMAGE = 3;
	
	public static int shieldDamage = 0;

	public static int maxObj = 1_000;
	public static int skipFrames = 1;
	
	public static int getCost(int id) {
		switch (id) {
		case UPDATE_HP:
			return (24+updates[UPDATE_HP]*updates[UPDATE_HP]*2)*50;
		case UPDATE_SIZE:
			return ((int) Math.ceil(10+(updates[UPDATE_SIZE]*updates[UPDATE_SIZE]/10))*4)*10;
		case UPDATE_TIME:
			return (25+((updates[UPDATE_TIME] + updates[UPDATE_HP])/5))*10;
		case UPDATE_DAMAGE:
			return ((int) Math.ceil(updates[UPDATE_DAMAGE]))*20;
		default:
			return 0;
		}
	}
	
	public static void setAllUpdate(int lvl) {
		for (int i = 0; i < updates.length; i++) {
			updates[i] = defUpdates[i] + lvl;
		}
	}
	
	public static int getUpdate(int id) {
		return updates[id];
	}
	
	public static boolean canUpdate(int id) {
		return $ >= getCost(id) && getLvl(id) < 10 + (stage+1)*10;
	}
	
	public static void update(int id) {
		if(canUpdate(id)) {
			$ -= getCost(id);
			updates[id]++;
		}
	}
	
	public static int getLvl(int id) {
		return updates[id] - defUpdates[id] + 1;
	}
	
}
