import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;


public class DBBFrontier extends AbstractFrontier implements Frontier{
	private StoredMap pendingUrisDB = null;
	
	/*使用默认的路径和缓存大小构造函数*/
	public DBBFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException
	{
		super(homeDirectory);
		//确定数据库中存储数据的类型，string和Url类型数据，它们将被序列化到磁盘
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog, Url.class);
		//创建数据存储的映射视图
		pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
	}

	@Override
	public Url getNext() throws Exception {
		Url result = null;
		if(!pendingUrisDB.isEmpty())
		{
			Set entrys = pendingUrisDB.entrySet();
			Entry<String, Url> entry = (Entry<String, Url>)pendingUrisDB.entrySet().iterator().next();
			result  = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}

	@Override
	public boolean putUrl(Url url) throws Exception {
		put(url.getOriUrl(), url);
		return true;
	}

	@Override
	protected void put(Object key, Object value) {
		pendingUrisDB.put(key, value);
	}

	@Override
	protected Object get(Object key) {		
		return pendingUrisDB.get(key);
	}

	@Override
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}
	
	protected boolean contains(String key)
	{
		return pendingUrisDB.containsKey(key);
	}
	
	protected boolean isEmpty()
	{
		return pendingUrisDB.isEmpty();
	}
	
	protected int size()
	{
		return pendingUrisDB.size();
	}
	
	/*测试函数未调用：根据url计算键值key，可以使用各种压缩算法，MD5，这里DB的key就是url，非IP*/
	private String caculateUrl(String url){
		return url;
	}
	
}
