/**
 * Test6.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/05/2007
 */
package jcolibri.test.test17;


import java.util.Collection;
import java.util.LinkedList;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.connector.WekaConnector;
import jcolibri.exception.ExecutionException;
import jcolibri.method.maintenance.algorithms.ICFRedundancyRemoval;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.EuclideanDistance;
import jcolibri.method.reuse.classification.KNNClassificationConfig;
import jcolibri.method.reuse.classification.MajorityVotingMethod;
import jcolibri.method.revise.classification.BasicClassificationOracle;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


/**
 * This example shows how to use the Plain Text connector.
 * Here we only read the cases and store a new one in the persistence file.
 * <p>
 * The case base (iris_data_jCOLIBRI.txt) contains information about iris:
 * <pre>
 * #Columns are: Sepal Length, Sepal Width, Petal Length, Petal Width, Type of Iris,
 * 
 * Case 1,5.1,3.5,1.4,0.2,Iris-setosa
 * Case 2,4.9,3,1.4,0.2,Iris-setosa
 * Case 3,4.7,3.2,1.3,0.2,Iris-setosa
 * ...
 * </pre>
 * 
 * These cases are mapped into the following structure:
 *  * <pre>
 * Case
 *  |
 *  +- Description
 *  |       |
 *  |       +- id *          (1)
 *  |       +- sepalLength   (2)
 *  |       +- sepalWidth    (3)
 *  |       +- petalLength   (4)
 *  |       +- petalWidth    (5)
 *  |
 *  +- Solution
 *          |
 *          +- type *        (6)
 * </pre>
 * The attributes with * are the ids of the compound objects and the numbers between parenthesis are the corresponding columns in the text file.
 * <p>
 * The mapping is configured by the <b>plaintextconfig.xml</b> file following the schema defined in PlainTextConnector:
 * <pre>
 * &lt;TextFileConfiguration&gt;
 *	&lt;FilePath&gt;jcolibri/test/test6/iris_data_jCOLIBRI.txt&lt;/FilePath&gt;
 *	&lt;Delimiters&gt;,&lt;/Delimiters&gt;
 *	&lt;DescriptionClassName&gt;jcolibri.test.test6.IrisDescription&lt;/DescriptionClassName&gt;
 *	&lt;DescriptionMappings&gt;
 *		&lt;Map&gt;sepalLength&lt;/Map&gt;
 *		&lt;Map&gt;sepalWidth&lt;/Map&gt;
 *		&lt;Map&gt;petalLength&lt;/Map&gt;
 *		&lt;Map&gt;petalWidth&lt;/Map&gt;		
 *	&lt;/DescriptionMappings&gt;
 *	&lt;SolutionClassName&gt;jcolibri.test.test6.IrisSolution&lt;/SolutionClassName&gt;
 *	&lt;SolutionMappings&gt;
 *      &lt;Map&gt;type&lt;/Map&gt;
 *	&lt;/SolutionMappings&gt;
 * &lt;/TextFileConfiguration&gt;
 * </pre>
 * First, we define the path containing the data and the characters used as delimiters (comma in this example).
 * <br>
 * Then we map each part of the case. Following the order of the columns in the text file we have to indicate to which attributes are mapped.
 * This connector only uses the id of the description. It must be the first column of each row and is not included in the mapping file
 * <br>
 * 
 * 
 * @author S. Ramírez-Gallego
 * @version 1.0
 * 
 * @see jcolibri.connector.PlainTextConnector
 */
public class Test17 implements StandardCBRApplication {

	WekaConnector _connector;
	CBRCaseBase _caseBase;
	
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#configure()
	 */
	public void configure(LinkedList<Instance> init) throws ExecutionException {
		_connector = new WekaConnector(init);
		_caseBase  = new LinealCaseBase();
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#preCycle()
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		for(CBRCase c: cases)
			System.out.println(c);
		return _caseBase;
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#cycle()
	 */
	public void cycle(CBRQuery query) throws ExecutionException {		
		// Configure KNN
		KNNClassificationConfig wekaSimConfig = new KNNClassificationConfig();
		
		wekaSimConfig.setDescriptionSimFunction(new Average());
		try {
			wekaSimConfig.addMapping(new Attribute("instance", Class.forName("jcolibri.connector.WekaInstanceDescription")), 
					new EuclideanDistance());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		wekaSimConfig.setClassificationMethod(new MajorityVotingMethod());
		wekaSimConfig.setK(3);
		

		ICFRedundancyRemoval icf = new ICFRedundancyRemoval();
		Collection<CBRCase> deleted = icf.retrieveCasesToDelete(_caseBase.getCases(), wekaSimConfig);
		System.out.println();		
		System.out.println("Num Cases deleted by Alg: " + deleted.size());
		System.out.println("Cases deleted by Alg: ");
		for(CBRCase c: deleted)
		{	System.out.println(c.getID());
		}
		
		//Collection<CBRCase> newCB = _caseBase.getCases();
		//newCB.removeAll(deleted);
		_caseBase.forgetCases(deleted);
		
		BasicClassificationOracle oracle = new BasicClassificationOracle();
		boolean isCorrect = oracle.isCorrectPrediction(query, _caseBase, wekaSimConfig);
		System.out.println("Predicted class:" + isCorrect);
		
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbraplications.StandardCBRApplication#postCycle()
	 */
	public void postCycle() throws ExecutionException {
		//_connector.close();

	}
	
	public Collection<CBRCase> getCaseBase(){
		return _caseBase.getCases();
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test17 test = new Test17();

		DataSource source;
		Instances data = null;
		try {
			source = new DataSource("/home/sramirez/software/survey_preprocessing_streaming/IS/jCOLIBRI2/src/jcolibri/test/test17/iris.arff");
			data = source.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// setting class attribute if the data format does not provide this information
		// For example, the XRFF format saves the class attribute information as well
		if (data.classIndex() == -1)
		 data.setClassIndex(data.numAttributes() - 1);
		
		LinkedList<Instance> l = new LinkedList<Instance>();
		for(int i = 0; i < data.numInstances(); i++) {
			l.add(data.get(i));
		}		 
		
		try {
			test.configure(l);
			test.preCycle();
			test.cycle(null);			
		} catch (ExecutionException e) {
			org.apache.commons.logging.LogFactory.getLog(Test17.class).error(e);
		}

	}

	@Override
	public void configure() throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

}
