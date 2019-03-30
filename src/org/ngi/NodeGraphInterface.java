package org.ngi;

/**
 *  Java Node Graph application.
 * @author Tom
 */
public class NodeGraphInterface {

    /**
     * @param args the command line arguments (none used)
     */
    public static void main(String[] args) {
        //open the UI via the event queue
            java.awt.EventQueue.invokeLater(() -> {
                NodeGraphInterfaceUI ngui = new NodeGraphInterfaceUI();
                ngui.setVisible(true);
            });
    }
    
}
