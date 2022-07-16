package Recognizer;

import Capture.Capture;
import Util.ConectaBanco;
import com.mysql.cj.x.protobuf.MysqlxExpr.Array;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imencode;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import org.bytedeco.opencv.opencv_core.Rect;

/**
 *
 * @author Kenny
 */
public class Recognizer extends javax.swing.JFrame {

    private Recognizer.DaemonThread myThread = null;

    //JavaCV
    VideoCapture webSource = null;
    Mat cameraImage = new Mat();
    CascadeClassifier cascade = new CascadeClassifier("C:\\Users\\Kenny\\Pictures\\VRChat\\haarcascade_frontalface_alt.xml");
    FaceRecognizer recognizer = LBPHFaceRecognizer.create();
    //FaceRecognizer recognizer = opencv_face.LBPHFaceRecognizer.create();

    BytePointer mem = new BytePointer();
    RectVector detectedFaces = new RectVector();

    //Vars
    String root, firstNamePerson, lastNamePerson, officerPerson, dobPerson;
    int idPerson;

    //Utils
    ConectaBanco conecta = new ConectaBanco();

    public Recognizer() {
        initComponents();
        
        recognizer.read("C:\\Users\\Kenny\\Pictures\\VRChat\\classifierLBPH.yml");
        recognizer.setThreshold(80);
        
        startCamera();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label_photo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labelOffice = new javax.swing.JLabel();
        label_name = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Security System - Recognizer Person");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_photo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(200, 200, 200))); // NOI18N
        jPanel1.add(label_photo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 440));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelOffice.setBackground(new java.awt.Color(72, 120, 200));
        labelOffice.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelOffice.setForeground(new java.awt.Color(255, 255, 255));
        labelOffice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelOffice.setText("Office");
        labelOffice.setOpaque(true);
        jPanel2.add(labelOffice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 320, 40));

        label_name.setBackground(new java.awt.Color(72, 120, 200));
        label_name.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        label_name.setForeground(new java.awt.Color(255, 255, 255));
        label_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_name.setText("First - Lastname");
        label_name.setOpaque(true);
        jPanel2.add(label_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 320, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 330, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 570));

        setSize(new java.awt.Dimension(374, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Recognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Recognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Recognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Recognizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Recognizer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelOffice;
    private javax.swing.JLabel label_name;
    private javax.swing.JLabel label_photo;
    // End of variables declaration//GEN-END:variables

    class DaemonThread implements Runnable {

        protected volatile boolean runnable = false;

        @Override
        public void run() {
            synchronized (this) {
                while (runnable) {
                    try {
                        if (webSource.grab()) {
                            webSource.retrieve(cameraImage);
                            Graphics g = label_photo.getGraphics();

                            Mat imageGray = new Mat();
                            cvtColor(cameraImage, imageGray, COLOR_BGRA2GRAY);

                            RectVector detectedFace = new RectVector();
                            cascade.detectMultiScale(imageGray, detectedFace, 1.1, 2, 0, new Size(150, 150), new Size(500, 500));

                            for (int i = 0; i < detectedFace.size(); i++) {
                                Rect dadosFace = detectedFace.get(i);
                                rectangle(cameraImage, dadosFace, new Scalar(0, 255, 0, 3), 3, 0, 0);
                                Mat faceCapturada = new Mat(imageGray, dadosFace);
                                opencv_imgproc.resize(faceCapturada, faceCapturada, new Size(160, 160));

                                IntPointer rotulo = new IntPointer(1);
                                DoublePointer confidence = new DoublePointer(1);
                                recognizer.predict(faceCapturada, rotulo, confidence);
                                int prediction = rotulo.get(0);
                                String name = null;
                                //String nome;
                                //nome = firstNamePerson;

                                if (prediction == -1) {
                                    //rectangle(cameraImage, dadosFace, new Scalar(0, 0, 255, 3), 3, 0, 0);
                                    //idPerson = 0;
                                    label_name.setText("Desconhecido");
                                    labelOffice.setText("");
                                    idPerson = 0;
                                    //label_office.setText("");
                                    
                                } else {
                                    //rectangle(cameraImage, dadosFace, new Scalar(0, 255, 0, 3), 3, 0, 0);
                                    System.out.println(confidence.get(0));
                                    idPerson = prediction;
//                                    System.out.println("PESSOA RECONHECIDA COMO: " + idPerson);
                                    rec();
                                }
                            }

                            imencode(".bmp", cameraImage, mem);
                            Image im = ImageIO.read(new ByteArrayInputStream(mem.getStringBytes()));
                            BufferedImage buff = (BufferedImage) im;

                            //try {
                                if (g.drawImage(buff, 0, 0, 360, 390, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                                    if (runnable == false) {
                                        this.wait();
                                    }
                                }
                            //} catch (Exception e) {
                            }
                        //}
                    } //catch (Exception ex) {
                        catch (IOException | InterruptedException ex) {
                    }
                }
            }
        }
    }

    private void rec() {
        //Recognizer face with database
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                conecta.conexao();
                try {
                    String SQL = "SELECT * FROM person WHERE id = " + String.valueOf(idPerson);
                    conecta.executeSQL(SQL);
                    while (conecta.rs.next()){
                        label_name.setText(conecta.rs.getString("first_name") + " " + conecta.rs.getString("last_name"));
                        labelOffice.setText(conecta.rs.getString("office"));
                        
                        System.out.println("Person: " + conecta.rs.getString("id"));
                        
                        java.sql.Array ident = conecta.rs.getArray("first_name");
                        String[] person = (String[]) ident.getArray();
                        
                        for(int i = 0; i < person.length; i++){
                            System.out.println(person[i]);
                        }
                    }
                } catch (Exception e) {
                }
                conecta.desconectar();
                return null;
            }
        };
        worker.execute();
    }
    
    public void stopCamera() {
        myThread.runnable = false;
        webSource.release();
        dispose();
    }

    public void startCamera() {
        webSource = new VideoCapture(0);
        myThread = new Recognizer.DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }
}
