package jcolibri.connector;

import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.cbrcore.Connector;
import jcolibri.exception.InitializingException;
import weka.core.Instance;

/**
 * <p>
 * Implements a generic PlainText Connector.
 * </p>
 * It manages the persistence of the cases automatically into textual files. Features:
 * <ul>
 * <li>By default it only can manage a few data types, although developers can add
 * their own ones implementing the TypeAdaptor interface.<br>
 * Supported types and the type extension mechanism is explained in PlainTextTypeConverter.
 * <li>Only works with one file.
 * </ul>
 * <p>
 * This connector uses the property in the initFromXMLfile() parameter to obtain the
 * configuration file. This file is a xml that follows the Schema defined in
 * <a href="PlainTextConnector.xsd">/doc/configfilesSchemas/PlainTextConnector.xsd</a>:<p>
 * <img src="PlainTextConnectorSchema.jpg">
 * <p>
 * This class does not implement any cache mechanims, so cases are read and
 * written directly. This can be very inefficient in some operations (mainly in
 * reading)
 * <p>
 * Some methods will fail when executing the connector with a case base file inside a jar file.
 * The retrieve() methods will work properly but the methods that write in the file will fail. 
 * Extract the file to the file system and run the connector with that location to solve these problems.
 * <p>
 * For an example see Test6.
 * 
 * @author Sergio Ramírez-Gallego
 * @version 2.0
 * @see jcolibri.connector.plaintextutils.PlainTextTypeConverter
 * @see jcolibri.connector.TypeAdaptor
 * @see jcolibri.test.test6.Test6
 */
public class WekaConnector implements Connector {

    
	public Class descriptionClass;
	public Class solutionClass;
	private List<Instance> _init;
	
	public WekaConnector(List<Instance> init) throws InitializingException {
		_init = init;
		try{
			this.descriptionClass = Class.forName("jcolibri.connector.WekaInstanceDescription");
			this.solutionClass =  Class.forName("jcolibri.connector.WekaSolution");
		}catch(Exception e){
			throw new InitializingException(e);
		}
	}
	

	public void initFromXMLfile(URL file) throws InitializingException {	
		// TODO Auto-generated method stub
	}
	

	public void close() {
		//does nothing
	}


	/**
	 * Retrieves all cases from the text file. It maps data types using the
	 * PlainTextTypeConverter class.
	 * 
	 * @return Retrieved cases.
	 */
	public Collection<CBRCase> retrieveAllCases() {
		LinkedList<CBRCase> cases = new LinkedList<CBRCase>();
		try {

			Iterator<Instance> it = _init.iterator();
			int nelem = 0;
 			while (it.hasNext()) {
 				Instance inst = it.next();
				
				CBRCase _case = new CBRCase();
				
				CaseComponent description = (CaseComponent)this.descriptionClass.newInstance();
				Attribute idAttribute = description.getIdAttribute();
				idAttribute.setValue(description, nelem + "");
				Attribute att = new Attribute("instance", this.descriptionClass);
				att.setValue(description, inst);
				/*for(int i = 0; i < inst.numAttributes(); i++)
				{
					Attribute att = new Attribute("att" + i, Class.forName("java.lang.Double"));
					att.setValue(description, new Double(inst.value(i)));
				}*/
				_case.setDescription(description);
				
				CaseComponent solution = (CaseComponent)this.solutionClass.newInstance();
				att = new Attribute("type", this.solutionClass);
				att.setValue(solution, inst.classValue());
				_case.setSolution(solution);
				
				cases.add(_case);
				nelem++;
			}
		} catch (Exception e) {
			org.apache.commons.logging.LogFactory.getLog(this.getClass()).error(
					"Error retrieving cases " + e.getMessage());
		}
		return cases;
	}
	
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void storeCases(Collection<CBRCase> cases) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteCases(Collection<CBRCase> cases) {
		// TODO Auto-generated method stub
		
	}



}