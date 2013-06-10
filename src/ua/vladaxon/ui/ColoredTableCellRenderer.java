package ua.vladaxon.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.BasicManager;

/**
 * �����, ��������� �������. �� ������������ ��������� ���������� ������������
 * ��������� ���� ������ � ����������� �� ��������� ������.
 */
public class ColoredTableCellRenderer extends JTextField implements TableCellRenderer {

	/**
	 * ����������� ���������.
	 * @param usermanager �������� �������, ��������� ��� ����������� ����� ������.
	 */
	public ColoredTableCellRenderer(BasicManager<?> basicmanager){
		this.basicmanager = basicmanager;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value!=null) {
			this.setBackground(this.getUserFlag(row, isSelected));
			this.setText(value.toString());
			this.setBorder(null);
		}
		return this;
	}
	
	/**
	 * �����, ������������ ������� ���� ������.
	 * ���� ������� �� ��������� ������(��������, �������������, ������, ����������) �
	 * ������� �� ������ ������ � �������.
	 * @param number ����� ������.
	 * @param isSelected ������� �� ������(������) � �������.
	 * @return ���� ���� ��� ������.
	 */
	private Color getUserFlag(int number, boolean isSelected){
		assert(basicmanager!=null);
		Flag flag = basicmanager.getFlag(number);
		Color colorflag = null;
		switch (flag) {
		case ADDED:{
			colorflag = added;
			break;
		}
		case DELETED:{
			colorflag = deleted;
			break;
		}
		case MODIFIED:{
			colorflag = modified;
			break;
		}
		default:{
			colorflag = normal;
			break;
		}
		}
		if(isSelected)
			return this.mixColors(colorflag, selected);
		return colorflag;
	}
	
	/**
	 * �����, ����������� 2 �����. ��������� ���� ������ � ������ ����.
	 * ����� ������� ���� ��� ��������� ����� �������� ��������� ������(���������, �������������� � �.�.)
	 * @param first ������ ����.
	 * @param second ������ ����.
	 * @return ������� ���� �� ���� ���������.
	 */
	public Color mixColors(Color first, Color second){
		Color result = null;
		int r = (first.getRed() + second.getRed())/2;
		int g = (first.getGreen() + second.getGreen())/2;
		int b = (first.getBlue() + second.getBlue())/2;
		result = new Color(r, g, b);
		return result;
	}
	
	/**�������� �������*/
	private BasicManager<?> basicmanager = null;
	/**���� ���� ����������� ������*/
	public static final Color added = new Color(100, 255, 100);
	/**���� ���� ���������������� ������*/
	public static final Color modified = new Color(255, 255, 100);
	/**���� ���� ��������� ������*/
	public static final Color deleted = new Color(255, 100, 100);
	/**���� ���� ��������� ������*/
	public static final Color selected = new Color(180, 180, 255);
	/**���� ���� �� ���������*/
	public static final Color normal = Color.WHITE;
	private static final long serialVersionUID = 1L;

}