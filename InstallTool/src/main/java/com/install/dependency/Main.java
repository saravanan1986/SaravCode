package com.install.dependency;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.Queue;

/**
 * The entry point for the Test program
 */
public class Main {
	ComponentGraph graph = null;
    public static void main(String[] args) {

        //read input from stdin
        Scanner scan = new Scanner(System.in);
        Main main = new Main();
        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }
            printInputLine(line);
            //the END command to stop the program
            if ("END".equals(line)) {
                break;
            }

            //Add depency to an eisting component or new component
            if(line.trim().split(" ")[0].equals("DEPEND")){
            	main.depend(line);
            }
            
           if(line.trim().split(" ")[0].equals("INSTALL")){
            	main.install(line.trim().split(" ")[1]);
            }
           
           if(line.trim().split(" ")[0].equals("REMOVE")){
        	   main.remove(line.trim().split(" ")[1]); 
            }
           
           if(("LIST").equals(line)){
        	   main.listIntalledCopmponents();
            }
        }
    }
    
    public void depend(String line){
    	if(graph == null){
    		graph = new ComponentGraph();
    	}
    	String[] depencyinfo = line.trim().split(" ");
    	graph.addDepency(depencyinfo[1], Arrays.asList(Arrays.copyOfRange(depencyinfo, 2, depencyinfo.length)));
    }
    
    public void install(String component){
    	 Deque<String> stack = new ArrayDeque<String>();
         Set<String> visited = new HashSet<String>();
         try{
          if(graph.getInstalledComponents().contains(component)){
        	  System.out.println(component+ " is already installed"); 
          }
          installDepency(component, stack, visited);
         } catch(Exception exception){
        	 exception.printStackTrace();
         }
         while(!stack.isEmpty()){
        	 System.out.println("Installing "+stack.pop());
         }
    }
    
	private void installDepency(String component, Deque<String> stack, Set<String> checkedComponent){
    	HashSet<String> components = graph.getAllComponents();
    	if(graph.getInstalledComponents().contains(component)){
    		return;
    	}
        if(components.contains(component)){
        	checkedComponent.add(component);
       	 	List<String> depencyList = graph.getDepencyComponents(component);
       	 	if(depencyList == null || depencyList.size() == 0){
       	 		//when there is no dependency for a component, add the component graph, to used later when needed
       	 		graph.addDepency(component, null);
       	 		stack.offerLast(component);
       	 		graph.addInstalledComponent(component);
       	 		return;
       	 	}
       	 	for(String dependensOnComp : depencyList){
            	if(checkedComponent.contains(dependensOnComp)){
                    continue;
                }
            	installDepency(dependensOnComp,stack,checkedComponent);
            }
            stack.offerLast(component);
            graph.addInstalledComponent(component);
        } else{
        	stack.offerLast(component);
        	graph.addInstalledComponent(component);
        	graph.addDepency(component, null);
        	
        }
    }
	
	public void remove(String component){
		List<String> dependencyList = graph.getDepencyComponents(component);
		if(dependencyList == null){
			System.out.println(component+" is not installed");
			return;
		}
		List<String> tmpList = new ArrayList<>(dependencyList);
		removeUtil(component, true); 
		for(String dependencyComp : tmpList){
			removeUtil(dependencyComp,false); 
		}
	}
	
	public boolean removeUtil(String component, boolean isDirectremove) {
		HashSet<String> components = graph.getAllComponents();
		Iterator<String> itr = components.iterator();
		boolean status = true;
		while (itr.hasNext()) {
			List<String> dependencies = graph.getDepencyComponents(itr.next());
			if (dependencies != null) {
				for (String indvdependComp : dependencies) {
					if (indvdependComp.equalsIgnoreCase(component)) {
						if(isDirectremove){
							System.out.println(component + " is still needed");
						}
						status = false;
						return status;
					}
				}
			}
		}
		if(status){
			graph.removeComponent(component);
			System.out.println("Removing "+component);
		}
		return status;
	}
		
    
    public void listIntalledCopmponents(){
    	for(String installedComp : graph.getInstalledComponents()){
    		System.out.println(installedComp);
    	}
    }
    
    public static void printInputLine(String line){
    	System.out.println(line);
    }
}