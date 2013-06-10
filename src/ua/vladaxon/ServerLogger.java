package ua.vladaxon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import ua.vladaxon.actions.ClearLogAction;

/**
 * Класс логера сервера.
 * В сервере этот компонент создается первым т.к. он необходим для регистрации сообщений всех компонентов.
 * Получает сообщения от различных компонентов и отображает их в пользовательском интерфейсе.
 * Внутри конструктора создается экземпляр многострочного поля ввода для вывода туда информации.
 * В конструкторе интерфейса пользователя логгер передает ссылку на это поле для размещения его в
 * интерфейсе.
 */
public class ServerLogger {
	
	/**
	 * Добавляет сообщение в окно логгирования.
	 * Сообщение составляется из текущей даты и времени и передаваемого сообщения.
	 * Параметр компонента и само сообщение было разделено на разные параметры для 
	 * лучшего понимания интерфейса логгера.
	 * @param from Имя компонента, от которого пришло сообщение.
	 * @param message Передаваемое сообщение.
	 */
	public void setMessage(String from, String message){
		String tolog = " ["+getDateTime()+"] " + from + ": " + message;
		textarea.append(tolog+"\n");
		clraction.setEnabled(true);
	}
	
	/**
	 * Возвращает текущую дату для формирования события в логе.
	 * @return Текущую дату и время в виде строки.
	 */
    private String getDateTime() {
        return dateFormat.format(new Date());
    }
    
    /**
     * Метод очищения окна логгирования.
     * Вызывается из обработчика консоли.
     */
    public void clearLog(){
    	textarea.setText("");
    	clraction.setEnabled(false);
    }
    
    /**
     * Возвращает объект многострочного текстового поля.
     * Необходим для размещения текстового поля на интерфейсе.
     * @return Объект текстового поля логгера.
     */
    public JTextArea getTextArea(){
    	return textarea;
    }
    
    /**
     * Возвращает объект действия, связанного с очисткой лога.
     * @return Объект действия.
     */
    public ClearLogAction getClearAction(){
    	return clraction;
    }
    
	/**Формат вывода даты перед сообщением*/
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**Экземпляр текстового поля*/
	private JTextArea textarea = new JTextArea();
	/**Объект действия, связанный с очищением лога*/
	private ClearLogAction clraction = new ClearLogAction(this);
    
}