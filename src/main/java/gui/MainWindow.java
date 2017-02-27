package main.java.gui;

import main.java.data.Program;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static main.java.data.Program.getSelectedProgram;
import static main.java.data.Program.readProgramsFromFiles;

public class MainWindow {
    private JFrame mainFrame;

    private JPanel buttonPanel;
    private JLabel statusLabel;
    private JPanel buttonAndStatusPanel;

    private List<JCheckBox> countryPreferredCheckBoxList;
    private List<JCheckBox> countryExperiencedCheckBoxList;
    private List<JCheckBox> preferenceCheckBoxList;
    private JComboBox<String> genderBox;
    private JComboBox<String> ageBox;
    private JPanel comboPanel;
    private JPanel checkBoxPanel;
    private JPanel countryPreferredCheckBoxPanel;
    private JPanel countryExperiencedCheckBoxPanel;
    private JPanel preferenceCheckBoxPanel;
    private JPanel comboAndCheckboxPanel;

    private String age = null;
    private String gender = null;
    private Set<String> countryPreferredSet = new HashSet<>();
    private Set<String> countryExperiencedSet = new HashSet<>();
    private Set<String> preferenceSet = new HashSet<>();

    public MainWindow() {
        prepareGUI();
    }

    public static void main(String[] args) {
        readProgramsFromFiles();
        MainWindow mainWindow = new MainWindow();
        mainWindow.openMainWindow();
    }

