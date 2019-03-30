
package org.ngi;


import java.io.Serializable;



/**
 *  Bounding box class for 2d objects.  Uses Min and Max X,Y values to define a region.
 * @author Tom
 */

public class Bounds implements Serializable {

  protected float _min_x;
  protected float _min_y;
  protected float _max_x;
  protected float _max_y;

  /**
   * Construct a default bounds at 0,0 with a size of 100.
   */
  public Bounds() {
      
    _min_x = 0;
    _min_y = 0;
    _max_x = 100;
    _max_y = 100;
      
      
  }

 /**
  * Construct a Bounds from min and max x,y values.
  * @param min_x  Minimum X value.
  * @param min_y  Minimum Y value.
  * @param max_x  Maximum X value.
  * @param max_y  Maximum Y value.
  */
  public Bounds(float min_x, float min_y, float max_x, float max_y) {


    //normalize the bounds
    if(min_x <= max_x) {
        _min_x = min_x;
        _max_x = max_x;
    } else {
        _min_x = max_x;
        _max_x = min_x;
    }
    
    if(min_y <= max_y) {
        _min_y = min_y;
        _max_y = max_y;
    } else {
        _min_y = max_y;
        _max_y = min_y;
    }
    

  }//end constructor


  /**
   * Construct a bounds from another bounds object.
   * @param other Bounds to copy.
   */
  public Bounds(Bounds other) {
    
    _min_x = other.minX();
    _min_y = other.minY();
    _max_x = other.maxX();
    _max_y = other.maxY();
    
  }//end copy constructor

  
  /**
   * Set the location and size of the Bounds.
   * @param x X location of the Bounds.
   * @param y Y location of the Bounds.
   * @param width Width of the Bounds.
   * @param height Height of the Bounds.
   */
  public void set(float x, float y, float width, float height){
      
      _min_x = x;
      _min_y = y;
      _max_x = x + width;
      _max_y = y + height;
      
  }
  
  /**
   * Set the location of the Bounds.
   * @param x X location of the Bounds.
   * @param y Y location of the Bounds.
   */
  public void setLocation(float x, float y){
      float width = this.width();
      float height = this.height();
      
      _min_x = x;
      _min_y = y;
      _max_x = x + width;
      _max_y = y + height;
      
  }  
  
  
/**
 * Test if this Bounds contains another Bounds.
 * @param other Bounds to test.
 * @return True if this Bounds contains another Bounds, false otherwise.
 */
  public boolean contains(Bounds other) {

      return _min_x <= other.minX() && _min_y <= other.minY() && _max_x >= other.maxX() && _max_y >= other.maxY();
      
  }//end contains bounds


  /**
   * Test if this Bounds contains another bounds with a proximity setting.
   * The Bounds can be within the proximity to another.
   * @param other Bounds to test.
   * @param proximity Proximity distance to other Bounds.
   * @return True if Bounds is within this bounds with proximity applied.
   */
  public boolean contains(Bounds other, float proximity) {
    
    Bounds prox_bounds = new Bounds(this);
    prox_bounds.expandBy(proximity);
    return prox_bounds.contains(other);
 
    
  }

  /**
   * The string representation of the Bounds.
   * @return The string representation of the Bounds.
   */
  @Override
  public String toString() {
    
     String msg = "bounds: " + _min_x + "," + _min_y + " min; " + _max_x + "," + _max_y + " max;" ;
     msg += " [" + width() + " X " + height() + " = " + area() + "]";
     
    return msg;
    
  }
  

/**
 * Containment test for point.
 * @param px Point x.
 * @param py Point y.
 * @return True if Bounds contains point, false otherwise.
 */
  public boolean contains(float px, float py) {

    if (px >= _min_x && py >= _min_y && px <= _max_x && py <= _max_y) {
      return true;
    } 
    else {
      return false;
    }
  }//end contains point

  /**
   * Expand the bounds in all dimensions by amount.
   * @param ex Amount to increase the bounds. 
   */
  public void expandBy(float ex) {
    
   
    _min_x -= ex;
    _min_y -= ex;
    _max_x += ex;
    _max_y += ex;
    
  }


  /**
   * Get the area that the bounds overlaps.
   * @param other  The bounds to overlap with.
   * @return The area value of the overlap area of the two bounds.
   */
  public float overlapArea(Bounds other) {
    
    Bounds ib = intersect(other);
    
    if (ib == null) {
      return 0;
    }
    
    return ib.area();
    
  }//end overlap area


