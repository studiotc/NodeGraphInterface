package org.ngi.menus;

import org.ngi.menus.actions.file.FileMenuSaveAction;
import org.ngi.menus.actions.file.FileMenuNewAction;
import org.ngi.menus.actions.file.FileMenuOpenAction;
import org.ngi.menus.actions.file.FileMenuSaveAsAction;
import org.ngi.menus.actions.AddNodeMenuAction;
import org.ngi.menus.actions.SetStaticValueAction;
import org.ngi.menus.actions.DuplicateNodeAction;
import org.ngi.menus.actions.DeleteConnectionAction;
import org.ngi.menus.actions.DeleteOptionalInputAction;
import org.ngi.menus.actions.RenameNodeAction;
import org.ngi.menus.actions.DeleteNodeAction;
import org.ngi.menus.actions.AddOptionalInputAction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.ngi.FileHandler;
import org.ngi.Node;
import org.ngi.NodeGraph;
import org.ngi.NodeInput;
import org.ngi.enums.InputDataRequirements;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *  Main menu system for building the main menus and the Node menus.
 * @author Tom
 */
public class MenuSystem {

    private NodeGraph _nodeGraph;
    private FileHandler _fileHandler;

    private final JPopupMenu _mainPopupMenu;
    private final JMenuBar _mainMenuBar;

    private JPopupMenu _nodeInputContextMenuPU;

    private JPopupMenu _nodeContextMenuPU;

    private final DeleteConnectionAction _nodeInputDeleteAction;
    private final SetStaticValueAction _nodeInputSetStaticAction;

    private final DeleteNodeAction _nodeDeleteAction;
    private final RenameNodeAction _nodeRenameAction;
    private final DuplicateNodeAction _nodeDuplicateAction;

    private final AddOptionalInputAction _nodeAddOptInputAction;
    private final DeleteOptionalInputAction _nodeDeleteOptInputAction;

    public MenuSystem(NodeGraph nodeGraph, FileHandler fileHandler) {

        _nodeGraph = nodeGraph;

        _fileHandler = fileHandler;
                
        _mainPopupMenu = new JPopupMenu();
        _mainMenuBar = new JMenuBar();

        //constructMainMenus(_mainPopupMenu, nodeGraph);
        constructMainMenus(_mainPopupMenu);

        constructMainMenus(_mainMenuBar);

        //delete connection action
        _nodeInputDeleteAction = new DeleteConnectionAction(nodeGraph);
        _nodeInputSetStaticAction = new SetStaticValueAction(nodeGraph);

        _nodeDeleteAction = new DeleteNodeAction(nodeGraph);
        _nodeRenameAction = new RenameNodeAction(nodeGraph);
        _nodeDuplicateAction = new DuplicateNodeAction(nodeGraph);

        _nodeAddOptInputAction = new AddOptionalInputAction(nodeGraph);

        _nodeDeleteOptInputAction = new DeleteOptionalInputAction(nodeGraph);

        //init node context menu
        _nodeContextMenuPU = new JPopupMenu();
        _nodeInputContextMenuPU = new JPopupMenu();

        //System.out.println("menu system init");
    }

    

    /**
     * Construct the menu for the given container.  This works for the main popup and the menu bar menu.
     * @param comp  The Popup menu or main menu bar to the menu to.
     * @param ng The Nodegraph for reference.
     */
    private void constructMainMenus(JComponent comp) {

 
        //construct the file menu
        constructFileMenu(comp);

        //construct the Node menu
        constructNodeMenu(comp);

    }
        
    
    
