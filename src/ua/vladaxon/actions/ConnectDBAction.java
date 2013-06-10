package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ua.vladaxon.Server;
import ua.vladaxon.ui.DBLogin;

/**
 * Объект действия подключения к базе данных
 */
public class ConnectDBAction extends DBBindingAbstractAction{

	public ConnectDBAction(JFrame owner){
		super(connectname, shortdescription, connicon);
		login = new DBLogin(owner);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(dbmanager!=null)
			if(login.showDialog()){
				dbmanager.connect(login.getLogin(), login.getPassword());
			}
	}
	
	/**Диалоговое окно подключения к БД*/
	private DBLogin login = null;
	/**Имя действия*/
	private static final String connectname = "Подключиться к БД";
	/**Короткое описание действия*/
	private static final String shortdescription = "Подключает сервер к базе данных";
	/**Иконка действия*/
	public static final ImageIcon connicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/connect.png"));
	private static final long serialVersionUID = 1L;
	
}