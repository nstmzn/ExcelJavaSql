package com.app.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.app.commons.AppConstants;
import com.app.commons.ResponseVO;
import com.app.main.TaskVO;

public class XMLTaskReader {

	private final Logger log = Logger.getLogger(getClass().getName());

	
	public static void main(String[] args)throws Exception {
		System.out.println("starting");
		
		XMLTaskReader o = new XMLTaskReader();

		List<TaskVO> list = o.read(AppConstants.appXmlPath);
		
		for(TaskVO taskVO : list){
			o.log.debug(taskVO);
		}
		
		System.out.println("end");
	}

	public List<TaskVO> read(String xmlPath) throws Exception{
		List<TaskVO> list = new ArrayList<TaskVO>();
		try {
			File file = new File(xmlPath);
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			String rootEle = doc.getDocumentElement().getNodeName();

			log.info("----------------------------");
			log.info("Root element- " + rootEle);
			log.info("----------------------------");

			NodeList taskList = doc.getElementsByTagName("task");

			log.info("Number of tasks configured in "+file.getName()+" are:- "+taskList.getLength());
			
			for (int i = 0; i < taskList.getLength(); i++) {
				Node node = taskList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					if (node.getNodeName().equalsIgnoreCase("task")) {
						
						String taskName = getAttributeByTagName(node, "task", "name");
						String taskOwner = getAttributeByTagName(node, "task", "owner");
						String taskStatus = getAttributeByTagName(node, "task", "status");
						
						log.info("----------------------------");
						log.info("Task Name:- "+ taskName);
						log.info("Task owner:- "+ taskOwner);
						log.info("Task status:- "+ taskStatus);
						log.info("------------");
						
						if(taskStatus != null && taskStatus.equalsIgnoreCase(AppConstants.TASK_STATUS_ACTIVE)){
							TaskVO taskVO = readXMLData(node);
							
							taskVO.setTaskName(taskName);
							taskVO.setTaskOwner(taskOwner);
							taskVO.setTaskStatus(taskStatus);
							
							list.add(taskVO);//--add all tasks
							
						}else{
							log.error("Tasks satus expected:- "+AppConstants.TASK_STATUS_ACTIVE );
							log.error("Task "+taskName +" is not active and hence is not going to be processed. status "+taskStatus);
						}
					
					}
				}
			}

			log.info("----------------------------");
			log.info("end getting task from- " + rootEle);
			log.info("----------------------------");
			
		} catch (Exception e) {
			log.error(e);

			if(log.isDebugEnabled()){
				for(StackTraceElement st : e.getStackTrace()){
					log.debug(st.toString());
				}
			}
			
			throw e;
		}
		
		return list;
	}

	private TaskVO readXMLData(Node pn) {

		TaskVO vo = new TaskVO();
		NodeList nl1, nl2;
		nl1 = pn.getChildNodes();
		Node n1, node;

		for (int i = 0; i < nl1.getLength(); i++) {

			n1 = nl1.item(i);

			if (n1.getNodeType() == Node.ELEMENT_NODE) {
//				log.debug(n1.getNodeName().trim()); 

				if (n1.getNodeName().equalsIgnoreCase("insert")) {

					nl2 = n1.getChildNodes();
					for (int j = 0; j < nl2.getLength(); j++) {

						node = nl2.item(j);
//						log.debug("---@!@--- " + node.getNodeName()); 

						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element parentElement = (Element) node;
							
							if (node.getNodeName().equalsIgnoreCase("incoming-excel")) {
//								vo.setExcelFilePath(getChildElementValueByTagName(parentElement, "file-path"));
								vo.setExcelColumns(getChildElementValueByTagName(parentElement, "columns"));

							}else if (node.getNodeName().equalsIgnoreCase("database")) {

								vo.setConnection4db(getChildElementValueByTagName(parentElement, "connection4db"));
								vo.setDbTableName(getChildElementValueByTagName(parentElement, "table-name"));
								vo.setDbTableColumns(getChildElementValueByTagName(parentElement, "columns"));
								
//								vo.setDbDatatype(getChildElementValueByTagName(parentElement, "data-type"));

							}else if (node.getNodeName().equalsIgnoreCase("post-action")) {
								vo.setActionName(getChildElementValueByTagName(parentElement, "action"));
								vo.setActionPath(getChildElementAttrByTagName(parentElement, "action", "path"));
								vo.setActionSuffix(getChildElementAttrByTagName(parentElement, "action", "suffix"));

							}else if (node.getNodeName().equalsIgnoreCase("mail")) {
								vo.setMailTo(getChildElementValueByTagName(parentElement, "to"));
								vo.setMailCc(getChildElementValueByTagName(parentElement, "cc"));
								vo.setMailBcc(getChildElementValueByTagName(parentElement, "bcc"));
								vo.setMailSubject(getChildElementValueByTagName(parentElement, "subject"));
								vo.setMailSuccessBody(getChildElementValueByTagName(parentElement, "success-body"));
								vo.setMailFailureBody(getChildElementValueByTagName(parentElement, "failure-body"));

							}

						} // end-if
					} // end-for
				} // end-if

			}
		}

		return vo;
	}

	private String getChildElementValueByTagName(Element parentEle, String childTagName) {
		try {
			return parentEle.getElementsByTagName(childTagName).item(0).getTextContent().trim();

		} catch (Exception excep) {
			log.error("\tEXCEPTION: within element:- " + parentEle + "  ,tagname:- " + childTagName);
			log.error("\t\tEXCEP-MSG:- "+excep.getMessage());
			
		}
		return null;
	}

	private String getChildElementAttrByTagName(Element parentEle, String childTagName, String attrName) {
		try {
			return parentEle
					.getElementsByTagName(childTagName)
					.item(0)
					.getAttributes()
					.getNamedItem(attrName)
					.getTextContent()
					.trim();

		} catch (Exception excep) {
			log.error(
					"\tEXCEPTION: within element:- " 
					+ parentEle 
					+ "  ,tagname:- " 
					+ childTagName 
					+ " ,attrName:- " 
					+ attrName);
			
			log.error("\t\tEXCEP-MSG:- "+excep.getMessage());
		}
		return null;
	}
	
	private String getAttributeByTagName(Node node, String tagName, String attrName) {
		try {
			return node.getAttributes().getNamedItem(attrName).getTextContent().trim();

		} catch (Exception excep) {
			log.error("\tEXCEPTION: within node:- " + node + "  ,tagname:- " + tagName + " ,attrName:- " + attrName);
			log.error("\t\tEXCEP-MSG:- "+excep.getMessage());
		}
		return null;
	}

}