    /**
     * Construct the Node menu.  The Add submenu is constructed from the menuNodes.txt in the resources.  
     * The file is generated from a list of all the java files under org\ngi\nodes. 
     * 
     * first step - generate all the file names excluding the base types.
     * dos command to generate base list from: <project path>/src/org/ngi/nodes
     * dir *.java /S /B | find /V "Base" > ../resources/menuNodes.txt
     * 
     * second step - strip out the leading path up to (but not including)
     * org/ngi (this is the root of the remaining path).
     * i.e: "org\ngi\nodes\geometry\Sphere.java"
     * 
     * @param mainMenu The menu to attach the node menu to.
     * 
     */
    private void constructNodeMenu(JComponent mainMenu) {

        /**
         * Begin Node Menu
         */
        JMenu nodeMenu = new JMenu("Node");        
        mainMenu.add(nodeMenu);
        
        /*  Add menu - root of dynamically loaded menu for nodes*/
        

        HashMap<String, JMenu> menus = new HashMap<>();

        try {

            //read text file
            InputStream is = this.getClass().getResourceAsStream("/org/ngi/resources/menuNodes.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String strLine;

            //read the file lines
            while ((strLine = br.readLine()) != null) {

                //trim the line
                strLine = strLine.trim();
                //check for empty
                if (!strLine.isEmpty()) {

                    //starting parent menu
                    JMenu curMenu = nodeMenu; //addNodeMenu;
                    //convert paths to periods (dot)
                    String nodeCreateStr = strLine.replace("\\", ".");
                    //remove extension
                    nodeCreateStr = nodeCreateStr.replace(".java", "");
                    //remove package name prefixes
                    String menuDescStr = nodeCreateStr.replace("org.ngi.nodes.", "");

                    //spilt by "." literal
                    String[] stack = menuDescStr.split("\\.");

                    int sl = stack.length;
                    String menuPath = "";
                    String curMenuName = "";
                    //menu item name is last
                    String name = stack[sl - 1];

                    //loop through menu path to makesure all meus exist
                    for (int i = 0; i < sl - 1; i++) {

                        curMenuName = stack[i];
                        if (!menuPath.isEmpty()) {
                            menuPath += ".";
                        }
                        menuPath += curMenuName;

                        //System.out.println("--get menu :" + curMenuName + " @ " + menuPath);
                        //get the menu and create if not present
                        JMenu menu = getMenu(menus, curMenu, menuPath, curMenuName);
                        //menuStack[i] = menu;
                        //make menu current
                        curMenu = menu;

                    }//end for

                    //create and add menu item to last menu created (or available)
                    AddNodeMenuAction anma = new AddNodeMenuAction(_nodeGraph, name, nodeCreateStr);
                    JMenuItem menuItem = new JMenuItem(anma);
                    curMenu.add(menuItem);


                }//end if empty string  

            }//end while           

        } catch (IOException ex) {

            System.out.println("***ERROR READING TEXT FILE*** " + ex);
        }


    }

    /**
     * Get a menu from the passed HashMap.  If Menu does not exist, it is created and the parent is set.
     * @param menus  Collection of menus.
     * @param parent If menu is created, this menu is used as the parent.
     * @param path The menu path serves as the HashMap key.
     * @param name Name of the menu to be used if menu is created.
     * @return The menu specified attached to the parent.
     */
    private JMenu getMenu(HashMap<String, JMenu> menus, JMenu parent, String path, String name) {

        JMenu menu = null;

        if (menus.containsKey(path)) {

            menu = menus.get(path);

        } else {

            //init menu
            menu = new JMenu(prettyMenuName(name));
            //set parent
            parent.add(menu);
            //add to hashmap
            menus.put(path, menu);

            //System.out.println("---Added Menu :" + name + " to " + path);
        }

        return menu;
    }


    /**
     * Make a pretty (capitalized ) name from a simple menu name.
     * @param name The string to make pretty.
     * @return The pretty name.
     */
    private String prettyMenuName(String name) {
        
        if(name == null ) return "<NULL Name>";
        
        String pName = name.trim();
        
        if(pName.isEmpty()) return"<Empty Name>";
        
        int plen = pName.length();
        char f = pName.charAt(0);
        f = Character.toUpperCase(f);
        pName = f + pName.substring(1, plen);
        
        return pName;
        
    }

 
    /**
     * Construct the File Menu.
     * @param comp  Main menu to parent to.
     * @param ng NodeGraph for file operation calls.
     */
    private void constructFileMenu(JComponent comp) {
        
       JMenu fileMenu = new JMenu("File");

        JMenuItem fileNewMenuItem = new JMenuItem(new FileMenuNewAction(_fileHandler));
        JMenuItem fileOpenMenuItem = new JMenuItem(new FileMenuOpenAction(_fileHandler));
        JMenuItem fileSaveMenuItem = new JMenuItem(new FileMenuSaveAction(_fileHandler));
        JMenuItem fileSaveAsMenuItem = new JMenuItem(new FileMenuSaveAsAction(_fileHandler));

        fileMenu.add(fileNewMenuItem);
        fileMenu.add(fileOpenMenuItem);
        fileMenu.add(fileSaveMenuItem);
        fileMenu.add(fileSaveAsMenuItem);

        //add the file menu
        comp.add(fileMenu);

    }
    

    /**
     * Show the main menu at the specified location.
     * @param x  Menu x location.
     * @param y  Menu y location.
     */
    public void showMainPopup(int x, int y) {
        //
        _mainPopupMenu.show(_nodeGraph, x, y);

    }

    /**
     * Get the Menu Bar Object for attaching to them main frame.
     * @return MenuBar object with the standard menus.
     */
    public JMenuBar getMenuBar() {
        return _mainMenuBar;
    }

    
    
    
    
    /**
     * Show the NodeInput context menu (right-click menu).  This is the NodeInput menu 
     * that contains all the menus items specific to NodeInputs.  Them menu
     * items are determined by the type and state of the NodeInput, i.e.: Set Static Value,
     * Delete Connection, Delete Optional Input,etc.
     * @param nodeInput  NodeINput object to generate menu items for,
     * @param x Menu x location.
     * @param y Menu y location.
     */
    public void showNodeInputContextMenu(NodeInput nodeInput, int x, int y) {

        _nodeInputContextMenuPU.removeAll();

        if (nodeInput.hasConnection()) {

            _nodeInputDeleteAction.setInputNode(nodeInput);

            JMenuItem delConnMI = new JMenuItem();
            delConnMI.setAction(_nodeInputDeleteAction);
            _nodeInputContextMenuPU.add(delConnMI);

        } else {

            if (nodeInput.getDataRequirements() != InputDataRequirements.NOT_NULL_CONN) {

                _nodeInputSetStaticAction.setInputNode(nodeInput);

                JMenuItem setStaticMI = new JMenuItem();
                setStaticMI.setAction(_nodeInputSetStaticAction);
                _nodeInputContextMenuPU.add(setStaticMI);

            }

        }

        //check to see if it is optional
        if (nodeInput.isOptional()) {

            _nodeDeleteOptInputAction.setInputNode(nodeInput);

            JMenuItem setStaticMI = new JMenuItem();
            setStaticMI.setAction(_nodeDeleteOptInputAction);
            _nodeInputContextMenuPU.add(setStaticMI);

        }

        //show the popup menu
        _nodeInputContextMenuPU.show(_nodeGraph, x, y);

    }

    
    /**
     * Show the Node context menu.  Contains standard Node menu items,i.e.: Rename and Delete.
     * If the Node permits optional inputs, this will be a menu item here (Add Optional Input).
     * @param node  Node to generate menu items for.
     * @param x  Menu x location.
     * @param y  Menu y location.
     */
    public void showNodeContextMenu(Node node, int x, int y) {

        //reset the menu
        //_nodeContextMenuPU = new JPopupMenu();
        _nodeContextMenuPU.removeAll();

        _nodeRenameAction.setNode(node);
        _nodeDeleteAction.setNode(node);
        _nodeDuplicateAction.setNode(node);

        JMenuItem renameNodeMI = new JMenuItem();
        renameNodeMI.setAction(_nodeRenameAction);

        JMenuItem deleteNodeMI = new JMenuItem();
        deleteNodeMI.setAction(_nodeDeleteAction);
        
        JMenuItem duplicateNodeMI = new JMenuItem();
        duplicateNodeMI.setAction(_nodeDuplicateAction);        

        _nodeContextMenuPU.add(renameNodeMI);        
        _nodeContextMenuPU.add(duplicateNodeMI);

        //if node allows optional inputs
        if (node.allowOptionalInputs()) {

            _nodeContextMenuPU.addSeparator();
            _nodeAddOptInputAction.setNode(node);
            JMenuItem addOptionalMI = new JMenuItem("Add Input");
            addOptionalMI.setAction(_nodeAddOptInputAction);
            _nodeContextMenuPU.add(addOptionalMI);
        }

        _nodeContextMenuPU.addSeparator();
        _nodeContextMenuPU.add(deleteNodeMI);
        
        _nodeContextMenuPU.show(_nodeGraph, x, y);

    }

}
