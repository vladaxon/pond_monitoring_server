package ua.vladaxon.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import ua.vladaxon.basicitem.BasicItem;
import ua.vladaxon.connections.TaskHandler;

/**
 * ������ ���������� �������� ������ ������ � ��.
 * ������������� ����� ���� ����� ������������ � ��������
 * ��� ����� �������� ����������� ������ � �������.
 */
public class Pond extends BasicItem{
	
	/**
	 * ����������� � ������� � ����������.
	 * @param id ����� ������
	 * @param name ��� ������
	 * @param area ������� ������
	 * @param settlement ��������� ���������� �����
	 * @param fishspot ���������� ���� ��� �������
	 * @param fishcost ��������� ������� � ���
	 * @param fishes ���� ���
	 * @param other ������ ����������
	 * @param flag ���� �������
	 */
	public Pond(int id, String name, int area, String settlement, int fishspot,
			int fishcost, String fishes, String other, Flag flag) {
		super();
		this.id = id;
		this.name = name;
		this.area = area;
		this.settlement = settlement;
		this.fishspot = fishspot;
		this.fishcost = fishcost;
		this.fishes = fishes;
		this.other = other;
		uname = "";
		tel = "";
		this.flag = flag;
	}

	/**
	 * ����������� ������, ������� ������ �� ������ ������� � ��.
	 * @param result ��������� �����������
	 * @throws SQLException ��� ������ �������
	 */
	public Pond(ResultSet result) throws SQLException{
		id = result.getInt("pid");
		name = result.getString("pname");
		area = result.getInt("parea");
		settlement = result.getString("settlmnt");
		fishspot = result.getInt("fishspot");
		fishcost = result.getInt("fishcost");
		fishes = result.getString("fishes");
		other = result.getString("other");
		uname = result.getString("uname");
		if(uname==null)
			uname="";
		tel = result.getString("tel");
		if(tel==null)
			tel="";
		this.flag = Flag.NORMAL;
	}
	
	/**
	 * ����������� ������� �� �������� ���������. ��������� ������ ��������� ��� ���������� ������
	 * � ��������. ��� ������ ������� ���� ��������� �����(���, ����� id) �������� �������� �� ���������.
	 * �������� ���� ���� ����� �������� null - ���� ������� ���������������� ��� "" ��� 0. ������
	 * ������������ ������ � ������ ������ �������������� ��������� ���� id.
	 * @param item ������� ���������.
	 * @throws Exception ��� ������ �������������� ��������� ����.
	 */
	public Pond(Element item) throws Exception{
		NodeList pondchilds = item.getChildNodes();
		id = Integer.parseInt(TaskHandler.getString((Element) pondchilds.item(0)));
		for (int i = 1; i < pondchilds.getLength(); i++) {
			String value = TaskHandler.getString((Element) pondchilds.item(i));
			try {
				modifyValue(i, value);
			} catch (NullPointerException e) {
			}
		}
		flag = Flag.MODIFIED;
	}
	
	/**
	 * ����� ��� �������� ������� � ������ ������.
	 * ����� ��� �������� ������������ ������ ��� �������� ����� ������.
	 * @return ���������� ����� ������.
	 */
	public int getId(){
		return id;
	}
	
	@Override
	public boolean modifyValue(int field, Object value) {
		boolean modified = false;
		String text = value.toString();
		switch (field) {
		case 1: {
			if (!name.equals(text)) {
				name = text;
				modified = true;
			}
			break;
		}
		case 2: {
			try {
				int area = Integer.parseInt(text);
				if (this.area != area) {
					this.area = area;
					modified = true;
				}
				break;
			} catch (NumberFormatException e) {
				break;
			}
		}
		case 3: {
			if (!settlement.equals(text)) {
				settlement = text;
				modified = true;
			}
			break;
		}
		case 4: {
			int fishspot = Integer.parseInt(text);
			if (this.fishspot != fishspot) {
				this.fishspot = fishspot;
				modified = true;
			}
			break;
		}
		case 5: {
			int fishcost = Integer.parseInt(text);
			if (this.fishcost != fishcost) {
				this.fishcost = fishcost;
				modified = true;
			}
			break;
		}
		case 6: {
			if (!fishes.equals(text)) {
				fishes = text;
				modified = true;
			}
			break;
		}
		case 7: {
			if (!other.equals(text)) {
				other = text;
				modified = true;
			}
			break;
		}
		}
		if (modified && flag != Flag.ADDED)
			flag = Flag.MODIFIED;
		return modified;
	}

