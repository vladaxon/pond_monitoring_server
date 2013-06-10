package ua.vladaxon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Класс управляющий свойствами. При запуске пытается загрузить
 * пользовательские свойства из файла. Если загрузка завершилась
 * неудачей - устанавливаются свойства по умолчанию и сохраняются
 * в файл.
 */
public class PropManager {
	
	/**
	 * Конструктор класса. Если свойства из файла
	 * не загрузились - используются свойства по умолчанию.
	 * @param logger Логгер для вывода сообщения о загрузке.
	 */
	public PropManager(ServerLogger logger){
		if(!loadProps(logger)){
			props = getDefault();
			saveProps();
		}
	}
	
	/**
	 * Возвращает адрес подключения к БД.
	 * @return Строковое значение адреса.
	 */
	public String getDatabaseURL(){
		return props.getProperty(keylist[2]);
	}
	
	/**
	 * Возвращает количество слотов для подключения клиентов.
	 * @return Количество слотов.
	 */
	public int getClientSlotCount(){
		return Integer.parseInt(props.getProperty(keylist[1]));
	}
	
	/**
	 * Возвращает номер порта, по которому прослушиваются входящие
	 * соединения от клиентов.
	 * @return Номер порта
	 */
	public int getServerPort(){
		return Integer.parseInt(props.getProperty(keylist[0]));
	}
	
	/**
	 * Загружает свойства из файла и проверяется корректность свойств.
	 * @param logger Логгер для вывода сообщений.
	 * @return true - если свойства загружены.
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
			logger.setMessage("Свойства", "Загружены пользовательские свойства!");
			return true;
		} catch (Exception e) {
			logger.setMessage("Свойства", "Не удалось загрузить пользовательские свойства!");
			return false;
		}
	}
	
	/**
	 * Сохраняет свойства в файл.
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
	 * Возвращает объект свойств по умолчанию.
	 * @return Объект свойств.
	 */
	private Properties getDefault(){
		Properties props = new Properties();
		props.setProperty(keylist[0], "2812");
		props.setProperty(keylist[1], "10");
		props.setProperty(keylist[2], "jdbc:postgresql:pondserver");
		return props;
	}
	
	/**Объект свойств*/
	private Properties props;
	/**Путь к внешнему файлу свойств по умолчанию*/
	private static final String propdefpath = "properties.txt";
	/**Список ключей свойств*/
	private static final String[] keylist = {"serverport","clientslotcount","dburl"};

}