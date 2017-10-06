import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

import com.death.gem.cutter.data.Data;
import com.death.gem.cutter.methods.BankingMethods;
import com.death.gem.cutter.methods.BoltMethods;
import com.death.gem.cutter.methods.CutMethods;
import com.death.gem.cutter.methods.cutTipsMethods;

import xobot.client.callback.listeners.MessageListener;
import xobot.client.callback.listeners.PaintListener;
import xobot.script.ActiveScript;
import xobot.script.Manifest;
import xobot.script.methods.tabs.Skills;
import xobot.script.util.Random;
import xobot.script.util.Time;

@Manifest(authors = {
		"Death Dead" }, name = "Death Dead Gem Cutter & Bolter", version = 1.0, description = "Cuts uncut gems to gems or gems to bolt tips")
public class GemCutter extends ActiveScript implements PaintListener, MessageListener {

	private static long startTime;

	public static int startExp = 0;
	public static int startLvl = 0;
	public static boolean cutTips = false;
	public final static double version = 3.0;
	public static String itemToDecant = "";
	JComboBox<String> combo;

	private final Color color = new Color(19, 197, 255);

	private boolean doBolts;

	@Override
	public boolean onStart() {
		JDialog frame = new JDialog();
		frame.setPreferredSize(new Dimension(300, 150));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		FlowLayout layout = new FlowLayout();
		layout.setHgap(5);
		layout.setVgap(5);
		frame.setLayout(layout);

		combo = new JComboBox<String>();
		combo.setPreferredSize(new Dimension(200, 30));
		combo.setFocusable(false);

		loadUncuts();

		JTextField boothId = new JTextField();
		boothId.setToolTipText("Bank booth id");
		boothId.setPreferredSize(new Dimension(60, 25));

		JCheckBox bolttips = new JCheckBox("Cut into tips");
		bolttips.setPreferredSize(new Dimension(150, 30));
		bolttips.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				cutTips = bolttips.isSelected();
				if (cutTips) {
					loadcutTips();
				} else {
					loadUncuts();
				}
			}
		});

		JCheckBox makeBolts = new JCheckBox("Make Bolts");
		makeBolts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				doBolts = makeBolts.isSelected();
				if (doBolts) {
					loadBolts();
				} else {
					loadUncuts();
				}
			}
		});

		JButton button = new JButton("Start");
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(60, 30));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cutTips) {
					switch (combo.getSelectedItem().toString()) {
					case "--Bolt tips--":
						onStop();
						break;
					case "Opal":
						Data.mainIng = 1755;
						Data.secondaryIng = 1609;
						break;
					case "Sapphire":
						Data.mainIng = 1755;
						Data.secondaryIng = 1607;
						break;
					case "Emerald":
						Data.mainIng = 1755;
						Data.secondaryIng = 1605;
						break;
					case "Ruby":
						Data.mainIng = 1755;
						Data.secondaryIng = 1603;
						break;
					case "Diamond":
						Data.mainIng = 1755;
						Data.secondaryIng = 1601;
						break;
					case "Dragonstone":
						Data.mainIng = 1755;
						Data.secondaryIng = 1615;
						break;
					case "Onyx":
						Data.mainIng = 1755;
						Data.secondaryIng = 6573;
						break;
					case "default":
						onStop();
						break;
					}
				} else if (doBolts) {
					switch (combo.getSelectedItem().toString()) {
					case "--Make Bolts--":
						onStop();
						break;
					case "Opal":
						Data.mainIng = 877;
						Data.secondaryIng = 45;
						break;
					case "Sapphire":
						Data.mainIng = 9142;
						Data.secondaryIng = 9189;
						break;
					case "Emerald":
						Data.mainIng = 9142;
						Data.secondaryIng = 9190;
						break;
					case "Ruby":
						Data.mainIng = 9143;
						Data.secondaryIng = 9191;
						break;
					case "Diamond":
						Data.mainIng = 9143;
						Data.secondaryIng = 9192;
						break;
					case "Dragonstone":
						Data.mainIng = 9144;
						Data.secondaryIng = 9193;
						break;
					case "Onyx":
						Data.mainIng = 9144;
						Data.secondaryIng = 9194;
						break;
					case "default":
						onStop();
						break;
					}
				} else {
					switch (combo.getSelectedItem().toString()) {
					case "--Uncut Gems--":
						onStop();
						break;
					case "Uncut Opal":
						Data.mainIng = 1755;
						Data.secondaryIng = 1625;
						break;
					case "Uncut Sapphire":
						Data.mainIng = 1755;
						Data.secondaryIng = 1623;
						break;
					case "Uncut Emerald":
						Data.mainIng = 1755;
						Data.secondaryIng = 1621;
						break;
					case "Uncut Ruby":
						Data.mainIng = 1755;
						Data.secondaryIng = 1619;
						break;
					case "Uncut Diamond":
						Data.mainIng = 1755;
						Data.secondaryIng = 1617;
						break;
					case "Uncut Dragonstone":
						Data.mainIng = 1755;
						Data.secondaryIng = 1631;
						break;
					case "Uncut Onyx":
						Data.mainIng = 1755;
						Data.secondaryIng = 6571;
						break;
					case "default":
						onStop();
						break;
					}
				}
				itemToDecant = combo.getSelectedItem().toString();
				if (boothId.getText() != null && !boothId.getText().isEmpty()) {
					Data.boothId = Integer.parseInt(boothId.getText());
				}
				frame.dispose();
				startTime = System.currentTimeMillis();
				if (cutTips) {
					startExp = Skills.getCurrentExp(Skills.FLETCHING);
					startLvl = Skills.getCurrentLevel(Skills.FLETCHING);
				} else if (doBolts) {
					startExp = Skills.getCurrentExp(Skills.FLETCHING);
					startLvl = Skills.getCurrentLevel(Skills.FLETCHING);
				} else {
					startExp = Skills.getCurrentExp(Skills.CRAFTING);
					startLvl = Skills.getCurrentLevel(Skills.CRAFTING);
				}
			}
		});

		frame.add(combo);
		frame.add(boothId);
		frame.add(bolttips);
		frame.add(makeBolts);
		frame.add(button);
		frame.setTitle("Death Dead's Gem Cutter");

		frame.pack();
		frame.setVisible(true);
		while (frame.isVisible()) {
			Time.sleep(500);
		}
		return true;
	}

	@Override
	public int loop() {
		if (cutTips) {
			if (cutTipsMethods.canCutTips()) {
				cutTipsMethods.doCutTips();
			} else if (Data.outOfSupplies) {
				System.out.println("Out of supplies");
				return -1;
			} else if (BankingMethods.needTipsBank() && BankingMethods.canBank()) {
				BankingMethods.doTipsBank();
			}
		} else if (doBolts) {
			if (BoltMethods.canMakeBolts()) {
				BoltMethods.doBolts();
			} else if (Data.outOfSupplies) {
				System.out.println("Out of supplies");
				return -1;
			} else if (BankingMethods.needBoltsBank() && BankingMethods.canBank()) {
				BankingMethods.doBoltsBank();
			}
		} else {
			if (CutMethods.canCut()) {
				CutMethods.cutUncuts();
			} else if (Data.outOfSupplies) {
				System.out.println("Out of supplies");
				return -1;
			} else if (BankingMethods.needBank() && BankingMethods.canBank()) {
				BankingMethods.doBank();
			}
		}
		return Random.nextInt(100, 250);
	}

	@Override
	public void repaint(Graphics g) {
		int xpGained;
		int perHour;
		int currentLvl;

		long runTime = System.currentTimeMillis() - startTime;
		if (cutTips) {
			xpGained = Skills.getCurrentExp(Skills.FLETCHING) - startExp;
			perHour = (int) ((xpGained) * 3600000D / (runTime));
			currentLvl = Skills.getCurrentLevel(Skills.FLETCHING);
		} else if (doBolts) {
			xpGained = Skills.getCurrentExp(Skills.FLETCHING) - startExp;
			perHour = (int) ((xpGained) * 3600000D / (runTime));
			currentLvl = Skills.getCurrentLevel(Skills.FLETCHING);
		} else {
			xpGained = Skills.getCurrentExp(Skills.CRAFTING) - startExp;
			perHour = (int) ((xpGained) * 3600000D / (runTime));
			currentLvl = Skills.getCurrentLevel(Skills.CRAFTING);
		}

		int x = 10, y = 20;

		g.setColor(color);
		g.drawString("Death Dead's Gem Cutter & Bolter v1.0", x, y);
		g.drawString("Runtime: " + formatTimeDHMS(runTime), x, y += 15);
		g.drawString("XP Gained: " + formatNumber(xpGained), x, y += 15);
		g.drawString("XP/h: " + formatNumber(perHour), 10, y += 15);
		g.drawString("Current lvl: " + currentLvl + "(" + "+" + (currentLvl - startLvl) + ")", x, y += 15);

	}

	private static String formatNumber(int start) {
		DecimalFormat nf = new DecimalFormat("0.00");
		double i = start;
		if (i >= 1000000) {
			return nf.format((i / 1000000)) + "m";
		}
		if (i >= 1000) {
			return nf.format((i / 1000)) + "k";
		}
		return "" + start;
	}

	private static String formatTimeDHMS(final long time) {
		final int sec = (int) (time / 1000), d = sec / 86400, h = sec / 3600 % 24, m = sec / 60 % 60, s = sec % 60;
		return (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	public void loadcutTips() {
		combo.removeAllItems();
		combo.addItem("--Bolt tips--");
		combo.addItem("Opal");
		combo.addItem("Sapphire");
		combo.addItem("Emerald");
		combo.addItem("Ruby");
		combo.addItem("Diamond");
		combo.addItem("Dragonstone");
		combo.addItem("Onyx");
	}

	public void loadUncuts() {
		combo.removeAllItems();
		combo.addItem("--Uncut Gems--");
		combo.addItem("Uncut Opal");
		combo.addItem("Uncut Sapphire");
		combo.addItem("Uncut Emerald");
		combo.addItem("Uncut Ruby");
		combo.addItem("Uncut Diamond");
		combo.addItem("Uncut Dragonstone");
		combo.addItem("Uncut Onyx");
	}

	public void loadBolts() {
		combo.removeAllItems();
		combo.addItem("--Make Bolts--");
		combo.addItem("Opal");
		combo.addItem("Sapphire");
		combo.addItem("Emerald");
		combo.addItem("Ruby");
		combo.addItem("Diamond");
		combo.addItem("Dragonstone");
		combo.addItem("Onyx");
	}

	@Override
	public void MessageRecieved(String arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

}
