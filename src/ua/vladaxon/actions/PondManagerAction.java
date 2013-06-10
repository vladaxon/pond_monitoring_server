package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.ponds.PondManager;

/**
 * Действие, открывающее менеджер записей ставков.
 * Если объект не был создан - он создается.
 */
public class PondManagerAction extends DBBindingAbstractAction{

	public PondManagerAction(ServerLogger logger){
		super(pondmanagename, shortdescription, pondicon);
		this.logger = logger;
		this.setEnabled(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(manager==null)
			manager = new PondManager(dbmanager, logger);
		manager.openManager();
	}
	
	/**Объект логера, необходим для менеджера*/
	private ServerLogger logger = null;
	/**Менеджер записей ставков*/
	private PondManager manager = null;
	/**Имя действия*/
	private static final String pondmanagename = "Ставки";
	/**Короткое описание действия*/
	private static final String shortdescription = "Открывает окно управления ставками";
	/**Иконка действия*/
	public static final ImageIcon pondicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/lake.png"));
	private static final long serialVersionUID = 1L;
}