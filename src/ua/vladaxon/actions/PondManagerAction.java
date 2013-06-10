package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.ponds.PondManager;

/**
 * ��������, ����������� �������� ������� �������.
 * ���� ������ �� ��� ������ - �� ���������.
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
	
	/**������ ������, ��������� ��� ���������*/
	private ServerLogger logger = null;
	/**�������� ������� �������*/
	private PondManager manager = null;
	/**��� ��������*/
	private static final String pondmanagename = "������";
	/**�������� �������� ��������*/
	private static final String shortdescription = "��������� ���� ���������� ��������";
	/**������ ��������*/
	public static final ImageIcon pondicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/lake.png"));
	private static final long serialVersionUID = 1L;
}