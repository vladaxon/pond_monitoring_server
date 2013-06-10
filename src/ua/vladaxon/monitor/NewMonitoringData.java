package ua.vladaxon.monitor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ua.vladaxon.CorrectListener;
import ua.vladaxon.actions.PondManagerAction;
import ua.vladaxon.basicitem.NewItemDialog;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.database.Monitor;
import ua.vladaxon.ponds.NumberInputListener;
import ua.vladaxon.ui.ColoredJTextField;
import ua.vladaxon.ui.PanelBuilder;
import ua.vladaxon.ui.ServerUI;

/**
 * ������ �������� ����� ������ �����������.
 */
public class NewMonitoringData extends JDialog implements NewItemDialog<Monitor>{
	
	public NewMonitoringData(JFrame owner, MonitorManager manager){
		super(owner,true);
		this.owner = owner;
		this.manager = manager;
		JLabel pondbnuml = new JLabel(Monitor.getHeader(0));
		JLabel mondatel = new JLabel(Monitor.getHeader(1));
		JLabel montempl = new JLabel(Monitor.getHeader(2));
		JLabel monlevl = new JLabel(Monitor.getHeader(3));
		Box labelbox = PanelBuilder.buildVerticalBox(11, pondbnuml, mondatel, montempl, monlevl);
		day = new JComboBox<Integer>();
		month = new JComboBox<String>();
		year = new JComboBox<Integer>();
		datelistener = new DateFormatListener(day, month, year);
		Box datebox = Box.createHorizontalBox();
		datebox.add(day);
		datebox.add(Box.createHorizontalStrut(5));
		datebox.add(month);
		datebox.add(Box.createHorizontalStrut(5));
		datebox.add(year);
		pondnum = new JComboBox<Integer>();
		Dimension combodim = new Dimension(Integer.MAX_VALUE, day.getPreferredSize().height-3);
		day.setMaximumSize(combodim);
		month.setMaximumSize(combodim);
		year.setMaximumSize(combodim);
		pondnum.setMaximumSize(combodim);
		pondnum.setPreferredSize(day.getMaximumSize());
		wtemp = new ColoredJTextField();
		monlevel = new ColoredJTextField();
		NumberInputListener nonneg = new NumberInputListener(monlevel, false, false);
		NumberInputListener tempinput = new NumberInputListener(wtemp, false, true);
		Dimension fielddim = new Dimension(Integer.MAX_VALUE, monlevl.getPreferredSize().height);
		wtemp.setMaximumSize(fielddim);
		monlevel.setMaximumSize(fielddim);
		Box textbox = PanelBuilder.buildVerticalBox(6, pondnum,datebox,wtemp,monlevel);
		JButton okbtn = new JButton(ServerUI.oklabel, ServerUI.okicon);
		JButton cancelbtn = new JButton(ServerUI.cancellabel, ServerUI.cancelicon);
		totallistener = new CorrectListener(okbtn, nonneg, tempinput);
		Box buttonbox = PanelBuilder.buildHorizontalBox(okbtn, cancelbtn);
		Box upperbox = PanelBuilder.buildHorizontalBox(labelbox, textbox);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(upperbox);
		mainpanel.add(buttonbox);
		mainpanel.add(Box.createVerticalStrut(PanelBuilder.strutsize));
		mainpanel.setPreferredSize(new Dimension(320, mainpanel.getPreferredSize().height));
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createdmonitor = new Monitor
				((Integer)pondnum.getSelectedItem(), datelistener.getDate(), 
						Integer.parseInt(wtemp.getText()), Integer.parseInt(monlevel.getText()), Flag.ADDED);
				NewMonitoringData.this.result = true;
				NewMonitoringData.this.setVisible(false);
			}
		});
		cancelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewMonitoringData.this.setVisible(false);	
			}
		});
		setTitle(titlename);
		setIconImage(PondManagerAction.pondicon.getImage());
		setContentPane(mainpanel);
		pack();
		setResizable(false);
	}

	@Override
	public boolean showDialog() {
		result = false;
		monlevel.setText("");
		wtemp.setText("");
		createdmonitor = null;
		setLocationRelativeTo(owner);
		setLoadStatus();
		totallistener.checkCorrectness();
		setVisible(true);
		return result;
	}
	
	@Override
	public Monitor getItem() {
		return createdmonitor;
	}
	
	/**
	 * ������������� ������ � ��������� �������� ������.
	 * ��� �������� ����� ������ ����� ���������
	 * ������ ������� �������.
	 */
	private void setLoadStatus(){
		manager.requestPondIds();
		pondnum.setEnabled(false);
		pondnum.removeAllItems();
		day.setEnabled(false);
		month.setEnabled(false);
		year.setEnabled(false);
		wtemp.setEnabled(false);
		monlevel.setEnabled(false);
		setTitle(titlename + loadtitlename);
	}
	
	/**
	 * �����, ���������� ��������� ������� �������.
	 * ���� ��������� ������ - ��������� ��������� � �������������
	 * �������� ����� ������. ���� ��������� �� ������ - ����������
	 * ���������� ��� ����� ������.
	 * @param pondid ��������� ������� �������
	 */
	public void setAllPondId(final Set<Integer> pondid){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				setTitle(titlename);
				if(pondid.isEmpty()){
					JOptionPane.showMessageDialog(owner, nopond);
					setVisible(false);
				} else {
					day.setEnabled(true);
					month.setEnabled(true);
					year.setEnabled(true);
					wtemp.setEnabled(true);
					monlevel.setEnabled(true);
					for(Integer i: pondid)
						pondnum.addItem(i);
					pondnum.setEnabled(true);
				}
			}
		});
	}
	
	/**����� �������� ��������� ����*/
	private JFrame owner = null;
	/**�������� ������� �����������*/
	private MonitorManager manager = null;
	/**������ ��� ������ ������ ������*/
	private JComboBox<Integer> pondnum = null;
	/**������ ��� ������ ���*/
	private JComboBox<Integer> day = null;
	/**������ ��� ������ ������*/
	private JComboBox<String> month = null;
	/**������ ��� ������ ����*/
	private JComboBox<Integer> year = null;
	/**���� ��� ����� �����������*/
	private ColoredJTextField wtemp = null;
	/**���� ��� ����� ������ ����*/
	private ColoredJTextField monlevel = null;
	/**������ ��������� ������*/
	private Monitor createdmonitor = null;
	/**��������� ����� ����*/
	private DateFormatListener datelistener = null;
	/**��������� �����*/
	private CorrectListener totallistener = null;
	/**���� ���������� ���������� �������*/
	private boolean result = false;
	/**��������� ����*/
	private static final String titlename = "����� ������";
	/**��������� ���� ��� �������� �������*/
	private static final String loadtitlename = " (�������� ������...)";
	/**���������, ��������� ��� ������������� �������� ����� ������*/
	private static final String nopond = "���������� ������� ����� ������!";
	private static final long serialVersionUID = 1L;

}