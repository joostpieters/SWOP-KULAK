package init;

import UI.swingGUI.MainFrame;
import controller.HandlerFactory;
import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.GenericUser;
import domain.ProjectContainer;
import domain.time.Clock;
import java.io.FileReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mathias :)
 */
public class Bootstrap {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        
        Clock clock = new Clock();
        Database db = new Database();
        int option = JOptionPane.showConfirmDialog(null, "Would you like to initialize the system with an input file?");
        ProjectContainer manager = new ProjectContainer();
        if (option == 0) {
            initManagerFromFile(manager, clock, db);
        } else if (option == 2) {
            return;
        }
        
        Auth auth = new Auth(db);
        
        auth.registerUser(new GenericUser("root", "admin"));
        String username;
        while(true){
            username = JOptionPane.showInputDialog("Enter your username");
            
            if(username == null || auth.login(username)){
                break;
            }else{
                JOptionPane.showMessageDialog(null, "You entered the wrong credentials.", null, JOptionPane.WARNING_MESSAGE);
            }
                
            
        }
        
        
        Acl acl = new Acl();
        acl.addEntry("admin", Arrays.asList("UpdateTaskStatus", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator", "updateTaskStatus"));
        acl.addEntry("developer", Arrays.asList("UpdateTaskStatus"));
        acl.addEntry("manager", Arrays.asList("CreateTask", "CreateProject", "PlanTask", "RunSimulation", "CreateTask", "CreateTaskSimulator", "PlanTaskSimulator"));
        HandlerFactory factory = new HandlerFactory(manager, clock, auth, acl, db);
        
         //display uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            JOptionPane.showMessageDialog(null, e.getMessage(), null, JOptionPane.WARNING_MESSAGE);
        });
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame(factory).setVisible(true);
        });
    }
    
    /**
     * Show an input file dialog and initialize the given manager from the chosen
     * file.
     * 
     * @param manager The manager to initialize
     */
    private static void initManagerFromFile(ProjectContainer manager, Clock clock, Database db) {
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Task Man inputfile", "tman");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            try (FileReader fileReader = new FileReader(chooser.getSelectedFile())) {

                ProjectContainerFileInitializor fileInitializor = new ProjectContainerFileInitializor(fileReader, manager, clock, db);
                fileInitializor.processFile();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An error occured while reading/processing the file, please try again.", null, JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
