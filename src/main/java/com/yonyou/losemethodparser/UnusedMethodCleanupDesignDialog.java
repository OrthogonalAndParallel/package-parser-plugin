package com.yonyou.losemethodparser;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 对话面板
 */
public class UnusedMethodCleanupDesignDialog extends DialogWrapper {

    private JTextArea methodInputArea;
    private final Project project;
    private JCheckBox controllerCheckBox;
    private JCheckBox dtoCheckBox;
    private JCheckBox enumCheckBox;
    private JCheckBox testCheckBox;
    private JCheckBox deprecatedCheckBox;
    private JCheckBox resourceCheckBox;

    public UnusedMethodCleanupDesignDialog(Project project) {
        super(project);
        this.project = project;
        setTitle("失效方法治理");
        init();
    }

    /**
     * 中间内容面板
     * @return
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setPreferredSize(new Dimension(600, 300)); // 增加整体高度以容纳内容

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // 排除检查的范围
        JLabel excludeScopeLabel = new JLabel("排除检查范围:");
        JPanel excludeScopePanel = new JPanel(new GridLayout(2, 1)); // 垂直排列
        controllerCheckBox = new JCheckBox("Controller");
        dtoCheckBox = new JCheckBox("DTO");
        enumCheckBox = new JCheckBox("Enum");
        testCheckBox = new JCheckBox("test");
        deprecatedCheckBox = new JCheckBox("deprecated");
        resourceCheckBox = new JCheckBox("resource");
        controllerCheckBox.setSelected(true);
        dtoCheckBox.setSelected(true);
        enumCheckBox.setSelected(true);
        testCheckBox.setSelected(true);
        deprecatedCheckBox.setSelected(true);
        resourceCheckBox.setSelected(true);
        excludeScopePanel.add(controllerCheckBox);
        excludeScopePanel.add(dtoCheckBox);
        excludeScopePanel.add(enumCheckBox);
        excludeScopePanel.add(testCheckBox);
        excludeScopePanel.add(deprecatedCheckBox);
        excludeScopePanel.add(resourceCheckBox);


        gbc.gridx = 0;
        gbc.gridy = 0; // 放置在方法列表下方
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 0, 0, 10); // 上边距
        inputPanel.add(excludeScopeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 0, 0, 0); // 上边距
        inputPanel.add(excludeScopePanel, gbc);

        // 方法列表输入
        JLabel methodLabel = new JLabel("失效方法列表:");
        methodInputArea = new JTextArea();
        methodInputArea.setText("");
        methodInputArea.setRows(5);
        methodInputArea.setLineWrap(true); // 自动换行（可选）
        methodInputArea.setWrapStyleWord(true);

        JBScrollPane scrollPane = new JBScrollPane(methodInputArea);

        // 标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 10); // 右边距
        inputPanel.add(methodLabel, gbc);

        // 滚动面板
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        inputPanel.add(scrollPane, gbc);

        dialogPanel.add(inputPanel, BorderLayout.CENTER);
        return dialogPanel;
    }

    /**
     * 底部按钮面板
     * @return
     */
    @Override
    protected JComponent createSouthPanel() {
        JPanel southPanel = new JPanel(new FlowLayout());
        JButton parseButton = new JButton("获取失效方法");
        parseButton.setEnabled(true);
        JButton cancelButton = new JButton("取消");
        cancelButton.setEnabled(false);
        parseButton.addActionListener(e -> {
            // 禁用按钮，防止重复点击
            parseButton.setEnabled(false);
            parseButton.setText("处理中...");
            try {
                java.util.List<String> excludedScopes = getExcludedScopes(controllerCheckBox, dtoCheckBox, enumCheckBox, testCheckBox, deprecatedCheckBox, resourceCheckBox);
                ProjectMethodManager.compileAndGetAllClassesAndMethods(project, parseButton, cancelButton, "获取失效方法", methodInputArea, excludedScopes);
            } catch (Exception ex) {
                Messages.showErrorDialog("获取失效方法失败：" + ex.getMessage(), "错误");
                ex.printStackTrace();
                // 恢复按钮状态
                parseButton.setEnabled(true);
                parseButton.setText("获取失效方法");
            }
        });
        cancelButton.addActionListener(e -> {
            ProjectMethodManager.cancel();
        });
        southPanel.add(parseButton);
        southPanel.add(cancelButton);
        return southPanel;
    }


    private java.util.List<String> getExcludedScopes(JCheckBox controllerCheckBox, JCheckBox dtoCheckBox, JCheckBox enumCheckBox, JCheckBox testCheckBox, JCheckBox deprecatedCheckBox, JCheckBox resourceCheckBox) {
        java.util.List<String> excludedScopes = new ArrayList<>();
        if (controllerCheckBox.isSelected()) {
            excludedScopes.add("Controller");
        }
        if (dtoCheckBox.isSelected()) {
            excludedScopes.add("DTO");
        }
        if (enumCheckBox.isSelected()) {
            excludedScopes.add("Enum");
        }
        if (testCheckBox.isSelected()) {
            excludedScopes.add("test");
        }
        if (deprecatedCheckBox.isSelected()) {
            excludedScopes.add("deprecated");
        }
        if (resourceCheckBox.isSelected()) {
            excludedScopes.add("resource");
        }
        return excludedScopes;
    }
}