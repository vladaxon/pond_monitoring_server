package ua.vladaxon.users;

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
import javax.swing.JTextField;

import ua.vladaxon.CorrectListener;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.NewItemDialog;
import ua.vladaxon.database.User;
import ua.vladaxon.ui.ColoredJPasswordField;
import ua.vladaxon.ui.ColoredJTextField;
import ua.vladaxon.ui.PanelBuilder;
import ua.vladaxon.ui.ServerUI;

/**
 * ���������� ���� �������� ������ ������������.
 * ����������� �������� ���������, ��������� ��������� ������������ �����.
 * ��� ������ �������, ����������� ������ �� ��������� ��������� ������� �������,
 * � ������� �� ��������� ������������, ��������� ��� �������� ������ ������������
 * �� ������ ���� �������� � ������ ������ ��� ������������� �� ����. ���� �� �����
 * �������� ��������� - ������ �������� �������� � ���������� ���������(���� ����� �������������).
 * ���� � ���������� ��������� �� �������� ��������� ������� - ��������� ���� � 
 * ���������� � ������������� �������� ����� ������ � ������ �������� �����������. 
 * ���� ��������� ����� ��������� ������ ��������� ������� - ���� ����� ��������������.
 * ��� �������� ����� ������ ����������� ������������ ������ � ������������ ����� ������.
 * ����� �������� ����� ������ - ��� ����������� � ���������� ������ ��������� ������� � �����
 * ��������� �� ����� ������������� � ����� ������.
 */
public class NewUser extends JDialog implements NewItemDialog<User>{
	
