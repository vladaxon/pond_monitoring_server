package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ua.vladaxon.Server;
import ua.vladaxon.ui.AboutFrame;

/**
 * Объект действия, вызывающее окно "О программе"
 */
public class AboutAction extends AbstractAction {

	public AboutAction(JFrame owner){
		super();
		this.owner = owner;
		this.putValue(NAME, usermanagename);
		this.putValue(SHORT_DESCRIPTION, shortdescription);
		this.putValue(SMALL_ICON, abouticon);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(frame==null)
			frame = new AboutFrame(owner);
		frame.showFrame();
	}
	
	/**Владелец диалогового окна*/
	private JFrame owner = null;
	/**Один экземпляр окна*/
	private AboutFrame frame = null;
	/**Имя действия*/
	private static final String usermanagename = "О программе";
	/**Короткое описание действия*/
	private static final String shortdescription = "Отображает окно о программе";
	/**Иконка о программе*/
	public static final ImageIcon abouticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/about.png"));
	private static final long serialVersionUID = 1L;

}