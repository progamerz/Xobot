package com.death.gem.cutter.methods;

import com.death.gem.cutter.data.Data;

import xobot.script.methods.Bank;
import xobot.script.methods.GameObjects;
import xobot.script.methods.tabs.Inventory;
import xobot.script.util.Time;
import xobot.script.wrappers.interactive.GameObject;
import xobot.script.wrappers.interactive.Item;

public class BankingMethods {

	public static boolean canBank() {
		final GameObject bank = GameObjects.getNearest(Data.boothId);
		return bank != null && bank.getDistance() <= 3;
	}

	public static boolean needBank() {
		return (!Inventory.Contains(Data.mainIng) || !Inventory.Contains(Data.secondaryIng));
	}

	public static void doBank() {
		final GameObject bank = GameObjects.getNearest(Data.boothId);
		if (Bank.isOpen()) {
			if (!Inventory.isEmpty()) {
				System.out.println("Deposit all");
				Bank.depositAll();
				Methods.conditionalSleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return Inventory.isEmpty();
					}
				}, 3000);
			} else if ((!Inventory.Contains(Data.mainIng) && !Inventory.Contains(Data.secondaryIng))) {
				Item main = Bank.getItem(Data.mainIng);
				Item sec = Bank.getItem(Data.secondaryIng);
				System.out.println("main is " + main);
				System.out.println("sec is " + sec);
				if (main != null && sec != null) {
					System.out.println("Withdraw " + main.getID() + "   " + 1);
					Bank.withdraw(Data.mainIng, 1);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.mainIng);
						}
					}, 3000);
					System.out.println("Withdraw " + sec.getID() + "   " + 27);
					Bank.withdraw(Data.secondaryIng, 27);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.secondaryIng);
						}
					}, 3000);
				} else {
					Data.outOfSupplies = true;
				}
			}
		} else {
			if (bank != null && bank.getDistance() <= 3) {
				System.out.println("Open Bank");
				bank.interact("Bank");
				Methods.conditionalSleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return Bank.isOpen();
					}
				}, 5000);
			}
		}
	}

	public static boolean needTipsBank() {
		return (!Inventory.Contains(Data.mainIng) || !Inventory.Contains(Data.secondaryIng));
	}

	public static void doTipsBank() {
		final GameObject bank = GameObjects.getNearest(Data.boothId);
		if (Bank.isOpen()) {
			if (Inventory.getCount() > 0) {
				Bank.depositAll();
				Time.sleep(490, 590);
			}
			if ((!Inventory.Contains(Data.mainIng) && !Inventory.Contains(Data.secondaryIng))) {
				Item main = Bank.getItem(Data.mainIng);
				Item sec = Bank.getItem(Data.secondaryIng);
				System.out.println("main is " + main);
				System.out.println("sec is " + sec);
				if (main != null && sec != null) {
					System.out.println("Withdraw " + main.getID() + "   " + 1);
					Bank.withdraw(Data.mainIng, 1);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.mainIng);
						}
					}, 3000);
					System.out.println("Withdraw " + sec.getID() + "   " + 26);
					Bank.withdraw(Data.secondaryIng, 26);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.secondaryIng);
						}
					}, 3000);
				} else {
					Data.outOfSupplies = true;
				}
			}
		} else {
			if (bank != null && bank.getDistance() <= 3) {
				bank.interact("Bank");
				Methods.conditionalSleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return Bank.isOpen();
					}
				}, 5000);
			}
		}
	}
	
	public static boolean needBoltsBank() {
		return (!Inventory.Contains(Data.mainIng) || !Inventory.Contains(Data.secondaryIng));
	}

	public static void doBoltsBank() {
		final GameObject bank = GameObjects.getNearest(Data.boothId);
		if (Bank.isOpen()) {
			if (Inventory.getCount() > 0) {
				Bank.depositAll();
				Time.sleep(490, 590);
			}
			if ((!Inventory.Contains(Data.mainIng) && !Inventory.Contains(Data.secondaryIng))) {
				Item main = Bank.getItem(Data.mainIng);
				Item sec = Bank.getItem(Data.secondaryIng);
				System.out.println("main is " + main);
				System.out.println("sec is " + sec);
				if (main != null && sec != null) {
					System.out.println("Withdraw " + main.getID() + "   " + 1);
					Bank.withdraw(Data.mainIng, 99999);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.mainIng);
						}
					}, 3000);
					System.out.println("Withdraw " + sec.getID() + "   " + 26);
					Bank.withdraw(Data.secondaryIng, 99999);
					Methods.conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.Contains(Data.secondaryIng);
						}
					}, 3000);
				} else {
					Data.outOfSupplies = true;
				}
			}
		} else {
			if (bank != null && bank.getDistance() <= 3) {
				bank.interact("Bank");
				Methods.conditionalSleep(new SleepCondition() {
					@Override
					public boolean isValid() {
						return Bank.isOpen();
					}
				}, 5000);
			}
		}
	}
}
