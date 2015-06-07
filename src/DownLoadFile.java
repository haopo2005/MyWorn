

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

public class DownLoadFile extends Thread{
	private String url;
	
	public DownLoadFile(String url)
	{
		this.url = url;
	}

	public void run()
    {
		boolean value = true;
		File dir0 = new File(MyCrawler.path);
        if(!dir0.exists()){
            dir0.mkdir();
        }
         
		try {
	        Parser myParser = new Parser(url);
	        // ���ñ���
	        myParser.setEncoding("gbk");
	        
	        //��������
	        NodeFilter title_filter = new TagNameFilter("title");
	        NodeList nodelist1 = myParser.extractAllNodesThatMatch(title_filter);//���˵÷��Ϲ���Ҫ��Ľڵ��LIST  
            Node node_title = nodelist1.elementAt(0);//ȡ�ڵ�  
            StringBuffer buftitle = new StringBuffer();  
            if(node_title == null){//�ж��Ƿ�Ϊ��  
                buftitle.append("");  
            }  
            else{  
                buftitle.append(node_title.toPlainTextString());//�ѽڵ�����ı��ڵ�ת��ΪString �ӵ�buftitle��  
            }  
            String title = buftitle.toString();//ת��ΪString 
            title = title.replaceAll("[/////////://*//?//\"//<//>//|]", " ");
            myParser.reset();//����
            
            //����ͼƬ����
	        NodeFilter img_filter = new TagNameFilter("img");
	        NodeList nodeList = myParser.extractAllNodesThatMatch(img_filter);
	        for (int i = 1; i < nodeList.size(); i++) {
	        	value = false;
	            ImageTag imgtag = (ImageTag) nodeList.elementAt(i);
	            String imageUrl = imgtag.getImageURL();
	            
	            for(String temp1:MyCrawler.down_include)
				{
					if(!imageUrl.contains(temp1))
					{
						value = true;
						break;
					}
				}
	            
	            if(value)
	            {
	             	System.out.println("invalid image url filterd:"+imageUrl);
	             	continue;
	            }
	            
	            //����ͼƬ
	            URL url_d = new URL(imageUrl);
	            URLConnection uc = url_d.openConnection();
	            InputStream is = uc.getInputStream();
	            String picName = imageUrl.split("/")[imageUrl.split("/").length - 1];
	            //picName = hehe + picName;
	            File dir = new File(MyCrawler.path + title +"\\");
	            if(!dir.exists()){
	                dir.mkdir();
	            }
	            File imageFile = new File(MyCrawler.path + title +"\\" + picName);
	            System.out.println(MyCrawler.path + title +"\\" + picName);
	            FileOutputStream out = new FileOutputStream(imageFile);
	            int j = 0;
	            while ((j = is.read()) != -1) {
	                out.write(j);
	            }
	            is.close();
	            out.close();
	            
	            System.out.println(imageUrl+"-"+"-�������");
	        }
	    } catch (Exception e) {
	        System.out.println("����������");
	    }
    }
}
