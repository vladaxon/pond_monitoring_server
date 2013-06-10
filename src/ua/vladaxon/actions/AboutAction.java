package ua.vladaxon.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ua.vladaxon.Server;
import ua.vladaxon.ui.AboutFrame;

/**
 * ������ ��������, ���������� ���� "� ���������"
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
	
	/**�������� ����������� ����*/
	private JFrame owner = null;
	/**���� ��������� ����*/
	private AboutFrame frame = null;
	/**��� ��������*/
	private static final String usermanagename = "� ���������";
	/**�������� �������� ��������*/
	private static final String shortdescription = "���������� ���� � ���������";
	/**������ � ���������*/
	public static final ImageIcon abouticon = 
			new ImageIcon(Server.class.getResource("/ua/vladaxon/res/about.png"));
	private static final long serialVersionUID = 1L;

}