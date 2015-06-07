

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.je.DatabaseException;
public class LinkQueue {
	//�ѷ��ʵ� url ����
	private  DBBFrontier visitedUrl;
	//�����ʵ� url ����
	private  DBBFrontier unVisitedUrl;
    
	public LinkQueue(String addr1, String addr2) throws FileNotFoundException, DatabaseException
    {
		File dir = new File(addr1);
		if(!dir.exists()){
            dir.mkdir();
        }
		
		File dir2 = new File(addr2);
		if(!dir2.exists()){
            dir2.mkdir();
        }
		
		visitedUrl = new DBBFrontier(addr1);
		unVisitedUrl = new DBBFrontier(addr2);	
    }
	//���URL����
	public  DBBFrontier getUnVisitedUrl() {
		return unVisitedUrl;
	}
    //��ӵ����ʹ���URL������
	public void addVisitedUrl(Url url) {
		try {
			visitedUrl.putUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    //�Ƴ����ʹ���URL
	public  void removeVisitedUrl(Url url) {
		visitedUrl.delete(url);
	}
    //δ���ʵ�URL������
	public  Object unVisitedUrlDeQueue() throws Exception {
		return unVisitedUrl.getNext();
	}

	// ��֤ÿ�� url ֻ������һ��
	public  void addUnvisitedUrl(Url url) throws Exception {
		
		if (url != null && !url.getOriUrl().trim().equals("")&& !visitedUrl.contains(url.getOriUrl()) && !unVisitedUrl.contains(url.getOriUrl()))
	    {
			unVisitedUrl.putUrl(url);
		}
	}
    //����Ѿ����ʵ�URL��Ŀ
	public  int getVisitedUrlNum() {
		return visitedUrl.size();
	}
    //�ж�δ���ʵ�URL�������Ƿ�Ϊ��
	public  boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}

}
