

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
		//��ȡ����
        XmlParse xmlTest = new XmlParse();
        ConfigBean mconfig = xmlTest.myconfig;  
		xmlTest.readXMLFile("config.xml");
		
		//��ȡ����������
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
	 * ʹ�����ӳ�ʼ�� URL ����
	 * @return
	 * @param seeds ����URL
	 * @throws Exception 
	 */ 
	private void initCrawlerWithSeeds(Url seeds) throws Exception
	{
		myqueue.addUnvisitedUrl(seeds);
	}	
	/**
	 * ץȡ����
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
		
		//��ʼ�� URL ����
		initCrawlerWithSeeds(new Url(init_seed));
		
		//ѭ����������ץȡ�����Ӳ���
		while(!myqueue.unVisitedUrlsEmpty())
		{
			Thread.currentThread();
			Thread.sleep(sleep_time*1000);
			
			//��ͷURL������
			Url visitUrl=(Url)myqueue.unVisitedUrlDeQueue();
			
			if(visitUrl.getOriUrl() == null)
				continue;
			
			DownLoadFile downLoader = new DownLoadFile(visitUrl.getOriUrl());
			
			//����ͼƬ,�̳߳ؼ���
			threadPool.execute(downLoader);
			
			//�� url ���뵽�ѷ��ʵ� URL ��
			myqueue.addVisitedUrl(visitUrl);
			
			//��ȡ��������ҳ�е� URL
			Set<String> links=HtmlParserTool.extracLinks(visitUrl.getOriUrl(),filter);
			
			//�µ�δ���ʵ� URL ���
			for(String link:links)
			{
				myqueue.addUnvisitedUrl(new Url(link));
			}
		}
	}
	
	//main �������
	public static void main(String[]args) throws Exception
	{
		//��ʼ������ʵ��
		System.out.println("programe start...");
		MyCrawler crawler = new MyCrawler();
		//��ʼ��
		crawler.crawling();
	}

}
