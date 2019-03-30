package org.ngi;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * File IO Handling helper to remove the file functions from the main body.
 *
 *
 * @author Tom
 */
public class FileHandler {

    private boolean _fileDirty;
    private String _filePath;

    private JFrame _owner;
    private NodeGraph _nodeGraph;

    public FileHandler(JFrame owner, NodeGraph nodeGraph) {

        _owner = owner;
        _nodeGraph = nodeGraph;

        _fileDirty = false;

        _filePath = "";

    }

    /**
     * Flag the file as being modified.
     */
    public void setDirty() {

        _fileDirty = true;

        setWindowTitle();
    }

    /**
     * Allow external check of file modification - used when closing.
     *
     * @return True if the file has unsaved changes, false otherwise.
     */
    public boolean isDirty() {
        return _fileDirty;
    }

    /**
     * Close procedure to check for dirty file and save as needed or signal an
     * abort on window close by returning false.
     *
     * @return True if window can close, or false if window should not close.
     */
    public boolean closeProcedure() {

        return fileDirtyCheck();

    }

    /**
     * Set the file path to display. If the file has not been saved, the file
     * path will be a message.
     *
     */
    public void setWindowTitle() {

        String title = " - ";
        title += _fileDirty ? "*!*" : "";

        if (_filePath.isEmpty()) {

            title += "<file not saved>";
        } else {
            title += _filePath;
        }

        _owner.setTitle(title);

    }

    /**
     * Start a new File
     */
    public void newFile() {

        if (fileDirtyCheck()) {
            //cleaar the node graph
            _nodeGraph.resetAll();

            _filePath = "";
            _fileDirty = false;

            setWindowTitle();
        }

    }

    /**
     * Open a saved Graph Node.
     */
    public void openFile() {

        if (fileDirtyCheck()) {

            _filePath = getFileName("Open NodeGraph", true);

            //cancel or something...
            if (_filePath.isEmpty()) {
                return;
            }

            //deserialize the nodes
            deserializeFile(_filePath);

            //flag as clean
            _fileDirty = false;

            setWindowTitle();
        }

    }

    /**
     * Check to see if the file is dirty (has unsaved changes) and prompt to
     * save.
     *
     * @return True if ok to proceed with calling operation, or false if the
     * user canceled along the way.
     */
    private boolean fileDirtyCheck() {

        boolean proceed = false;

        //check for dirty
        if (_fileDirty) {
            //prompt to save....
            int dialogResult = JOptionPane.showConfirmDialog(_owner, "The current Node Graph has unsaved changes, save changes?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (dialogResult) {
                case JOptionPane.YES_OPTION:
                    //try to save the file
                    proceed = saveFile();
                    break;

                case JOptionPane.NO_OPTION:
                    //discard changes
                    proceed = true;
                    break;

                case JOptionPane.CANCEL_OPTION:
                    //operation canceled
                    proceed = false;
                    break;

            }//end switch

        } else {
            //no changes to save
            proceed = true;
        }//end if dirty        

        return proceed;

    }

    /**
     * Save the current Node Graph file. This will prompt for a file if none is
     * specified.
     * @return True if process can proceed, halt on false (window closing ).
     */
    public boolean saveFile() {

        if (_filePath.isEmpty()) {

            _filePath = getFileName("Save NodeGraph", false);
        }

        //cancel or something...
        if (_filePath.isEmpty()) {
            return false;
        }

        //serialize the nodes
        serializeNodes(_filePath);

        //flag as clean
        _fileDirty = false;

        setWindowTitle();

        //save success
        return true;

    }//end save file

    /**
     * Save the current NodeGraph to a new file.
     *
     */
    public void saveFileAs() {

        //get the new filename
        String newFileName = getFileName("Save NodeGraph As", false);

        //cancel or something...
        if (newFileName.isEmpty()) {
            return;
        }

        //set the active filepath after checking for cancel, etc.
        _filePath = newFileName;
        
        //serialize the nodes
        serializeNodes(_filePath);

        //flag as clean
        _fileDirty = false;

        setWindowTitle();

    }//end save file    

