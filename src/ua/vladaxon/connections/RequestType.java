package ua.vladaxon.connections;

/**
 * ������������ ����� ��������. ������������ ��� ������� � ����������
 * �������� �� �������.
 */
public enum RequestType {
	
	POND,MONITOR,UPDATE,AUTHORIZATION,WRONG,NONE;
	
	/**
	 * ���������� ������� �� ����� ��� WRONG ���� ��� ������ �������.
	 * @param name ��� ������� ��� ������.
	 * @return ������, ��������������� ����� ��� WRONG � ��������� ������.
	 */
	public static RequestType getByName(String name){
		try{
			return valueOf(name);
		} catch (IllegalArgumentException e) {
			return WRONG;
		}
	}
	
}