package com.yonyou.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.yonyou.losemethodparser.UnusedMethodCleanupDesignDialog;
import org.jetbrains.annotations.NotNull;

/**
 * @author jinchenj
 * @description 失效方法治理
 * @create:2025-06-0614:42:44
 */
public class UnusedMethodCleanupAction extends AnAction {

    public UnusedMethodCleanupAction() {
        super("失效方法治理"); // 设置默认文本
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showErrorDialog("无法获取当前项目", "错误");
            return;
        }
        // 这里可以添加清理逻辑
        // Messages.showInfoMessage("开始执行失效方法清理", "提示");
        UnusedMethodCleanupDesignDialog dialog = new UnusedMethodCleanupDesignDialog(project);
        dialog.pack();
        dialog.show();
    }
}