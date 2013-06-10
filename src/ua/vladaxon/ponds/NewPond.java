package ua.vladaxon.ponds;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ua.vladaxon.CorrectListener;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.NewItemDialog;
import ua.vladaxon.database.Pond;
import ua.vladaxon.ui.ColoredJTextField;
import ua.vladaxon.ui.PanelBuilder;
import ua.vladaxon.ui.ServerUI;

/**
 * ������ �������� ����� ������ ������.
 * ����������� �������� ���������, ��������� ����������� ��������� ������������.
 */
public class NewPond extends JDialog implements NewItemDialog<Pond> {

	public NewPond(JFrame owner, PondManager manager){
		super(owner,true);
		this.owner = owner;
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		//����� ��������� � ���������� �������
		JLabel[] labels = new JLabel[Pond.getColumnCount()-2];
		for(int i=0; i<Pond.getColumnCount()-2; i++)
			labels[i] = new JLabel(Pond.getHeader(i)+":");
		Box labelbox = PanelBuilder.buildVerticalBox(9, labels);
		//��������� ����
		uidf = new ColoredJTextField();
		pnamef = new JTextField();
		pareaf = new ColoredJTextField();
		settf = new JTextField();
		fishspotf = new ColoredJTextField();
		fishcostf = new ColoredJTextField();
		fishesf = new JTextField();
		otherf = new JTextField();
		Dimension fielddimension = new Dimension(Integer.MAX_VALUE, uidf.getPreferredSize().height);
		uidf.setMaximumSize(fielddimension);
		pnamef.setMaximumSize(fielddimension);
		pareaf.setMaximumSize(fielddimension);
		settf.setMaximumSize(fielddimension);
		fishspotf.setMaximumSize(fielddimension);
		fishcostf.setMaximumSize(fielddimension);
		fishesf.setMaximumSize(fielddimension);
		otherf.setMaximumSize(fielddimension);
		Box textfieldbox = PanelBuilder.buildVerticalBox
				(5, uidf,pnamef,pareaf,settf,fishspotf,fishcostf,fishesf,otherf);
		//������� ������
		Box upperbox = PanelBuilder.buildHorizontalBox(labelbox, textfieldbox);
		//������ � ��������� ������
		JButton okbtn = new JButton(ServerUI.oklabel, ServerUI.okicon);
		JButton cancelbtn = new JButton(ServerUI.cancellabel, ServerUI.cancelicon);
		Box buttonbox = PanelBuilder.buildHorizontalBox(okbtn, cancelbtn);
		//���������
		UniqueIDListener idlist = new UniqueIDListener(uidf, manager);
		NumberInputListener areainput = new NumberInputListener(pareaf, true, false);
		NumberInputListener fishspotinput = new NumberInputListener(fishspotf, true, false);
		NumberInputListener fishcostinput = new NumberInputListener(fishcostf, true, false);
		totallistener = new CorrectListener(okbtn, idlist, areainput, fishspotinput, fishcostinput);
		//������� ������
		mainpanel.add(upperbox);
		mainpanel.add(buttonbox);
		mainpanel.add(Box.createVerticalStrut(PanelBuilder.strutsize));
		mainpanel.setPreferredSize(new Dimension(300,mainpanel.getPreferredSize().height));
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int uid = Integer.parseInt(uidf.getText());
				String name = pnamef.getText();
				int parea = 0;
				if(!pareaf.getText().equals(""))
					parea = Integer.parseInt(pareaf.getText());
				String settlm = settf.getText();
				int fishspotn = 0;
				if(!fishspotf.getText().equals(""))
					fishspotn = Integer.parseInt(fishspotf.getText());
				int fishcostn = 0;
				if(!fishcostf.getText().equals(""))
					fishcostn = Integer.parseInt(fishcostf.getText());
				String fishesn = fishesf.getText();
				String othern = otherf.getText();
				NewPond.this.createdpond = new Pond
						(uid,name,parea,settlm,fishspotn,fishcostn,fishesn,othern,Flag.ADDED);
				NewPond.this.result = true;
				NewPond.this.setVisible(false);	
			}
		});
		cancelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewPond.this.setVisible(false);
			}
		});
		setTitle(titlename);
		setContentPane(mainpanel);
		pack();
		setResizable(false);
	}
	
	/**
	 * �����, ������������ ������.
	 * @return true - ���� ������ ���������� �������.
	 */
	public boolean showDialog(){
		result = false;
		uidf.setText("");
		pnamef.setText("");
		pareaf.setText("");
		settf.setText("");
		fishspotf.setText("");
		fishcostf.setText("");
		fishesf.setText("");
		otherf.setText("");
		createdpond = null;
		totallistener.checkCorrectness();
		setLocationRelativeTo(owner);
		setVisible(true);
		return result;
	}

	@Override
	public Pond getItem() {
		return createdpond;
	}
	
	/**����� �������� ����*/
	private JFrame owner = null;
	/**���� ��� ����� ����������� ������*/
	private ColoredJTextField uidf = null;
	/**���� ��� ����� ����� ������*/
	private JTextField pnamef = null;
	/**���� ��� ����� ������� ������*/
	private ColoredJTextField pareaf = null;
	/**���� ��� ����� ���������� ����������� ������*/
	private JTextField settf = null;
	/**���� ��� ����� ���������� ���� �������*/
	private ColoredJTextField fishspotf = null;
	/**���� ��� ����� ��������� �������*/
	private ColoredJTextField fishcostf = null;
	/**���� ��� ����� ����� ��� � ������*/
	private JTextField fishesf = null;
	/**���� ��� ����� �������������� ����������*/
	private JTextField otherf = null;
	/**������ ���������� ������*/
	private Pond createdpond = null;
	/**��������� �����*/
	private CorrectListener totallistener = null;
	/**���� ���������� ���������� �������*/
	private boolean result = false;
	/**��������� ����*/
	private static final String titlename = "����� ������";
	private static final long serialVersionUID = 1L;
	
}