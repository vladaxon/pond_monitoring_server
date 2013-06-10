package ua.vladaxon.monitor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;

/**
 * ������ ���������, �������� �� ���������� ������� �����������
 * ����� ���� ��� �������� ����� ������ �����������. ���������
 * ���������� ���� � ������ ��� ��������� ������ ��� ����.
 * ����� ����� ����� ����������� ��������� ���� � ���� �������.
 */
public class DateFormatListener implements ItemListener {
	
	public DateFormatListener(JComboBox<Integer> days, JComboBox<String> month, JComboBox<Integer> year){
		this.days = days;
		this.month = month;
		this.year = year;
		month.addItemListener(this);
		year.addItemListener(this);
		fillComboBoxes();
	}
	
	/**
	 * �����, ����������� ����� ����.
	 */
	private void fillComboBoxes(){
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		days.removeAllItems();
		for(int i=1980; i<2100; i++)
			this.year.addItem(i);
		this.month.removeAllItems();
		for(String m: monthlist)
			this.month.addItem(m);
		calendar.set(year, month, day);
		int daysinmonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i=1; i<=daysinmonth; i++)
			this.days.addItem(i);
		this.days.setSelectedIndex(day);
		this.month.setSelectedIndex(month);
		this.year.setSelectedIndex(year-1980);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		int selectedyear = Integer.parseInt(year.getSelectedItem().toString());
		int selectedmonth = month.getSelectedIndex();
		calendar.set(selectedyear, selectedmonth, 1);
		int daycount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (days.getItemCount() != daycount) {
			days.removeAllItems();
			for (int i = 1; i <= daycount; i++)
				days.addItem(i);
		}
	}
	
	/**
	 * ���������� ������ ��������� ����.
	 * ������ ����������� ������ sql � �� util
	 * @return ������ ����.
	 */
	public Date getDate(){
		int selectedyear = Integer.parseInt(year.getSelectedItem().toString());
		int selectedmonth = month.getSelectedIndex();
		int selectedday = days.getSelectedIndex()+1;
		calendar.set(selectedyear, selectedmonth, selectedday);
		return new Date(calendar.getTimeInMillis());
	}
	
	/**���� ����� ��� ������*/
	private JComboBox<Integer> days = null;
	/**���� ����� ������*/
	private JComboBox<String> month = null;
	/**���� ����� ����*/
	private JComboBox<Integer> year = null;
	/**������ ���������, ��������� ��� ������ � ������*/
	private Calendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
	/**������ ���� �������*/
	private String[] monthlist = {"������","�������","����","������","���","����",
			"����","������","��������","�������","������","�������"};
	
}