package org.athrun.test.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
		System.out.println(getProductPkgName("AndroidManifest.xml"));
		//modifyTargetPackage("AndroidManifest.xml", "com.taobao.juhuasuan");
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
    
    public String getRootAttrValueByAttrName(Document document, String attributeName)  
    {  
        Element root = document.getRootElement();   
        return root.attributeValue(attributeName);
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
