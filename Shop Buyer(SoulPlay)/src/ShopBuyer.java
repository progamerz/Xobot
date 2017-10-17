import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import xobot.client.callback.listeners.MessageListener;
import xobot.client.callback.listeners.PaintListener;
import xobot.script.ActiveScript;
import xobot.script.Manifest;
import xobot.script.methods.Bank;
import xobot.script.methods.GameObjects;
import xobot.script.methods.NPCs;
import xobot.script.methods.Packets;
import xobot.script.methods.Players;
import xobot.script.methods.Shop;
import xobot.script.methods.Walking;
import xobot.script.methods.tabs.Inventory;
import xobot.script.util.Time;
import xobot.script.wrappers.Tile;
import xobot.script.wrappers.interactive.GameObject;
import xobot.script.wrappers.interactive.NPC;

@Manifest(authors = {
		"Death Dead" }, name = "DD's Shop Buyer", version = 1.2, description = "Buys items from stores in ::skill")
public class ShopBuyer extends ActiveScript implements PaintListener, MessageListener {

	private int NPCid = 0;
	private int Itemid = 0;
	private int slotid = 0;

	private String itemtype = "";

	private long startTime;
	private int itemsBought;
	private final Color color = new Color(19, 197, 255);

	private boolean skillStore = false;

	private String status = "";

	private JComboBox<String> combo;

