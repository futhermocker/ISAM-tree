package edu.cornell.cs4320.hw2;

public class IsamIndexNode extends IsamNode {
	protected final Integer[] keys = new Integer[getSize()];
	protected final IsamNode[] children = new IsamNode[getSize() + 1];

	IsamIndexNode(int size) {
		super(size);
	}

	public Integer getIndex(int pos) {
		return keys[pos];
	}

	@Override
	public String search(Integer key) {
		
		int index = 0;
                /*EVERYTIME WHILE TRAVERSING DOWN THE TREE FROM ROOT, WE WILL CHECK 3 CONDITIONS. 
                WE WILL SEE IF THE NODE IS NOT EMPTY, KEY SHOULD BE > KEYS AT NODE AND INDEX SHOULD BE LESS THAN PAGESIZE
                */
		while (keys[index]!=null && key >= keys[index] && index < getSize())
                    index++;
		
                //RECURSION FOR FURTHER 
		return children[index].search(key);
	}

	@Override
	public boolean insert(Integer key, String value) {
		int a=0;
		boolean flag = false;
		for(int i = 0; i < getSize(); i++) 
                {
			/*if (this.children[i] != null)
				break;*/
                    
                         if (this.children[i] != null)//null index node, cannot insert
				break;
			
			if(this.keys[i] == null) 
                        {
                                flag = true;
				
                                this.keys[i] = key;
				this.children[i] = null;
				
                                break;
			}
		}

		if(flag == true) 
			return true;
		

		if(flag == false) 
                {
			
			while (a < getSize() && keys[a] != null && key >= keys[a])
				a++;
			//NODE NOT FOUND HERE, RECURSIVELY CALL OTHER CHILD
			return children[a].insert(key, value);
		}
		return false;
	}

	/**
	 * Create a child at a specific position and give the height of the subtree
	 * Only for internal use
	 */
	protected void createChild(int i, int height) {
		/*
		 * TODO: create the correct type of child depending on height and
		 * position
		 */
            
	}

	/**
	 * Get child at a specific position
	 */
	public IsamNode getChild(int pos) {
		return children[pos];
	}

	/**
	 * Get the to string for a specific child (this is a helper function for
	 * toString)
	 */
	private String getChildInOrderString(int pos) {
		IsamNode child = getChild(pos);
		if (child == null) {
			return "()";
		} else {
			return child.toString();
		}
	}

	@Override
	public String toString() {
		// Don't change this. This already does everything it is supposed to do
		String output = "";

		output += "(";

		int i = 0;

		for (; i < getSize(); ++i) {
			output += getChildInOrderString(i);

			Integer index = getIndex(i);
			if (index != null) {
				output += " " + index + " ";
			} else {
				output += " E ";
			}
		}

		output += getChildInOrderString(i);
		output += ")";

		return output;
	}

	@Override
	public boolean delete(Integer key) {
		int i = 0;
                
                //TRAVERSE TOWARDS THE KEY
		while (i < getSize() && keys[i] != null && key >= keys[i]) 
			i++;
                
                //IF THE KEY IS NOT FOUND IN THIS NODE, RECURESIVELY TRAVERSE ITS CHILDREN
		return children[i].delete(key);
	}
}
