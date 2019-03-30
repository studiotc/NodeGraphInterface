package org.ngi;

import java.io.Serializable;
/**
 *  Base Class for common graphical entities.
 * @author Tom
 */
public class Element implements Serializable {
    
    protected Bounds _bounds;
    protected String _name;
    protected String _id;
    protected String _desc;
    
    
    /**
     * Constructor
     * @param name String Name of the Element
     * @param bounds Bounds Bounds of the Element
     */
    public Element(String name, Bounds bounds) {
        
        _id = NodeGraph.generate_id();
        _name = name;
        _bounds = bounds;
        _desc = "";
        
        
    }
    
    /**
     * Constructor with description.
     * @param name  The name of the element.
     * @param bounds  The bounds of the element.
     * @param description The description of the Element.
     */
    public Element(String name, Bounds bounds, String description) {
        
        _id = NodeGraph.generate_id();
        _name = name;
        _bounds = bounds;
        _desc = description;
        
        
    }    
    
    
 
    
    
    /**
     * Get the Bounds of the Element
     * @return Bounds The Bounds of the Element.
     */
    public Bounds getBounds() {
        return _bounds;
    }
    
    /**
     * Set the Bounds of the Element.
     * @param bounds Bounds to use for Element.
     */
    public void setBounds(Bounds bounds) {
        _bounds = bounds;
    }
    
    /**
     * get the Name of the Element
     * @return String The name of the element.
     */
    public String getName() {
        return _name;
    }
    
    
    /**
     * Set the name of the Node
     * @param name String Name of the Element
     */
    public void setName(String name ) {
        
        if(!"".equals(name)) {
            _name = name;
        }
    }
    
    /**
     * Get the description of this Element.
     * @return The description of the Element.
     */
    public String getDescription() {
        return _desc;
    }
    
    
    /**
     * Set the description for the Element.
     * @param description The description of the Element.
     */
    public void setDescription(String description) {
        _desc = description;
    }
    
    
    /**
     * Get the Id of the Element
     * @return String the ID of the Element
     */
    public String getId() {
        return _id;
    }
    
    /**
     * Used for File read purposes.  Do not use otherwise!
     * @param newId String New Id for the Element
     */
    public void setId(String newId){
        _id = newId;
    }
}
