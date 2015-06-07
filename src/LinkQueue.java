

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.je.DatabaseException;
public class LinkQueue {
	//已访问的 url 集合
	private  DBBFrontier visitedUrl;
	//待访问的 url 集合
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
	//获得URL队列
	public  DBBFrontier getUnVisitedUrl() {
		return unVisitedUrl;
	}
    //添加到访问过的URL队列中
	public void addVisitedUrl(Url url) {
		try {
			visitedUrl.putUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    //移除访问过的URL
	public  void removeVisitedUrl(Url url) {
		visitedUrl.delete(url);
	}
    //未访问的URL出队列
	public  Object unVisitedUrlDeQueue() throws Exception {
		return unVisitedUrl.getNext();
	}

	// 保证每个 url 只被访问一次
	public  void addUnvisitedUrl(Url url) throws Exception {
		
		if (url != null && !url.getOriUrl().trim().equals("")&& !visitedUrl.contains(url.getOriUrl()) && !unVisitedUrl.contains(url.getOriUrl()))
	    {
			unVisitedUrl.putUrl(url);
		}
	}
    //获得已经访问的URL数目
	public  int getVisitedUrlNum() {
		return visitedUrl.size();
	}
    //判断未访问的URL队列中是否为空
	public  boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}

}