	@Override
	public Object getValue(int field) {
		switch(field){
		case 0:
			return id;
		case 1:
			return name;
		case 2:
			return area;
		case 3:
			return settlement;
		case 4:
			return fishspot;
		case 5:
			return fishcost;
		case 6:
			return fishes;
		case 7:
			return other;
		case 8:
			return uname;
		case 9:
			return tel;
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
			return String.format
					(addquery, id, name, area, settlement, fishspot, fishcost, fishes, other);
		case MODIFIED:
			return String.format
					(updatequery, name, area, settlement, fishspot, fishcost, fishes, other, id);
		default:
			return String.format(deletequery, id);
		}
	}
	
	/**
	 * ����� ���������� �������� ��������� �� ������ �������.
	 * ������������� ������ � ������� �������� ��� ���������� ��������.
	 * @param doc ������ ���������, ����� ��� �������� ���������.
	 * @return �������, ���������� ������ �������.
	 */
	public Element getElement(Document doc){
		Element pond = doc.createElement("pond");
		for(int i=0; i<elementheaders.length; i++){
			Element field = doc.createElement(elementheaders[i]);
			Text textnode = doc.createTextNode(getValue(i).toString());
			field.appendChild(textnode);
			pond.appendChild(field);
		}
		return pond;
	}
	
	/**
	 * �����, ������������ ��������� ������� ��� ����.
	 * @param column ����� �������(����)
	 * @return ��������� ��������� ��� �������.
	 */
	public static String getHeader(int column) {
		return tableheaders[column];
	}

	/**
	 * �����, ������������ ���������� ������� � �������.
	 * ���������� ������� �� ���������� ����������.
	 * @return ���������� ��������.
	 */
	public static int getColumnCount() {
		return tableheaders.length;
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
		return "Pond [id=" + id + ", name=" + name + ", area=" + area
				+ ", settlement=" + settlement + ", fishspot=" + fishspot
				+ ", fishcost=" + fishcost + ", fishes=" + fishes + ", other="
				+ other + ", uname=" + uname + ", tel=" + tel + "]";
	}

	/**���������� �����*/
	private int id = 0;
	/**��� ������*/
	private String name = "";
	/**������� ������ � ������*/
	private int area = 0;
	/**�������� ���������� ����������� ������*/
	private String settlement = "";
	/**���������� ���� ��� �������*/
	private int fishspot = 0;
	/**��������� ������� � ���*/
	private int fishcost = 0;
	/**���� ��� � ������*/
	private String fishes = "";
	/**������ ����������*/
	private String other = "";
	/**����������� ������������*/
	private String uname = null;
	/**������� ������������*/
	private String tel = null;
	/**������ ���������� �������*/
	private static final String[] tableheaders = 
		{"ID","���","�������","���. �����","���� �������",
		"���������/���","���� ���","������","��������","�������"};
	/**������ ���������� ���������*/
	private static final String[] elementheaders = 
		{"pid","pname","parea","psettl","fishspot","fishcost","fishes","other","uname","utel"};
	/**������ ������ ��������������*/
	private static final boolean[] editable = {false,true,true,true,true,true,true,true,false,false};
	/**������ �� ���������� ����� ������ ������ � ��*/
	private static final String addquery = 
			"INSERT INTO ponds VALUES (%d,'%s',%d,'%s',%d,%d,'%s','%s')";
	/**������ �� ��������� ������ ������ � ��*/
	private static final String updatequery = "UPDATE ponds SET pname='%s', parea=%d, settlmnt='%s'," +
			" fishspot=%d, fishcost=%s, fishes='%s', other='%s' WHERE pid=%d";
	/**������ �� �������� ������ ������ �� ��*/
	private static final String deletequery = "DELETE FROM ponds WHERE pid=%d";

}