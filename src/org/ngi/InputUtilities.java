package org.ngi;

/**
 *  Input utilities for various common testing and string formatting functions.
 * @author Tom
 */
public class InputUtilities {
    
  
    /**
     * Test a string to see if it is a valid number.  String cannot be null or empty,
     * and must parse Float.parseFloat(string).
     * @param numStr  String to test.
     * @return boolean True if the string can be parsed to a number (float), false otherwise.
     */
    public static boolean isValidNumber(String numStr) {
        
        boolean isNumber = false;
   
        //check for null
        if(numStr != null ) {
            //check for empty
            String testStr = numStr.trim();
            if(!testStr.isEmpty()) {
                try {
                    float nVal = Float.parseFloat(testStr);
                    isNumber = true;
                } catch (NumberFormatException e) {
                    isNumber = false;//not needed... shown for clarity
                }        
                     
            }//end if empty
 
        }//end if null
        
        return isNumber;
        
    }
    
    
   /**
    * Test to see if a string can be used as a variable name.
    * Test for null or empty and making sure sure first character is a letter.  
    * All other characters must be a number or letter.
    * @param nameStr String to check.
    * @return True if string can be used as a variable name, false if not valid.
    */
    public static boolean isValidVariableName(String nameStr) {
        
        boolean isOkName = false;
        
        //check for null
        if(nameStr != null) {
            //trim and check for empty
            String testStr = nameStr.trim();
            if(!testStr.isEmpty()) {
 
                int strLen = testStr.length();
                char c0 = testStr.charAt(0);
                //grab first char and test for letter
                if(!Character.isLetter(c0)) return false;
                for(int i = 1; i < strLen;i++) {
                    
                    char c =testStr.charAt(i);
                    if(!Character.isLetterOrDigit(c)) {
                        return false;
                    }
                    
                }
                //made it through...
                isOkName = true;
                
                
            }
            
        }
        
        return isOkName;
        
    }
     
    /**
     * Test to see if string is a valid boolean: either "true" or "false".
     * @param boolStr  String to check.
     * @return True if string is a valid boolean, false otherwise.
     */
    public static boolean isValidBoolean(String boolStr) {
        
        boolean isBool = false;
   
        //check for null
        if(boolStr != null ) {
            //check for empty
            String testStr = boolStr.trim();
            if(!testStr.isEmpty()) {
               if(testStr.equals("true") || testStr.equals("false")) isBool = true;      
            }//end if empty
 
        }//end if null
        
        return isBool;
        
    }    
    
    /**
     * Test to see if string is a valid expression.
     * Only tests to see if string is not null or empty.
     * @param expStr String to check.
     * @return True if string is not null or empty, false otherwise.
     */
    public static boolean isValidExpression(String expStr) {
        
        boolean isExp = false;
        
        //check for null
        if(expStr != null) {
            //trim and check for empty
            String testStr = expStr.trim();
            if(!testStr.equalsIgnoreCase("")) {
                //it's something.... Too Complicated, Didn't Check... TC;DC
                isExp = true; 
            }
            
        }
        
        return isExp;        
        
        
    }
    
    /**
     * Check for non-null or empty string
     * @param str String to check
     * @return True if string is not null or empty.
     */
    public static boolean isValidString(String str) {
        
        boolean isExp = false;
        
        //check for null
        if(str != null) {
            //trim and check for empty
            String testStr = str.trim();
            if(!testStr.equalsIgnoreCase("")) {
                //it's something.... Too Complicated, Didn't Check... TC;DC
                isExp = true; 
            }
            
        }
        
        return isExp;        
        
        
    }    
    
    
    /**
     * Simple function to indent text
     * @param text  Text to indent.
     * @return Indented text.
     */
    public static String indentText(String text) {
        
        String indented = text;
        
        if(!indented.endsWith("\n")) {
            
            indented = "\t" + text.replace("\n", "\n\t") + "\n";
        } else {
             indented = "\t" + text.replace("\n", "\n\t");
             //remove railing tab
             indented = indented.substring(0, indented.length() - 1);
        }
        
        
        
        return indented;
        
        //return  "\t" + text.replace("\n", "\n\t");
        
    }
    
    
    

    
}
