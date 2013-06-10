package ua.vladaxon.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.actions.AboutAction;
import ua.vladaxon.actions.ConnectDBAction;
import ua.vladaxon.actions.DBBindingAbstractAction;
import ua.vladaxon.actions.DisconnectDBAction;
import ua.vladaxon.actions.MonitorManagerAction;
import ua.vladaxon.actions.PondManagerAction;
import ua.vladaxon.actions.UserManagerAction;

/**
 * Объект главного интерфейса сервера
 */
public class ServerUI {

	public ServerUI(ServerLogger logger){
		this.logger = logger;
		mainframe = new JFrame(frametitle);
		actions[0] = new ConnectDBAction(mainframe);
		actions[1] = new DisconnectDBAction();
		actions[2] = new UserManagerAction(logger);
		actions[3] = new PondManagerAction(logger);
		actions[4] = new MonitorManagerAction(logger);
		JPanel vertpanel = new JPanel();
		vertpanel.setLayout(new BoxLayout(vertpanel, BoxLayout.Y_AXIS));
		vertpanel.add(Box.createVerticalStrut(strutsize));
		JTextArea log = logger.getTextArea();
		JScrollPane scrolllog = new JScrollPane(log);
		scrolllog.setAlignmentX(Component.LEFT_ALIGNMENT);
		log.setEditable(false);
		log.setWrapStyleWord(true);
		log.setLineWrap(true);
		vertpanel.add(scrolllog);
		vertpanel.add(Box.createVerticalStrut(strutsize));
		vertpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.X_AXIS));
		mainpanel.add(Box.createHorizontalStrut(strutsize));
		mainpanel.add(vertpanel);
		mainpanel.add(Box.createHorizontalStrut(strutsize));
		mainpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainpanel.setPreferredSize(new Dimension(700, 250));
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setContentPane(mainpanel);
		mainframe.setIconImage
		(new ImageIcon(Server.class.getResource("/ua/vladaxon/res/server.png")).getImage());
		mainframe.setJMenuBar(createMainMenu(mainframe));
		mainframe.pack();
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
	
	/**
	 * Метод, возвращающий массив действий для менеджера БД.
	 * @return Массив действий.
	 */
	public DBBindingAbstractAction[] getActions(){
		return actions;
	}
	
	/**
	 * Метод, создающий главное меню.
	 * Составляет меню, добавляет необходимые слушатели.
	 * @return Главное меню сервера.
	 */
	private JMenuBar createMainMenu(JFrame mainframe){
		JMenuBar menu = new JMenuBar();
		Font menufont = new Font("Arial", Font.PLAIN, 13);
		JMenu server = new JMenu(servmenuname);
		server.setFont(menufont);
		JMenuItem clrlog = new JMenuItem(logger.getClearAction());
		clrlog.setFont(menufont);
		JMenuItem conndb = new JMenuItem(actions[0]);
		conndb.setFont(menufont);
		JMenuItem disconndb = new JMenuItem(actions[1]);
		disconndb.setFont(menufont);
		server.add(conndb);
		server.add(disconndb);
		server.addSeparator();
		server.add(clrlog);	
		JMenu managment = new JMenu(manegmenuname);
		managment.setFont(menufont);
		JMenuItem users = new JMenuItem(actions[2]);
		users.setFont(menufont);
		managment.add(users);
		JMenuItem ponds = new JMenuItem(actions[3]);
		ponds.setFont(menufont);
		managment.add(ponds);
		JMenuItem monitor = new JMenuItem(actions[4]);
		monitor.setFont(menufont);
		managment.add(monitor);
		JMenu helpmenu = new JMenu(helpmenuname);
		helpmenu.setFont(menufont);
		JMenuItem aboutmenu = new JMenuItem(new AboutAction(mainframe));
		aboutmenu.setFont(menufont);
		helpmenu.add(aboutmenu);
		menu.add(server);
		menu.add(managment);
		menu.add(helpmenu);
		return menu;
	}
	
	/**Объект логгера*/
	private ServerLogger logger = null;
	/**Объект главного окна*/
	private JFrame mainframe = null;
	/**Массив действий, которые используются в пунктах меню и управляются менеджером БД.*/
	private DBBindingAbstractAction[] actions = new DBBindingAbstractAction[5];
	/**Метка кнопки подтверждения*/
	public static final String oklabel = "Готово";
	/**Иконка подтверждения*/
	public static final ImageIcon okicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/ok.png"));
	/**Метка кнопки отмены*/
	public static final String cancellabel = "Отмена";
	/**Иконка отмены*/
	public static final ImageIcon cancelicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/cancel.png"));
	/**Размер распорки менеджера компонентов*/
	private static final int strutsize = 10;
	/**Заголовок окна*/
	private static final String frametitle = "Pond Monitoring Server";
	//Названия пунктов меню
	private static final String servmenuname = "Сервер";
	private static final String manegmenuname = "Управление";
	private static final String helpmenuname = "Справка";

}