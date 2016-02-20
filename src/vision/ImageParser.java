package vision;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class ImageParser{


	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db;

	public ImageParser()
	{
		try
		{
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// TODO Auto-generated constructor stub
	}

	public VisionStruct parseString(String str) {

		VisionStruct vs = new VisionStruct();

		Document doc;
		try
		{
			doc = ImageParser.loadXMLFromString(str);
		
						
			NodeList allList = doc.getElementsByTagName("Vision");
			
			doc.getFirstChild();			
			NodeList transList = doc.getElementsByTagName("Translation");  

			
//			for (int i = 0; i < transList.getLength(); i++) {
//				if (transList.item(i).getParentNode().getNodeName().equals("GoalOne")) vs.goalOne[0] = Integer.valueOf(transList.item(i).getTextContent().trim());
//				if (transList.item(i).getParentNode().getNodeName().equals("GoalTwo")) vs.goalTwo[0] = Integer.valueOf(transList.item(i).getTextContent());
//				if (transList.item(i).getParentNode().getNodeName().equals("GoalThree")) vs.goalThree[0] = Integer.valueOf(transList.item(i).getTextContent());
//
//				
//				
//			}
//
//
//			NodeList rotList = doc.getElementsByTagName("Rotation");
//
//			for (int i = 0; i < rotList.getLength(); i++)
//			{
//				if (rotList.item(i).getParentNode().getNodeName().equals("GoalOne")) vs.goalOne[1] = Integer.valueOf(rotList.item(i).getTextContent());
//				if (rotList.item(i).getParentNode().getNodeName().equals("GoalTwo")) vs.goalTwo[1] = Integer.valueOf(rotList.item(i).getTextContent());
//				if (rotList.item(i).getParentNode().getNodeName().equals("GoalThree")) vs.goalThree[1] = Integer.valueOf(rotList.item(i).getTextContent());
//
//			}

		vs.goalOne = Goal.getGoal(doc.getElementsByTagName("GoalOne").item(0));
		vs.goalTwo = Goal.getGoal(doc.getElementsByTagName("GoalTwo").item(0));
		vs.goalThree = Goal.getGoal(doc.getElementsByTagName("GoalThree").item(0));

		
		
		//vs.frameNumber =  Integer.valueOf(doc.getElementsByTagName("FrameInfo").item(0).getAttributes().item(0).getTextContent().trim());
		}

		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vs;
	} 



public static Document loadXMLFromString(String xml) throws Exception
{
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	InputSource is = new InputSource(new StringReader(xml));
	return builder.parse(is);
}
}