  /**
   * Get the intersection of two bounds.
   * @param other  Bounds to intersect with.
   * @return The intersection Bounds or null if no intersection.
   */
  public Bounds intersect(Bounds other) {
    
    
    if(this.contains(other) ) {
      return new Bounds(other);
    }
    if ( other.contains(this)) {
      return new Bounds(this);
    }
    
    
    float o_min_x = other.minX();
    float o_min_y = other.minY();
    float o_max_x = other.maxX();
    float o_max_y = other.maxY();    
    
    
    float int_x1 = 0;
    float int_y1 = 0;
    float int_x2 = 0;
    float int_y2 = 0;    
   
    
    
    //determine vertical edge condition
    //full x overlap
    if ( _min_x <= o_min_x && _max_x >= o_max_x ) {
       int_x1 = o_min_x;
       int_x2 = o_max_x;
    //max_x overlap
    } else if (_min_x <= o_min_x && _max_x <= o_max_x && _max_x > o_min_x) {
      int_x1 = o_min_x;
      int_x2 = _max_x;
      
     //x contained
    } else if (_min_x >= o_min_x && _max_x <= o_max_x ) {
      int_x1 = _min_x;
      int_x2 = _max_x;
     //min_x contained
    } else if (_min_x >= o_min_x && _max_x >= o_max_x && _min_x < o_max_x ) {
      int_x1 = _min_x;
      int_x2 = o_max_x;
    } else {
      //no x relationship - no intersection
      return null;
    }
    
   //full y overlap
    if ( _min_y <= o_min_y && _max_y >= o_max_y ) {
       int_y1 = o_min_y;
       int_y2 = o_max_y;
    //max_y overlap
    } else if (_min_y <= o_min_y && _max_y <= o_max_y && _max_y > o_min_y) {
      int_y1 = o_min_y;
      int_y2 = _max_y;
      
     //y contained
    } else if (_min_y >= o_min_y && _max_y <= o_max_y ) {
      int_y1 = _min_y;
      int_y2 = _max_y;
     //min_y contained
    } else if (_min_y >= o_min_y && _max_y >= o_max_y && _min_y < o_max_y) {
      int_y1 = _min_y;
      int_y2 = o_max_y;
    } else {
      //no y relationship - no intersection
      return null;
    }    
    
    return new Bounds(int_x1, int_y1, int_x2, int_y2);
    
  }//end intersect


/**
 * Expand to include another Bounds.  This is the Union between two bounds.
 * @param other Bounds to union with.
 */
 public void expandToInclude(Bounds other) {
   
   
    float o_min_x = other.minX();
    float o_min_y = other.minY();
    float o_max_x = other.maxX();
    float o_max_y = other.maxY();
   
   
    if(_min_x > o_min_x) {
      _min_x = o_min_x;
    }
   
   if(_min_y > o_min_y) {
     _min_y = o_min_y;
   }
   
   
   if (_max_x < o_max_x) {
     _max_x = o_max_x;
   }
   
   if (_max_y < o_max_y) {
     
     _max_y = o_max_y;
   }
   
   
 }//end expandToInclude

 /**
  * Width of the bounds.
  * @return The width of this Bounds.
  */
  public float width() {
    return Math.abs(_max_x - _min_x);
  }

  /**
   * Height of the Bounds.
   * @return The height of the Bounds.
   */
  public float height() {
    return Math.abs(_max_y - _min_y);
  }
  
  
  /**
   * Area of the bounds.
   * @return The area of the bounds.
   */
  public float area() {
    return this.width() * this.height();
  }


  
  
/**
 * Get the aspect ratio of the bounds.
 * @return The aspect ratio of the width to height of the Bounds.
 */
  public float aspectRatio() {
    
    float w = width();
    float h = height();
    return w/h;
    
  }


  /**
   * 
   * Get the sub-bonds from the min x and min y to the center.
   * In screen coordinates this is the North West quadrant.
   * 
   * @return The Bounds covering the NW quadrant.
   */
  public Bounds getQuadrantNW() {
      
      float maxX = _min_x + width() / 2;
      float maxY = _min_y + height() / 2;
      return new Bounds(_min_x, _min_y, maxX, maxY);
      
  }
  
  /**
   * 
   * Get the sub-bonds from the center x and min y to the center/right.
   * In screen coordinates this is the North East quadrant.
   * 
   * @return The Bounds covering the NE quadrant.
   */  
  public Bounds getQuadrantNE() {
      
      float minX = _min_x + width() / 2;
      float maxY = _min_y + height() / 2;
      return new Bounds(minX, _min_y, _max_x, maxY);
      
  }  
  
  
  /**
   * 
   * Get the sub-bonds from the min x and center y to the bottom/center.
   * In screen coordinates this is the South West quadrant.
   * 
   * @return The Bounds covering the SW quadrant.
   */  
  public Bounds getQuadrantSW() {
      
      float maxX = _min_x + width() / 2;
      float minY = _min_y + height() / 2;
      
      return new Bounds(_min_x, minY, maxX, _max_y);
      
  }   

  
  
    /**
   * 
   * Get the sub-bonds from the center x and center y to the bottom/right.
   * In screen coordinates this is the South East quadrant.
   * 
   * @return The Bounds covering the SE quadrant.
   */  
  public Bounds getQuadrantSE() {
      
      float minX = _min_x + width() / 2;
      float minY = _min_y + height() / 2;
      
      return new Bounds(minX, minY, _max_x, _max_y);
      
  }  
 

  /**
   * Translate the Bounds.
   * @param x Amount to translate the Bounds in x.
   * @param y Amount to translate the Bounds in y.
   */
public void translate(float x, float y) {
    _min_x += x;
    _max_x += x;
    _min_y += y;
    _max_y += y;
}


/**
 * Minimum X value of the Bounds.
 * @return Bounds minimum x.
 */
  public float minX() {
    return _min_x;
  }


  /**
   * Minimum Y value of the Bounds.
   * @return Bounds minimum y.
   */
  public float minY() {
    return _min_y;
  }

  /**
   * Maximum X value of the Bounds.
   * @return Bounds maximum x.
   */
  public float maxX() {
    return _max_x;
  }
  
  
   /**
   * Maximum Y value of the Bounds.
   * @return Bounds maximum y.
   */
  public float maxY() {
    return _max_y;
  }





}//end class Bounds 

