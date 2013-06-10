package ua.vladaxon.ponds;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ua.vladaxon.actions.CloseManagerAction;
import ua.vladaxon.actions.DeleteItemAction;
import ua.vladaxon.actions.NewItemAction;
import ua.vladaxon.actions.PondManagerAction;
import ua.vladaxon.actions.UpdateItemAction;
import ua.vladaxon.ui.BasicManagerUI;

/**
 * Пользовательский интерфейс менеджера ставков.
 * Конструктор создает интерфейс и добавляет нужные слушатели корректности.
 * @see BasicManagerUI
 */
public class PondManagerUI extends BasicManagerUI{

	public PondManagerUI(PondManager manager){
		super(frametitle, manager);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		setFixedWidthByHeader(table.getColumnModel().getColumn(0), 25);
		setFixedWidthByHeader(table.getColumnModel().getColumn(2), 15);
		setFixedWidthByHeader(table.getColumnModel().getColumn(4), 15);
		setFixedWidthByHeader(table.getColumnModel().getColumn(5), 15);
		JScrollPane scroll = new JScrollPane(table);
		mainpanel.add(scroll);
		mainpanel.setPreferredSize(new Dimension(1000,300));
		JMenuBar menu = new JMenuBar();
		JMenu control = new JMenu(controlmenu);
		control.setFont(menufont);
		JMenu view = new JMenu(viewmenu);
		view.setFont(menufont);
		menu.add(control);
		menu.add(view);
		NewItemAction newitem = new NewItemAction
				(manager, newpondname, shortdescr, PondManagerAction.pondicon);
		JMenuItem add = new JMenuItem(newitem);
		add.setIcon(PondManagerAction.pondicon);
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
		mainframe.setIconImage(PondManagerAction.pondicon.getImage());
		mainframe.setJMenuBar(menu);
	}
	
	/**Заголовок окна*/
	private static final String frametitle = "Управление ставками";
	/**Имя действия создания новой записи ставка*/
	private static final String newpondname = "Новый ставок";
	/**Описание действия создания новой записи ставка*/
	private static final String shortdescr = "Отображает окно диалога создания нового ставка";

}