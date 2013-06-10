package ua.vladaxon.users;

import java.util.Set;

import ua.vladaxon.ServerLogger;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.User;

/**
 * �������� ������� �������������. ��������� �������� � ��. ���������
 * ��������� ����� ������, ������������� ������������ ��� �������.
 * @see BasicManager
 */
public class UserManager extends BasicManager<User>{
	
	public UserManager(DBManager dbmanager, ServerLogger logger) {
		super(dbmanager, logger);
		ui = new UserManagerUI(this);
		newitem = new NewUser(ui.getOwnerFrame(), this);
	}
	
	/**
	 * �����, ����������� �������� ����� �� ������������. ��������� ��� �������� 
	 * ����������� ������ ��� ������ ������������. ����������� ��� ������: �������
	 * ������������ � �� � ��, ������� �������, �� ��� �� �������� � ��.
	 * @param login ����� ��� ��������.
	 * @return true - ���� ����� ����������.
	 */
	public boolean isUniqueLogin(String login){
		for(User u: itemlist)
			if(u.getLogin().equals(login))
				return false;
		return true;
	}
	
	/**
	 * �����, ����������� ������ ��������� �� �� ��������� ���������
	 * ������� �������, � ������� �� ��������� ������������.
	 * ���������� ����� ��������� ����� ������ ������������.
	 */
	public void requestFreePondId(){
		dbmanager.loadFreePondId(this);
	}
	
	/**
	 * �����, ���������� ������� �������� ������ ������������
	 * ��������� ������� �������, � ������� �� �������� ������������
	 * ��������� ���������� ��� �������� ������������ �.�. ��� ��������
	 * �� ����� ������ ���� �������� � ������. ������ ����� ��������
	 * �������� �� �� ������ ���������� �������. �������� �������������
	 * ������� ������, �� ������� ��� ������� ������, �����������
	 * � ���������� ������, �� ��� �� ����������� � ��, ����� ��������
	 * ���������� ��������� ������� �������� ������.
	 * @param freeidset ��������� ������� �������.
	 */
	public void setFreePondId(Set<Integer> freeidset){
		for(User u: itemlist)
			if(u.getFlag()==Flag.ADDED)
				freeidset.remove(u.getValue(4));
		((NewUser) newitem).setFreePondId(freeidset);
	}

	@Override
	public int getColumnCount() {
		return User.getColumnCount();
	}

	@Override
	public String getHeader(int columnIndex) {
		return User.getHeader(columnIndex);
	}

	@Override
	public boolean isEditable(int columnIndex) {
		return User.isEditable(columnIndex);
	}

}