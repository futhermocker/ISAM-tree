package edu.cornell.cs4320.hw2;

public class IsamDataNode extends IsamNode {
	protected final Integer[] keys = new Integer[getSize()];
	protected final String[] values = new String[getSize()];

	private IsamDataNode successor = null;

	IsamDataNode(int size) {
		super(size);
	}

	@Override
	public String search(Integer key) {
		int i = 0;
		while (i < getSize() && keys[i] != null && key > keys[i])
                        i++;	//go till the base first
		if(i != getSize() && keys[i] == key)
                        return values[i];//see if value found
			
                boolean isOver=hasOverflow();
		if(isOver) //if overflow pages exist search in them
                {
			successor = getOverflowPage();
			return (successor.search(key));
		}
		return null;

	}

	public boolean hasOverflow() {
		return successor != null;
	}

	public IsamDataNode getOverflowPage() {
		return successor;
	}

	@Override
	public boolean insert(Integer key, String value) {
		
            
                boolean flag = false;
                
		if(this.search(key)!=null)//first see if the node is already present. if its already present return false and exit
			return false;
		
		boolean find=false;
			
			
		for(int i = 0; i < getSize(); i++) 
                {
			if(this.keys[i]==key)//key already present..false
                        {
				find = true;
				break;
			}
			if(this.keys[i]==null && this.keys[i]!=key) 
                        {
				flag = true;//indicate that we are entering key into node.. success
                                
                                this.keys[i] = key;
				this.values[i] = value;
				
                                break;
			}
		}

		if (flag)//key inserted
		return true;
		
		if(!flag)//key not inserted now go into overflow
                {
			if(find)//element already present
			return false;
			
			if(!hasOverflow())//element to be inserted in successor as out of space
			successor = new IsamDataNode(getSize());
			return (successor.insert(key, value));
		}

		return false;//if overflow also not present/or returns false, finally return false
	}

	@Override
	public String toString() {
		String data = "[ ";

		for (int i = 0; i < getSize(); ++i) {
			Integer key = keys[i];

			if (key == null) {
				data += "E ";
			} else {
				data += keys[i] + " ";
			}
		}

		if (hasOverflow()) {
			return data + successor.toString() + " ]";
		} else {
			return data + "]";
		}
	}

	@Override
	public boolean delete(Integer key) {
		
                int num = 0;
                boolean flag = false;
		for(int i = 0; i < getSize(); i++)//traverse nodes to find key
                {
			if(this.keys[i] == key)
                        {
                                flag = true;//mark as found
				
                                this.keys[i] = null;//delete that node 
				this.values[i] = null;			
				
                                break;
			}
		}

		if(flag)//if key found and deleted then success
                    return true;
		
		if(!flag && hasOverflow())//not found.. now search in overflow nodes
                {
                  
//recursively search and delete if found in overflow pages	
			flag = successor.delete(key);
				
		if(flag)
                {
//kind of garbage collection, dealloacate any successor pages which are empty
		for(int i = 0; i < getSize(); i++)
                    if (this.successor.keys[i] == null) 
                    {num++;}
		
		if(num == getSize())
                {if (successor.hasOverflow())
                    this.successor = successor.successor;//merge if not empty
		else
                    this.successor = null;//dealloate if empty			
                }
		return true;
		}
		}
		return false;		
	}
}
