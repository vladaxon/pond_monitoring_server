package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.monitor.MonitorManager;

/**
 * Объект действия открытия менеджера записей мониторинга.
 * Создает объект менеджера и запускает его.
 */
public class MonitorManagerAction extends DBBindingAbstractAction{

	public MonitorManagerAction(ServerLogger logger){
		super(monitormanagename, shortdescription, monitoricon);
		this.logger = logger;
		this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(manager==null)
			manager = new MonitorManager(dbmanager, logger);
		manager.openManager();
	}
	
	/**Объект логера, необходим для менеджера*/
	private ServerLogger logger = null;
	/**Объект менеджера*/
	MonitorManager manager = null;
	/**Имя действия*/
	private static final String monitormanagename = "Мониторинг";
	/**Короткое описание действия*/
	private static final String shortdescription = "Открывает окно управления мониторингом";
	/**Иконка действия*/
	public static final ImageIcon monitoricon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/monitoring.png"));
	private static final long serialVersionUID = 1L;
	
}