package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;

/**
 * ������ ��������, ��������� ���� �������.
 * ������ ������ ���������� � ������� ������� � ���������� ����������� ���������� �� �������������.
 * ������ ����� �������������� ���������� ����� �������� �� ���� ����� ���������� �����������.
 * @see ServerLogger
 */
public class ClearLogAction extends AbstractAction{
	
	public ClearLogAction(ServerLogger logger){
		super();
		this.logger = logger;
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescr);
		this.putValue(SMALL_ICON, clricon);
		this.setEnabled(false);
	}
	
	/**
	 * ������� ��� ���������.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		logger.clearLog();
	}
	
	/**������ �������, ����������� ��� ������ �������*/
	private ServerLogger logger = null;
	/**��� ��������*/
	private static final String actionname = "�������� ���";
	/**�������� �������� ��������*/
	private static final String shortdescr = "������� ���� ���� �� ���� ���������";
	/**������ ��������*/
	public static final ImageIcon clricon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/clear.png"));
	private static final long serialVersionUID = 1L;

}