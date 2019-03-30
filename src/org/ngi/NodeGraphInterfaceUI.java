package org.ngi;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import org.ngi.menus.MenuSystem;
import javax.swing.ImageIcon;

/**
 * This is the UI parent class JFrame for the application.
 *
 * @author Tom
 */
public class NodeGraphInterfaceUI extends JFrame {

    private NodeGraph _nodeGraph;

    private MenuSystem _menuSys;

    /**
     * Constructor for the UI.
     */
    public NodeGraphInterfaceUI() {
        super();

        initComponents();
    }

    /**
     * Initialize the components.
     */
    private void initComponents() {

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
        
        URL iconURL = this.getClass().getResource("/org/ngi/resources/ng_icon.png");
        // iconURL is null when not found
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());

        //node graph panel
        _nodeGraph = new NodeGraph(this);

        //init menu system
        _menuSys = _nodeGraph.getMenuSystem();

        //allow window resizing
        this.setResizable(true);

        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        //close handler
        this.addWindowListener(new WindowHandler());

        //add display (canvas)
        this.add(_nodeGraph, BorderLayout.CENTER);

        //set the menu bar
        this.setJMenuBar(_menuSys.getMenuBar());

        this.pack();

        //make it visible once intialized...
        //do from top level now...
        //this.setVisible(true);
    }

    /**
     * Close Check - wrapper over closing check.  Checks for fileDirty before window close.
     * @return True if ok to close, false otherwise.
     */
    private boolean closeCheck() {

        return _nodeGraph.closeCheck();

    }

    /**
     * Set the Frame title.
     *
     * @param suffix String to append to the main title (the file path name).
     */
    @Override
    public void setTitle(String suffix) {
        super.setTitle("Node Graph Interface for OpenSCAD [Alpha 1.0] " + suffix);
    }

    static class WindowHandler extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent we) {

            //boolean doClose = true;
            NodeGraphInterfaceUI jngUI = (NodeGraphInterfaceUI) we.getWindow();

            // check for dirty files before close.....
            if (jngUI.closeCheck()) {
                System.exit(0);
            }

        }

    }//end class windowHandler  

}//end class JavaNodeGraphUI

