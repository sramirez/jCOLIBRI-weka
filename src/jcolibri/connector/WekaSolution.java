/**
 * IrisSolution.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.connector;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.extensions.classification.ClassificationSolution;

/**
 * Bean storing the solution for the Iris data base
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class WekaSolution implements ClassificationSolution {

	Double type;
	
	public String toString()
	{
		return "" + type;
	}
	
	public Attribute getIdAttribute() {
		return new Attribute("type", this.getClass());
	}

	/**
	 * @return Returns the type.
	 */
	public Double getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(Double type) {
		this.type = type;
	}

	@Override
	public Object getClassification() {
		return type;
	}
	
}