	public NewUser(JFrame owner, UserManager manager){
		super(owner,true);
		this.owner = owner;
		this.manager = manager;
		//����� � �� ������
		JLabel loginlab = new JLabel(User.getHeader(0)+":");
		JLabel passlab = new JLabel(User.getHeader(1)+":");
		JLabel repasslab = new JLabel("��������� ������:");
		JLabel usernamelab = new JLabel(User.getHeader(2)+":");
		JLabel telephonelab = new JLabel(User.getHeader(3)+":");
		JLabel pondliabel = new JLabel("������:");
		Box labelbox = PanelBuilder.buildVerticalBox
				(10, loginlab, passlab, repasslab, usernamelab, telephonelab, pondliabel);
		loginfield = new ColoredJTextField();
		Dimension fielddimension = new Dimension(Integer.MAX_VALUE, loginfield.getPreferredSize().height);
		loginfield.setMaximumSize(fielddimension);
		passwfield = new ColoredJPasswordField();
		passwfield.setMaximumSize(fielddimension);
		reppasswfield = new ColoredJPasswordField();
		reppasswfield.setMaximumSize(fielddimension);
		usernamefield = new JTextField();
		usernamefield.setMaximumSize(fielddimension);
		telephonefield = new JTextField();
		telephonefield.setMaximumSize(fielddimension);
		pondlist = new JComboBox<Integer>();
		pondlist.setMaximumSize(fielddimension);
		Box textfieldbox = PanelBuilder.buildVerticalBox
				(5, loginfield, passwfield, reppasswfield, usernamefield, telephonefield, pondlist);
		Box upperbox = PanelBuilder.buildHorizontalBox(labelbox, textfieldbox);
		JButton okbtn = new JButton(ServerUI.oklabel, ServerUI.okicon);
		JButton cancelbtn = new JButton(ServerUI.cancellabel, ServerUI.cancelicon);
		Box buttonbox = PanelBuilder.buildHorizontalBox(okbtn, cancelbtn);
		BusyLoginListener loginlistener = new BusyLoginListener(loginfield, manager);
		PasswordTypeListener passlistener = new PasswordTypeListener(passwfield, reppasswfield);
		totallistener = new CorrectListener(okbtn, loginlistener, passlistener);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(upperbox);
		mainpanel.add(buttonbox);
		mainpanel.add(Box.createVerticalStrut(PanelBuilder.strutsize));
		mainpanel.setPreferredSize(new Dimension(300, mainpanel.getPreferredSize().height));
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String login = loginfield.getText();
				int hashedpassword = new String(reppasswfield.getPassword()).hashCode();
				String username = usernamefield.getText();
				String telephone = telephonefield.getText();
				int pondid = (int) pondlist.getSelectedItem();
				NewUser.this.createduser = new User
						(login, hashedpassword, username, telephone, pondid, Flag.ADDED);
				NewUser.this.result = true;
				NewUser.this.setVisible(false);
			}
		});
		cancelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewUser.this.setVisible(false);
			}
		});
		setIconImage(UserManagerUI.newusericon.getImage());
		setContentPane(mainpanel);
		pack();
		setResizable(false);
	}
	
	@Override
	public boolean showDialog(){
		result = false;
		loginfield.setText("");
		passwfield.setText("");
		reppasswfield.setText("");
		usernamefield.setText("");
		telephonefield.setText("");
		pondlist.removeAllItems();
		createduser = null;
		totallistener.checkCorrectness();
		setLocationRelativeTo(owner);
		setLoadStatus();
		setVisible(true);
		return result;
	}
	
	@Override
	public User getItem() {
		return createduser;
	}
	
	/**
	 * �����, �������� ������ ����������.
	 * ��������� ��� ���� ����� � ��������� � ��������� "�������� ������...".
	 * ���� �������� ���������� ���� �� ����� �������� ���������
	 * ������� ��������� �������, � ������� �� �������� ������������.
	 */
	private void setLoadStatus(){
		manager.requestFreePondId();
		setTitle(titlename + loadtitlename);
		loginfield.setEnabled(false);
		passwfield.setEnabled(false);
		reppasswfield.setEnabled(false);
		usernamefield.setEnabled(false);
		telephonefield.setEnabled(false);
		pondlist.setEnabled(false);
	}
	
	/**
	 * �����, ���������� ��������� ��������� ������� �������.
	 * ���� ��������� �� �������� ������� - ������������ ��������� �
	 * ������������� �������� ����� ������ � ������ �����������.
	 * ���� ��������� �������� ������ - ���� ��������������.
	 * ������ ���������� � ������ ������� ������� �.�. �����
	 * ���������� �� ���� ��������.
	 * @param freeidset ��������� ��������� ������� �������.
	 */
	public void setFreePondId(final Set<Integer> freeidset){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				setTitle(titlename);
				if(freeidset.isEmpty()){
					JOptionPane.showMessageDialog(owner, nofreepond);
					setVisible(false);
				} else {
					loginfield.setEnabled(true);
					passwfield.setEnabled(true);
					reppasswfield.setEnabled(true);
					usernamefield.setEnabled(true);
					telephonefield.setEnabled(true);
					for(Integer i: freeidset)
						pondlist.addItem(i);
					pondlist.setEnabled(true);
				}
			}
		});
	}

	/**������ ���������� ������������*/
	private User createduser = null;
	/**����� ��������*/
	private JFrame owner = null;
	/**�������� �������������*/
	private UserManager manager = null;
	/**���� ��� ����� ������*/
	private ColoredJTextField loginfield = null;
	/**���� ��� ����� ������*/
	private ColoredJPasswordField passwfield = null;
	/**���� ��� ����� ������������� ������*/
	private ColoredJPasswordField reppasswfield = null;
	/**���� ��� ����� ��� ������������*/
	private JTextField usernamefield = null;
	/**���� ��� ����� �������� ������������*/
	private JTextField telephonefield = null;
	/**������ ��� ������ �������� � ������*/
	private JComboBox<Integer> pondlist = null;
	/**��������� �����*/
	private CorrectListener totallistener = null;
	/**���� ���������� ���������� �������*/
	private boolean result = false;
	/**��������� ����*/
	private static final String titlename = "����� ������������";
	/**��������� ���� ��� �������� �������*/
	private static final String loadtitlename = " (�������� ������...)";
	/**���������, ��������� ��� ���������� ������� ��������� �������*/
	private static final String nofreepond = "   ���������� ������� ����� ������! \n" +
			"��� ������ ��������� � �������������!";
	private static final long serialVersionUID = 1L;
	
}