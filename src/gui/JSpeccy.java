/*
 * JSpeccy.java
 *
 * Created on 21 de enero de 2008, 14:27
 */

package gui;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import machine.Spectrum;

/**
 *
 * @author  jsanchez
 */
public class JSpeccy extends javax.swing.JFrame {
    Spectrum spectrum;
    JSpeccyScreen jscr;
    File currentDir;
    JFileChooser jFile;
    /** Creates new form JSpeccy */
    public JSpeccy() {
        initComponents();
        spectrum = new Spectrum();
        jscr = new JSpeccyScreen(spectrum);
        spectrum.setScreen(jscr);
        getContentPane().add(jscr,BorderLayout.CENTER);
        pack();
        addKeyListener(spectrum);
        spectrum.startEmulation();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        fileOpenSnapshot = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSpeccy");
        setResizable(false);

        jMenu1.setText("File");

        fileOpenSnapshot.setText("Open snapshot");
        fileOpenSnapshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileOpenSnapshotActionPerformed(evt);
            }
        });
        jMenu1.add(fileOpenSnapshot);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileOpenSnapshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileOpenSnapshotActionPerformed
        // TODO add your handling code here:
        if( jFile == null ) {
            jFile = new JFileChooser("/home/jsanchez/src/JSpeccy/dist");
            jFile.setFileFilter(new FileFilterSnapshot());
        }
        else
            jFile.setCurrentDirectory(currentDir);

        spectrum.stopEmulation();
        int status = jFile.showOpenDialog(getContentPane());
        if( status == JFileChooser.APPROVE_OPTION ) {
            //spectrum.stopEmulation();
            currentDir = jFile.getCurrentDirectory();
            spectrum.loadSNA(jFile.getSelectedFile());
//            jscr.invalidateScreen();
//            spectrum.startEmulation();
        }
        spectrum.startEmulation();
    }//GEN-LAST:event_fileOpenSnapshotActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JSpeccy().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem fileOpenSnapshot;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration//GEN-END:variables
    
}
