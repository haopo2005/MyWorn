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
		//��ʼ��������
	    myconfig = new ConfigBean();
	}

	public void readXMLFile(String inFile) throws Exception {
	
		// ����DocumentBuilderFactoryʵ��,ָ��DocumentBuilder
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce);
			// ���쳣ʱ����쳣��Ϣ��Ȼ���˳�����ͬ
			System.exit(1);
		}
		Document doc = null;
		try {
			doc = db.parse(inFile);
		} catch (DOMException dom) {
			System.err.println(dom.getMessage());
			// ���쳣ʱ����쳣��Ϣ��Ȼ���˳�����ͬ
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(1);
		}

		// �����ǽ���XML��ȫ����
		Element root = doc.getDocumentElement();
		// ȡ����Ԫ���б�
		NodeList configs = root.getElementsByTagName("����");

		Element config = (Element) configs.item(0);

		myconfig.setDb_addr1(config.getAttribute("���ݿ�·��һ"));
		myconfig.setDb_addr2(config.getAttribute("���ݿ�·����"));
		myconfig.setInit_seed(config.getAttribute("��ʼurl��ַ"));
		myconfig.setSleep_time(Integer.parseInt(config.getAttribute("����ʱ��")));
		myconfig.setPath(config.getAttribute("ͼƬ·��"));

		NodeList filter_incs = config.getElementsByTagName("���˰���");
		for (int i = 0; i < filter_incs.getLength(); i++) 
		{
			Element incs = (Element) filter_incs.item(i);
			myconfig.filter_include_put(incs.getAttribute("url"));
		}
		
		NodeList filter_dis = config.getElementsByTagName("�����ų�");
		for (int j = 0; j < filter_dis.getLength(); j++) 
		{
			Element dis = (Element) filter_dis.item(j);
			myconfig.filter_distinct_put(dis.getAttribute("url"));
		}
		
		NodeList down_incs = config.getElementsByTagName("���ذ���");
		for (int k = 0; k < down_incs.getLength(); k++) 
		{
			Element d_incs = (Element) down_incs.item(k);
			myconfig.down_include_put(d_incs.getAttribute("url"));
		}
	}
	
	/*public static void main(String[] args) throws Exception 
	{
		//��������ʵ��
		XmlParse xmlTest = new XmlParse();
		xmlTest.readXMLFile("fuck.xml");
		System.out.println("������ϣ�");
		xmlTest.myconfig.dump_all();
	} */
}
