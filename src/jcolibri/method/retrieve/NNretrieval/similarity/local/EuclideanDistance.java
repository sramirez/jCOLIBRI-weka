package jcolibri.method.retrieve.NNretrieval.similarity.local;



import weka.core.Instance;
import jcolibri.connector.WekaInstanceDescription;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;


/**
 * This function returns the similarity of two enum values as the their distance
 * sim(x,y)=|ord(x) - ord(y)|
 * 
 * @author Juan A. Recio-Garc�a
 */
public class EuclideanDistance implements LocalSimilarityFunction {

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 *            StringEnum or String
	 * @param o2
	 *            StringEnum or String
	 * @return the result of apply the similarity function.
	 */
	public double compute(Object o1, Object o2) throws jcolibri.exception.NoApplicableSimilarityFunctionException{
		if ((o1 == null) || (o2 == null))
			return 0;
		if(!(o1 instanceof Instance))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if(!(o2 instanceof Instance))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		Instance e1 = (Instance)o1;
		Instance e2 = (Instance)o2;		
		
		if(e1.numAttributes() != e2.numAttributes())
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		double accum = 0;
		for(int i = 0; i < e1.numAttributes(); i++) {
			if(i != e1.classIndex()) {
				accum += Math.pow(e1.value(i) - e2.value(i), 2);
			}
		}
		
		return -Math.sqrt(accum);
	}

	/** Applicable to Enum */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof Instance;
		else if(o2==null)
			return o1 instanceof Instance;
		else
			return (o1 instanceof Instance)&&(o2 instanceof Instance);
	}

}
