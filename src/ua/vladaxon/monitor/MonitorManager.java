package ua.vladaxon.monitor;

import java.util.Set;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Monitor;

/**
 * ћенеджер записей мониторинга.
 * ѕредоставл€ет работу с запис€ми, их создание и удаление.
 */
public class MonitorManager extends BasicManager<Monitor>{

	public MonitorManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new MonitorManagerUI(this);
		newitem = new NewMonitoringData(ui.getOwnerFrame(), this);
	}
	
	/**
	 * —оздает запрос на получение множества номеров ставков.
	 * ћножество необходимо дл€ создани€ новой записи мониторинга,
	 * чтобы прив€зать запись к ставку.
	 */
	public void requestPondIds(){
		dbmanager.loadAllPondId(this);
	}
	
	/**
	 * ћетод, получающий множество номеров ставков и
	 * передающий его диалогу создани€ новой записи мониторинга.
	 * @param pondids ћножество номеров ставков
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