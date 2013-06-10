package ua.vladaxon.users;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJTextField;

/**
 * Класс, объект которого следит за уникальностью логина для добавления нового пользователя.
 * Логин должен содержать только латинские буквы, цифры, символ подчеркивания.
 * Логин должен быть уникальным и иметь не менее 4 разрешенных символов.
 * @see CorrectFieldListener
 */
public class BusyLoginListener extends CorrectFieldListener{
	
	public BusyLoginListener(ColoredJTextField loginfield, UserManager manager){
		this.loginfield = loginfield;
		this.manager = manager;
		loginfield.getDocument().addDocumentListener(this);
		loginfield.setToolTipText(typelogin);
	}
	
	/**
	 * Проверка логина на соответствие правилам.
	 * Логин может содержать латинские буквы, цифры, символ подчеркивания.
	 * Логин должен иметь не менее 4 символов и быть уникальным среди
	 * существующих логинов, уникальность проверяет менеджер записей пользователей.
	 */
	protected void testInput(){
		String text = loginfield.getText();
		if(text.length()>0){
			if(text.matches(wordsymb)){
				if(text.length()>=4){
					if(manager.isUniqueLogin(text)){
						loginfield.setAccepted();
						loginfield.setToolTipText(loginunic);
						correctness = true;
					} else {
						loginfield.setDenied();
						loginfield.setToolTipText(notunic);
						correctness = false;
					}
				} else {
					loginfield.setDenied();
					loginfield.setToolTipText(shortlogin);
					correctness = false;					
				}
			} else {
				loginfield.setDenied();
				loginfield.setToolTipText(notwordsymbols);
				correctness = false;
			}
		} else {
			loginfield.setDefault();
			loginfield.setToolTipText(typelogin);
			correctness = false;
		}
		correctlistener.checkCorrectness();
	}
	
	/**Поле ввода логина*/
	private ColoredJTextField loginfield = null;
	/**Менеджер пользователей, предоставляет проверку уникальности*/
	private UserManager manager = null;
	/**Всплывающая подсказка при пустом поле ввода*/
	private static final String typelogin = "Введите логин";
	/**Всплывающая подсказка при недостаточной длине логина*/
	private static final String shortlogin = "Логин должен содержать не менее 4 символов";
	/**Всплывающая подсказка, если введеный логин не уникален*/
	private static final String notunic = "Такой логин уже зарегестрирован";
	/**Всплывающая подсказка при вводе логина, который соответствует требованиям*/
	private static final String loginunic = "Такой логин свободен";

}