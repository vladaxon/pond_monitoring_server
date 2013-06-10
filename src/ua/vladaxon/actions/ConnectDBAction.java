package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ua.vladaxon.Server;
import ua.vladaxon.ui.DBLogin;

/**
 * ������ �������� ����������� � ���� ������
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
	
	/**���������� ���� ����������� � ��*/
	private DBLogin login = null;
	/**��� ��������*/
	private static final String connectname = "������������ � ��";
	/**�������� �������� ��������*/
	private static final String shortdescription = "���������� ������ � ���� ������";
	/**������ ��������*/
	public static final ImageIcon connicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/connect.png"));
	private static final long serialVersionUID = 1L;
	
}