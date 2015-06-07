

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class MyCrawler {
	private LinkQueue myqueue;
	private static ThreadPoolExecutor threadPool;
	public static Set<String> filter_distinct;
	public static Set<String> filter_include;
	public static Set<String> down_include;
	public static String path;
	private String init_seed;
	private int sleep_time;
	
	public MyCrawler() throws Exception
	{
		//读取配置
        XmlParse xmlTest = new XmlParse();
        ConfigBean mconfig = xmlTest.myconfig;  
		xmlTest.readXMLFile("config.xml");
		
		//获取解析的数据
		String dbaddr1 = mconfig.getDb_addr1();
		String dbaddr2 = mconfig.getDb_addr2();
		
		this.init_seed = mconfig.getInit_seed();
		this.sleep_time = mconfig.getSleep_time();
		MyCrawler.filter_distinct = mconfig.getFilter_distinct();
		MyCrawler.filter_include = mconfig.getFilter_include();
		MyCrawler.down_include = mconfig.getDown_include(); 
		MyCrawler.path = mconfig.getPath();
		
		myqueue = new LinkQueue(dbaddr1,dbaddr2);
		threadPool = ThreadPool.getInstance();
	}
	/**
	 * 使用种子初始化 URL 队列
	 * @return
	 * @param seeds 种子URL
	 * @throws Exception 
	 */ 
	private void initCrawlerWithSeeds(Url seeds) throws Exception
	{
		myqueue.addUnvisitedUrl(seeds);
	}	
	/**
	 * 抓取过程
	 * @return
	 * @param seeds
	 * @throws Exception 
	 */
	public void crawling() throws Exception
	{  
		
		
		LinkFilter filter = new LinkFilter(){
			public boolean accept(String url) 
			{
				boolean value = true;
				for(String temp1:MyCrawler.filter_distinct)
				{
					if(url.contains(temp1))
					{
						value &= false;
						break;
					}
				}
				
				for(String temp2:MyCrawler.filter_include)
				{
					if(!url.contains(temp2))
					{
						value &= false;
						break;
					}
				}
				return value;
			}
		};
		
		//初始化 URL 队列
		initCrawlerWithSeeds(new Url(init_seed));
		
		//循环条件：待抓取的链接不空
		while(!myqueue.unVisitedUrlsEmpty())
		{
			Thread.currentThread();
			Thread.sleep(sleep_time*1000);
			
			//队头URL出队列
			Url visitUrl=(Url)myqueue.unVisitedUrlDeQueue();
			
			if(visitUrl.getOriUrl() == null)
				continue;
			
			DownLoadFile downLoader = new DownLoadFile(visitUrl.getOriUrl());
			
			//下载图片,线程池技术
			threadPool.execute(downLoader);
			
			//该 url 放入到已访问的 URL 中
			myqueue.addVisitedUrl(visitUrl);
			
			//提取出下载网页中的 URL
			Set<String> links=HtmlParserTool.extracLinks(visitUrl.getOriUrl(),filter);
			
			//新的未访问的 URL 入队
			for(String link:links)
			{
				myqueue.addUnvisitedUrl(new Url(link));
			}
		}
	}
	
	//main 方法入口
	public static void main(String[]args) throws Exception
	{
		//初始化爬虫实例
		System.out.println("programe start...");
		MyCrawler crawler = new MyCrawler();
		//开始爬
		crawler.crawling();
	}

}
