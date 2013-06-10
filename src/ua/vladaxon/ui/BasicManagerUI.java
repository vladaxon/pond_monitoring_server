package ua.vladaxon.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import ua.vladaxon.Server;
import ua.vladaxon.basicitem.BasicManager;
import ua.vladaxon.basicitem.ItemTableModel;

/**
 * ����������� �����, ���������� ����� ������ � ������, �����������
 * ��� ���� ���������� �������.
 */
public abstract class BasicManagerUI {

	/**
	 * ������� ������ ���� � ��������� ��������� �� ��������(��� ������ �������������).
	 * ��������� �������, ������ � ���, � �������� ��������.
	 * @param frametitle ��������� ����.
	 * @param manager �������� �������.
	 */
	public BasicManagerUI(String frametitle, BasicManager<?> manager){
		mainframe = new JFrame(frametitle);
		setWindowListener(manager);
		ItemTableModel tablemodel = new ItemTableModel((BasicManager<?>) manager);
		table = new JTable(tablemodel);
		table.setDefaultRenderer(Object.class, new ColoredTableCellRenderer(manager));
	}

	/**
	 * �����, ����������� ������� � �������.
	 * ������ ����������� � ������� ������� � �����������
	 * �� ���� ������.
	 */
	public void refreshTable(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				table.clearSelection();
				table.revalidate();
				table.repaint();
			}
		});
	}
	
	/**
	 * �����, ������������ ���� ����������������� ����������.
	 */
	public void show(){
		mainframe.pack();
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
	
	/**
	 * �����, ������������ ���� ������������� ������.
	 * ���� ������������ � ��� ������, ���� ������, � �������� �������� ��������,
	 * ���� ��������.
	 * @return ����� �� ���������� ����.
	 */
	public int trySave(){
		return JOptionPane.showConfirmDialog(mainframe, confirmsave);
	}
	
	/**
	 * �����, ������������ ���� � ���������� � �������� �������� ������� �� ��.
	 */
	public void loadProblem(){
		JOptionPane.showMessageDialog(mainframe, loadproblem);
	}
	
	/**
	 * �����, ����������� ���� ���������.
	 * ������ ���� ���������.
	 */
	public void closeUI(){
		mainframe.setVisible(false);
	}
	
	/**
	 * �����, ������������ ������ ������, �����������
	 * ��� �������� ����������� ���� �������� ����� ������.
	 * @return ������ ������.
	 */
	public JFrame getOwnerFrame(){
		return mainframe;
	}
	
	/**
	 * �����, ��������� ������� ����������� ������� �������.
	 * ������ ���� ������� �� ��������� �������, ������� ����� �������������.
	 * ����� ������������� ��� ���������� ���� � ������� �����������.
	 * @param manager �������� �������, ��� �������� ����� ��������� �������.
	 * @return ������ ������� ����������� ������� �������.
	 * @see ColumnsSubmenuListener
	 */
	protected JMenu columnSubmenu(BasicManager<?> manager){
		JMenu columnsmenu = new JMenu("�������");
		columnsmenu.setIcon(BasicManagerUI.colmicon);
		columnsmenu.setFont(menufont);
		ActionListener columnlistener = new ColumnsSubmenuListener(table);
		for(int i=0; i<manager.getColumnCount(); i++){
			JCheckBoxMenuItem colmenuitem = new JCheckBoxMenuItem(manager.getHeader(i));
			colmenuitem.setFont(menufont);
			colmenuitem.setSelected(true);
			colmenuitem.setActionCommand(i +"");
			colmenuitem.addActionListener(columnlistener);
			columnsmenu.add(colmenuitem);
		}
		return columnsmenu;
	}
	
	/**
	 * �����, ��������������� ������������ ������ ������� �� ������ ���������.
	 * ����������� ����� ����� ��������� ���� ������ ����� ������ � �������.
	 * @param column �������, ������� ������ ����� ������������� ������
	 * @param gap �������������� ������
	 */
	protected void setFixedWidthByHeader(TableColumn column, int gap){
		String header = column.getHeaderValue().toString();
		FontRenderContext fontrender = new FontRenderContext(new AffineTransform(), true, false);
		Rectangle2D rect = table.getFont().getStringBounds(header, fontrender);
		int size = (int) (gap + Math.round(rect.getWidth()));
		column.setMaxWidth(size);
		column.setPreferredWidth(size);
	}
	
	/**
	 * �����, ��������� ��������� ���� ��� ����������� ������������� ������
	 * ��� ������� ������ �������� ����. ���� ������ ���� �������� - ��� ������
	 * �������� ���� ������������� ���������� ������ ��� ������������.
	 * @param manager �������� �������.
	 */
	protected void setWindowListener(final BasicManager<?> manager){
		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				manager.closeManager();
			}
		};
		mainframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainframe.addWindowListener(adapter);
	}
	
	/**������ �������*/
	protected JTable table = null;
	/**���� ���������*/
	protected JFrame mainframe = null;	
	/**��������� ��������� ��� ������������� ���������� �������*/
	private static final String confirmsave = "��������� ���������� ������?";
	/**���������, ��������� ��� ������ �������� �������*/
	private static final String loadproblem = "�� ������� ��������� ������ �� ��";
	/**����� ���� ����������, ���� ������ ������ ��� ���� �������*/
	protected static final String controlmenu = "����������";
	/**����� ���� ���, ���� ������ ������ ��� ���� �������*/
	protected static final String viewmenu = "���";
	/**������ ������*/
	protected static final ImageIcon exiticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/cancel.png"));
	/**������ ����������*/
	protected static final ImageIcon updticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/refresh.png"));
	/**������ ��������*/
	protected static final ImageIcon delticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/delete.png"));
	/**������ �������*/
	protected static final ImageIcon colmicon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/columns.png"));
	/**����� ������� ����*/
	protected static final Font menufont = new Font("Arial", Font.PLAIN, 13);

}