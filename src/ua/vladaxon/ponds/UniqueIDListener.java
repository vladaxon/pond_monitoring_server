package ua.vladaxon.ponds;

import ua.vladaxon.CorrectFieldListener;
import ua.vladaxon.ui.ColoredJTextField;

/**
 * ������ ��������� ������������ ����� � ����. ��������� ������������
 * ��� �������� ����� ������ ������ � ������ �� ������������ ������.
 * ����� ������ ������ ���� �� ������������� � ���� ����������.
 * ������������ ��������� �������� �������.
 */
public class UniqueIDListener extends CorrectFieldListener{

	public UniqueIDListener(ColoredJTextField idfield, PondManager manager){
		this.idfield = idfield;
		this.manager = manager;
		this.idfield.getDocument().addDocumentListener(this);
		this.idfield.setToolTipText(defmsg);
	}
	
	/**
	 * �������� �� ���� ����� � �� ������������ �����.
	 * ����� �� ������ ���� �������������.
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
	
	/**���� ����� ������*/
	private ColoredJTextField idfield = null; 
	/**�������� �������*/
	private PondManager manager = null;
	/**����������� ��������� �� ���������*/
	private static final String defmsg = "������� ���������� �����";
	/**����������� ��������� ��� ����� ������������� �����*/
	private static final String notunique = "����� �� ��������";
	/**����������� ��������� ��� ����� ����������� �����*/
	private static final String unique = "����� ��������";

}