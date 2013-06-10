package ua.vladaxon.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ua.vladaxon.basicitem.BasicItem;

/**
 * �����, ������ �������� �������� �������� ������������.
 * ����� ���� ������ � ������ ��� ����� � �������.
 * ����� ���� ����� � �������� ������� ����� ��������.
 * ������ ������������ ������ ���� �������� � ������, ����
 * ������ ����� ����������� ����, ������������ ��������� ����� �������.
 * ���� ��������� ��� ��������� �� ����� ���������������� ���������� ������ � ��.
 */
public class User extends BasicItem{
	
	/**
	 * ����������� ������� ������������ �� ��������� �����.
	 * @param login ����� ������������
	 * @param password �������������� ������
	 * @param uname ��� ������������
	 * @param telephone ������� ������������
	 * @param pond ����� ������, � �������� �������� ������������
	 * @param flag ���� �������
	 */
	public User(String login, int password, String uname, String telephone, int pond, Flag flag) {
		this.login = login;
		this.password = password;
		this.uname = uname;
		this.telephone = telephone;
		this.pond = pond;
		this.flag = flag;
	}
	
	/**
	 * ����������� ������� ������������ �� ������ ��.
	 * ����������� ������� ��������������� ��� ���� � ����������� �� �������.
	 * ���� ������� ��������������� ��� ����������.
	 * @param result ������ ��
	 * @throws SQLException ���� ��������� ���������� ��� ��������� ������
	 */
	public User(ResultSet result) throws SQLException{
		login = result.getString("login");
		password = result.getInt("passw");
		uname = result.getString("uname");
		telephone = result.getString("tel");
		pond = result.getInt("pond");
		flag = Flag.NORMAL;
	}
	
	@Override
	public boolean modifyValue(int field, Object value) {
		if(field==3){
			String newvalue = value.toString();
			if(!newvalue.equals(telephone)){
				telephone = value.toString();
				if(flag!=Flag.ADDED)
					flag = Flag.MODIFIED;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Object getValue(int field) {
		switch(field){
		case 0:
			return login;
		case 1:
			return password;
		case 2:
			return uname;
		case 3:
			return telephone;
		case 4:
			return pond;
		default:
			return null;
		}
	}

	@Override
	public String getQuery() {
		switch (flag) {
		case NORMAL:
			return null;
		case ADDED:
			return String.format(addquery, login, password, uname, telephone, pond);
		case MODIFIED:
			return String.format(updatequery, telephone, login);
		default:
			return String.format(deletequery, login);
		}
	}
	
	/**
	 * ���������� ����� ������������.
	 * ����� ��������� ��� ������� �������� ������������ ������.
	 * @return ����� ������������.
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * �����, ������������ ��������� ������� ��� ����.
	 * @param column ����� �������(����)
	 * @return ��������� ��������� ��� �������.
	 */
	public static String getHeader(int column) {
		return headers[column];
	}
	
	/**
	 * �����, ������������ ���������� ������� � �������.
	 * ���������� ������� �� ���������� ����������.
	 * @return ���������� ��������.
	 */
	public static int getColumnCount() {
		return headers.length;
	}
	
	/**
	 * �����, ������������ ���� ����������� �������������� ������� ����.
	 * @param column ����� �������.
	 * @return true - ���� ��������� ��������������.
	 */
	public static boolean isEditable(int column){
		return editable[column];
	}

	@Override
	public String toString() {
		return "User [login=" + login + ", password=" + password + ", uname="
				+ uname + ", telephone=" + telephone + ", pond=" + pond + "]";
	}
	
	/**����� ������������*/
	private String login = null;
	/**������ � ������������� ����*/
	private int password = -1;
	/**��� ������������*/
	private String uname = null;
	/**������� ������������*/
	private String telephone = null;
	/**����� ������, � �������� �������� ������������*/
	private int pond = -1;
	/**������ ���������� ��� �������*/
	private static final String[] headers = {"�����","������","���","�������","������"};
	/**������ ������ �������������� �����*/
	private static final boolean[] editable = {false,false,false,true,false};
	/**������ ������� �� ���������� ������*/
	private static final String addquery = "INSERT INTO users VALUES ('%s',%d,'%s','%s',%d)";
	/**������ ������� �� ��������� ������*/
	private static final String updatequery = "UPDATE users SET tel='%s' WHERE login='%s'";
	/**������ ������� �� �������� ������*/
	private static final String deletequery = "DELETE FROM users WHERE login='%s'";

}