import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlParse {
	public ConfigBean myconfig;
	
	public XmlParse()
	{
		//初始化配置类
	    myconfig = new ConfigBean();
	}

	public void readXMLFile(String inFile) throws Exception {
	
		// 创建DocumentBuilderFactory实例,指定DocumentBuilder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce);
			// 出异常时输出异常信息，然后退出，下同
			System.exit(1);
		}
		Document doc = null;
		try {
			doc = db.parse(inFile);
		} catch (DOMException dom) {
			System.err.println(dom.getMessage());
			// 出异常时输出异常信息，然后退出，下同
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(1);
		}

		// 下面是解析XML的全过程
		Element root = doc.getDocumentElement();
		// 取配置元素列表
		NodeList configs = root.getElementsByTagName("配置");

		Element config = (Element) configs.item(0);

		myconfig.setDb_addr1(config.getAttribute("数据库路径一"));
		myconfig.setDb_addr2(config.getAttribute("数据库路径二"));
		myconfig.setInit_seed(config.getAttribute("初始url地址"));
		myconfig.setSleep_time(Integer.parseInt(config.getAttribute("休眠时间")));
		myconfig.setPath(config.getAttribute("图片路径"));

		NodeList filter_incs = config.getElementsByTagName("过滤包含");
		for (int i = 0; i < filter_incs.getLength(); i++) 
		{
			Element incs = (Element) filter_incs.item(i);
			myconfig.filter_include_put(incs.getAttribute("url"));
		}
		
		NodeList filter_dis = config.getElementsByTagName("过滤排出");
		for (int j = 0; j < filter_dis.getLength(); j++) 
		{
			Element dis = (Element) filter_dis.item(j);
			myconfig.filter_distinct_put(dis.getAttribute("url"));
		}
		
		NodeList down_incs = config.getElementsByTagName("下载包含");
		for (int k = 0; k < down_incs.getLength(); k++) 
		{
			Element d_incs = (Element) down_incs.item(k);
			myconfig.down_include_put(d_incs.getAttribute("url"));
		}
	}
	
	/*public static void main(String[] args) throws Exception 
	{
		//建立测试实例
		XmlParse xmlTest = new XmlParse();
		xmlTest.readXMLFile("fuck.xml");
		System.out.println("读入完毕！");
		xmlTest.myconfig.dump_all();
	} */
}
