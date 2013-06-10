package ua.vladaxon.basicitem;

/**
 * ��������� �������� �������� ����� �������.
 * ������������� ������ ��� ������� ������� � ��������� ����������.
 * @param <T> ������, � ������� �������� ���������� ����������.
 */
public interface NewItemDialog<T extends BasicItem> {
	
	/**
	 * �����, ���������� ����������� ����������� ����.
	 * @return true - ���� ������ ���������� �������.
	 */
	public boolean showDialog();
	
	/**
	 * ���������� ��������� ������ ������.
	 * @return ��������� ������.
	 */
	public T getItem();

}