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
 * Диалоговое окно создания нового пользователя.
 * Конструктор собирает интерфейс, добавляет слушатели корректности ввода.
 * При вызове диалога, выполняется запрос на получение множества номеров ставков,
 * к которым не привязаны пользователи, поскольку при создании записи пользователя
 * он должен быть привязан к записи ставка как ответственный за него. Пока не будет
 * получено множество - диалог создания остается в неактивном состоянии(поля ввода заблокированы).
 * Если в полученном множестве не окажется свободных номеров - выводится окно с 
 * сообщением о невозможности создания новой записи и диалог создания закрывается. 
 * Если множество будет содержать номера свободных ставков - поля ввода разблокируются.
 * При создании новой записи проверяется уникальность логина и правильность ввода пароля.
 * После создания новой записи - она добавляется в добавочный список менеджера записей и будет
 * добавлена во время синхронизации с базой данных.
 */
public class NewUser extends JDialog implements NewItemDialog<User>{
	
	public NewUser(JFrame owner, UserManager manager){
		super(owner,true);
		this.owner = owner;
		this.manager = manager;
		//Метки и их панель
		JLabel loginlab = new JLabel(User.getHeader(0)+":");
		JLabel passlab = new JLabel(User.getHeader(1)+":");
		JLabel repasslab = new JLabel("Повторите пароль:");
		JLabel usernamelab = new JLabel(User.getHeader(2)+":");
		JLabel telephonelab = new JLabel(User.getHeader(3)+":");
		JLabel pondliabel = new JLabel("Ставок:");
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
	 * Метод, делающий диалог неактивным.
	 * Выключает все поля ввода и добавляет к заголовку "Загрузка данных...".
	 * Окно остается неактивным пока не будет получено множество
	 * номеров свободных ставков, к которым не привязан пользователь.
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
	 * Метод, получающий множество свободных номеров ставков.
	 * Если множество не содержит номеров - отображается сообщение о
	 * невозможности создания новой записи и диалог закрывается.
	 * Если множество содержит номера - поля разблокируются.
	 * Работа происходит в потоке очереди событий т.к. метод
	 * вызывается из пула запросов.
	 * @param freeidset Множество свободных номеров ставков.
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

	/**Объект созданного пользователя*/
	private User createduser = null;
	/**Фрейм владелец*/
	private JFrame owner = null;
	/**Менеджер пользователей*/
	private UserManager manager = null;
	/**Поле для ввода логина*/
	private ColoredJTextField loginfield = null;
	/**Поле для ввода пароля*/
	private ColoredJPasswordField passwfield = null;
	/**Поле для ввода подтверждения пароля*/
	private ColoredJPasswordField reppasswfield = null;
	/**Поле для ввода ФИО пользователя*/
	private JTextField usernamefield = null;
	/**Поле для ввода телефона пользователя*/
	private JTextField telephonefield = null;
	/**Список для выбора привязки к ставку*/
	private JComboBox<Integer> pondlist = null;
	/**Слушатель ввода*/
	private CorrectListener totallistener = null;
	/**Флаг результата завершения диалога*/
	private boolean result = false;
	/**Заголовок окна*/
	private static final String titlename = "Новый пользователь";
	/**Заголовок окна при загрузке номеров*/
	private static final String loadtitlename = " (Загрузка данных...)";
	/**Сообщение, выводимое при отсутствии записей свободных ставков*/
	private static final String nofreepond = "   Невозможно создать новую запись! \n" +
			"Все ставки привязаны к пользователям!";
	private static final long serialVersionUID = 1L;
	
}