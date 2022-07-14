package Capture;

import Util.ConectaBanco;
import Util.ControlPerson;
import Util.ModelPerson;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgproc;
//import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.*;
import org.bytedeco.opencv.global.opencv_core;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imencode;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

/**
 *
 * @author Kenny
 */
public class Capture extends javax.swing.JFrame {

    private Capture.DaemonThread myThread = null;

    //JavaCV
    VideoCapture webSource = null;
    Mat cameraImage = new Mat();
    CascadeClassifier cascade = new CascadeClassifier();
    BytePointer mem = new BytePointer();
    RectVector detectedFaces = new RectVector();

    //Vars
    String root, firstNamePerson, lastNamePerson, officerPerson, dobPerson;
    int numSamples = 25, sample = 1, idPerson;

    //Utils
    ConectaBanco conecta = new ConectaBanco();

    public Capture(int id, String fName, String lName, String office, String dob) {
        initComponents();
        
        idPerson = id;
        firstNamePerson = fName;
        lastNamePerson = lName;
        officerPerson = office;
        dobPerson = dob;
        
        startCamera();
    }

    private Capture() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        label_photo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        counterLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Security System - Capture Photo");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CAPTURE 25 SNAPSHOTS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 360, 60));

        label_photo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(200, 200, 200))); // NOI18N
        jPanel1.add(label_photo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 330, 430));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        counterLabel.setBackground(new java.awt.Color(72, 120, 200));
        counterLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        counterLabel.setForeground(new java.awt.Color(255, 255, 255));
        counterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        counterLabel.setText("00");
        counterLabel.setOpaque(true);
        jPanel2.add(counterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 100, 60));

        saveButton.setBackground(new java.awt.Color(73, 174, 117));
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("Capture");
        saveButton.setContentAreaFilled(false);
        saveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveButton.setOpaque(true);
        jPanel2.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 70, 130, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 330, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 610));

        setSize(new java.awt.Dimension(368, 650));
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
            java.util.logging.Logger.getLogger(Capture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Capture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Capture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Capture.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Capture().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel counterLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel label_photo;
    private javax.swing.JButton saveButton;
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
                            Graphics g = label_photo.getGraphics(); //mostra a imagem no jlabel
                            Mat imageColor = new Mat(); //imagem colorida
                            imageColor = cameraImage;

                            Mat imageGray = new Mat(); //imagem pb
                            cvtColor(imageColor, imageGray, COLOR_BGRA2GRAY);
//                            flip(cameraImage, cameraImage, +1);

                            RectVector detectedFaces = new RectVector(); //face detectada
                            cascade.detectMultiScale(imageColor, detectedFaces, 1.1, 1, 1, new Size(150, 150), new Size(500, 500));

                            for (int i = 0; i < detectedFaces.size(); i++) { //repetição pra encontrar as faces

                                Rect dadosFace = detectedFaces.get(0);
                                rectangle(imageColor, dadosFace, new Scalar(255, 255, 255, 5));
                                Mat face = new Mat(imageGray, dadosFace);
                                opencv_imgproc.resize(face, face, new Size(160, 160));

                                if (saveButton.getModel().isPressed()) { //quando apertar o botão saveButton
                                    if (sample <= numSamples) {
                                        String cropped = "C:\\photos\\person." + "." + sample + ".jpg";
                                        imwrite(cropped, face);

                                        counterLabel.setText(String.valueOf(sample));
                                        sample++;
                                    }

                                    if (sample > 25) {
                                        generate();
                                        insertDatabase();
                                        System.out.println("File Generated");
                                        stopCamera();
                                    }
                                    /*if (txt_first_name.getText().equals("") || txt_first_name.getText().equals(" ")) {
                                        JOptionPane.showMessageDialog(null, "Campo vazio");
                                    } else if (txt_first_name.getText().equals("") || txt_first_name.getText().equals(" ")) {
                                        JOptionPane.showMessageDialog(null, "Campo vazio");
                                    } else if (txt_last_name.getText().equals("") || txt_last_name.getText().equals(" ")) {
                                        JOptionPane.showMessageDialog(null, "Campo vazio");
                                    } else if (txt_office.getText().equals("") || txt_office.getText().equals(" ")) {
                                        JOptionPane.showMessageDialog(null, "Campo vazio");
                                    } else {
                                        if (sample <= numSamples) {
//                                        salva a imagem cortada [160,160]
//                                        nome do arquivo: idpessoa + a contagem de fotos. ex: person.10(id).6(sexta foto).jpg
                                            String cropped = "C:\\photos\\person." + txt_id_label.getText() + "." + sample + ".jpg";
                                            imwrite(cropped, face);

                                            //System.out.println("Foto " + amostra + " capturada\n");
                                            counterLabel.setText(String.valueOf(sample) + "/25");
                                            sample++;
                                        }
                                        if (sample > 25) {
                                            new TrainLBPH().trainPhotos();//se a contagem for maior que 25, termina de tirar a foto, gera o arquivo
                                            insertDatabase(); //insere os dados no banco

                                            System.out.println("File Generated");
                                            stopCamera(); // e fecha a camera
                                        }
                                    }*/
                                }
                            }
                        }

                        imencode(".bmp", cameraImage, mem);
                        Image im = ImageIO.read(new ByteArrayInputStream(mem.getStringBytes()));
                        BufferedImage buff = (BufferedImage) im;

                        if (g.drawImage(buff, 0, 0, getWidth(), getHeight() - 90, 0, 0, buff.getWidth(), buff.getHeight(), null)) {
                            if (runnable == false) {
                                System.out.println("Salve a Foto");
                                this.wait();
                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro ao iniciar camera (IOEx)\n" + ex);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro ao iniciar camera (Interrupted)\n" + ex);
                    }
                }
            }

        }

    }

    public void generate() {
        File directory = new File("C:\\Users\\Kenny\\Pictures\\VRChat");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jng") || name.endsWith(".png");
            }
        };

        File[] files = directory.listFiles(); //only oour filter
        MatVector photos = new MatVector(files.length);
        Mat labels = new Mat(files.length, 1, CV_32SC1);
        IntBuffer labelsBuffer = labels.createBuffer();

        int counter = 0;
        for (File image : files) {
            Mat photo = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int idPerson = Integer.parseInt(image.getName().split("\\.")[1]);
            opencv_imgproc.resize(photo, photo, new Size(160, 160));
        
            photos.put(counter, photo);
            labelsBuffer.put(counter, idPerson);
            counter++;
        }
        
        FaceRecognizer lbph = LBPHFaceRecognizer.create();
        lbph.train(photos, labels);
        lbph.save("C:\\Users\\Kenny\\Pictures\\VRChat\\classifierLBPH.yml");
    }

    public void insertDatabase() {
        ControlPerson cod = new ControlPerson();
        ModelPerson mod = new ModelPerson();

        mod.setFirst_name(firstNamePerson);
        mod.setLast_name(lastNamePerson);
        mod.setDob(dobPerson);
        mod.setOfficer(officerPerson);
        cod.inserir(mod);
    }

    public void stopCamera() {
        myThread.runnable = false;
        webSource.release();
        dispose();
    }

    public void startCamera() {
        webSource = new VideoCapture(0);
        myThread = new Capture.DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        t.start();
    }
}
