package com.yonyou.methoddesign;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * 对话面板
 */
public class MethodDesignDialog extends DialogWrapper {

    private JTextArea methodInputArea;
    private final Project project;

    public MethodDesignDialog(Project project) {
        super(project);
        this.project = project;
        setTitle("方法设计助手");
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

        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 方法列表输入
        JLabel methodLabel = new JLabel("未登记方法列表 (classMethodStr):");
        methodInputArea = new JTextArea();
        methodInputArea.setText("ForwardQuery.addTreeNode,\n" +
                "ForwardQuery.selectTeeNodeByFinish,\n" +
                "ForwardQuery.selectTeeNodeByOrder,\n" +
                "ForwardQuery.selectTeeNodeByWarehouse");
        methodInputArea.setRows(5);
        JBScrollPane scrollPane = new JBScrollPane(methodInputArea);
        inputPanel.add(methodLabel);
        inputPanel.add(scrollPane);

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
        JButton parseButton = new JButton("开始解析并导出Excel");
        parseButton.addActionListener(e -> {
            // 禁用按钮，防止重复点击
            parseButton.setEnabled(false);
            parseButton.setText("处理中...");
            
            try {
                ProjectManager.compileAndGetAllClassesAndMethods(project, methodInputArea.getText(), parseButton, "开始解析并导出Excel");
            } catch (Exception ex) {
                Messages.showErrorDialog("导出失败：" + ex.getMessage(), "错误");
                ex.printStackTrace();
                // 恢复按钮状态
                parseButton.setEnabled(true);
                parseButton.setText("开始解析并导出Excel");
            }
        });
        southPanel.add(parseButton);
        return southPanel;
    }
}