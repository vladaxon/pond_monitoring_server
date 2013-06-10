package ua.vladaxon.basicitem;

import ua.vladaxon.database.Monitor;
import ua.vladaxon.database.Pond;
import ua.vladaxon.database.User;

/**
 * �����������, ������� �����, ���������� � ���� ���� ������, ����������� ������ ��� ���������������
 * � ������ ������, ������ ��� ������ �������.
 * @see User
 * @see Pond
 * @see Monitor
 */
public abstract class BasicItem {
	
	/**
	 * ���������� �������� ���������� ����. ���������� ��������� ���� �����
	 * � ����������� �� ������ � �������� ��� ��������.
	 * @param field ����� ����.
	 * @return �������� ����.
	 */
	public abstract Object getValue(int field);
	
	/**
	 * ����� ��������� ���� �������. � ����������� �� ���� �������� ����� ��������� ��� ���.
	 * ���������� ��������� ���� ����� � ����������� �� ������, � �������� ��� ��������.
	 * @param field ����� ����.
	 * @param value ����� ��������.
	 * @return true - ���� ������ ���� ��������. false - ���� ����� �������� ��������� �� ������.
	 */
	public abstract boolean modifyValue(int field, Object value);
	
	/**
	 * �����, ����������� ������ ��� ��, � ����������� �� ��������� �������.
	 * ������ ��������� ��� ����������, ���������, �������� ������ �� ��.
	 * ������ ����������� � ������������ � ������ �������.
	 * @return ������ ��� ��.
	 */
	public abstract String getQuery();
	

	/**
	 * ���������� ���� ������.
	 * @return ���� ������.
	 */
	public Flag getFlag() {
		return flag;
	}

	/**
	 * ������������� ���� ������.
	 * @param flag ���� ������.
	 */
	public void setFlag(Flag flag) {
		this.flag = flag;
	}
	
	/**���� ������*/
	protected Flag flag = Flag.NORMAL;
	/**��������� ������*/
	public static enum Flag {NORMAL, ADDED, MODIFIED, DELETED};

}