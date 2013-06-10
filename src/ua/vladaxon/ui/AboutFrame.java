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
 * Класс создающий диалоговое окно "О программе"
 */
public class AboutFrame extends JDialog{

	/**
	 * Конструктор по умолчанию
	 * @param owner Фрейм-владелец
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
	 * Метод, отображающий окно информации.
	 */
	public void showFrame(){
		setLocationRelativeTo(owner);
		setVisible(true);
	}
	
	/**Окно владелец, необходимо для отображения*/
	private JFrame owner = null;	
	/**Размер распорки для менеджера компонентов*/
	private static final int strutsize = 10;
	/*Строки отображения*/
	private static final String frametitle = "О программе";
	private static final String title = "Дипломная работа";
	private static final String theme = "Система сбора и накопления данных о ставках";
	private static final String theme2 = "бассейна Днепра";
	private static final String group = "Группа КНгрС-11-1";
	private static final String vlad = "Бабушкин Владислав";
	private static final String pasha = "Ермак Павел";
	private static final String year = "2013г.";
	private static final long serialVersionUID = 1L;

}