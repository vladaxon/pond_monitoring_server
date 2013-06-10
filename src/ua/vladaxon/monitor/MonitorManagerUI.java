package ua.vladaxon.monitor;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ua.vladaxon.actions.CloseManagerAction;
import ua.vladaxon.actions.DeleteItemAction;
import ua.vladaxon.actions.MonitorManagerAction;
import ua.vladaxon.actions.NewItemAction;
import ua.vladaxon.actions.UpdateItemAction;
import ua.vladaxon.ui.BasicManagerUI;

public class MonitorManagerUI extends BasicManagerUI{

	private static final String frametitle = "Управление мониторингом";
	
	public MonitorManagerUI(MonitorManager manager) {
		super(frametitle, manager);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(table);
		mainpanel.add(scroll);
		mainpanel.setPreferredSize(new Dimension(600,300));
		//Сборка главного меню
		JMenuBar menu = new JMenuBar();
		JMenu control = new JMenu("Управление");
		control.setFont(menufont);
		JMenu view = new JMenu("Вид");
		view.setFont(menufont);
		menu.add(control);
		menu.add(view);
		NewItemAction newitem = new NewItemAction(manager);
		JMenuItem add = new JMenuItem(newitem);
		add.setIcon(MonitorManagerAction.monitoricon);
		add.setFont(menufont);
		DeleteItemAction deleteitem = new DeleteItemAction(manager, mainframe, table);
		JMenuItem delete = new JMenuItem(deleteitem);
		delete.setIcon(BasicManagerUI.delticon);
		delete.setFont(menufont);
		UpdateItemAction updatepond = new UpdateItemAction(manager);
		JMenuItem update = new JMenuItem(updatepond);
		update.setIcon(BasicManagerUI.updticon);
		update.setFont(menufont);
		CloseManagerAction closeaction = new CloseManagerAction(manager);
		JMenuItem closemenuitem = new JMenuItem(closeaction);
		closemenuitem.setIcon(BasicManagerUI.exiticon);
		closemenuitem.setFont(menufont);
		control.add(add);
		control.add(delete);
		control.add(update);
		control.addSeparator();
		control.add(closemenuitem);
		view.add(columnSubmenu(manager));
		mainframe.setContentPane(mainpanel);
		mainframe.setIconImage(MonitorManagerAction.monitoricon.getImage());
		mainframe.setJMenuBar(menu);
	}

}