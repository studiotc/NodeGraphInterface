package org.ngi;

import org.ngi.enums.FocusType;
import org.ngi.enums.TileType;
import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Random;
import org.ngi.enums.DataType;
import org.ngi.enums.InteractionState;

import org.ngi.menus.MenuSystem;
import org.ngi.quadtree.QuadTree;
import static javax.swing.SwingUtilities.*;
import javax.swing.border.Border;

/**
 *
 * NodeGraph is a node graph interface for OpenSCAD.
 *
 * It allows users to create programs by connecting graphical nodes.
 * The Nodes act as overlays to elements of the OpenSCAD language.  By connecting Nodes,
 * the user constructs larger OpenSCAD statements (or functions or modules).
 *
 *
 * @author Tom
 */
public class NodeGraph extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    final static String FILE_EXTENSION = "scng";

    private PanelCamera _camera;

    private QuadTree _quadTree;

    private Bounds _nodeBounds;

    private float _last_mouse_x;
    private float _last_mouse_y;

    private ArrayList<Node> _nodes;

    private ArrayList<NodeConnection> _connections;

    private MouseFocus _mouseFocus;
    private InteractionState _is;
    private MouseFocus _mouseDragFocus;

    private MenuSystem _menuSystem;

    private DragConnectorTool _dragTool;

    private boolean _showInfoPanel;
    private InfoPanel _infoPanel;


    private JFrame _ownerFrame;
    private FileHandler _fileHandler;

    
    private Timer _autoPanTimer;
    private Point _autoPanDelta;    
    
    
    public NodeGraph(JFrame ownerFrame) {
        super();

        _ownerFrame = ownerFrame;

        init();

    }

    @Override
    public Border getBorder() {
        Color bc = new Color(240, 240, 240);
        return BorderFactory.createLineBorder(bc, 6);
    }

    /**
     * Initialize the Node Graph - initialization, start up procedures, etc.
     */
    private void init() {

        _fileHandler = new FileHandler(_ownerFrame, this);

        _fileHandler.setWindowTitle();

        _camera = new PanelCamera(this, false);

        _quadTree = new QuadTree(20480, 20480);

        _nodeBounds = new Bounds(0, 0, 1, 1);

        _showInfoPanel = false;

        _infoPanel = new InfoPanel(this);

        _is = InteractionState.NEUTRAL;

        _mouseFocus = new MouseFocus();
        _mouseDragFocus = new MouseFocus();

        _last_mouse_x = 0;
        _last_mouse_y = 0;

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        _nodes = new ArrayList<>();

        _connections = new ArrayList<>();

        _menuSystem = new MenuSystem(this, _fileHandler);

        _dragTool = new DragConnectorTool(this);
        
        //autopan feature variables
        _autoPanTimer = new Timer(250, this);
        _autoPanDelta = new Point(0,0);

    }

    /**
     * Call this when closing to check for file changes before window close.
     *
     * @return True if window can close, or false if user canceled close.
     */
    public boolean closeCheck() {
        return _fileHandler.closeProcedure();
    }

    /**
     * Get the list of nodes. Used by the File Handler.
     *
     * @return The list of nodes.
     */
    public ArrayList<Node> getNodes() {

        return _nodes;

    }

    /**
     * Set the list of nodes. This is used by the FileHandler when
     * de-serializing a file.
     *
     * @param newNodes List of nodes to replace current nodes.
     */
    public void setNodes(ArrayList<Node> newNodes) {

        if (newNodes != null) {

            _connections.clear();
            _nodes.clear(); //?...

            _quadTree.clear();

            //set the nodes...
            _nodes = newNodes;

            for (Node n : _nodes) {

                //System.out.println("Loaded Node: " + n.getName());
                //gather outputs
                ArrayList<NodeOutput> outputs = n.getOuputs();

                for (NodeOutput output : outputs) {

                    //gather the connections
                    ArrayList<NodeConnection> connections = output.getNodeConnections();

                    //push connections to global list for display
                    for (NodeConnection conn : connections) {
                        _connections.add(conn);
                    }//end for connections  
                }//end for outputs 

                //index the node
                _quadTree.indexNode(n);

            }//end for nodes
        }

        //update bounds for the camera
        updateNodeBounds();
        //do a zoom extents
        _camera.zoomExtents();

        //evaluation cycle?....
        //so far none needed with deserialize
        this.repaint();

    }//end set nodes

    /**
     * General reset all for use when starting a new file. TODO Refine this -
     * quick and dirty for testing.
     */
    public void resetAll() {

        //reset nodes list
        _nodes.clear();
        //clear all connections
        _connections.clear();

        //_camera = new PanelCamera(this, false);
        _quadTree.clear();

        _nodeBounds = new Bounds(0, 0, 100, 100);

        _is = InteractionState.NEUTRAL;

        _mouseFocus = new MouseFocus();
        _mouseDragFocus = new MouseFocus();

        _last_mouse_x = 0;
        _last_mouse_y = 0;

        _dragTool.reset();

    }

    /**
     * Create a node from it's fully qualified class name (complete package).
     * This is called from the menu
     *
     * @param type Full name of Node type to create (full class name).
     */
    public void createNode(String type) {

        try {

            Class<?> c = Class.forName(type);

            Node n = (Node) c.newInstance();

            //add node to center of screen in world
            Point2D.Double camcen = _camera.getCameraTargetInWorld();
            n.setLocation((float) camcen.x, (float) camcen.y);

            addNode(n);

        } catch (Exception ex) {

            System.out.println("***  Error Adding New Node ***" + ex);

        }

    }

    /**
     * Add a Node to the NodeGraph. This adds it to the collection and registers
     * it in spatial index, etc.
     *
     * @param n Node to add to the NodeGraph.
     */
    public void addNode(Node n) {

        try {

            
            //make sure Node is being created in limits
            Bounds nb = n.getBounds();
            Bounds chkB = _quadTree.ensureContainment(nb);
            //n.setBounds(chkB);
            
            float dx =  chkB.minX() - nb.minX();
            float dy =  chkB.minY() - nb.minY();
            
            n.translate(dx, dy);            
            
            _nodes.add(n);

            //evaluate the node
            //n.evaluateNode();
            evalHook(n);

            System.out.println("Adding node of type: " + n.getClass().getName());


            //update the bounds
            updateNodeBounds();

            // index the node
            _quadTree.indexNode(n);

            this.repaint();

            //flag file change
            _fileHandler.setDirty();

        } catch (Exception ex) {

            System.out.println("***  Error Adding Node ***" + ex);

        }

    }

    public MenuSystem getMenuSystem() {
        return _menuSystem;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(300, 200);
    }

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int cw = this.getWidth();
        int ch = this.getHeight();

        g2.setBackground(Color.GRAY);
        g2.clearRect(0, 0, cw, ch);

        AffineTransform org_trans = g2.getTransform();
        //transform based on camera
        g2.transform(_camera.getTransform());

        _quadTree.paintQuadTree(g2);


        //list for highlighted connections
        ArrayList<NodeConnection> hl_conn = new ArrayList<>();


        //paint the connections - set aside highlighted ones
        _connections.forEach((conn) -> {
            if(conn.isHighlighted()) {
                hl_conn.add(conn);
            } else {
                conn.paintConnection(g2);
            }
        });
        
        //paint highlighted connections on top
        hl_conn.forEach(conn -> {
            conn.paintConnection(g2);
        });

        //paint the node
        _nodes.forEach((n) -> {
            n.paintNode(g2);
        });

        //dragging connector?
        if (_is == InteractionState.DRAGGING_CONNECTOR) {
            _dragTool.paintConnector(g2);
        }

        //show info panel
        if (_is == InteractionState.NEUTRAL && _showInfoPanel) {
            _infoPanel.paintInfoPanel(g2);
        }

        //reset transform
        g2.setTransform(org_trans);

    }

    /**
     * Update the viewable bounds established by all the nodes Update the camera
     * for zoom extents.
     */
    private void updateNodeBounds() {
        boolean first = true;
        Bounds b = new Bounds(0, 0, 1, 1);
        for (Node node : _nodes) {
            Bounds curB = node.getBounds();
            if (first) {
                b = new Bounds(curB);
                first = false;
            } else {
                b.expandToInclude(curB);
            }

        }//end for

        _nodeBounds = b;
        Rectangle2D br = new Rectangle2D.Float(b.minX(), b.minY(), b.width(), b.height());
        _camera.setViewBounds(br.getBounds());

    }

    /**
     * Update the info panel with the information from the node or tile the
     * mouse is over.
     *
     * @param mf MouseFocus object containing hover information.
     */
    private void updateInfoPanel(MouseFocus mf) {

        switch (mf.getFocusType()) {

            case NODE:
                Node node = mf.getFocusNode();

                _infoPanel.setInfo(node);
                _showInfoPanel = true;
                break;

            case TILE:
                NodeTile nt = mf.getFocusTile();
                _infoPanel.setInfo(nt);
                _showInfoPanel = true;
                break;

            default:

                _showInfoPanel = false;
                break;

        }//end switch case

    }

    ////////////////////////////////////////////
    /////////////Mouse Events//////////////
    //////////////////////////////////////////
    /**
     * Look for mouse hover over Nodes and Inputs/Outputs.
     *
     * @param e MouseEvent for move.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        float x = e.getX();
        float y = e.getY();
        Point2D.Double wc = _camera.getWorldCoords();

        x = (float) wc.getX();
        y = (float) wc.getY();

        MouseFocus mmHtr = new MouseFocus();

        switch (_is) {
            case NEUTRAL:

                mmHtr = _quadTree.mouseFocusTest(x, y);

                //update info panel
                updateInfoPanel(mmHtr);

                _quadTree.mouseFocusTest(x, y);

                break;

            case DRAGGING_NODE:
                //do nothing, moving node on screen
                break;

            case DRAGGING_CONNECTOR:

                break;

        }

        //swap mouse focus
        _mouseFocus.clearHighlight();
        _mouseFocus = mmHtr;
        _mouseFocus.setHighlight();

        this.repaint();

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if (isLeftMouseButton(e)) {
            //if actually dragging object
            if(_is != InteractionState.NEUTRAL) {
                automaticPanSet(e);
            }
            //left mouse dragged event
            leftMouseDragged();
        }
        
        
    }
    
    /**
     * Mouse Drag Event for left mouse button.
     */
    public void leftMouseDragged() {
        
        Point2D.Double wc = _camera.getWorldCoords();

        float x = (float) wc.getX();
        float y = (float) wc.getY();

        //check bounds for drag
        // boolean inBounds = _numericalLimit.contains(x, y);
        float dx = x - _last_mouse_x;
        float dy = y - _last_mouse_y;

        switch (_is) {
            case NEUTRAL:

                break;

            case DRAGGING_NODE:

                _showInfoPanel = false;

                //do a limits check
                Node dnode = _mouseFocus.getFocusNode();
                Bounds db = new Bounds(dnode.getBounds());
                db.translate(dx, dy);

                //check bounds
                if (_quadTree.contains(db)) {

                    dnode.translate(dx, dy);
                    //index the node
                    _quadTree.indexNode(dnode);
                }

                this.repaint();
                break;

            case DRAGGING_CONNECTOR:

                _showInfoPanel = false;

                //System.out.println("Dragging connector....");
                //MouseFocus mdHtr = new MouseFocus();

                //get node through index
                MouseFocus mdHtr = _quadTree.mouseFocusTest(x, y);

                _dragTool.update(mdHtr, x, y);

                _mouseDragFocus.clearHighlight();
                _mouseDragFocus = mdHtr;
                _mouseDragFocus.setHighlight();

                this.repaint();
                break;
        }

        _last_mouse_x = x;
        _last_mouse_y = y;
        
        

    }

    
    
    
    @Override
    public void mousePressed(MouseEvent e) {

        if (isLeftMouseButton(e)) {
            leftMousePressed(e);
            //System.out.println("Left mouse button");
        } else if (isRightMouseButton(e)) {
            rightMousePressed(e);
            // System.out.println("Right mouse button");
        } else if (isMiddleMouseButton(e)) {
            //System.out.println("Middle mouse button");
        } else {
            //System.out.println("ERROR *** Unable to determine mouse button ***");
        }

    }

    /**
     * Action on Left Mouse Pressed event
     *
     * @param e MouseEvent
     */
    private void leftMousePressed(MouseEvent e) {

//        float x = e.getX();
//        float y = e.getY();
        Point2D.Double wc = _camera.getWorldCoords();

        float x = (float) wc.getX();
        float y = (float) wc.getY();

        if (_is == InteractionState.NEUTRAL) {

            if (_mouseFocus.getFocusType() == FocusType.NODE) {

                _is = InteractionState.DRAGGING_NODE;
                _last_mouse_x = x;
                _last_mouse_y = y;

            } else if (_mouseFocus.getFocusType() == FocusType.TILE) {

                if (_dragTool.setSource(_mouseFocus)) {

                    _is = InteractionState.DRAGGING_CONNECTOR;
                } else {

                    _dragTool.reset();
                }

                _last_mouse_x = x;
                _last_mouse_y = y;

            }//end else/if

        }//end if        

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (isLeftMouseButton(e)) {
            //clear any automtic pan
            automaticPanClear();
            //mouse released event
            leftMouseReleased(e);
            
        } else if (isRightMouseButton(e)) {
            rightMouseReleased(e);
        } else if (isMiddleMouseButton(e)) {

        } else {
            // System.out.println("ERROR *** Unable to determine mouse button ***");
        }

    }

    /**
     * Action on Left Mouse Released event
     *
     * @param e MouseEvent
     */
    private void leftMouseReleased(MouseEvent e) {

        //if dragging connection - look for target
        if (_is == InteractionState.DRAGGING_CONNECTOR) {

            _dragTool.makeConnection();

            //reset state
            _is = InteractionState.NEUTRAL;
            _mouseDragFocus.clearResults();
            
            //repaint the screen
            this.repaint();
            //end if dragging node state
        } else if (_is == InteractionState.DRAGGING_NODE) {

            _mouseDragFocus.clearResults();
            _is = InteractionState.NEUTRAL;

            //update the bounds
            updateNodeBounds();

            
            
            this.repaint();

        }

    }//end left mouse released

    /**
     * Right Mouse Released
     *
     * @param e MouseEvent for event data.
     */
    private void rightMouseReleased(MouseEvent e) {

        //bail on camera zoom
        if(e.isControlDown() || e.isShiftDown()) {
            return;
        }
        
     

    }//end rightMouseReleased

    private void rightMousePressed(MouseEvent e) {
        
        if(e.isControlDown() || e.isShiftDown()) {
            return;
        }        
      
        
        int clkcnt = e.getClickCount();
        int x = e.getX();
        int y = e.getY();

        //looking for a click for menu trigger
        if (clkcnt != 1) {
            return;
        }

        if (_is == InteractionState.NEUTRAL) {

            if (_mouseFocus.getFocusType() == FocusType.NODE) {

                Node focusNode = _mouseFocus.getFocusNode();
                _menuSystem.showNodeContextMenu(focusNode, x, y);

            } else if (_mouseFocus.getFocusType() == FocusType.TILE) {

                Node focusNode = _mouseFocus.getFocusNode();
                NodeTile focusTile = _mouseFocus.getFocusTile();
                //System.out.println("Right click on  Tile:" + focusTile.getName() + " in Node:" + focusNode.getName());

                if (focusTile.getTileType() == TileType.INPUT) {

                    NodeInput ni = (NodeInput) focusTile;
                    _menuSystem.showNodeInputContextMenu(ni, x, y);

                }

            } else if (_mouseFocus.getFocusType() == FocusType.NONE) {

                _menuSystem.showMainPopup(x, y);
                // _mainMenu.show(this, e.getX(), e.getY());

            }

        }//end if              
        
        
        
    }
    
    
    @Override
    public void mouseEntered(MouseEvent e) {
        //mouseEventReport("Mouse entered", e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // mouseEventReport("Mouse exited", e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //simple testing
        if (isLeftMouseButton(e)) {
            if (_is == InteractionState.NEUTRAL) {
                if (_mouseFocus.hasFocus() && _mouseFocus.getFocusType() == FocusType.TILE) {
                    NodeTile tile = _mouseFocus.getFocusTile();
                    TileType tileType = tile.getTileType();
                    if (tileType == TileType.OUTPUT) {

                        NodeOutput outputN = (NodeOutput) tile;
                        //System.out.println("output value for node:" + tile.getName() + " = " + outputN.getOutputValue());
                        //System.out.println();

                    }

                }

            }
        }

    }

    /**
     * Testing new main evaluation function.
     *
     * @param n Node to evaluate.
     */
    public void evalHook(Node n) {

        //System.out.println(">>>Eval Hook @" + n.getName() + " :: " + n.getId());
        //init new list
        TerminalNodeList tnl = new TerminalNodeList();

        //invalidate nodes
        n.invalidate(tnl, 0);

        //evaluate the terminal nodes
        tnl.evalTerminals();

    }

    /**
     * Create a connection between a NodeOutput and NodeInput. Check for
     * circular references here. It is not being done in the compatibility check
     * for fear of slowing the user interaction down.
     *
     * @param output NodeOutput to connect.
     * @param input NodeInput to connect.
     */
    public void createConnection(NodeOutput output, NodeInput input) {

        if (output != null && input != null) {

            if (!input.isCircularReference(output)) {

                //remove existing connection
                if (input.hasConnection()) {

                    NodeConnection inConn = input.getConnection();
                    //remove the existing connection
                    deleteConnection(inConn);

                }
                //make new connection
                NodeConnection conn = new NodeConnection(output, input);
                conn.connect();

                //add to connection list
                _connections.add(conn);

                //start the evaluation chain
                //input.getParent().evaluateNode();
                evalHook(input.getParent());
                //repaint the graph
                this.repaint();

                //flag file change
                _fileHandler.setDirty();

            } else {

                System.out.println("*** ERROR *** Circular reference detected");
            }//end if circular refrence

        }//end if null        

    }

    /**
     * Delete a NodeInput Connection.
     *
     * @param conn Connection to delete.
     */
    public void deleteConnection(NodeConnection conn) {

        //get teh input
        NodeInput input = conn.getNodeInput();

        //clear the highlight
        conn.clearHighlight();
        //disconnect input/output
        conn.disconnect();

        //remove from connection list
        if (_connections.contains(conn)) {
            _connections.remove(conn);
        }

        //update input node (output not effected)
        //input.getParent().evaluateNode();
        evalHook(input.getParent());

        this.repaint();

        //flag file change
        _fileHandler.setDirty();

    }

    /**
     * Delete a Node.
     *
     * @param node Node to delete.
     */
    public void deleteNode(Node node) {

        if (_nodes.contains(node)) {

            ArrayList<NodeConnection> connections = new ArrayList<>();
            //remove all connections...
            ArrayList<NodeInput> inputs = node.getInputs();

            for (NodeInput input : inputs) {
                //if it has a connection, delete it
                if (input.hasConnection()) {
                    //this.deleteConnection(input);
                    connections.add(input.getConnection());
                }
            }//end for

            ArrayList<NodeOutput> outputs = node.getOuputs();

            //prep for reuse
            inputs.clear();

            //gather all inputs to be disconnected - accessing them from
            //this loop will error, sincewe are removing them from here...
            for (NodeOutput output : outputs) {
                //ArrayList<NodeInput> tmpInputs = output.getConnections();
                //inputs.addAll(tmpInputs);

                connections.addAll(output.getNodeConnections());
            }//end for outputs - generally one...

            //now remove all connections
            for (NodeConnection conn : connections) {
                this.deleteConnection(conn);
            }//end for 

            //delete the input
            //System.out.println("Deleting Node: " + node.getName());
            _nodes.remove(node);

            //remove from spatial index
            _quadTree.deleteNode(node);

            //update the bounds
            updateNodeBounds();
            //repaint
            this.repaint();

            //flag file change
            _fileHandler.setDirty();

        }

    }//end delete node

    /**
     * Delete an Optional Input from a node
     *
     * @param input NodeINput to delete
     */
    public void deleteOptionalInput(NodeInput input) {

        if (input.isOptional()) {

            if (input.hasConnection()) {
                NodeConnection conn = input.getConnection();
                deleteConnection(conn);
            }

            Node parent = input.getParent();
            parent.removeInput(input);

            //evaluate the node
            //parent.evaluateNode();
            evalHook(parent);

            this.repaint();

            //flag file change
            _fileHandler.setDirty();

        }

    }

    /**
     * Add an Optional Input to the Node.
     *
     * @param node Node to add optional input to.
     */
    public void addOptionalInput(Node node) {

        if (node.allowOptionalInputs()) {
            node.addOptionalInput();
            //evaluate the node
            //node.evaluateNode();
            evalHook(node);

            this.repaint();

            //flag file change
            _fileHandler.setDirty();

        }

    }

    /**
     * Rename a Node. This changes the display name on screen just to add some
     * more meaningful labels for users.
     *
     *
     * @param node Node to rename.
     */
    public void renameNode(Node node) {

        String result = null;

        String cName = node.getName();

        String title = "Rename Node :: " + cName;
        String prompt = "Name = " + cName + ", New Name:";

        //show dialog
        result = JOptionPane.showInputDialog(this, prompt, title, JOptionPane.PLAIN_MESSAGE);

        //ge result on OK
        if (result != null) {
            node.setName(result);
            this.repaint();

            //flag file change
            _fileHandler.setDirty();

        }

    }//edn rename node

    /**
     * Set the static value of a NodeInput. Input type is looked at and
     * different methods of setting the value are presented for special cases.
     * Note: NodeInputs that do not accept static values should be filtered out
     * and not called here. Currently, no menu option is presented for
     * non-static inputs, so this can't be reached.
     *
     *
     * @param input The NodeInput to set the static value of.
     */
    public void setStaticValue(NodeInput input) {

        DataType dt = input.getDataType();
        String result = null;

        if (dt == DataType.FILE_NAME) {

            String scadPath = _fileHandler.getSCADFileName();

            if (!scadPath.isEmpty()) {
                result = scadPath;
            }

        } else if (dt == DataType.BOOLEAN) {

            String cBool = input.getInputValue();
            result = cBool.equals("true") ? "false" : "true";

        } else {

            String cval = input.getStaticValue();
            String title = "Set Static Value :: " + input.getName();
            String prompt = "Description:\n" + input.getDescription() + "\nValue = " + cval + ", New Value:";

            //show dialog
            result = JOptionPane.showInputDialog(this, prompt, title, JOptionPane.PLAIN_MESSAGE);

        }

        //get result on OK
        if (result != null) {
            input.setStaticValue(result);
            //input.getParent().evaluateNode();
            evalHook(input.getParent());

            //flag file change
            _fileHandler.setDirty();

        }
        //set static value
        //System.out.println("--- setStatic = " + result);

    }

    /**
     * Duplicates a Node and all it's input connections.
     * Using the serialization technique.
     *
     * @param n Node to Duplicate
     */
    public void duplicateNode(Node n) {

        try {
            ByteArrayOutputStream bytearrayOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(bytearrayOutStream);
            objOutStream.writeObject(n);
            objOutStream.flush();
            objOutStream.close();
            bytearrayOutStream.close();
            byte[] nodeData = bytearrayOutStream.toByteArray();

            ByteArrayInputStream baInStream = new ByteArrayInputStream(nodeData);
            Node dupNode = (Node) new ObjectInputStream(baInStream).readObject();

            //tweak some things....
            //new unique id
            String newId = generate_id();
            dupNode.setId(newId);

            //clear outputs - there are no connections for these
            ArrayList<NodeOutput> outputs = dupNode.getOuputs();

            for (NodeOutput output : outputs) {
                output.setId(generate_id());
                output.getNodeConnections().clear();
            }

            //get inputs
            ArrayList<NodeInput> inputs = dupNode.getInputs();

            for (NodeInput input : inputs) {

                //id for this input (for connections)
                String inId = input.getId();
                //create new id
                input.setId(generate_id());

                if (input.hasConnection()) {

                    //remove input connection
                    //connection and attached output are cloned and invalid...
                    input.removeInputConnection();

                    
                    //find the valid (not cloned) NodeOutput 
                    //from the node that is being duplicated
                    NodeOutput connOut = getConnOutput(n, inId);

                    if (connOut != null) {
                        //create connection from original to this
                        createConnection(connOut, input);
                    }

                }

            }

            //offset the position
            dupNode.translate(25, 25);

            //finally add the node
            addNode(dupNode);

        } catch (Exception ex) {

            System.out.println("***Error: Failed to duplicate node: " + ex.getMessage());
        }

    }

    /**
     * get the connected output node from a Node's inputs where the ID matches
     * the NodeInput.
     *
     * @param n Node to search inputs for.
     * @param id Id of NodeInput to reference
     * @return NodeOutput if found , NULL otherwise.
     */
    private NodeOutput getConnOutput(Node n, String id) {

        NodeOutput output = null;

        ArrayList<NodeInput> inputs = n.getInputs();

        for (NodeInput input : inputs) {

            if (input.getId().equals(id)) {

                if (input.hasConnection()) {
                    NodeConnection conn = input.getConnection();
                    output = conn.getNodeOutput();
                }
            }//end if id = id
        } //end for inputs  
        return output;
    }

    /**
     * Set the Automatic Pan if the mouse is close to the edge.
     * @param e MouseEvent passed from the mouse drag event
     */
    private void automaticPanSet(MouseEvent e) {
        
        //edge region (outside of these bounds)
        Rectangle edgeBounds = new Rectangle(this.getBounds());
        int gs = -60;
        edgeBounds.grow(gs, gs);   
        
        int x = e.getX();
        int y = e.getY();
        
        //inside border region
        if (!edgeBounds.contains(x, y)) {

            int dx = 0;
            int dy = 0;
            
            int delta = 6;
            
            
            if (x < edgeBounds.getMinX()) {
                dx = delta;
            } else if (x > edgeBounds.getMaxX()) {
                dx = -delta;
            }

            if (y < edgeBounds.getMinY()) {
                dy = delta;
            } else if (y > edgeBounds.getMaxY()) {
                dy = -delta;
            }

            //set the pan incrment values
            _autoPanDelta.setLocation(dx, dy);

            if(!_autoPanTimer.isRunning()) {
                _autoPanTimer.setDelay(10);
                _autoPanTimer.setRepeats(true);
                _autoPanTimer.start();
            }
            

        } else {

            automaticPanClear();

        }//end if contains        
        
        
        
        
    }
    
    /**
     * Clear the Automatic pan timer.  This stops the AtutomaticPan timer that 
     * is initiated when user drags at edge.
     */
    private void automaticPanClear() {
        
            if(_autoPanTimer.isRunning()) {
                _autoPanTimer.stop();
                _autoPanTimer.setRepeats(false);
            }        
        
    }     
    /**
     * Event to handle AutoPan Timer actions.
     * @param e Action Event object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //System.out.println("Automatic Pan Timer Called.");
        _camera.panCamera(_autoPanDelta.x, _autoPanDelta.y);
        
        //update drag coords
        leftMouseDragged();

    }    
    
   
    
    /**
     * Generate a unique ID for Nodes,Inputs, and Outputs. Simple global id
     * generator. Not really used currently.
     *
     * @return Unique ID for a Node, Input, or Output.
     */
    public static String generate_id() {
        final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz0123456789";
        final int a_len = alpha.length();
        final int id_len = 24;
        final Random r = new Random();
        String id = "";

        for (int i = 0; i < id_len; i++) {
            int j = r.nextInt(a_len - 1);
            id += alpha.charAt(j);
        }
        return id;
    }

}//end classs
