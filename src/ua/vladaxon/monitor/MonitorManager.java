package ua.vladaxon.monitor;

import java.util.Set;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Monitor;

/**
 * �������� ������� �����������.
 * ������������� ������ � ��������, �� �������� � ��������.
 */
public class MonitorManager extends BasicManager<Monitor>{

	public MonitorManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new MonitorManagerUI(this);
		newitem = new NewMonitoringData(ui.getOwnerFrame(), this);
	}
	
	/**
	 * ������� ������ �� ��������� ��������� ������� �������.
	 * ��������� ���������� ��� �������� ����� ������ �����������,
	 * ����� ��������� ������ � ������.
	 */
	public void requestPondIds(){
		dbmanager.loadAllPondId(this);
	}
	
	/**
	 * �����, ���������� ��������� ������� ������� �
	 * ���������� ��� ������� �������� ����� ������ �����������.
	 * @param pondids ��������� ������� �������
	 */
	public void setAllPondId(Set<Integer> pondids){
		((NewMonitoringData)newitem).setAllPondId(pondids);
	}

	@Override
	public int getColumnCount() {
		return Monitor.getColumnCount();
	}

	@Override
	public String getHeader(int columnIndex) {
		return Monitor.getHeader(columnIndex);
	}

	@Override
	public boolean isEditable(int columnIndex) {
		return false;
	}

}