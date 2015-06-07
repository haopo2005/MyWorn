import java.io.FileNotFoundException;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;


public class DBBFrontier extends AbstractFrontier implements Frontier{
	private StoredMap pendingUrisDB = null;
	
	/*ʹ��Ĭ�ϵ�·���ͻ����С���캯��*/
	public DBBFrontier(String homeDirectory) throws DatabaseException, FileNotFoundException
	{
		super(homeDirectory);
		//ȷ�����ݿ��д洢���ݵ����ͣ�string��Url�������ݣ����ǽ������л�������
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog, Url.class);
		//�������ݴ洢��ӳ����ͼ
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
	
	/*���Ժ���δ���ã�����url�����ֵkey������ʹ�ø���ѹ���㷨��MD5������DB��key����url����IP*/
	private String caculateUrl(String url){
		return url;
	}
	
}
