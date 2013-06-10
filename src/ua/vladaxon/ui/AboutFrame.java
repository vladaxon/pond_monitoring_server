package ua.vladaxon.ui;

import java.awt.Component;
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

import ua.vladaxon.actions.AboutAction;

/**
 * ����� ��������� ���������� ���� "� ���������"
 */
public class AboutFrame extends JDialog{

	/**
	 * ����������� �� ���������
	 * @param owner �����-��������
	 */
	public AboutFrame(JFrame owner){
		super(owner, true);
		this.owner = owner;
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(Box.createVerticalStrut(strutsize));
		JLabel label = new JLabel(title);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		label = new JLabel(theme);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		label = new JLabel(theme2);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		label = new JLabel(group);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		label = new JLabel(vlad);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		label = new JLabel(pasha);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		label = new JLabel(year);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainpanel.add(label);
		mainpanel.add(Box.createVerticalStrut(strutsize/2));
		JButton ok = new JButton("Ok");
		ok.setIcon(ServerUI.okicon);
		ok.setAlignmentX(Component.CENTER_ALIGNMENT);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutFrame.this.setVisible(false);	
			}
		});
		mainpanel.add(ok);
		mainpanel.add(Box.createVerticalStrut(strutsize));
		mainpanel.setPreferredSize(new Dimension(300,180));	
		setContentPane(mainpanel);
		pack();
		setResizable(false);
		setTitle(frametitle);
		setIconImage(AboutAction.abouticon.getImage());
	}
	
	/**
	 * �����, ������������ ���� ����������.
	 */
	public void showFrame(){
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
	/**���� ��������, ���������� ��� �����������*/
	private JFrame owner = null;	
	/**������ �������� ��� ��������� �����������*/
	private static final int strutsize = 10;
	/*������ �����������*/
	private static final String frametitle = "� ���������";
	private static final String title = "��������� ������";
	private static final String theme = "������� ����� � ���������� ������ � �������";
	private static final String theme2 = "�������� ������";
	private static final String group = "������ �����-11-1";
	private static final String vlad = "�������� ���������";
	private static final String pasha = "����� �����";
	private static final String year = "2013�.";
	private static final long serialVersionUID = 1L;

}