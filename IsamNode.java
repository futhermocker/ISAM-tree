package edu.cornell.cs4320.hw2;

/**
 * Superclass for DataNode and IndexNode
 * You most likely won't need to modify this
 */
public abstract class IsamNode {
	private final int size;
	
	/**
	 * Constructor
	 * Note, size must be identical for all nodes in a tree
	 */
	IsamNode(int size) {
		this.size = size;
	}
	
	/**
	 * Get the size of this node
	 * Only for internal use
	 */
	protected int getSize() {
		return size;
	}
	
	/**
	 * Insert a new entry
	 * - See IsamTree for more information
	 */
	public abstract boolean insert(Integer key, String value);
	
	/**
	 * Search for a specific entry
	 * - See IsamTree for more information
	 */
	public abstract String search(Integer key);

	/**
	 * Delete an entry with the specified key
	 * - See IsamTree for more information
	 */
	public abstract boolean delete(Integer key);
	
	/**
	 * @return a string representation of the (sub)tree
	 * @note the representation shall be in-order
	 */
	public abstract String toString();
}