    /**
     * Prompt the user for a file to save.
     *
     * @param title The title of the dialog box: save or open, etc.
     * @param load Use true to open a file, false to save or saveas.
     * @return The full file path selected if successful or an empty string if
     * not.
     */
    private String getFileName(String title, boolean load) {

        String fdesc = "*." + NodeGraph.FILE_EXTENSION + " (Node Graph files)";
        DialogFileFilter dff = new DialogFileFilter(NodeGraph.FILE_EXTENSION, fdesc);
        JFileChooser fileDialog = new JFileChooser();

        fileDialog.setDialogTitle(title);

        fileDialog.setFileFilter(dff);
        fileDialog.setMultiSelectionEnabled(false);

        String fname = "";

        String cpath = _filePath;
        File cFile = null;

        if (!cpath.isEmpty()) {
            cFile = new File(cpath);
            fileDialog.setCurrentDirectory(cFile.getParentFile());
        }

        int result = JFileChooser.CANCEL_OPTION;

        if (load) {
            result = fileDialog.showOpenDialog(null);
        } else {
            result = fileDialog.showSaveDialog(null);
        }

        if (result == JFileChooser.APPROVE_OPTION) {

            File selFile = fileDialog.getSelectedFile();

            String selFilePath = selFile.getPath();

            String suffix = "." + NodeGraph.FILE_EXTENSION;

            if (!selFilePath.endsWith(suffix)) {
                selFilePath += suffix;
            }

            //String selFileName = selFile.getName();
            fname = selFilePath;
        }

        return fname;

    }

    /**
     * Get the Output CAD file path - used specifically by the File Output Node.
     *
     * @return Path of the SCAD output file.
     */
    public String getSCADFileName() {
        String ext = "scad";
        String fdesc = "*." + ext + " (OpenSCAD files)";
        DialogFileFilter dff = new DialogFileFilter(ext, fdesc);
        JFileChooser fileDialog = new JFileChooser();

        fileDialog.setDialogTitle("SCAD File Output");

        fileDialog.setFileFilter(dff);
        fileDialog.setMultiSelectionEnabled(false);

        String fname = "";

        String cpath = _filePath;
        File cFile = null;

        if (!cpath.isEmpty()) {
            cFile = new File(cpath);
            fileDialog.setCurrentDirectory(cFile.getParentFile());
        }

        int result = JFileChooser.CANCEL_OPTION;
        result = fileDialog.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selFile = fileDialog.getSelectedFile();

            String selFilePath = selFile.getPath();

            String suffix = "." + ext;

            if (!selFilePath.endsWith(suffix)) {
                selFilePath += suffix;
            }

            //String selFileName = selFile.getName();
            fname = selFilePath;
        }

        return fname;

    }

    /**
     * Serial the Nodes using basic java serialization.
     *
     * @param fileName
     *
     */
    private void serializeNodes(String fileName) {

        boolean saveOk = false;
        String errMssg = "";

        try {

            //get the nodes from the Nodegraph
            ArrayList<Node> nodes = _nodeGraph.getNodes();

            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(nodes);
            out.close();
            fileOut.close();
            saveOk = true;

            System.out.println("Serialized data is saved in:" + fileName);

        } catch (IOException i) {

            errMssg = "File IO Exception: " + i.getMessage();
            System.out.println("*** Serialized Error Occured:" + i.getMessage() + "***");

        }

        //check for errors
        if (!saveOk) {

            JOptionPane.showMessageDialog(null, "Error saving file:" + fileName + ":\n " + errMssg);

        }

    }

    /**
     * read a serialized Graph Node from Disk.
     *
     * @param fileName The name of the file to read.
     */
    private void deserializeFile(String fileName) {

        ArrayList<Node> dsNodes = null;

        boolean openOk = false;
        String errMssg = "";

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            dsNodes = (ArrayList<Node>) in.readObject();
            in.close();
            fileIn.close();

            //set the nodes in the NodeGraph
            _nodeGraph.setNodes(dsNodes);

            openOk = true;

        } catch (IOException ioEx) {

            errMssg = "File IO Exception: " + ioEx.getMessage();

            System.out.println("***Deserialize IOException*** " + errMssg);

        } catch (ClassNotFoundException cnfEx) {

            errMssg = "Class Not Found Exception: " + cnfEx.getMessage();
            errMssg += "\nThis file was made with an incompatible version of the NodeGraph.";

            System.out.println("***Deserialize ClassNotFoundException*** " + errMssg);

        }

        //check for errors
        if (!openOk) {

            JOptionPane.showMessageDialog(null, "Error opening file:" + fileName + ":\n " + errMssg);

        }

    }

}
