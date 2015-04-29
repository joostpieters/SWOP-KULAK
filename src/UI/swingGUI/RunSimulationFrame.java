package UI.swingGUI;

import controller.HandlerFactory;
import controller.RunSimulationHandler;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;

/**
 * This frame is the UI for the create project use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class RunSimulationFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -8172848294908727774L;
    private final HandlerFactory factory;
    private final RunSimulationHandler handler;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Creates new form CreateProjectFrame
     *
     * @param factory The handlerfactory to use
     */
    public RunSimulationFrame(HandlerFactory factory) {
        initComponents();
        this.factory = factory;
        handler = factory.getSimulationHandler();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        createTaskSimulatorButton = new javax.swing.JButton();
        PlanTaskButton = new javax.swing.JButton();
        showProjectsSimulator = new javax.swing.JButton();
        carryOutSimulationButton = new javax.swing.JButton();
        endSimulationButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+15f));
        jLabel2.setText("Run a Simulation");

        createTaskSimulatorButton.setText("Create Task");
        createTaskSimulatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createTaskSimulatorButtonActionPerformed(evt);
            }
        });

        PlanTaskButton.setText("Plan Task");

        showProjectsSimulator.setText("Show Projects");
        showProjectsSimulator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showProjectsSimulatorActionPerformed(evt);
            }
        });

        carryOutSimulationButton.setBackground(new java.awt.Color(153, 255, 102));
        carryOutSimulationButton.setForeground(new java.awt.Color(0, 153, 51));
        carryOutSimulationButton.setText("Carry out simulation");
        carryOutSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                carryOutSimulationButtonActionPerformed(evt);
            }
        });

        endSimulationButton.setBackground(new java.awt.Color(255, 102, 102));
        endSimulationButton.setForeground(new java.awt.Color(255, 51, 51));
        endSimulationButton.setText("End simulation");
        endSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endSimulationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(205, 205, 205)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(174, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(endSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(carryOutSimulationButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(createTaskSimulatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(PlanTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(showProjectsSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(29, 29, 29)))))
                        .addGap(0, 193, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(14, 14, 14)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(showProjectsSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(createTaskSimulatorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(PlanTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carryOutSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showProjectsSimulatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showProjectsSimulatorActionPerformed
        new ShowProjectFrame(factory.getShowProjectHandler()).setVisible(true);
    }//GEN-LAST:event_showProjectsSimulatorActionPerformed

    private void createTaskSimulatorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createTaskSimulatorButtonActionPerformed
        new CreateTaskFrame(handler.getCreateTaskSimulatorHandler()).setVisible(true);
    }//GEN-LAST:event_createTaskSimulatorButtonActionPerformed
    
    /**
     * The carry out simulation button is pressed
     * @param evt 
     */
    private void carryOutSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carryOutSimulationButtonActionPerformed
        this.dispose();
        for(Frame frame : JFrame.getFrames()){
            if(frame instanceof MainFrame){
                frame.setVisible(true);
                break;
            }
                
        }
    }//GEN-LAST:event_carryOutSimulationButtonActionPerformed
    
    /**
     * The end simulation button is pressed
     * 
     * @param evt 
     */
    private void endSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endSimulationButtonActionPerformed
        handler.cancelSimulation();
        this.dispose();
         for(Frame frame : JFrame.getFrames()){
            if(frame instanceof MainFrame){
                frame.setVisible(true);
                break;
            }
                
        }
    }//GEN-LAST:event_endSimulationButtonActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton PlanTaskButton;
    private javax.swing.JButton carryOutSimulationButton;
    private javax.swing.JButton createTaskSimulatorButton;
    private javax.swing.JButton endSimulationButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton showProjectsSimulator;
    // End of variables declaration//GEN-END:variables
}
