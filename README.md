#Node Graph for OpenSCAD.#

![UI Image](/images/NodeGraphInterfaceUI.png)
![Output Image](/images/NodeGraphOutput.png)

##A Node Graph interface for OpenSCAD##

The Node Graph allows the user to place and connect nodes that represent parts of the OpenSCAD language.  In essence, this program acts as a GUI overlay to the OpenSCAD Language.  Nodes can represent anything from atomic values (numbers, variable names, math operators, etc.) to modeling and transformation statements (Sphere, Union, Translate, Rotate, etc.).   


##User Interface##
The interface provided is a bit bare-bones: all work is done through the menu system and other menus.  The main menu can be accessed in the normal location (menu bar in the top left) or by right-clicking in the screen background while not over a Node.  Nodes have their own menus for Node-specific actions and general Node actions (more below).


##Navigation##

Middle Mouse Wheel: Click and drag to pan, rotate the wheel to zoom in or out.  Double click the mouse wheel for zoom extents (frame all Nodes).
Right Mouse: Shift + Right click and drag to pan.  Control + Right click and drag to zoom (vertical or horizontal motion).  Control + Right double click for zoom extents (frame all nodes).


##Nodes##

Nodes are added to the Graph through the "Node" menu (Nodes are added in the Graph at the center of the screen - panning the view changes placement).
Nodes consist of a rectangle with a title at the top, a list of Inputs ( rectangles with labels - refered to as Tiles from here on out) on the left-hand side, and generally a single Output Tile on the right-hand side.  Inputs can generally either store a static value (local to the Node) or have a connection to another Node's Output.  Some Nodes can only have a static value, and others only accept a connection (no static value).  Connections between Nodes are made by clicking and dragging an Output Tile to an Input Tile or vice-versa.  Dragging an Output onto an existing Input connection replaces it.  Outputs can have multiple connections.  Note that not all Inputs and Outputs are compatible, but this will be indicated when a connection is attempted.  In Node Graph nomenclature "Expressions" are used as small part of code or mathematical expressions and "Operations" are blocks of code that require being wrapped in braces {}.  Most nodes will output an expression (or smaller atomic value) or an operation.  When connecting Nodes over a large screen distance, the screen will auto-pan when the connector line is dragged towards the edge.

####Moving Nodes####
Nodes can be moved by clicking and dragging on them with the left mouse button (either on the title or any place where there are no Inputs or Outputs).  The screen will auto-pan when Nodes are dragged towards the edge.

####Node Menus####
Nodes and Node Inputs have their own menus.  The Node menu can be accessed by right-clicking on the Node title or any area that is not covered by an Input or Output.  Common menu items are "Rename", "Delete", and "Duplicate". 

The Node Input menu is accessible by right-clicking on the Input Tile. Note: there are conditions where where there can be no menu options (if no menu shows for the Input, this is by design).  Static values (local values), when permitted,  are set through the menu option "Set Static Value".  Connections, when present, can be removed with the menu option "Delete Connection".  If there is a connection present, "Set Static Value" will not be available.  Connections must be deleted before a static value can be set.

Currently, there is no menu for Node Outputs.


####Renaming Nodes####
Nodes can be renamed to help the user annotate their model.  Rename the Node by using the Node context menu.

####Duplicating Nodes####
Nodes can be duplicated using the Duplicate menu option in the Node context menu.  All Input connections are duplicated.  They can be discconnected manually.

###Optional Inputs###
Some Nodes have optional Inputs, i.e.:  geometry Nodes (Sphere, Cube, etc.) allow for an Optional Input of "FragmentSpec" which allows for locally specified values of $fn, $fa, and $fs.  Nodes like Vector and MergeBlock allow for a user specifed number of Inputs.  Optional Inputs are added through the Node's context menu.  If the Node has, or allows, Optional Inputs, a menu item will be present in the Node context menu.  Optional Inputs can be removed through the Optional Input's context menu.  Any Optional Input will have a "Delete Input" menu item present that will remove the Input.

##Node Information##
To get information about a Node, Node Input, or Node Output, simply hover over the Node or Tile.

##Integration to OpenSCAD##
SCADOutput nodes are the mechanism that interfaces with OpenSCAD.  Every Node Graph must have at least one SCADOutput Node (Node Menu->Output->SCADOutput).  The output file (OpenSCAD file) can be set though the Node Input context menu by selecting "Set Static Value" (a file browser will be launched).  Once the SCADOutput target has been set, open the resultant file in OpenSCAD.  When the file is open in OpenSCAD, any changes made to the Node Graph will be updated in OpenSCAD when "Automatic Reload and Preview" is enabled in the OpenSCAD Design menu.  A node Graph may contain multiple SCADOutput Nodes (do not point them to the same OpenSCAD file), i.e.: one SCADOutput may export your full model, while another can export a projection or section of your model.  Both files can viewed at the same time in different OpenSCAD windows.


##OpenSCAD Language Support##
Although a large portion of the OpenSCAD language is supported, the full language is not supported through the Node Graph interface.  For any functions or features missing, it is reccommended to use  a combination of UserExpression, MergeExpression, and MergeBlock Nodes (Node Menu->Utilities) These Nodes should allow for the user to fill any feature gaps in this software.  Keep in mind that this software is really just a "guided" string concatenator, and these three Nodes are equivalant to: String (UserExpression), String + String (MergeExpression), and Line + NewLine(\n) + Line (MergeBlock).  Yes, you can build everything from these three nodes and a SCADOutput Node.  

See also: FunctionCall Node (Node Menu->Language).

##Notes and Tips##

###Replacing Connections###
If a Node Input has an existing connection, you can simply replace it by draggingg a new connection onto the Input.  


###Convert Expression to Operation###
There may be a rare case that you find you need an OperationInput to accept an ExpressionOutput.   In this case, the Expression can be connected to a MergeBlock Node with a single input (Node Menu->Utilities).  MergeBlock will output an Operation.


###Multiple Outputs to an SCADOutput###
If you have a case where you want to run multiple Outputs to your OpenSCAD file, i.e.: Declaring global variables before modeling operations, MergeBlock was designed just for this purpose (Node Menu->Utilities).  It can also be used in the same manner inside loops (For) or other operations (transforms, etc.).

##Additioanl Documentation##

Please refer to the accompaning javadocs for  additional documentation.

