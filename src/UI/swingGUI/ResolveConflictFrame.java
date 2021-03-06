package UI.swingGUI;

import controller.PlanTaskHandler;
import domain.dto.DetailedTask;
import java.awt.CardLayout;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

/**
 * This jFrame handles the update task status use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class ResolveConflictFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -7570577954812578587L;

    private DefaultTableModel taskModel;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final PlanTaskFrame originator;
    private final PlanTaskHandler planHandler;

    /**
     * Creates new form ListProjectsFrame
     *
     * @param originator The frame from which the error came 
     * @param conflictingTasks The conflicting task
     * @param planHandler The handler that handles the planning of a task.
     */
    public ResolveConflictFrame(PlanTaskFrame originator, Set<DetailedTask> conflictingTasks, PlanTaskHandler planHandler) {
        this.originator = originator;
        initComponents();
        initConflictingTaskTable(conflictingTasks);
        this.planHandler = planHandler;
    }

    /**
     * Fills the available task table with the appropriate data
     */
    private void initConflictingTaskTable(Set<DetailedTask> conflictingTasks) {
        String[] columnNames = {"Id", "Description", "Estimated Duration", "Acceptable Deviation", "Project id", "Project"};
        
        Object[][] data = new Object[conflictingTasks.size()][];

        int i = 0;
        for (DetailedTask task : conflictingTasks) {
            data[i] = new Object[]{
                task.getId(),
                task.getDescription(),
                task.getEstimatedDuration(),
                task.getAcceptableDeviation() + "%",
                task.getProject().getId(),
                task.getProject().getName()
            };
            i++;
        }

        taskModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        conflictingTaskTable.setModel(taskModel);
        conflictingTaskTable.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        conflictingTaskTable = new javax.swing.JTable();
        MoveCurrentTaskButton = new javax.swing.JButton();
        selectTaskButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+15f));
        jLabel2.setText("Conflicting Tasks");

        conflictingTaskTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(conflictingTaskTable);

        MoveCurrentTaskButton.setText("Move Currently Being Planned Task");
        MoveCurrentTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveCurrentTaskButtonActionPerformed(evt);
            }
        });

        selectTaskButton.setText("Select Task To Move");
        selectTaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectTaskButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("OR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jScrollPane3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(282, 282, 282)
                        .addComponent(jLabel2)
                        .addGap(0, 259, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(344, 344, 344))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(selectTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(263, 263, 263))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(MoveCurrentTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(229, 229, 229))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(selectTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(MoveCurrentTaskButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * The select task button is pressed in the available tasks list overview
     *
     * @param evt
     */
    private void MoveCurrentTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoveCurrentTaskButtonActionPerformed
       CardLayout card = (CardLayout) originator.getMainPanel().getLayout();
        card.show(originator.getMainPanel(), "selectTime");
        originator.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_MoveCurrentTaskButtonActionPerformed

    private void selectTaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectTaskButtonActionPerformed
        
        
        int tId = (int) taskModel.getValueAt(conflictingTaskTable.convertRowIndexToModel(conflictingTaskTable.getSelectedRow()), 0);
        int pId = (int) taskModel.getValueAt(conflictingTaskTable.convertRowIndexToModel(conflictingTaskTable.getSelectedRow()), 4);
        PlanTaskFrame planTaskFrame = new PlanTaskFrame(planHandler);
        planTaskFrame.setSelectedProjectId(pId);
        planTaskFrame.setSelectedTaskId(tId);
        planTaskFrame.initTimeList();
         CardLayout card = (CardLayout) planTaskFrame.getMainPanel().getLayout();
        card.show(planTaskFrame.getMainPanel(), "selectTime");
        planTaskFrame.setVisible(true);
    }//GEN-LAST:event_selectTaskButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton MoveCurrentTaskButton;
    private javax.swing.JTable conflictingTaskTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton selectTaskButton;
    // End of variables declaration//GEN-END:variables
}
