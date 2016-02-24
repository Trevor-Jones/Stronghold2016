package vision;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLParser {
	VisionStruct vs = new VisionStruct();
	
	public VisionStruct parseString(String xml) {
		try {
			Document doc = stringToDoc(xml);
			doc.getDocumentElement().normalize();
					
			NodeList goalList = doc.getElementsByTagName("goal");
			
			for (int i = 0; i < goalList.getLength(); i++) {	
				Node goalNode = goalList.item(i);
	
				Element goalElement = (Element) goalNode;
				
				vs.goals[i].translation = Integer.parseInt(goalElement.getAttribute("translation"));
				vs.goals[i].rotation = Integer.parseInt(goalElement.getAttribute("rotation"));
				vs.goals[i].distance = Integer.parseInt(goalElement.getAttribute("distance"));
				vs.goals[i].area = Integer.parseInt(goalElement.getAttribute("area"));
			
		    }	
			vs.frameNumber = Integer.parseInt(((Element)doc.getElementsByTagName("vision").item(0)).getAttribute("frameNumber"));
			
		} catch (Exception e) {
			e.printStackTrace();
	    }
		
		return vs;
	}
		
	public static Document stringToDoc(String xml) throws Exception {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}
