package edu.cornell.cs4320.hw2;
import java.util.Map.Entry;
import java.util.Set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A very simple ISAM tree that makes a few assumptions
 * 
 * - Key is always an Integer
 * - Value is always a String
 */
public class IsamTree {	
	public IsamTree(int pageSize) {
		assert(pageSize > 0);
		
		this.pageSize = pageSize;
	}
	
	/**
	 * Create initial tree from a data set
	 */
	public void create(Set<Entry<Integer,String>> entries) {
		assert(entries.size() > this.pageSize);
                
		//MAKE AN ARRAY-LIST TO STORE THE ENTRIES RECIEVED
		ArrayList<Entry<Integer,String>> initialData = new ArrayList<Entry<Integer,String>>(entries);
                
                //CALCULATION OF HEIGHT OF TREE
                /*int no_of_elements=datanode.length;
                height=(int)Math.ceil(
                        (Math.log(no_of_elements/pageSize)
                                /
                         Math.log(2))
                                /
                        (Math.log(3)/Math.log(2))
                );
                //System.out.println("height="+height);*/
                
                
                //SORT THE INITIAL DATA
               
		Collections.sort(initialData, new Comparator<Entry<Integer,String>>() {
			public int compare(Entry<Integer, String> entry1,Entry<Integer, String> entry2) {int difference;difference=entry1.getKey()-entry2.getKey();return difference;}
		});
		
		
		int noOfNodes=0;

//ARRAY-LIST TO STORE DATA NODES
		ArrayList<IsamDataNode> datanode = new ArrayList<IsamDataNode>();
                
		IsamDataNode ism=null;
		
		int count = pageSize;//to keep a track of keys
		boolean nodeDone = false;
		//FOR EACH LOOP USED FOR CREATION OF EACH DATA NODE
		for(Entry<Integer, String> entry1:initialData)
		{
			nodeDone = false;
			noOfNodes++;
			if(count == pageSize)
				 ism = new IsamDataNode(pageSize);
			count--;
                        ism.insert(entry1.getKey(), entry1.getValue());
			if(count == 0){
				count = pageSize;
				nodeDone = true;
				datanode.add(ism);
			}
		}
                
		
		if(noOfNodes == initialData.size() && nodeDone == false)
		{
			datanode.add(ism);
		}
		
		//CREATION OF INDEX NODE
		int noOfChildren = (pageSize +1);
                
             
		ArrayList<IsamIndexNode> nodes = new ArrayList<IsamIndexNode>();
		
                
                noOfNodes=0;
		IsamIndexNode indexnode = null;
		
		int num = (int) Math.pow(noOfChildren,noOfNodes)*pageSize;
		
		ArrayList<Integer> nodesNum = new ArrayList<Integer>();
                
                while(num < initialData.size())
                {
			int k = num;
			count = pageSize;
                        
			while(num<initialData.size())
                        {
			nodeDone = false;
                        
                        if(count == pageSize)
                        {
			indexnode = new IsamIndexNode(pageSize);
                        }
                        
                        count--;
			
                        indexnode.insert(initialData.get(num).getKey(),null);
				
			if(count == 0)
                        {
			count = pageSize;
		 	nodeDone = true;
			nodes.add(indexnode);
			}
				 
			num+=k;
			if(num 
                           % 
                                (
                                (
                                Math.pow(noOfChildren,noOfNodes+1)
                                
                                )*pageSize
                                
                                ) ==0 
                          ){
                        int inSize=initialData.size();
			if(inSize-num <= (
                                                 (Math.pow(noOfChildren,noOfNodes))
                                                 *
                                                 pageSize
                                         )&& inSize - num > 0)
                                         {
						 indexnode = new IsamIndexNode(pageSize);
						 indexnode.insert(null,null);
						 nodes.add(indexnode);
					 }
					 num +=k;
			 }			 
			}
			if( num >= initialData.size()  && nodeDone == false){
				nodes.add(indexnode);			
			}
			noOfNodes++;
			nodesNum.add(nodes.size());
			num = (int) Math.pow(noOfChildren,noOfNodes)*pageSize;			
		}
	
		
                
                //HEIGHT=1 : HERE WE WILL HAVE 3 LEAF NODES CONNECTED TO ROOT NODE
		int j1 =0;
                int rootIndex =0 ;
		int nodenum = (int) Math.ceil(
                        (
                                (double)datanode.size()
                                        /
                                (double)Math.pow(noOfChildren,1)
                                
                        )
             
                                               );
		int size = datanode.size();
		while(rootIndex < nodenum){
			for(int a=0;a<noOfChildren;a++){
				if(a<size){
                                    nodes.get(rootIndex).children[a] = datanode.get(j1);
					j1 ++;					
				}
				
			}
			size = size-noOfChildren;
			rootIndex++;
		}
		this.root = new IsamIndexNode(pageSize);
		root = nodes.get(nodes.size()-1);
                
                
                //HEIGHT>1
                /*Following logic applied
              
                All leaves will exist at 'height'
                we will start from level height-1
               
                so insert above number of times into the index nodes
                what will you insert?
                we have already made the data nodes.
                now starting from current=2, go on incrementing current
                before putting into the index node, check if the data node is not null
                while doing so also point the children to respective data nodes.
                
                now this level is done.
                lets go to height-2 now
                start from the next index and repeat above steps.
                this time point to the lower index nodes.
                Do this till you reach height=0
                when height=0 just assign everthing to root
                */
		
		int current = 0;
		size = nodes.size();
		int prev = rootIndex;
		int m =0,k=0;
		while(rootIndex < nodes.size())
                {
                    for(int a=0;a<noOfChildren;a++) 
                    {				 
			if(prev!=current)
                        {
			nodes.get(rootIndex).children[a] = nodes.get(current);
                        current++;
			m++;
                        }
			if(m >= nodesNum.get(k) && a==noOfChildren-1)
                        {
			k++;
			prev = rootIndex+1;
			m =0;
			}
		}
		rootIndex++;			 
				
		}
		this.root = new IsamIndexNode(pageSize);
		root = nodes.get(nodes.size()-1);
}
	
	/**
	 * Get the height of this tree
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the root of this tree
	 * Note: Should only be used internally and/or by helper classes
	 */
	protected IsamIndexNode getRoot() {
		return root;
	}
	
	/**
	 * Get a in-order string representation of the tree
	 * 
	 * Note:
	 *  - this only prints the indexes
	 *  - index nodes should be shown by curly braces
	 *  - data nodes by square brackets
	 *  - empty indexes by the letter 'E'
	 *  - and empty nodes/subtrees by ()
	 *  
	 *  See the unit tests for examples
	 */
	public String toString() {
		return root.toString();
	}
	
	/**
	 * Search for a specific entry
	 * Returns the entry if found and null otherwise
	 */
	public String search(Integer key) {
		return root.search(key);
	}
	
	/**
	 * Insert a new value
	 * This will return false if the value already exists
	 */
	public boolean insert(Integer key, String value) {
		return root.insert(key, value);
	}
	
	/**
	 * Remove the entry with the specified key
	 * This will return false if the value wasn't found
	 */
	public boolean delete(Integer key) {
		return root.delete(key);
	}
	
	/**
	 * Get the size of one page/node
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Root of the tree
	 * It is assumed that this is never a data node
	 */
	private IsamIndexNode root;

	/**
	 * Size of each node/page 
	 * This is set via the constructor
	 */
	private final int pageSize;
	
	/**
	 * The height of the tree
	 * Should be calculated by create()
	 */
	private int height;
}
