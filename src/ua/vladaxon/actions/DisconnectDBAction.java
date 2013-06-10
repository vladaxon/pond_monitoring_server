package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;

/**
 * Объект действия, отключающий сервер от БД
 */
public class DisconnectDBAction extends DBBindingAbstractAction{

	public DisconnectDBAction(){
		super(disconnectname, shortdescription, disconnicon);
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(dbmanager!=null)
			dbmanager.disconnect();
	}
	
	/**Имя действия*/
	private static final String disconnectname = "Отключиться от БД";
	/**Короткое описание действия*/
	private static final String shortdescription = "Отключает сервер от базы данных";
	/**Иконка действия*/
	public static final ImageIcon disconnicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/disconnect.png"));
	private static final long serialVersionUID = 1L;

}