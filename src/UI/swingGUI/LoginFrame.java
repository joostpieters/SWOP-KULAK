package UI.swingGUI;

import controller.LoginHandler;
import domain.dto.DetailedBranchOffice;
import domain.user.User;
import exception.NoAccessException;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * This jFrame handles the update task status use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class LoginFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = -7570577954812578587L;

    private final LoginHandler handler;
    private final MainFrame originator;

    /**
     * Creates new form ListProjectsFrame
     *
     * @param handler The use case handler to use to pass the requests to.
     * @param originator The origination mainframe
     */
    public LoginFrame(LoginHandler handler, MainFrame originator) {
        this.handler = handler;
        initComponents();
        initOfficeList();
        this.originator = originator;
    }

    /**
     * Fills the available task table with the appropriate data
     */
    private void initOfficeList() {
        DefaultListModel listModel = new DefaultListModel();
        for (DetailedBranchOffice office : handler.getOffices()) {
            listModel.addElement(office.getLocation());
        }

        officeList.setModel(listModel);
    }

    /**
     * Fills the user list with the given data
     */
    private void initUserList(List<User> users) {
        
        DefaultListModel listModel = new DefaultListModel();
        for (User user : users) {
            listModel.addElement(user.getName());
        }

        userList.setModel(listModel);
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
        selectOfficeButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        officeList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        userList = new javax.swing.JList();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+15f));
        jLabel2.setText("Select Branch Office");

        selectOfficeButton.setText("Select Office");
        selectOfficeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectOfficeButtonActionPerformed(evt);
            }
        });

        officeList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(officeList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(selectOfficeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(selectOfficeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        mainPanel.add(jPanel1, "card3");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()+15f));
        jLabel3.setText("Identify Yourself");

        userList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(userList);

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(175, 175, 175))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(253, 253, 253)
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        mainPanel.add(jPanel2, "updateTask");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private void selectOfficeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectOfficeButtonActionPerformed
        if (officeList.getSelectedValue() != null) {
            
            initUserList(handler.getUsers(officeList.getSelectedIndex()));
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel, "updateTask");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a branch office.", null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_selectOfficeButtonActionPerformed

    /**
     * The login button is pressed
     *
     * @param evt
     */
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if (userList.getSelectedValue() != null) {
            try{
                handler.login(userList.getSelectedValue().toString());
                originator.getLoginButton().setForeground(new java.awt.Color(245, 0, 61));
                originator.getLoginButton().setText("Logout");
                
                // should the lunchbreak be changed?
                if (handler.askLunchbreak()) {
                    
                    while (true) {                        
                        String begintime = JOptionPane.showInputDialog("If you want you can change the beginning of your lunch break, now it's 12 o'clock");
                        if (begintime != null) {
                            try {
                                handler.setLunchbreak(begintime);
                                break;
                            } catch (HeadlessException | IllegalArgumentException exception) {
                                JOptionPane.showMessageDialog(null, exception.getMessage(), null, JOptionPane.WARNING_MESSAGE);
                            }
                        }else{
                            break;
                        }
                    }
                }
                this.dispose();
            }catch(NoAccessException e){
                JOptionPane.showMessageDialog(rootPane, e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
            

            
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select a user.", null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loginButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JList officeList;
    private javax.swing.JButton selectOfficeButton;
    private javax.swing.JList userList;
    // End of variables declaration//GEN-END:variables
}
