package ua.vladaxon.users;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJPasswordField;

/**
 * Класс, проверяющий правильность ввода паролей, при создании нового пользователя.
 * Проверяет оба поля на достаточное количество символов и отсутствие кириллицы.
 * Если оба поля имеют не менее 4 символов и не содержат кириллицу - проверяется идентичность
 * паролей в обоих полях. Если они совпадают - пароли считаются правильно введенными.
 */
public class PasswordTypeListener extends CorrectFieldListener{

	public PasswordTypeListener(ColoredJPasswordField first, ColoredJPasswordField second){
		this.first = first;
		this.second = second;
		first.getDocument().addDocumentListener(this);
		second.getDocument().addDocumentListener(this);
	}
	
	/**
	 * Метод, проверяющий введеные пароли на соответствие требованиям.
	 * В начале оба поля проверяются на соответствие первичным требованиям, таким как:
	 * наличием не менее 4 символов и отсутствие кириллицы.
	 * Если оба поля ввода соответствуют этим требованиям - проверяется соответствие этих паролей.
	 * Если пароли, введенные в оба поля, совпадают - пароль считается принятым.
	 * В конце вызывается метод проверки общей корректности.
	 */
	protected void testInput(){
		boolean firststate = checkCorrectness(first);
		boolean secondstate = checkCorrectness(second);
		if(firststate && secondstate){
			String text1 = new String(first.getPassword());
			String text2 = new String(second.getPassword());
			if(text1.equals(text2)){
				correctness = true;
			} else {
				second.setDenied();
				second.setToolTipText(noteq);
				correctness = false;
			}
		} else {
			correctness = false;
			if(!firststate){
				second.setDefault();
				second.setToolTipText(typepassword);
			}
		}
		correctlistener.checkCorrectness();
	}
	
	/**
	 * Метод, проверяющий введенный пароль на соответствие требованиям.
	 * Пароль не должен быть пустым, и быть короче 4 символов.
	 * Пароль может содержать латинские буквы, цифры, символ подчеркивания.
	 * @param field Поле, пароль которого проверяется
	 * @return true - если пароль введен правильно
	 */
	private boolean checkCorrectness(ColoredJPasswordField field){
		String password = new String(field.getPassword());
		if(password.length()>0){
			if(password.matches(wordsymb)){
				if(password.length()>=4){
					field.setAccepted();
					field.setToolTipText(passtyped);
					return true;
				} else {
					field.setDenied();
					field.setToolTipText(shortpassword);
					return false;					
				}
			} else {
				field.setDenied();
				field.setToolTipText(notwordsymbols);
				return false;
			}
		} else {
			field.setDefault();
			field.setToolTipText(typepassword);
			return false;
		}
	}
	
	/**Первое поля ввода пароля*/
	private ColoredJPasswordField first = null;
	/**Второе поле, подтверждающее ввод*/
	private ColoredJPasswordField second = null;
	/**Всплывающая подсказка при пустом поле ввода*/
	private static final String typepassword = "Введите пароль";
	/**Всплывающая подсказка при недостаточной длине пароля*/
	private static final String shortpassword = "Пароль должен содержать не меньше 4 символов";
	/**Всплывающая подсказка если пароли в полях не совпадают*/
	private static final String noteq = "Пароли не совпадают";
	/**Всплывающая подсказка при вводе пароля, который соответствует требованиям*/
	private static final String passtyped = "Пароль принят";

}