package org.ngi;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.SwingUtilities;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * Utility for Panels to handle pan and zoom.
 *
 * Camera Handles Pan and Zoom, but also coordinate tracking: screen to world
 * and world to screen. Pan mode: MouseWheel press and drag or Shift + Right
 * Mouse press and drag. Zoom Mode MouseWheel scroll or Control + RightMouse
 * press and drag in either x or y axis.
 *
 *
 * @author Tom
 */
public class PanelCamera
        implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {

    private static final double MIN_CAMERA_SCALE = 0.15; //0.000001;
    private static final double MAX_CAMERA_SCALE = 10;

    private double _cameraWorldCenterX;
    private double _cameraWorldCenterY;
    private double _cameraScale;

    private Point2D.Double _deviceCoords;
    private Point2D.Double _worldCoords;

    //panel screeen center
    private Point2D.Double _panelCenter;

    private int _lastMouseX;
    private int _lastMouseY;
    private boolean _panActive;
    private boolean _zoomActive;

    int _screenResolution;

    private AffineTransform _cameraM;

    private JPanel _ownerPanel;

    //use world space coords
    private boolean _flipY = false;

    //bounds of panel
    private Rectangle _screenBounds;

    //bounds of displayed info
    private Rectangle _viewBounds;



    public PanelCamera(JPanel ownerPanel, boolean flipY) {

        this._ownerPanel = ownerPanel;

        _screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();

        _cameraWorldCenterX = 0;
        _cameraWorldCenterY = 0;

        _cameraScale = 1.0;

        _cameraM = new AffineTransform();

        this._flipY = flipY;

        _deviceCoords = new Point2D.Double();
        _worldCoords = new Point2D.Double();
        _panelCenter = new Point2D.Double();

        //default focus
        _viewBounds = new Rectangle(-100, -100, 200, 200);

        _panActive = false;
        _zoomActive = false;


        attachCamera();

    }

    /**
     * Attach the Camera to all the listeners.
     */
    private final void attachCamera() {

        _ownerPanel.addMouseListener(this);
        _ownerPanel.addMouseMotionListener(this);
        _ownerPanel.addMouseWheelListener(this);
        _ownerPanel.addComponentListener(this);

        updateCamera();

    }

    public void setCamera(double cx, double cy, double ch) {

        _cameraWorldCenterX = cx;
        _cameraWorldCenterY = cy;
        _cameraScale = ch;

        updateCamera();
    }

    public void flipYOn() {
        _flipY = true;
    }

    public void flipYOff() {
        _flipY = false;
    }

    public AffineTransform getTransform() {
        return _cameraM;
    }

    public Point2D.Double screenPointInWorld(Point2D devicePoint) {

        Point2D.Double result = new Point2D.Double();

        try {
            _cameraM.inverseTransform(devicePoint, result);
        } catch (Exception e) {
        }

        return result;
    }

    private void updateCamera() {

        //reset camera matrix
        _cameraM.setToIdentity();

        //y-modifier
        double yModifier = _flipY ? -1.0d : 1.0d;

        double cxTrans = _panelCenter.x / _cameraScale;
        double cyTrans = _panelCenter.y * yModifier / _cameraScale;

        _cameraM.scale(_cameraScale, _cameraScale * yModifier);

        _cameraM.translate(cxTrans, cyTrans);

        _cameraM.translate(-_cameraWorldCenterX, -_cameraWorldCenterY * yModifier);

//       ownerPanel.repaint();
    }//end update Camera

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

            //smooth zooming now
            double wheelR = e.getWheelRotation();
            double zoomFactor = 1.0 + (wheelR * -0.1);

            _cameraScale *= zoomFactor;
            // System.out.println("zoom factor =" + zoomFactor + ", camera Height = " + _cameraScale);

            //hold to minimum and not 0!
            _cameraScale = Math.max(_cameraScale, MIN_CAMERA_SCALE);

            //hold max
            _cameraScale = Math.min(_cameraScale, MAX_CAMERA_SCALE);
            updateCamera();

            _ownerPanel.repaint();

        }//end if

    }//end mouseWheelMoved

    @Override
    public void mousePressed(MouseEvent e) {

        boolean startPan = false;
        boolean startZoom = false;

        if (SwingUtilities.isMiddleMouseButton(e)) {
            startPan = true;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (e.isShiftDown()) {
                startPan = true;
            } else if (e.isControlDown()) {
                startZoom = true;
            }
        }

        if (startPan || startZoom) {
            // capture starting point
            _lastMouseX = e.getX();
            _lastMouseY = e.getY();

            if (startPan) {
                _panActive = true;
                _zoomActive = false;
            } else if (startZoom) {
                _panActive = false;
                _zoomActive = true;
            }

        }

    }//end mouse Pressed

    @Override
    public void mouseReleased(MouseEvent e) {

        if (SwingUtilities.isMiddleMouseButton(e)) {
            _panActive = false;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            //ignore shift down to release
            _panActive = false;
            _zoomActive = false;
        }

    }//end mouse released

    @Override
    public void mouseDragged(MouseEvent e) {

        //update device Tracking
        _deviceCoords.x = e.getX();
        _deviceCoords.y = e.getY();

        if (_panActive) {
            // new x and y are defined by current mouse location subtracted
            // by previously processed mouse location
            int newX = e.getX() - _lastMouseX;
            int newY = e.getY() - _lastMouseY;

            // increment last offset to last processed by drag event.
            _lastMouseX += newX;
            _lastMouseY += newY;

            _cameraWorldCenterX -= newX / _cameraScale;
            //flip the y here
            _cameraWorldCenterY -= newY / _cameraScale;

            updateCamera();

            _ownerPanel.repaint();

        } else if (_zoomActive) {

            int mx = e.getX();
            int my = e.getY();
            int dx = mx - _lastMouseX;
            int dy = my - _lastMouseY;

            int adx = Math.abs(dx);
            int ady = Math.abs(dy);

            double zoomFactor = 1.0;
            // double adjust = 0.001;

            if (adx > ady) {
                if (dx > 0) {
                    zoomFactor = 1.1; //1.05;
                } else {
                    zoomFactor = 0.9;//0.95;
                }

            } else {
                if (dy > 0) {
                    zoomFactor = 0.9;//0.95; //1.05; //ady * adjust;
                } else {
                    zoomFactor = 1.1;//1.05; //0.95; //(ady +1) * adjust;
                }

            }

            _cameraScale *= zoomFactor;

            //hold to minimum and not 0!
            _cameraScale = Math.max(_cameraScale, MIN_CAMERA_SCALE);
            //hold max
            _cameraScale = Math.min(_cameraScale, MAX_CAMERA_SCALE);

            updateCamera();

            _lastMouseX = mx;
            _lastMouseY = my;

            //updateCamera();
            _ownerPanel.repaint();

        }//end if

//        //update world coords
        _worldCoords = screenPointInWorld(_deviceCoords);

    }//end mouseDraged

    @Override
    public void mouseMoved(MouseEvent e) {

        //update Tracking
        _deviceCoords.x = e.getX();
        _deviceCoords.y = e.getY();

        //update worldCoords here 
        _worldCoords = screenPointInWorld(_deviceCoords);

    }//end mouse moved

    @Override
    public void mouseClicked(MouseEvent e) {

        boolean rightAndCtrl = SwingUtilities.isRightMouseButton(e) && e.isControlDown();
        boolean mouseWheel = SwingUtilities.isMiddleMouseButton(e);

        if (rightAndCtrl || mouseWheel) {
            if (e.getClickCount() == 2) {
                zoomExtents();
            }
        }//end if button        

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Zoom extents is triggered with double click
     *
     */
    public void zoomExtents() {

        //grab bounds from owner
        _screenBounds = new Rectangle();
        _ownerPanel.getBounds(_screenBounds);

        double ccx = _viewBounds.getCenterX();
        double ccy = _viewBounds.getCenterY();

        //check aspect ratio, and use width or height for zoom extents
        double viewAR = _viewBounds.getWidth() / _viewBounds.getHeight();
        double cscale = 1;
        if (viewAR >= 1) {
            cscale = _screenBounds.getWidth() / _viewBounds.getWidth();
            double wChk = cscale * _viewBounds.getHeight();
            if (wChk > _screenBounds.getHeight()) {
                cscale = _screenBounds.getHeight() / _viewBounds.getHeight();
            }
        } else {

            cscale = _screenBounds.getHeight() / _viewBounds.getHeight();
            double hChk = cscale * _viewBounds.getWidth();
            if (hChk > _screenBounds.getWidth()) {
                cscale = _screenBounds.getWidth() / _viewBounds.getWidth();
            }
        }

        //5% zoom out for edge room
        cscale -= cscale * 0.05;

        //set the camera center and height
        setCamera(ccx, ccy, cscale);

        _ownerPanel.repaint();

    }//end zoom extents

    public double getCameraScale() {
        return _cameraScale;
    }

    public Point2D.Double getWorldCoords() {
        return _worldCoords;
    }

    public Point2D.Double getDeviceCoords() {
        return _deviceCoords;
    }

    public Point2D.Double getCameraTargetInWorld() {

        return new Point2D.Double(_cameraWorldCenterX, _cameraWorldCenterY);

    }

    /**
     * Sets the bounds of items being viewed (viewable area). This is used for
     * calculating zoom extents and for zoom scale factors.
     *
     * @param viewBounds Bounds of items to be viewed. I.e.:focus area for zoom
     * extents.
     */
    public void setViewBounds(Rectangle viewBounds) {

        // Rectangle nb = new Rectangle(0,0,viewBounds.width, viewBounds.height);
        this._viewBounds = new Rectangle(viewBounds);
        // this.viewBounds = new Rectangle(nb);

    }

    /**
     * Sets the bounds of Panel associated with Camera (display area). This is
     * used for calculating zoom extents and for zoom scale factors. This is
     * called on ComponentResized and needs to re-center the camera.
     *
     *
     */
    public void updateScreenBounds() {

        //grab bounds from owner
        _screenBounds = new Rectangle();
        _ownerPanel.getBounds(_screenBounds);
        //calc new screen center
        _panelCenter.setLocation(_screenBounds.getWidth() * 0.5, _screenBounds.getHeight() * 0.5);
        //update the camera
        updateCamera();

    }//end updateScreenBounds

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {

        updateScreenBounds();

    }



    /**
     * Pan the camera by the given x and y values.  These are screen relative values (screen coords).
     * @param x  X value to pan camera.
     * @param y  Y value to pan camera.
     */
    public void panCamera(int x, int y) {


        Point mp = _ownerPanel.getMousePosition();
        
        if(mp != null) {
            _deviceCoords.x = mp.getX();
            _deviceCoords.y = mp.getY();   
             _worldCoords = screenPointInWorld(_deviceCoords);            
        }

        _cameraWorldCenterX -= x / _cameraScale;
        _cameraWorldCenterY -= y / _cameraScale;

        updateCamera();
        
         _ownerPanel.repaint();

    }    
    
    
    
}//end class

