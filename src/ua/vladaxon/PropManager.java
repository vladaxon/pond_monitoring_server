package ua.vladaxon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ����� ����������� ����������. ��� ������� �������� ���������
 * ���������������� �������� �� �����. ���� �������� �����������
 * �������� - ��������������� �������� �� ��������� � �����������
 * � ����.
 */
public class PropManager {
	
	/**
	 * ����������� ������. ���� �������� �� �����
	 * �� ����������� - ������������ �������� �� ���������.
	 * @param logger ������ ��� ������ ��������� � ��������.
	 */
	public PropManager(ServerLogger logger){
		if(!loadProps(logger)){
			props = getDefault();
			saveProps();
		}
	}
	
	/**
	 * ���������� ����� ����������� � ��.
	 * @return ��������� �������� ������.
	 */
	public String getDatabaseURL(){
		return props.getProperty(keylist[2]);
	}
	
	/**
	 * ���������� ���������� ������ ��� ����������� ��������.
	 * @return ���������� ������.
	 */
	public int getClientSlotCount(){
		return Integer.parseInt(props.getProperty(keylist[1]));
	}
	
	/**
	 * ���������� ����� �����, �� �������� �������������� ��������
	 * ���������� �� ��������.
	 * @return ����� �����
	 */
	public int getServerPort(){
		return Integer.parseInt(props.getProperty(keylist[0]));
	}
	
	/**
	 * ��������� �������� �� ����� � ����������� ������������ �������.
	 * @param logger ������ ��� ������ ���������.
	 * @return true - ���� �������� ���������.
	 */
	private boolean loadProps(ServerLogger logger){
		props = new Properties();
		try {
			File propfile = new File(propdefpath);
			FileInputStream in = new FileInputStream(propfile);
			props.load(in);
			Integer.parseInt(props.getProperty(keylist[0]));
			Integer.parseInt(props.getProperty(keylist[1]));
			if(props.getProperty(keylist[2])==null){
				throw new Exception();
			}
			logger.setMessage("��������", "��������� ���������������� ��������!");
			return true;
		} catch (Exception e) {
			logger.setMessage("��������", "�� ������� ��������� ���������������� ��������!");
			return false;
		}
	}
	
	/**
	 * ��������� �������� � ����.
	 */
	private void saveProps(){
		try {
			File propfile = new File(propdefpath);
			FileOutputStream out = new FileOutputStream(propfile);
			props.store(out, "Server properties");
		} catch (IOException e) {
		}
	}
	
	/**
	 * ���������� ������ ������� �� ���������.
	 * @return ������ �������.
	 */
	private Properties getDefault(){
		Properties props = new Properties();
		props.setProperty(keylist[0], "2812");
		props.setProperty(keylist[1], "10");
		props.setProperty(keylist[2], "jdbc:postgresql:pondserver");
		return props;
	}
	
	/**������ �������*/
	private Properties props;
	/**���� � �������� ����� ������� �� ���������*/
	private static final String propdefpath = "properties.txt";
	/**������ ������ �������*/
	private static final String[] keylist = {"serverport","clientslotcount","dburl"};

}