package ua.vladaxon.ponds;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJTextField;

/**
 * Объект слушателя корректности ввода в поле. Слушатель используется
 * при создании новой записи ставка и следит за уникальность номера.
 * Номер ставка должен быть не отрицательным и быть уникальным.
 * Уникальность проверяет менеджер ставков.
 */
public class UniqueIDListener extends CorrectFieldListener{

	public UniqueIDListener(ColoredJTextField idfield, PondManager manager){
		this.idfield = idfield;
		this.manager = manager;
		this.idfield.getDocument().addDocumentListener(this);
		this.idfield.setToolTipText(defmsg);
	}
	
	/**
	 * Проверка на ввод числа и на уникальность числа.
	 * Число не должно быть отрицательным.
	 */
	@Override
	protected void testInput() {
		String strid = idfield.getText();
		if(strid.length()==0){
			idfield.setDefault();
			idfield.setToolTipText(defmsg);
			correctness = false;
			return;
		} else {
			if(strid.matches(nonegativ)){
				if(manager.isUnique(Integer.parseInt(strid))){
					idfield.setAccepted();
					idfield.setToolTipText(unique);
					correctness = true;
				} else {
					idfield.setDenied();
					idfield.setToolTipText(notunique);
					correctness = false;
				}
			} else {
				idfield.setDenied();
				idfield.setToolTipText(notnumber);
				correctness = false;
			}
		}
		correctlistener.checkCorrectness();
	}
	
	/**Поле ввода номера*/
	private ColoredJTextField idfield = null; 
	/**Менеджер ставков*/
	private PondManager manager = null;
	/**Всплывающее сообщение по умолчанию*/
	private static final String defmsg = "Введите уникальный номер";
	/**Всплывающее сообщение при вводе неуникального числа*/
	private static final String notunique = "Номер не уникален";
	/**Всплывающее сообщение при вводе уникального числа*/
	private static final String unique = "Номер уникален";

}