package jcolibri.connector;

import weka.core.Instance;

public class WrapperInstance {
	Instance _instance;
	
	public WrapperInstance(Instance instance) {
		_instance = instance;
	}
	
	public void setWrapperInstance(Instance instance){
		_instance = instance;
	}
	
	public Instance getWrapperInstance(){
		return _instance;
	}
	
}
