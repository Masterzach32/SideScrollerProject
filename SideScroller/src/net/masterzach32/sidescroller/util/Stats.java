package net.masterzach32.sidescroller.util;

public class Stats {
	
	public static final int DEATHS       = 0,
							DAMAGETAKEN  = 1,
							DAMAGEDELT   = 2,
							HEALING      = 3,
							DISTANCE     = 4,
							TIME         = 5;
	
	private static int[] stats = new int[6];

	/**
	 * Increases the specified stat.
	 * @param stat
	 * @param d
	 */
	public static void setStat(String stat, int increase) {
		stats[getType(stat)] += increase;
	}
	
	/**
	 * Retrieves the specified stat.
	 * @param stat
	 * @return
	 */
	public static int getStat(String stat) {
		return stats[getType(stat)];
	}
	
	/**
	 * Retrieves the specified stat.
	 * @param stat
	 * @return
	 */
	public static int getStat(int stat) {
		return stats[stat];
	}
	
	/**
	 * Returns the length of the stats array.
	 * @return
	 */
	public static int getNumberOfStats() {
		return stats.length;
	}
	
	/**
	 * Returns the integer value for the type of stat.
	 * @param stat
	 * @return
	 */
	public static int getType(String stat) {
		int type = -1;
		switch(stat){
			case "deaths":
				type = DEATHS;
				break;
			case "damageTaken":
				type = DAMAGETAKEN;
				break;
			case "damageDelt":
				type = DAMAGEDELT;
				break;
			case "healing":
				type = HEALING;
				break;
			case "distance":
				type = DISTANCE;
				break;
			case "time":
				type = TIME;
				break;
		}
		return type;
	}
	
	/**
	 * Returns the string value for the type of stat.
	 * @param stat
	 * @return
	 */
	public static String getType(int stat) {
		String type = "";
		switch(stat){
			case DEATHS:
				type = "Deaths";
				break;
			case DAMAGETAKEN:
				type = "Damage Taken";
				break;
			case DAMAGEDELT:
				type = "Damage Delt";
				break;
			case HEALING:
				type = "Healing Done";
				break;
			case DISTANCE:
				type = "Distance Travled";
				break;
			case TIME:
				type = "Total Time:";
				break;
		}
		return type;
	}
}