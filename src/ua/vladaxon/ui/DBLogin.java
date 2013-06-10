package ua.vladaxon.ui;

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

import ua.vladaxon.CorrectListener;
import ua.vladaxon.actions.ConnectDBAction;
import ua.vladaxon.database.DBManager;
import ua.vladaxon.database.LoginDBDocListener;

/**
 * Класс, конструирующий окно логина в БД.
 * Необходим для получения от пользователя логина и пароля подключения к БД.
 * @see DBManager
 */
public class DBLogin extends JDialog{
	
	public DBLogin(JFrame owner){
		super(owner,true);
		this.owner = owner;
		JLabel loglabel = new JLabel(loginlabel);
		JLabel passlabel = new JLabel(passwordlabel);
		Box labelbox = PanelBuilder.buildVerticalBox(labelstrutsize, loglabel, passlabel);
		loginfield = new ColoredJTextField();
		LoginDBDocListener loginlistener = new LoginDBDocListener(loginfield);
		Dimension fielddim = new Dimension(Integer.MAX_VALUE, loginfield.getPreferredSize().height);
		loginfield.setMaximumSize(fielddim);
		passwfield = new ColoredJPasswordField();
		LoginDBDocListener passwlistener = new LoginDBDocListener(passwfield);
		passwfield.setMaximumSize(fielddim);
		Box taxtfieldbox = PanelBuilder.buildVerticalBox(textfstrutsize, loginfield, passwfield);
		Box upperbox = PanelBuilder.buildHorizontalBox(labelbox, taxtfieldbox);
		JButton okbtn = new JButton(ServerUI.oklabel, ServerUI.okicon);
		JButton cancbtn = new JButton(ServerUI.cancellabel, ServerUI.cancelicon);
		Box buttonbox = PanelBuilder.buildHorizontalBox(okbtn, cancbtn);
		totallistener = new CorrectListener(okbtn, loginlistener, passwlistener);
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		mainpanel.add(upperbox);
		mainpanel.add(buttonbox);
		mainpanel.add(Box.createVerticalStrut(PanelBuilder.strutsize));
		mainpanel.setPreferredSize(new Dimension(250, mainpanel.getPreferredSize().height));
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login = loginfield.getText();
				password = new String(passwfield.getPassword());
				DBLogin.result = true;
				DBLogin.this.setVisible(false);	
			}
		});
		cancbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DBLogin.this.setVisible(false);
			}
		});
		this.setTitle(titlename);
		this.setIconImage(ConnectDBAction.connicon.getImage());
		this.setContentPane(mainpanel);
		this.pack();
		this.setResizable(false);
	}
	
	/**
	 * Метод, вызывающий отображение диалога.
	 * Если объект не был создан - он создается.
	 * @param owner Фрейм владелец окна.
	 * @return <b>true</b> - если логин и пароль были введены.
	 */
	public boolean showDialog(){
		result = false;
		this.loginfield.setText("");
		this.passwfield.setText("");
		this.login = null;
		this.password = null;
		totallistener.checkCorrectness();
		this.setLocationRelativeTo(owner);
		this.setVisible(true);
		return result;
	}
	
	/**
	 * Возвращает введенный логин или null если диалог завершился отменой.
	 * @return Строковое значение логина.
	 */
	public String getLogin(){
		return this.login;
	}
	
	/**
	 * Возвращает введенный пароль или null если диалог завершился отменой.
	 * @return Строковое значение пароля.
	 */	
	public String getPassword(){
		return this.password;
	}
	
	/**Слушатель ввода*/
	private CorrectListener totallistener = null;
	/**Фрейм владелец диалогового окна*/
	private JFrame owner = null;
	/**Введенный логин*/
	private String login = null;
	/**Введенный пароль*/
	private String password = null;
	/**Поле ввода логина*/
	private ColoredJTextField loginfield = null;
	/**Поле ввода пароля*/
	private ColoredJPasswordField passwfield = null;
	/**Результат завершения диалога*/
	private static boolean result = false;
	/**Размер распорки диспечера компоновки*/
	public static final int labelstrutsize = 7;
	/**Размер распорки диспечера компоновки*/
	public static final int textfstrutsize = 5;
	/**Заголовок окна*/
	private static final String titlename = "Подключение к БД";
	/**Метка логина*/
	private static final String loginlabel = "Логин:";
	/**Метка пароля*/
	private static final String passwordlabel = "Пароль:";
	private static final long serialVersionUID = 1L;

}