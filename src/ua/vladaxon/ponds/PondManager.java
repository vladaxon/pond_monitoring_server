package ua.vladaxon.ponds;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.Pond;

/**
 * �������� ������� �������. ��������� ��������. 
 * �������, �����������, ������� ������ �������.
 * @see BasicManager
 */
public class PondManager extends BasicManager<Pond>{

	public PondManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new PondManagerUI(this);
		newitem = new NewPond(ui.getOwnerFrame(), this);
	}
	
	/**
	 * �����, ����������� �� ������������ ����� ������ ��� ��������.
	 * ��������� ��������� ���������� ����� �������������� ������ �� �������� � ��������
	 * ������� �������, ��� ������������� ��������� ������������ ������ �������� �� ��.
	 * @param id ����� ��� ��������.
	 * @return true - ���� ����� ��������.
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