package ua.vladaxon.ponds;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Pond;

/**
 * Менеджер записей ставков. Управляет записями. 
 * Удаляет, редактирует, создает записи ставков.
 * @see BasicManager
 */
public class PondManager extends BasicManager<Pond>{

	public PondManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new PondManagerUI(this);
		newitem = new NewPond(ui.getOwnerFrame(), this);
	}
	
	/**
	 * Метод, проверяющий на уникальность номер ставка при создании.
	 * Поскольку серверное приложение имеет исключительный доступ на создание и удаление
	 * записей ставков, нет необходимости проверять уникальность номера напрямую из БД.
	 * @param id Номер для создания.
	 * @return true - если номер уникален.
	 */
	public boolean isUnique(int id){
		for(Pond p: itemlist)
			if(p.getId()==id)
				return false;
		return true;
	}

	@Override
	public int getColumnCount() {
		return Pond.getColumnCount();
	}

	@Override
	public String getHeader(int columnIndex) {
		return Pond.getHeader(columnIndex);
	}

	@Override
	public boolean isEditable(int columnIndex) {
		return Pond.isEditable(columnIndex);
	}

}