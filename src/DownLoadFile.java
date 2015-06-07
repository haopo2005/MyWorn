

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
	        // 设置编码
	        myParser.setEncoding("gbk");
	        
	        //解析标题
	        NodeFilter title_filter = new TagNameFilter("title");
	        NodeList nodelist1 = myParser.extractAllNodesThatMatch(title_filter);//过滤得符合过滤要求的节点的LIST  
            Node node_title = nodelist1.elementAt(0);//取节点  
            StringBuffer buftitle = new StringBuffer();  
            if(node_title == null){//判断是否为空  
                buftitle.append("");  
            }  
            else{  
                buftitle.append(node_title.toPlainTextString());//把节点里的文本节点转化为String 加到buftitle上  
            }  
            String title = buftitle.toString();//转化为String 
            title = title.replaceAll("[/////////://*//?//\"//<//>//|]", " ");
            myParser.reset();//重置
            
            //解析图片连接
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
	            
	            //下载图片
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
	            
	            System.out.println(imageUrl+"-"+"-下载完成");
	        }
	    } catch (Exception e) {
	        System.out.println("错误，无内容");
	    }
    }
}
