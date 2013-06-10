package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.users.UserManager;

/**
 * Действие, вызывающее менеджер пользователей.
 * Создает объект если он еще не был создан.
 * @see UserManager
 */
public class UserManagerAction extends DBBindingAbstractAction{

	public UserManagerAction(ServerLogger logger){
		super(usermanagename, shortdescription, usersicon);
		this.logger = logger;
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.enabled){
			if(usermanager==null)
				usermanager = new UserManager(dbmanager, logger);
			usermanager.openManager();
		}
	}
	
	/**Объект логера, необходим для менеджера*/
	private ServerLogger logger = null;
	/**Менеджер пользователей*/
	private UserManager usermanager = null;
	/**Имя действия*/
	private static final String usermanagename = "Пользователи";
	/**Короткое описание действия*/
	private static final String shortdescription = "Открывает окно управления пользователями";
	/**Иконка действия*/
	public static final ImageIcon usersicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/users.png"));
	private static final long serialVersionUID = 1L;

}