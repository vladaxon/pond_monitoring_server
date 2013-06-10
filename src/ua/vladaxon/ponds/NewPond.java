package ua.vladaxon.ponds;

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
import javax.swing.JTextField;

import ua.vladaxon.CorrectListener;
import ua.vladaxon.basicitem.BasicItem.Flag;
import ua.vladaxon.basicitem.NewItemDialog;
import ua.vladaxon.database.Pond;
import ua.vladaxon.ui.ColoredJTextField;
import ua.vladaxon.ui.PanelBuilder;
import ua.vladaxon.ui.ServerUI;

/**
 * Диалог создания новой записи ставка.
 * Конструктор собирает интерфейс, добавляет необходимые слушатели корректности.
 */
public class NewPond extends JDialog implements NewItemDialog<Pond> {

	public NewPond(JFrame owner, PondManager manager){
		super(owner,true);
		this.owner = owner;
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		//Метки привязаны к заголовкам таблицы
		JLabel[] labels = new JLabel[Pond.getColumnCount()-2];
		for(int i=0; i<Pond.getColumnCount()-2; i++)
			labels[i] = new JLabel(Pond.getHeader(i)+":");
		Box labelbox = PanelBuilder.buildVerticalBox(9, labels);
		//Текстовые поля
		uidf = new ColoredJTextField();
		pnamef = new JTextField();
		pareaf = new ColoredJTextField();
		settf = new JTextField();
		fishspotf = new ColoredJTextField();
		fishcostf = new ColoredJTextField();
		fishesf = new JTextField();
		otherf = new JTextField();
		Dimension fielddimension = new Dimension(Integer.MAX_VALUE, uidf.getPreferredSize().height);
		uidf.setMaximumSize(fielddimension);
		pnamef.setMaximumSize(fielddimension);
		pareaf.setMaximumSize(fielddimension);
		settf.setMaximumSize(fielddimension);
		fishspotf.setMaximumSize(fielddimension);
		fishcostf.setMaximumSize(fielddimension);
		fishesf.setMaximumSize(fielddimension);
		otherf.setMaximumSize(fielddimension);
		Box textfieldbox = PanelBuilder.buildVerticalBox
				(5, uidf,pnamef,pareaf,settf,fishspotf,fishcostf,fishesf,otherf);
		//Верхняя панель
		Box upperbox = PanelBuilder.buildHorizontalBox(labelbox, textfieldbox);
		//Кнопки и кнопочная панель
		JButton okbtn = new JButton(ServerUI.oklabel, ServerUI.okicon);
		JButton cancelbtn = new JButton(ServerUI.cancellabel, ServerUI.cancelicon);
		Box buttonbox = PanelBuilder.buildHorizontalBox(okbtn, cancelbtn);
		//Слушатели
		UniqueIDListener idlist = new UniqueIDListener(uidf, manager);
		NumberInputListener areainput = new NumberInputListener(pareaf, true, false);
		NumberInputListener fishspotinput = new NumberInputListener(fishspotf, true, false);
		NumberInputListener fishcostinput = new NumberInputListener(fishcostf, true, false);
		totallistener = new CorrectListener(okbtn, idlist, areainput, fishspotinput, fishcostinput);
		//Главная панель
		mainpanel.add(upperbox);
		mainpanel.add(buttonbox);
		mainpanel.add(Box.createVerticalStrut(PanelBuilder.strutsize));
		mainpanel.setPreferredSize(new Dimension(300,mainpanel.getPreferredSize().height));
		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int uid = Integer.parseInt(uidf.getText());
				String name = pnamef.getText();
				int parea = 0;
				if(!pareaf.getText().equals(""))
					parea = Integer.parseInt(pareaf.getText());
				String settlm = settf.getText();
				int fishspotn = 0;
				if(!fishspotf.getText().equals(""))
					fishspotn = Integer.parseInt(fishspotf.getText());
				int fishcostn = 0;
				if(!fishcostf.getText().equals(""))
					fishcostn = Integer.parseInt(fishcostf.getText());
				String fishesn = fishesf.getText();
				String othern = otherf.getText();
				NewPond.this.createdpond = new Pond
						(uid,name,parea,settlm,fishspotn,fishcostn,fishesn,othern,Flag.ADDED);
				NewPond.this.result = true;
				NewPond.this.setVisible(false);	
			}
		});
		cancelbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewPond.this.setVisible(false);
			}
		});
		setTitle(titlename);
		setContentPane(mainpanel);
		pack();
		setResizable(false);
	}
	
	/**
	 * Метод, отображающий диалог.
	 * @return true - если диалог завершился успешно.
	 */
	public boolean showDialog(){
		result = false;
		uidf.setText("");
		pnamef.setText("");
		pareaf.setText("");
		settf.setText("");
		fishspotf.setText("");
		fishcostf.setText("");
		fishesf.setText("");
		otherf.setText("");
		createdpond = null;
		totallistener.checkCorrectness();
		setLocationRelativeTo(owner);
		setVisible(true);
		return result;
	}

	@Override
	public Pond getItem() {
		return createdpond;
	}
	
	/**Фрейм владелец окна*/
	private JFrame owner = null;
	/**Поле для ввода уникального номера*/
	private ColoredJTextField uidf = null;
	/**Поле для ввода имени ставка*/
	private JTextField pnamef = null;
	/**Поле для ввода площади ставка*/
	private ColoredJTextField pareaf = null;
	/**Поле для ввода ближайшего населенного пункта*/
	private JTextField settf = null;
	/**Поле для ввода количества мест рыбалки*/
	private ColoredJTextField fishspotf = null;
	/**Поле для ввода стоимости рыбалки*/
	private ColoredJTextField fishcostf = null;
	/**Поле для ввода видов рыб в ставке*/
	private JTextField fishesf = null;
	/**Поле для ввода дополнительной информации*/
	private JTextField otherf = null;
	/**Объект созданного ставка*/
	private Pond createdpond = null;
	/**Слушатель ввода*/
	private CorrectListener totallistener = null;
	/**Флаг результата завершения диалога*/
	private boolean result = false;
	/**Заголовок окна*/
	private static final String titlename = "Новый ставок";
	private static final long serialVersionUID = 1L;
	
}