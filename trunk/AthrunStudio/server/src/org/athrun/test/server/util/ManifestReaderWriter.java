package org.athrun.test.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ManifestReaderWriter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		ModifyAndroidManifest modifier = new ModifyAndroidManifest();
		
		try {
			Document document = modifier.getDocument("AndroidManifest.xml");
			
			modifier.updateElementByName(document, "instrumentation", "targetPackage", "com.taobao.taohua");
			
			modifier.writeXml(document, "newAndroidManifest.xml");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//System.out.println(getProductPkgName("AndroidManifest.xml"));
		//modifyTargetPackage("AndroidManifest.xml", "com.taobao.juhuasuan");
		
		AppActivityContainer aac = getActivityEntities("AndroidManifest.xml");
		if (aac != null) {
			System.out.println("aac is not null!!");
		}
	}
	
	public static String getProductPkgName(String manifestFilePath) {
		ManifestReaderWriter reader = new ManifestReaderWriter();
		
		try {
			Document document = reader.getDocument(manifestFilePath);
			
			return reader.getRootAttrValueByAttrName(document, "package");
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getAppName(String manifestFilePath) {
		ManifestReaderWriter reader = new ManifestReaderWriter();
		
		try {
			Document document = reader.getDocument(manifestFilePath);
			
			return reader.getAppAttrValue(document, "label");
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getAppIconName(String manifestFilePath) {
		ManifestReaderWriter reader = new ManifestReaderWriter();
		
		try {
			Document document = reader.getDocument(manifestFilePath);
			
			return reader.getAppAttrValue(document, "icon");
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getRealAppName(String resRootDir, String appName) {
		if (appName.startsWith("@string/")) {
			String key = appName.substring(8);
			String stringsXmlPath = resRootDir + File.separator + "res" + File.separator + "values" + File.separator + "strings.xml";
			
			ManifestReaderWriter modifier = new ManifestReaderWriter();
			
			try {
				Document document = modifier.getDocument(stringsXmlPath);
				
				return modifier.getStrValue(document, key);

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return "";
	}
	
	public static String getRealIconPath(String resRootDir, String appIconName) {
		if (appIconName.startsWith("@drawable/")) {
			String key = appIconName.substring(10);
			String iconPath = resRootDir + File.separator + "res" + File.separator + "drawable" + File.separator + key + ".png";
			return iconPath;
		}
		
		return "";
	}
	
	
	public static void modifyTargetPackage(String manifestFilePath, String targetPackage) {
		ManifestReaderWriter modifier = new ManifestReaderWriter();
		
		try {
			Document document = modifier.getDocument(manifestFilePath);
			
			modifier.updateElementByName(document, "instrumentation", "targetPackage", targetPackage);
			
			modifier.writeXml(document, manifestFilePath);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static AppActivityContainer getActivityEntities(String manifestFilePath) {
		ManifestReaderWriter modifier = new ManifestReaderWriter();
		
		try {
			Document document = modifier.getDocument(manifestFilePath);
			
			return modifier.getActivityEntities(document);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** 
     * ��ȡXML �ļ�. 
     * @param filePath 
     * @return 
     * @throws DocumentException 
     */  
    private Document getDocument(String filePath) throws DocumentException  
    {  
        File file = new File(filePath);  
        SAXReader reader = new SAXReader();  
          
        return reader.read(file);  
    } 
    
    public String getAppAttrValue(Document document, String appAttrName) {
    	Element root = document.getRootElement();
    	Element appNode = root.element("application");
    	return appNode.attributeValue(appAttrName);
    }
    
    public String getStrValue(Document document, String strKey) {
    	Element root = document.getRootElement();
    	List children = root.elements("string");
    	
    	for(Iterator iter = children.iterator(); iter.hasNext();) {
    		Element child = (Element)iter.next();
    		String name = child.attributeValue("name");
    		if ((name != null)&&(name.equals(strKey))) {
    			return child.getText();
    		} else {
    			continue;
    		}
    	}
    	
    	return "";
    }
    
    public String getRootAttrValueByAttrName(Document document, String attributeName)  
    {  
        Element root = document.getRootElement();   
        return root.attributeValue(attributeName);
    }
    
    private AppActivityContainer getActivityEntities(Document document) {
    	AppActivityContainer aac = new AppActivityContainer();
    	Element root = document.getRootElement();
    	Element appNode = root.element("application");
    	List children = appNode.elements();
    	for(Iterator iter = children.iterator(); iter.hasNext();) {
    		Element child = (Element)iter.next();
    		if (child.getName().equals("activity")) {
    			String activityName = child.attributeValue("name");
    			String launchMode = child.attributeValue("launchMode");
    			System.out.println("activityName: " + activityName + ", launchMode: " + launchMode);
    			aac.addActivityEntity(new ActivityEntity(activityName, launchMode));
    		}
    	}
    	
    	return aac;
    }
      
    /** 
     * ���½ڵ������ֵ. 
     * @param document 
     * @param elementName 
     * @param attributeName 
     * @param newValue 
     */  
    @SuppressWarnings("deprecation")  
    private void updateElementByName(Document document, String elementName, String attributeName, String newValue)  
    {  
        Element root = document.getRootElement();   
        Element updatedNode = root.element(elementName); //parent element.  
          
        updatedNode.setAttributeValue(attributeName, newValue);  
    }  
    
    /** 
     * ����ļ�. 
     * @param document 
     * @param filePath 
     * @throws IOException 
     */  
    private void writeXml(Document document, String filePath) throws IOException  
    {  
        File file = new File(filePath);  
        XMLWriter writer = null;  
        try {  
            if(file.exists())  
            {  
                file.delete();  
            }  
            writer = new XMLWriter(new FileOutputStream(file));  
            writer.write(document);  
            writer.close();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            if(writer != null)  
            {  
                writer.close();  
            }  
        }  
    }  

}
