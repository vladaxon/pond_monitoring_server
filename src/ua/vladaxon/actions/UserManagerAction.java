package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.users.UserManager;

/**
 * ��������, ���������� �������� �������������.
 * ������� ������ ���� �� ��� �� ��� ������.
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
	
	/**������ ������, ��������� ��� ���������*/
	private ServerLogger logger = null;
	/**�������� �������������*/
	private UserManager usermanager = null;
	/**��� ��������*/
	private static final String usermanagename = "������������";
	/**�������� �������� ��������*/
	private static final String shortdescription = "��������� ���� ���������� ��������������";
	/**������ ��������*/
	public static final ImageIcon usersicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/users.png"));
	private static final long serialVersionUID = 1L;

}