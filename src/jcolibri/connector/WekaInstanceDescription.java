/**
 * WekaInstanceDescription.java
 * jCOLIBRI2 framework. 
 * @author Sergio Ramírez-Gallego
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.connector;

import weka.core.Instance;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

/**
 * Bean storing the description for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class WekaInstanceDescription implements CaseComponent {

	Instance instance;
	String id;
	
	public String toString()
	{
		return id+", "+instance;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("id", this.getClass());
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public Instance getInstance(){
		return instance;
	}
	
	public void setInstance(Instance inst){
		instance = inst;
	}
	
}
