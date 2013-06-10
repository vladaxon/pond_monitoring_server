package ua.vladaxon.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ua.vladaxon.basicitem.BasicItem;
import ua.vladaxon.connections.TaskHandler;

/**
 * ������ ����������� ������. ����� ���� ������ ������, ���� ���������, ����������� ������,
 * ������� ����. ���� ������ ���������� �������������.
 */
public class Monitor extends BasicItem{
	
	/**
	 * ����������� �� ���������.
	 * @param pnum ����� ������
	 * @param date ���� ������
	 * @param temp ����������� ����
	 * @param level ������� ����
	 * @param flag ��������� �������
	 */
	public Monitor(int pnum, Date date, int temp, int level, Flag flag) {
		this.pnum = pnum;
		this.date = date;
		this.temp = temp;
		this.level = level;
		this.flag = flag;
	}
	
	/**
	 * ����������� ������� �� ��. ������� ������ �� ������ ������ �� ��.
	 * @param result ������ �� ��
	 * @throws SQLException ��� ������ ��������������
	 */
	public Monitor(ResultSet result) throws SQLException{
		this.pnum = result.getInt(mntrpondincolname);
		this.date = result.getDate(mntrdatecolname);
		this.temp = result.getInt(mntrtempcolname);
		this.level = result.getInt(mntrlevelcolname);
		this.mnum = result.getInt(monitornum);
		this.flag = Flag.NORMAL;
	}
	
	/**
	 * ����������� �� �������� ���������.
	 * @param item ������� ���������, ������������ ���� ������.
	 * @throws Exception - ��� ������ ��������������.
	 */
	public Monitor(Element item) throws Exception{
		NodeList monitorchilds = item.getChildNodes();
		pnum = Integer.parseInt(TaskHandler.getString((Element) monitorchilds.item(0)));
		date = new Date(Long.parseLong(TaskHandler.getString((Element) monitorchilds.item(1))));
		temp = Integer.parseInt(TaskHandler.getString((Element) monitorchilds.item(2)));
		level = Integer.parseInt(TaskHandler.getString((Element) monitorchilds.item(3)));
		flag = Flag.ADDED;
	}
	
	public Element getElement(Document doc){
		Element monitor = doc.createElement("monitor");
		Element pnumel = formField(0, pnum+"", doc);
		monitor.appendChild(pnumel);
		Element dateel = formField(1, date.getTime()+"", doc);
		monitor.appendChild(dateel);
		Element tempel = formField(2, temp+"", doc);
		monitor.appendChild(tempel);
		Element levelel = formField(3, level+"", doc);
		monitor.appendChild(levelel);
		return monitor;
	}
	
	/**
	 * ��������� ������� �� ��������� ������.
	 * @param fieldnum ����� ���� ��� ��������� ����� ����
	 * @param data ������
	 * @param doc �������� ��� �������� ��������
	 * @return ������� ���������
	 */
	private Element formField(int fieldnum, String data, Document doc){
		Element field = doc.createElement(elementheaders[fieldnum]);
		Text textnode = doc.createTextNode(data);
		field.appendChild(textnode);
		return field;
	}
	
	@Override
	public boolean modifyValue(int field, Object value) {
		return false;
	}

	@Override
	public Object getValue(int field) {
		switch(field){
		case 0:
			return pnum;
		case 1:
			return date;
		case 2:
			return temp;
		case 3:
			return level;
		default:
			return null;
		}
	}
	
	@Override
	public String getQuery() {
		switch (flag) {
		case ADDED:
			return String.format(addquery, pnum, date, temp, level);
		case DELETED:
			return String.format(deletequery, mnum);
		default:
			return null;
		}
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
	
	@Override
	public String toString() {
		return "Monitor [pnum=" + pnum + ", date=" + date + ", temp=" + temp
				+ ", level=" + level + ", flag=" + flag + "]";
	}
	
	/**����� ������*/
	private int pnum = 0;
	/**���� ���������*/
	private Date date = null;
	/**����������� ����*/
	private int temp = 0;
	/**������� ����*/
	private int level = 0;
	/**����� ������*/
	private int mnum = 0;
	/**������ ���������� ��� �������*/
	private static final String[] headers = {"������","����","t ����","�������"};
	/**������ ���������� ���������*/
	private static final String[] elementheaders = {"pid","mdate","mtemp","mlevel"};
	/**��� ������� �����������*/
	public static final String monitortablename = "monitor";
	/**��� ���� ������ ������ � �������� ��������� ������ ���������*/
	public static final String mntrpondincolname = "pnum";
	/**��� ���� ���� ����� ���������*/
	public static final String mntrdatecolname = "mdate";
	/**��� ���� ����������� ���� � ������*/
	public static final String mntrtempcolname = "mtemp";
	/**��� ���� ������ ���� ������*/
	public static final String mntrlevelcolname = "wlevel";
	/**��� ���� ������ ������*/
	public static final String monitornum = "mnum";
	/**������ ������� �� ���������� ������ ����������� � ��*/
	private static final String addquery = "INSERT INTO monitor VALUES (%d,'%s',%d,%d," +
			"(SELECT COALESCE(MAX(mnum),0) FROM monitor)+1);";
	/**������ ������� �� �������� ������ ����������� �� ��*/
	private static final String deletequery = "DELETE FROM monitor WHERE mnum=%d";
	
}