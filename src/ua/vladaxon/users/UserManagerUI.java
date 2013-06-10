package ua.vladaxon.users;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ua.vladaxon.Server;
import ua.vladaxon.actions.CloseManagerAction;
import ua.vladaxon.actions.DeleteItemAction;
import ua.vladaxon.actions.NewItemAction;
import ua.vladaxon.actions.UpdateItemAction;
import ua.vladaxon.actions.UserManagerAction;
import ua.vladaxon.ui.BasicManagerUI;

/**
 * Пользовательский интерфейс менеджера пользователей.
 * Содержит в себе таблицу и действия, связанные с управлением пользователями.
 */
public class UserManagerUI extends BasicManagerUI{

	/**
	 * Конструктор пользовательского интерфейса.
	 * @param manager Объект менеджера пользователей используется в конструкторе
	 * для создания объектов действий.
	 */
	public UserManagerUI(UserManager manager){
		super(frametitle, manager);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		setFixedWidthByHeader(table.getColumnModel().getColumn(4), 15);
		JScrollPane scroll = new JScrollPane(table);
		mainpanel.add(scroll);
		JMenuBar menu = new JMenuBar();
		JMenu control = new JMenu(controlmenu);
		JMenu view = new JMenu(viewmenu);
		control.setFont(menufont);
		view.setFont(menufont);
		NewItemAction newiten = new NewItemAction(manager, newusractionname, actiondescr, newusericon);
		JMenuItem add = new JMenuItem(newiten);
		add.setFont(menufont);
		DeleteItemAction deleteuser = new DeleteItemAction(manager, mainframe, table);
		JMenuItem delete = new JMenuItem(deleteuser);
		delete.setIcon(BasicManagerUI.delticon);
		delete.setFont(menufont);
		UpdateItemAction updateuser = new UpdateItemAction(manager);
		JMenuItem update = new JMenuItem(updateuser);
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
		menu.add(control);
		menu.add(view);
		mainframe.setIconImage(UserManagerAction.usersicon.getImage());
		mainframe.setContentPane(mainpanel);
		mainframe.setJMenuBar(menu);
	}
	
	/**Заголовок окна*/
	private static final String frametitle = "Управление пользователями";
	/**Имя действия создания новой записи пользователя*/
	private static final String newusractionname = "Новый пользователь";
	/**Описание действия создания новой записи пользователя*/
	private static final String actiondescr = 
			"Отображает окно диалога создания нового пользователя";
	/**Иконка действия создания новой записи пользователя*/
	public static final ImageIcon newusericon =
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/user.png"));
	
}