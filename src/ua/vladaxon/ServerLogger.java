package ua.vladaxon;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

import ua.vladaxon.actions.ClearLogAction;

/**
 * ����� ������ �������.
 * � ������� ���� ��������� ��������� ������ �.�. �� ��������� ��� ����������� ��������� ���� �����������.
 * �������� ��������� �� ��������� ����������� � ���������� �� � ���������������� ����������.
 * ������ ������������ ��������� ��������� �������������� ���� ����� ��� ������ ���� ����������.
 * � ������������ ���������� ������������ ������ �������� ������ �� ��� ���� ��� ���������� ��� �
 * ����������.
 */
public class ServerLogger {
	
	/**
	 * ��������� ��������� � ���� ������������.
	 * ��������� ������������ �� ������� ���� � ������� � ������������� ���������.
	 * �������� ���������� � ���� ��������� ���� ��������� �� ������ ��������� ��� 
	 * ������� ��������� ���������� �������.
	 * @param from ��� ����������, �� �������� ������ ���������.
	 * @param message ������������ ���������.
	 */
	public void setMessage(String from, String message){
		String tolog = " ["+getDateTime()+"] " + from + ": " + message;
		textarea.append(tolog+"\n");
		clraction.setEnabled(true);
	}
	
	/**
	 * ���������� ������� ���� ��� ������������ ������� � ����.
	 * @return ������� ���� � ����� � ���� ������.
	 */
    private String getDateTime() {
        return dateFormat.format(new Date());
    }
    
    /**
     * ����� �������� ���� ������������.
     * ���������� �� ����������� �������.
     */
    public void clearLog(){
    	textarea.setText("");
    	clraction.setEnabled(false);
    }
    
    /**
     * ���������� ������ �������������� ���������� ����.
     * ��������� ��� ���������� ���������� ���� �� ����������.
     * @return ������ ���������� ���� �������.
     */
    public JTextArea getTextArea(){
    	return textarea;
    }
    
    /**
     * ���������� ������ ��������, ���������� � �������� ����.
     * @return ������ ��������.
     */
    public ClearLogAction getClearAction(){
    	return clraction;
    }
    
	/**������ ������ ���� ����� ����������*/
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**��������� ���������� ����*/
	private JTextArea textarea = new JTextArea();
	/**������ ��������, ��������� � ��������� ����*/
	private ClearLogAction clraction = new ClearLogAction(this);
    
}