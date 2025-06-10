package com.yonyou.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.yonyou.methoddesign.MethodDesignDialog;
import org.jetbrains.annotations.NotNull;

/**
 * @author jinchenj
 * @description 方法设计助手
 * @create:2025-06-0614:41:37
 */
public class MethodDesignAction extends AnAction {

    public MethodDesignAction() {
        super("方法设计助手"); // 设置默认文本
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("无法获取当前项目", "错误");
            return;
        }
        MethodDesignDialog dialog = new MethodDesignDialog(project);
        dialog.pack();
        dialog.show();
    }
}