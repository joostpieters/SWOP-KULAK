package UI.swingGUI;

import controller.HandlerFactory;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JOptionPane;


/**
 * This frame forms the entry point for the entire application.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class MainFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -2541384231489389714L;

    private final HandlerFactory controller;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Creates new form MainFrame
     *
     * @param controller The controller to use to pass the requests to
     */
    public MainFrame(HandlerFactory controller) {
        this.controller = controller;
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createProjectButton = new javax.swing.JButton();
        listAllProjectsButton = new javax.swing.JButton();
        createNewTaskButton = new javax.swing.JButton();
        planTaskButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        updateTaskButton = new javax.swing.JButton();
        modifySystemTimeButton1 = new javax.swing.JButton();
        simulationButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Taskman - A Project-Oriented Task Manager");

        createProjectButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        createProjectButton.setText("Create New Project");
        createProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewProject(evt);
            }
        });

        listAllProjectsButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        listAllProjectsButton.setText("List All Projects");
        listAllProjectsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listProjects(evt);
            }
        });

        createNewTaskButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        createNewTaskButton.setText("Create New Task");
        createNewTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewTask(evt);
            }
        });

        planTaskButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        planTaskButton.setText("Plan Task");
        planTaskButton.setToolTipText("");
        planTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planTask(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("TaskMan");

        updateTaskButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        updateTaskButton.setText("Update Task");
        updateTaskButton.setToolTipText("");
        updateTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTask(evt);
            }
        });

        modifySystemTimeButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        modifySystemTimeButton1.setText("Modify System Time");
        modifySystemTimeButton1.setToolTipText("");
        modifySystemTimeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifySystemTime(evt);
            }
        });

        simulationButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        simulationButton.setText("Run Simulation");
        simulationButton.setToolTipText("");
        simulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSimulation(evt);
            }
        });

        loginButton.setForeground(new java.awt.Color(0, 204, 51));
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listAllProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createNewTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modifySystemTimeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(updateTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createProjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(planTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(simulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
            .addGroup(layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginButton)
                .addGap(42, 42, 42))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(loginButton)))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listAllProjectsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createProjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createNewTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(planTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modifySystemTimeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(simulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Start the show projects use case
     * @param evt 
     */
    private void listProjects(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listProjects
        new ShowProjectFrame(controller.getShowProjectHandler()).setVisible(true);
    }//GEN-LAST:event_listProjects
    
    /**
     * Start the create new project use case
     * @param evt 
     */
    private void createNewProject(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewProject
        new CreateProjectFrame(controller.getCreateProjectHandler()).setVisible(true);
    }//GEN-LAST:event_createNewProject
    
    /**
     * Start the create new task use case
     * @param evt 
     */
    private void createNewTask(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewTask
        try {
            new CreateTaskFrame(controller.getCreateTaskHandler()).setVisible(true);
        } catch (IllegalStateException illegalStateException) {
            JOptionPane.showMessageDialog(rootPane, illegalStateException.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createNewTask
    
   
    /**
     * Start the update task use case
     * @param evt 
     */
    private void updateTask(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTask
        new UpdateTasksStatusFrame(controller.getUpdateTaskHandler()).setVisible(true);
    }//GEN-LAST:event_updateTask
    
    /**
     * Start the modify system time use case
     * @param evt 
     */
    private void modifySystemTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifySystemTime
        try {
            String currentTime = controller.getAdvanceSystemTimeHandler().getCurrentTime().format(formatter);
            String timestamp = JOptionPane.showInputDialog(rootPane, "Advance System Time to:", currentTime);
            //nothing entered
            if(timestamp == null){
                return;
            }
            controller.getAdvanceSystemTimeHandler().advanceTime(timestamp);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(rootPane, exception.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_modifySystemTime
    
    /**
     * Start the plan task use case
     * 
     * @param evt 
     */
    private void planTask(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planTask
        (new PlanTaskFrame(controller.getPlanTaskHandler())).setVisible(true);
    }//GEN-LAST:event_planTask
    
    /**
     * Start the run simulation use case
     * 
     * @param evt 
     */
    private void runSimulation(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSimulation
        this.setVisible(false);
        (new RunSimulationFrame(controller)).setVisible(true);
    }//GEN-LAST:event_runSimulation
    
    /**
     * Starts the login use case
     * 
     * @param evt 
     */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if(controller.getLoginHandler().loggedIn()){
            controller.getLoginHandler().logout();
            loginButton.setForeground(new java.awt.Color(0, 204, 51));
            loginButton.setText("Login");
        }else{
            (new LoginFrame(controller.getLoginHandler(), this)).setVisible(true);
        }
        
        
    }//GEN-LAST:event_loginButtonActionPerformed
    
    /**
     * 
     * @return The loginbutton of this frame 
     */
    public JButton getLoginButton(){
        return loginButton;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createNewTaskButton;
    private javax.swing.JButton createProjectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton listAllProjectsButton;
    private javax.swing.JButton loginButton;
    private javax.swing.JButton modifySystemTimeButton1;
    private javax.swing.JButton planTaskButton;
    private javax.swing.JButton simulationButton;
    private javax.swing.JButton updateTaskButton;
    // End of variables declaration//GEN-END:variables
}
