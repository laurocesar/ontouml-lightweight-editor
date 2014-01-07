package br.ufes.inf.nemo.antipattern;

import java.util.ArrayList;

public class Fix {

	private ArrayList<Object> deletedElements = new ArrayList<Object>();
	private ArrayList<Object> addedElements = new ArrayList<Object>();
	private ArrayList<Object> modifiedElements = new ArrayList<Object>();
	private ArrayList<String> addedRules = new ArrayList<String>();
	
	public Fix(){}
	
	// Include All and Exclude All methods...
	
	public void includeAllModified(ArrayList<Object> modified){
		for(Object obj: modified) includeModified(obj);
	}
	public void includeAllRule(ArrayList<String> rules){
		for(String obj: rules) includeModified(obj);
	}	
	public void includeAllAdded(ArrayList<Object> added){
		for(Object obj: added) includeAdded(obj);
	}
	public void includeAllDeleted(ArrayList<Object> deleted){
		for(Object obj: deleted) includeDeleted(obj);
	}
	public void excludeAllRule(ArrayList<String> rules){
		for(String obj: rules) excludeRule(obj);
	}
	public void excludeAllModified(ArrayList<Object> modified){
		for(Object obj: modified) excludeModified(obj);
	}
	public void excludeAllAdded(ArrayList<Object> added){
		for(Object obj: added) excludeAdded(obj);
	}
	public void excludeAllDeleted(ArrayList<Object> deleted){
		for(Object obj: deleted) excludeDeleted(obj);
	}
	
	// Include and exclude methods...
	
	public void includeModified(Object modified){
		if (!modifiedElements.contains(modified)) modifiedElements.add(modified);
	}
	public void includeRule(String rule){
		if (!addedRules.contains(rule)) addedRules.add(rule);
	}	
	public void includeAdded(Object added){
		if (!addedElements.contains(added)) addedElements.add(added);
	}
	public void includeDeleted(Object deleted){
		if (!deletedElements.contains(deleted)) deletedElements.add(deleted);
	}
	public void excludeRule(String rule){
		addedRules.remove(rule);
	}
	public void excludeModified(Object modified){
		modifiedElements.remove(modified);
	}
	public void excludeAdded(Object added){
		addedElements.remove(added);
	}
	public void excludeDeleted(Object deleted){
		deletedElements.remove(deleted);	
	}
	
	// Getters and Setters...
	
	public ArrayList<Object> getDeleted() 
	{
		return deletedElements;
	}
	public void setDeleted(ArrayList<Object> deletedElements) 
	{
		this.deletedElements = deletedElements;
	}
	public ArrayList<Object> getAdded() 
	{
		return addedElements;
	}
	public void setAdded(ArrayList<Object> addedElements) 
	{
		this.addedElements = addedElements;
	}
	public ArrayList<Object> getModified() 
	{
		return modifiedElements;
	}
	public void setModified(ArrayList<Object> modifiedElements) 
	{
		this.modifiedElements = modifiedElements;
	}
	public ArrayList<String> getAddedRules() 
	{
		return addedRules;
	}
	public void setAddedRules(ArrayList<String> addedRules) 
	{
		this.addedRules = addedRules;
	}
}