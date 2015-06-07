import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public abstract class AbstractFrontier {
    private Environment env;
    private static final String CLASS_CATALOG = "java_class_catalog";
    protected StoredClassCatalog javaCatalog;
    protected Database catalogdatabase;
    protected Database database;
    
    public AbstractFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException
    {
    	//��env,EnvironmentConfig������Թ��������ݿ�
    	System.out.println("Opening database in:" + homeDirectory);
    	EnvironmentConfig envConfig = new EnvironmentConfig();
    	envConfig.setTransactional(true);
    	envConfig.setAllowCreate(true);
    	//homeDirectoryΪͬһ��envConfig��������ļ�����־�ļ��ĵط�
    	env = new Environment(new File(homeDirectory), envConfig); 
    	
    	//����DatabaseConfig���������ݿ�
    	DatabaseConfig dbConfig = new DatabaseConfig();
    	dbConfig.setTransactional(true);
    	dbConfig.setAllowCreate(true);
    	
    	//�����ݿ�
    	catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
    	//��ʼ���洢���л������catalog��
    	javaCatalog = new StoredClassCatalog(catalogdatabase);  //�����ⲽ���ڴ�����ݿ���Ĺ����ж�����key
    	
    	//�ٸ�һ�����ݿ�
    	database =env.openDatabase(null, "URL", dbConfig);//�����ⲽ���ڴ���������ݣ��˲���֮ǰ����ָ���ж���key
    }
    
    /*�ر����ݿ⣬�رջ���*/
    public void close() throws DatabaseException{
    	database.close();
    	catalogdatabase.close();
    	//javaCatalog.close();
    	env.close();
    }
    
    //put����
    protected abstract void put(Object key, Object value);
    
    //get����
    protected abstract Object get(Object key);
    
    //delete����
    protected abstract Object delete(Object key);
}