	public boolean onStart() {
		JDialog frame = new JDialog();
		frame.setPreferredSize(new Dimension(250, 120));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		FlowLayout layout = new FlowLayout();
		layout.setHgap(5);
		layout.setVgap(5);
		frame.setLayout(layout);

		combo = new JComboBox<String>();
		combo.setPreferredSize(new Dimension(150, 30));
		combo.setFocusable(false);
		loadHerbloreStore();

		JCheckBox skillingStore = new JCheckBox("Skilling Store");
		skillingStore.setPreferredSize(new Dimension(150, 30));
		skillingStore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				skillStore = skillingStore.isSelected();
				if (skillStore) {
					loadSkillingStore();
				} else {
					loadHerbloreStore();
				}
			}
		});

		JButton button = new JButton("Start");
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(60, 30));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (skillStore) {
					itemtype = (String) combo.getSelectedItem();
					switch (itemtype) {
					case "Uncut Sapphire":
						NPCid = 519;
						Itemid = 1623;
						slotid = 10;
						break;
					case "Uncut Emerald":
						NPCid = 519;
						Itemid = 1621;
						slotid = 11;
						break;
					case "Uncut Ruby":
						NPCid = 519;
						Itemid = 1619;
						slotid = 12;
						break;
					case "Uncut Diamond":
						NPCid = 519;
						Itemid = 1617;
						slotid = 13;
						break;
					case "Leather":
						NPCid = 519;
						Itemid = 1741;
						slotid = 14;
						break;
					case "Green Dragon Leather":
						NPCid = 519;
						Itemid = 1745;
						slotid = 15;
						break;
					case "Blue Dragon Leather":
						NPCid = 519;
						Itemid = 2505;
						slotid = 16;
						break;
					case "Red Dragon Leather":
						NPCid = 519;
						Itemid = 2507;
						slotid = 17;
						break;
					}
				} else {
					itemtype = (String) combo.getSelectedItem();
					switch (itemtype) {
					case "Goat Horn Dust":
						NPCid = 587;
						Itemid = 9736;
						slotid = 9;
						break;
					case "Snape grass":
						NPCid = 587;
						Itemid = 231;
						slotid = 10;
						break;
					case "Wine of zamorak":
						NPCid = 587;
						Itemid = 245;
						slotid = 12;
						break;
					case "Potato cactus":
						NPCid = 587;
						Itemid = 3138;
						slotid = 13;
						break;
					case "Crushed nest":
						NPCid = 587;
						Itemid = 6693;
						slotid = 15;
						break;
					case "Mort Fungi":
						NPCid = 587;
						Itemid = 2970;
						slotid = 16;
						break;
					case "Dragon Shop":
						NPCid = 587;
						Itemid = 241;
						slotid = 11;
						break;
					case "JangerBerries":
						NPCid = 587;
						Itemid = 247;
						slotid = 14;
						break;
					case "White Berries":
						NPCid = 587;
						Itemid = 239;
						slotid = 8;
						break;
					case "Spider Eggs":
						NPCid = 587;
						Itemid = 223;
						slotid = 7;
						break;
					}
				}
				frame.dispose();
				startTime = System.currentTimeMillis();
			}
		});

		frame.add(combo);
		frame.add(skillingStore);
		frame.add(button);
		frame.setTitle("DD's Shop Buyer");

		frame.pack();
		frame.setVisible(true);
		while (frame.isVisible()) {
			Time.sleep(500);
		}
		return Itemid != 0 && slotid != 0;
	}

	@Override
	public int loop() {
		if (Inventory.isFull()) {
			bank();
		} else {
			buy();
		}
		return 0;
	}

	@Override
	public void repaint(Graphics g) {
		long runTime = System.currentTimeMillis() - startTime;
		int perHour = (int) ((itemsBought) * 3600000D / (runTime));
		int x = 10, y = 40;
		g.setColor(color);
		g.drawString("DD's Shop Buyer v1.2", x, y);
		g.drawString("Runtime: " + formatTimeDHMS(runTime), x, y += 15);
		g.drawString(itemtype + "(s) Bought: " + itemsBought + "(" + perHour + ")", x, y += 15);
		g.drawString("Status: " + status, x, y += 15);
	}

	private static String formatTimeDHMS(final long time) {
		final int sec = (int) (time / 1000), d = sec / 86400, h = sec / 3600 % 24, m = sec / 60 % 60, s = sec % 60;
		return (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	private void buy() {
		if (skillStore) {
			if (Shop.isOpen()) {
				if (Shop.contains(Itemid) && Shop.getItem(Itemid).getStack() >= 10) {
					int am = Inventory.Contains(Itemid) ? Inventory.getCount(Itemid) : 0;
					status = "Buying " + itemtype + "(s)";
					Packets.sendAction(54, Itemid, slotid, 3900);
					conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.getCount(Itemid) > am;
						}
					}, 1500);
					itemsBought += Inventory.getCount(Itemid) - am;
				}
			} else {
				Tile t1 = new Tile(2342, 3813);
				NPC npc = NPCs.getNearest(NPCid);
				if (npc != null) {
					if (npc.isReachable()) {
						if (Players.getMyPlayer().getLocation().equals(t1)) {
							status = "Trade NPC";
							npc.interact("Trade");
							conditionalSleep(new SleepCondition() {
								@Override
								public boolean isValid() {
									return Shop.isOpen();
								}
							}, 1500);
						} else {
							status = "Walking to " + t1;
							Walking.walkTo(t1);
							conditionalSleep(new SleepCondition() {
								@Override
								public boolean isValid() {
									return Players.getMyPlayer().getLocation().equals(t1);
								}
							}, 7000);
						}
					}
				}

			}
		} else {
			if (Shop.isOpen()) {
				if (Shop.contains(Itemid) && Shop.getItem(Itemid).getStack() >= 10) {
					int am = Inventory.Contains(Itemid) ? Inventory.getCount(Itemid) : 0;
					status = "Buying " + itemtype + "(s)";
					Packets.sendAction(54, Itemid, slotid, 3900);
					conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Inventory.getCount(Itemid) > am;
						}
					}, 1500);
					itemsBought += Inventory.getCount(Itemid) - am;
				}
			} else {
				Tile t1 = new Tile(2345, 3807);
				Tile t2 = new Tile(2347, 3806);
				NPC npc = NPCs.getNearest(NPCid);
				if (npc != null) {
					if (npc.isReachable()) {
						if (Players.getMyPlayer().getLocation().equals(t2)) {
							status = "Trading NPC";
							npc.interact("Trade");
							conditionalSleep(new SleepCondition() {
								@Override
								public boolean isValid() {
									return Shop.isOpen();
								}
							}, 2500);
						} else {
							status = "Walking to " + t2;
							Walking.walkTo(t2);
							conditionalSleep(new SleepCondition() {
								@Override
								public boolean isValid() {
									return Players.getMyPlayer().getLocation().equals(t2) || !npc.isReachable();
								}
							}, 7000);
						}
					} else {
						if (Players.getMyPlayer().getLocation().equals(t1)) {
							GameObject door = GameObjects.getTopAt(new Tile(2345, 3807));
							if (door != null) {
								status = "Open door at " + door.getLocation();
								Packets.sendAction(502, door.uid, door.getX(), door.getY(), 21341, 1);
								conditionalSleep(new SleepCondition() {
									@Override
									public boolean isValid() {
										return door == null;
									}
								}, 2500);
							} else {
								status = "Walking to " + t2;
								Walking.walkTo(t2);
								conditionalSleep(new SleepCondition() {
									@Override
									public boolean isValid() {
										return Players.getMyPlayer().getLocation().equals(t2) || !npc.isReachable();
									}
								}, 7000);
							}
						} else {
							status = "Walking to " + t1;
							Walking.walkTo(t1);
							conditionalSleep(new SleepCondition() {
								@Override
								public boolean isValid() {
									return Players.getMyPlayer().getLocation().equals(t1) || npc.isReachable();
								}
							}, 7000);
						}
					}
				}
			}

		}
	}

	private void bank() {
		if (Bank.isOpen()) {
			status = "Deposit all " + itemtype + "(s)";
			Inventory.getItem(Itemid).interact("store all");
			conditionalSleep(new SleepCondition() {
				@Override
				public boolean isValid() {
					return !Inventory.isFull();
				}
			}, 2000);
		} else {
			GameObject obj = GameObjects.getNearest(21301);
			if (obj != null) {
				if (obj.isReachable()) {
					status = "Open bank";
					obj.interact("Bank");
					conditionalSleep(new SleepCondition() {
						@Override
						public boolean isValid() {
							return Bank.isOpen();
						}
					}, 7000);
				} else {
					GameObject door = GameObjects.getTopAt(new Tile(2345, 3807));
					if (door != null) {
						status = "Open door at " + door.getLocation();
						Packets.sendAction(502, door.uid, door.getX(), door.getY(), 21341, 1);
						conditionalSleep(new SleepCondition() {
							@Override
							public boolean isValid() {
								return door == null;
							}
						}, 2500);
					}
				}
			}

		}
	}

	public static boolean conditionalSleep(SleepCondition conn, int timeout) {
		long start = System.currentTimeMillis();
		while (!conn.isValid()) {
			if (start + timeout < System.currentTimeMillis()) {
				return false;
			}
			Time.sleep(50);
		}
		return true;
	}

	@Override
	public void MessageRecieved(String arg0, int arg1, String arg2) {

	}

	public void loadHerbloreStore() {
		combo.removeAllItems();
		combo.addItem("Spider Eggs");
		combo.addItem("White Berries");
		combo.addItem("Goat Horn Dust");
		combo.addItem("Snape grass");
		combo.addItem("Dragon Shop");
		combo.addItem("Wine of zamorak");
		combo.addItem("Potato cactus");
		combo.addItem("JangerBerries");
		combo.addItem("Crushed nest");
		combo.addItem("Mort Fungi");
	}

	public void loadSkillingStore() {
		combo.removeAllItems();
		combo.addItem("Uncut Sapphire");
		combo.addItem("Uncut Emerald");
		combo.addItem("Uncut Ruby");
		combo.addItem("Uncut Diamond");
		combo.addItem("Leather");
		combo.addItem("Green Dragon Leather");
		combo.addItem("Blue Dragon Leather");
		combo.addItem("Red Dragon Leather");
	}

}