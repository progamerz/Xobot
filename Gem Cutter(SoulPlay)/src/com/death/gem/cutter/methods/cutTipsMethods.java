package com.death.gem.cutter.methods;

import com.death.gem.cutter.data.Data;

import xobot.script.methods.Packets;
import xobot.script.methods.Players;
import xobot.script.methods.tabs.Inventory;
import xobot.script.util.Time;
import xobot.script.util.Timer;
import xobot.script.wrappers.interactive.Item;
import xobot.script.wrappers.interactive.Player;

public class cutTipsMethods {

	public static boolean canCutTips() {
		return Inventory.Contains(Data.mainIng) && Inventory.Contains(Data.secondaryIng)
				&& !busy();
	}

	public static void doCutTips() {
		Item sec = Inventory.getItem(Data.secondaryIng);
		Item main = Inventory.getItem(Data.mainIng);

		if (sec != null && main != null) {
			Packets.sendAction(447, sec.getID(), sec.getSlot(), 3214);
			Time.sleep(190, 250);
			Packets.sendAction(870, main.getID(), main.getSlot(), 3214);
			Methods.conditionalSleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return Players.getMyPlayer().getAnimation() != -1;
				}
			}, 5000);
		}
	}
	
	 private static boolean busy(){
	        Timer t = new Timer();
	        int startCount = Inventory.getAll().length;
	        while(t.getElapsed() < 2120){
	            if(Player.getMyPlayer().getAnimation() == 886 || Inventory.getAll().length < startCount){
	                return true;
	            }
	            Time.sleep(40, 100);
	        }
	        return false;
	    }
}
