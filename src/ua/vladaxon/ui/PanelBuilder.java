package ua.vladaxon.ui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

public abstract class PanelBuilder {
	
	/**
	 * �����, �������� ���� � �������������� ����������� ���������.
	 * ����� ���� ��������� ��� ���������� ������� ������ � ������ ������
	 * ������� ���������� ���� �������� � ��������� �����.
	 * @param components ������ ��������� ��� ����������.
	 * @return ���� � �������������� ����������� ���������.
	 */
	public static Box buildHorizontalBox(JComponent... components){
		Box linebox = new Box(BoxLayout.X_AXIS);
		for(int i=0; i<components.length; i++){
			linebox.add(Box.createHorizontalGlue());
			linebox.add(Box.createHorizontalStrut(strutsize));
			linebox.add(components[i]);			
		}
		linebox.add(Box.createHorizontalGlue());
		linebox.add(Box.createHorizontalStrut(strutsize));
		linebox.setAlignmentX(Component.LEFT_ALIGNMENT);
		return linebox;
	}
	
	/**
	 * �����, �������� ���� � ������������ ����������� ���������.
	 * ����� ���� ��������� ��� ���������� ������ ����� � ���������� �����
	 * ������� ���������� ���� �������� � ��������� �����.
	 * @param components ������ ��������� ��� ����������.
	 * @return ���� � ������������ ����������� ���������.
	 */
	public static Box buildVerticalBox(int strutsize, JComponent... components){
		Box linebox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<components.length; i++){
			linebox.add(Box.createVerticalStrut(strutsize));
			components[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			linebox.add(components[i]);			
		}
		linebox.add(Box.createVerticalStrut(strutsize));
		linebox.setAlignmentX(Component.LEFT_ALIGNMENT);
		return linebox;
	}
		
	/**������ �������� ��������� ����������*/
	public static final int strutsize = 7;

}