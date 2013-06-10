package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import ua.vladaxon.Server;
import ua.vladaxon.ServerLogger;

/**
 * Объект действия, очищающий окно логгера.
 * Объект класса содержится в объекте логгера и передается компонентам управления по необходимости.
 * Логгер может контролировать активность этого действия не зная самих визуальных компонентов.
 * @see ServerLogger
 */
public class ClearLogAction extends AbstractAction{
	
	public ClearLogAction(ServerLogger logger){
		super();
		this.logger = logger;
		this.putValue(NAME, actionname);
		this.putValue(SHORT_DESCRIPTION, shortdescr);
		this.putValue(SMALL_ICON, clricon);
		this.setEnabled(false);
	}
	
	/**
	 * Очищает лог сообщений.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		logger.clearLog();
	}
	
	/**Объект логгера, необходимый для вызова очистки*/
	private ServerLogger logger = null;
	/**Имя действия*/
	private static final String actionname = "Очистить лог";
	/**Короткое описание действия*/
	private static final String shortdescr = "Очищает окно лога от всех сообщений";
	/**Иконка действия*/
	public static final ImageIcon clricon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/clear.png"));
	private static final long serialVersionUID = 1L;

}