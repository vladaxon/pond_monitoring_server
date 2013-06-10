package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;

/**
 * ������ ��������, ����������� ������ �� ��
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
	
	/**��� ��������*/
	private static final String disconnectname = "����������� �� ��";
	/**�������� �������� ��������*/
	private static final String shortdescription = "��������� ������ �� ���� ������";
	/**������ ��������*/
	public static final ImageIcon disconnicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/disconnect.png"));
	private static final long serialVersionUID = 1L;

}