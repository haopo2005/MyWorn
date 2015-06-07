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
    	//打开env,EnvironmentConfig对象可以管理多个数据库
    	System.out.println("Opening database in:" + homeDirectory);
    	EnvironmentConfig envConfig = new EnvironmentConfig();
    	envConfig.setTransactional(true);
    	envConfig.setAllowCreate(true);
    	//homeDirectory为同一个envConfig存放数据文件和日志文件的地方
    	env = new Environment(new File(homeDirectory), envConfig); 
    	
    	//设置DatabaseConfig，创建数据库
    	DatabaseConfig dbConfig = new DatabaseConfig();
    	dbConfig.setTransactional(true);
    	dbConfig.setAllowCreate(true);
    	
    	//打开数据库
    	catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
    	//初始化存储序列化对象的catalog类
    	javaCatalog = new StoredClassCatalog(catalogdatabase);  //猜想这步用于存放数据库里的规则，有多少列key
    	
    	//再搞一个数据库
    	database =env.openDatabase(null, "URL", dbConfig);//猜想这步用于存放所有数据，此步骤之前必须指定有多少key
    }
    
    /*关闭数据库，关闭环境*/
    public void close() throws DatabaseException{
    	database.close();
    	catalogdatabase.close();
    	//javaCatalog.close();
    	env.close();
    }
    
    //put方法
    protected abstract void put(Object key, Object value);
    
    //get方法
    protected abstract Object get(Object key);
    
    //delete方法
    protected abstract Object delete(Object key);
}

