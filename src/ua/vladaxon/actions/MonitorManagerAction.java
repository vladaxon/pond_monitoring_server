package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;
import ua.vladaxon.monitor.MonitorManager;

/**
 * ������ �������� �������� ��������� ������� �����������.
 * ������� ������ ��������� � ��������� ���.
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
	
	/**������ ������, ��������� ��� ���������*/
	private ServerLogger logger = null;
	/**������ ���������*/
	MonitorManager manager = null;
	/**��� ��������*/
	private static final String monitormanagename = "����������";
	/**�������� �������� ��������*/
	private static final String shortdescription = "��������� ���� ���������� ������������";
	/**������ ��������*/
	public static final ImageIcon monitoricon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/monitoring.png"));
	private static final long serialVersionUID = 1L;
	
}