    private void prepareGUI() {
        mainFrame = new JFrame("TripleE Product Filter");
        mainFrame.setSize(800, 800);
        mainFrame.setLayout(new BorderLayout());

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        buttonAndStatusPanel = new JPanel();
        buttonAndStatusPanel.setLayout(new BorderLayout());

        comboAndCheckboxPanel = new JPanel();
        comboAndCheckboxPanel.setLayout(new BorderLayout());

        mainFrame.add(comboAndCheckboxPanel, BorderLayout.NORTH);
        mainFrame.add(buttonAndStatusPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private void openMainWindow() {
        // combo and checkbox first
        JLabel genderLabel = new JLabel("性别");
        JLabel ageLabel = new JLabel("年龄");
        String[] ages = new String[]{"不限", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
        ageBox = new JComboBox<>(ages);
        String[] genders = new String[]{"不限", "男", "女"};
        genderBox = new JComboBox<>(genders);

        comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout());
        comboPanel.add(genderLabel);
        comboPanel.add(ageBox);
        comboPanel.add(ageLabel);
        comboPanel.add(genderBox);

        String[] countries = {"美国", "英国", "加拿大", "澳大利亚", "新西兰", "欧洲", "其他"};

        countryExperiencedCheckBoxPanel = new JPanel();
        countryExperiencedCheckBoxPanel.setLayout(new FlowLayout());
        JLabel countryExperiencedLabel = new JLabel("去过的国家(可多选)");
        countryExperiencedCheckBoxPanel.add(countryExperiencedLabel);

        countryExperiencedCheckBoxList = new ArrayList<>();
        for (String country : countries) {
            JCheckBox countryCheckBox = new JCheckBox(country, false);
            countryCheckBox.addItemListener(new CountryExperiencedCheckBoxItemListener());
            countryExperiencedCheckBoxList.add(countryCheckBox);
            countryExperiencedCheckBoxPanel.add(countryCheckBox);
        }

        countryPreferredCheckBoxPanel = new JPanel();
        countryPreferredCheckBoxPanel.setLayout(new FlowLayout());
        JLabel countryPreferredLabel = new JLabel("偏好的国家(可多选)");
        countryPreferredCheckBoxPanel.add(countryPreferredLabel);

        countryPreferredCheckBoxList = new ArrayList<>();
        for (String country : countries) {
            JCheckBox countryCheckBox = new JCheckBox(country, false);
            countryCheckBox.addItemListener(new CountryPreferredCheckBoxItemListener());
            countryPreferredCheckBoxList.add(countryCheckBox);
            countryPreferredCheckBoxPanel.add(countryCheckBox);
        }

        preferenceCheckBoxPanel = new JPanel();
        preferenceCheckBoxPanel.setLayout(new FlowLayout());
        JLabel preferenceLabel = new JLabel("兴趣(可多选)");
        preferenceCheckBoxPanel.add(preferenceLabel);

        String[] preferences = {"玩", "英语", "数学", "计算机", "音乐", "体育", "文学", "历史", "其他"};
        preferenceCheckBoxList = new ArrayList<>();
        for (String preference : preferences) {
            JCheckBox preferenceCheckBox = new JCheckBox(preference, false);
            preferenceCheckBox.addItemListener(new PreferenceCheckBoxItemListener());
            preferenceCheckBoxList.add(preferenceCheckBox);
            preferenceCheckBoxPanel.add(preferenceCheckBox);
        }

        checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(3, 1));
        checkBoxPanel.add(countryExperiencedCheckBoxPanel);
        checkBoxPanel.add(preferenceCheckBoxPanel);
        checkBoxPanel.add(countryPreferredCheckBoxPanel);

        comboAndCheckboxPanel.add(comboPanel, BorderLayout.NORTH);
        comboAndCheckboxPanel.add(checkBoxPanel, BorderLayout.SOUTH);

        // button and display panel
        JButton submitButton = new JButton("提交");
        JButton resetButton = new JButton("重置");
        submitButton.setActionCommand("Submit");
        resetButton.setActionCommand("Reset");

        submitButton.addActionListener(new ButtonClickListener());
        resetButton.addActionListener(new ButtonClickListener());
        ageBox.addItemListener(new AgeBoxItemListener());
        genderBox.addItemListener(new GenderBoxItemListener());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(resetButton);
        buttonPanel.add(submitButton);

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(600, 200);

        buttonAndStatusPanel.add(buttonPanel, BorderLayout.PAGE_START);
        buttonAndStatusPanel.add(statusLabel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Reset")) {
                statusLabel.setText("");
                genderBox.setSelectedItem("不限");
                ageBox.setSelectedItem("不限");
                countryPreferredCheckBoxList.forEach(checkBox -> checkBox.setSelected(false));
                countryExperiencedCheckBoxList.forEach(checkBox -> checkBox.setSelected(false));
                preferenceCheckBoxList.forEach(checkBox -> checkBox.setSelected(false));

            } else if (command.equals("Submit")) {
                List<Program> seletedProgram =
                        getSelectedProgram(gender, age, countryPreferredSet, countryExperiencedSet, preferenceSet);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<html>");
                seletedProgram.forEach(program -> stringBuilder.append(program + "<br>"));
                statusLabel.setText(stringBuilder.append("</html>").toString());
            }
        }
    }

    private class AgeBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = e.getItem().toString();
                if (item.equals("不限")) {
                    age = null;
                } else {
                    age = item;
                }
            }
        }
    }

    private class GenderBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = e.getItem().toString();
                if (item.equals("不限")) {
                    gender = null;
                } else {
                    gender = item;
                }
            }
        }
    }

    private class CountryPreferredCheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String country = ((JCheckBox) e.getItem()).getText();
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                countryPreferredSet.add(country);
            } else {
                countryPreferredSet.remove(country);
            }
            System.out.println("countryPreferredSet = " + countryPreferredSet);
        }
    }

    private class CountryExperiencedCheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String country = ((JCheckBox) e.getItem()).getText();
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                countryExperiencedSet.add(country);
            } else {
                countryExperiencedSet.remove(country);
            }
            System.out.println("countryExperiencedSet = " + countryExperiencedSet);
        }
    }

    private class PreferenceCheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String preference = ((JCheckBox) e.getItem()).getText();
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                preferenceSet.add(preference);
            } else {
                preferenceSet.remove(preference);
            }
            System.out.println("preferenceSet = " + preferenceSet);
        }
    }

}
