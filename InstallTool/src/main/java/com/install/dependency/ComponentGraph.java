package com.install.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ComponentGraph {

	private Map<String,List<String>> dependencyMap =  null;
	private final HashSet<String> componentList = new HashSet<String>();
	private final List<String> installedList = new ArrayList<String>();
	public ComponentGraph(){
		dependencyMap = new HashMap<String,List<String>>();
	}
	
	public boolean addDepency(String component, List<String> dependentComponent){
		if(dependentComponent == null){
			dependencyMap.put(component, new ArrayList<String>());
			return true;
		}
		for(String indvdependComp : dependentComponent){
			if(dependencyMap.containsKey(indvdependComp)){
				for(String transitivedepend : dependencyMap.get(indvdependComp)){
					if(transitivedepend.equalsIgnoreCase(component)){
						System.out.println(indvdependComp+" depends on "+component+", ignoring command");
						return false;
					}
				}
			}
		}
		if(dependencyMap.containsKey(component)){
			dependencyMap.get(dependencyMap).addAll(dependentComponent);
			
		}else{
			dependencyMap.put(component, dependentComponent);
			componentList.add(component);
		}
		return true;
	}
	
	public HashSet<String> getAllComponents(){
		return componentList;
	}
	
	public List<String> getDepencyComponents(String component){
		if(component == null || component.length() == 0){
			return null;
		}
		return dependencyMap.get(component);
	}
	
	public void addInstalledComponent(String component){
		installedList.add(component);
	}
	
	public List<String> getInstalledComponents(){
		return installedList;
	}
	
	public void removeComponent(String component){
		dependencyMap.remove(component);
		installedList.remove(component);
		installedList.remove(component);
	}
}
