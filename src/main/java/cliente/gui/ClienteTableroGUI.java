package cliente.gui;

import Chats.Cliente;
import extraPackage.InAndOut;
import flujodetrabajo.*;
import extraPackage.TextPrompt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;;

public class ClienteTableroGUI extends JDialog {
    private FlujoDeTrabajo flujoDeTrabajo;
    private DefaultListModel[] modeloL = new DefaultListModel[5];
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonAgregarActividad;
    private JButton buttonDelete;

    private JPanel contentPane;
    private JPanel panelPrincipal;
    private JPanel Listss;
    private JPanel panelSecundario;
    private JPanel panelBotones;

    private JList listTest;
    private JList listDone;
    private JList listIdeas;
    private JList listToDo;
    private JList listDoing;

    private JTextField textFieldActividad;
    private JButton ChatButton;
    private JTextField textFieldTarea = new JTextField(15);

    private JComboBox comboBoxActividad = new JComboBox();

    //Banderas
    private int dropFase;

    //Temporales
    private Fase faseTemp;
    private Actividad actTemp;
    private Tarea tareaTemp;


    public ClienteTableroGUI() {

        //flujoDeTrabajo = new FlujoDeTrabajo("Default");
        /**/
        //Inicializacion con lo que ya existe
        flujoDeTrabajo = InAndOut.deserialize();
        if (flujoDeTrabajo==null) {
            String [] str = {"Ideas", "To Do", "Doing", "Test", "Done"};
            flujoDeTrabajo = new FlujoDeTrabajo("Mi flujo de trabajo");
            for(int i=0; i<5; i++)
            {
                Fase fase = new Fase(str[i], flujoDeTrabajo);
                flujoDeTrabajo.getFases().add(fase);
            }
        }
        actualizarTablero();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //placeHolders for TextFields
        TextPrompt placeHAct = new TextPrompt("Actividad...", textFieldActividad);
        placeHAct.changeAlpha(0.75f);
        placeHAct.changeStyle(Font.ITALIC);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        //Botones
        {
            buttonAgregarActividad.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!textFieldActividad.getText().equals("")) {
                        actTemp = new Actividad(textFieldActividad.getText(), flujoDeTrabajo);
                        flujoDeTrabajo.getActividades().add(actTemp);

                        actualizarTablero();
                    } else {
                        JOptionPane.showMessageDialog(null, "Favor de asignar nombre a la actividad", "Actividad sin nombre", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });

            buttonOK.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });

            buttonCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    onCancel();
                }
            });
        }
        //List listeners
        {
            listIdeas.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (showPanelInput() == 0)
                        onAddTarea();

                    actualizarTablero();
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        tareaTemp = (Tarea) listIdeas.getModel().getElementAt(listIdeas.getSelectedIndex());
                    } catch (Exception ex){
                        //Nada
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if(dropFase != -1 && dropFase != 0 && dropFase != -2) {
                        faseTemp = tareaTemp.getFase();

                        faseTemp.getTareas().removeElement(tareaTemp);
                        tareaTemp.setFase(flujoDeTrabajo.getFases().get(dropFase));
                        flujoDeTrabajo.getFases().get(dropFase).getTareas().add(tareaTemp);
                    } else if (dropFase == -2){
                        faseTemp = flujoDeTrabajo.getFases().get(0);
                        onDeleteTarea();
                    }

                    actualizarTablero();
                }

                public void mouseEntered(MouseEvent e) {
                    dropFase = 0;
                }

                public void mouseExited(MouseEvent e) {
                    dropFase = -1;
                }
            });

            listToDo.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (showPanelInput() == 0)
                        onAddTarea();

                    actualizarTablero();
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        tareaTemp = (Tarea) listToDo.getModel().getElementAt(listToDo.getSelectedIndex());
                    } catch (Exception ex){
                        //Nada
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if(dropFase != -1 && dropFase != 1 && dropFase != -2) {
                        faseTemp = tareaTemp.getFase();

                        faseTemp.getTareas().removeElement(tareaTemp);
                        tareaTemp.setFase(flujoDeTrabajo.getFases().get(dropFase));
                        flujoDeTrabajo.getFases().get(dropFase).getTareas().add(tareaTemp);
                    } else if (dropFase == -2){
                        faseTemp = flujoDeTrabajo.getFases().get(1);
                        onDeleteTarea();
                    }
                    actualizarTablero();
                }

                public void mouseEntered(MouseEvent e) {
                    dropFase = 1;
                }

                public void mouseExited(MouseEvent e) {
                    dropFase = -1;
                }
            });

            listDoing.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (showPanelInput() == 0)
                        onAddTarea();

                    actualizarTablero();
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        tareaTemp = (Tarea) listDoing.getModel().getElementAt(listDoing.getSelectedIndex());
                    } catch (Exception ex){
                        //Nada
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if(dropFase != -1 && dropFase != 2 && dropFase != -2) {
                        faseTemp = tareaTemp.getFase();

                        faseTemp.getTareas().removeElement(tareaTemp);
                        tareaTemp.setFase(flujoDeTrabajo.getFases().get(dropFase));
                        flujoDeTrabajo.getFases().get(dropFase).getTareas().add(tareaTemp);
                    } else if (dropFase == -2){
                        faseTemp = flujoDeTrabajo.getFases().get(2);
                        onDeleteTarea();
                    }

                    actualizarTablero();
                }

                public void mouseEntered(MouseEvent e) {
                    dropFase = 2;
                }

                public void mouseExited(MouseEvent e) {
                    dropFase = -1;
                }
            });

            listTest.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (showPanelInput() == 0)
                        onAddTarea();

                    actualizarTablero();
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        tareaTemp = (Tarea) listTest.getModel().getElementAt(listTest.getSelectedIndex());
                    } catch (Exception ex){
                        //Nada
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if(dropFase != -1 && dropFase != 3 && dropFase != -2) {
                        faseTemp = tareaTemp.getFase();

                        faseTemp.getTareas().removeElement(tareaTemp);
                        tareaTemp.setFase(flujoDeTrabajo.getFases().get(dropFase));
                        flujoDeTrabajo.getFases().get(dropFase).getTareas().add(tareaTemp);
                    } else if (dropFase == -2){
                        faseTemp = flujoDeTrabajo.getFases().get(3);
                        onDeleteTarea();
                    }

                    actualizarTablero();
                }

                public void mouseEntered(MouseEvent e) {
                    dropFase = 3;
                }

                public void mouseExited(MouseEvent e) {
                    dropFase = -1;
                }
            });

            listDone.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (showPanelInput() == 0)
                        onAddTarea();

                    actualizarTablero();
                }

                public void mousePressed(MouseEvent e) {
                    try {
                        tareaTemp = (Tarea) listDone.getModel().getElementAt(listDone.getSelectedIndex());
                    } catch (Exception ex){
                        //Nada
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if(dropFase != -1 && dropFase != 4 && dropFase != -2) {
                        faseTemp = tareaTemp.getFase();

                        faseTemp.getTareas().removeElement(tareaTemp);
                        tareaTemp.setFase(flujoDeTrabajo.getFases().get(dropFase));
                        flujoDeTrabajo.getFases().get(dropFase).getTareas().add(tareaTemp);
                    } else if (dropFase == -2){
                        faseTemp = flujoDeTrabajo.getFases().get(4);
                        onDeleteTarea();
                    }

                    actualizarTablero();
                }

                public void mouseEntered(MouseEvent e) {
                    dropFase = 4;
                }

                public void mouseExited(MouseEvent e) {
                    dropFase = -1;
                }
            });
        }

        //Button listeners
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                dropFase = -2;
            }
            public void mouseExited(MouseEvent e) {
                dropFase = -1;
            }
        });

        ChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] args={};
                Cliente obj = new Cliente();
                try {
                    obj.main(args);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    private void onOK() {
        InAndOut.serialize(flujoDeTrabajo);
        dispose();
    }

    private void onCancel() {
        //Namas pa acabar el codigo sin guardar nada asi es
        dispose();
    }

    private void onAddTarea(){
        if (!textFieldTarea.getText().equals("")) {
            faseTemp = flujoDeTrabajo.getFases().get(dropFase);
            actTemp = flujoDeTrabajo.getActividades().get(comboBoxActividad.getSelectedIndex());
            tareaTemp = new Tarea(textFieldTarea.getText(), actTemp, faseTemp, flujoDeTrabajo);

            flujoDeTrabajo.getTareas().add(tareaTemp);
            actTemp.getTareas().add(tareaTemp);
            faseTemp.getTareas().add(tareaTemp);

        } else {
            JOptionPane.showMessageDialog(null, "Favor de asignar nombre a la tarea", "Tarea sin nombre", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDeleteTarea(){
        actTemp = tareaTemp.getActividad();

        flujoDeTrabajo.getTareas().removeElement(tareaTemp);
        faseTemp.getTareas().removeElement(tareaTemp);
        actTemp.getTareas().remove(tareaTemp);
    }

    private int showPanelInput(){
        JPanel p = new JPanel();

        textFieldTarea.setColumns(15);
        TextPrompt placeHTarea = new TextPrompt("Tarea...", textFieldTarea);
        placeHTarea.changeAlpha(0.75f);
        placeHTarea.changeStyle(Font.ITALIC);

        p.setLayout(new GridLayout (4,1));
        p.add(new JLabel("En actividad: "));
        p.add(comboBoxActividad);
        p.add(new JLabel("Añadir tarea: "));
        p.add(textFieldTarea);

        return JOptionPane.showConfirmDialog(null, p, "Añadir tarea ", JOptionPane.OK_CANCEL_OPTION);
    }

    private void actualizarTablero(){
        comboBoxActividad.removeAllItems();
        for (int j = 0; j < flujoDeTrabajo.getActividades().size(); j++) {
            comboBoxActividad.addItem(flujoDeTrabajo.getActividades().get(j).getNombre());
        }

        for(int k = 0; k < 5; k++)
        {
            modeloL[k] = new DefaultListModel();
        }

        listIdeas.setModel(modeloL[0]);
        listToDo.setModel(modeloL[1]);
        listDoing.setModel(modeloL[2]);
        listTest.setModel(modeloL[3]);
        listDone.setModel(modeloL[4]);

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < flujoDeTrabajo.getFases().get(i).getTareas().size(); j++){
                modeloL[i].addElement(flujoDeTrabajo.getFases().get(i).getTareas().get(j));
            }
        }
    }

    public static void main(String[] args) {
        ClienteTableroGUI dialog = new ClienteTableroGUI();
        dialog.pack();
        dialog.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        dialog.setTitle("Tablero Kanban - Cliente");
        dialog.setSize(1280, 720);
        dialog.setResizable(false);

        try{
            dialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        dialog.setVisible(true);
        System.exit(0);

    }
}
