package com.yonyou.action;

/**
 * @author jinchenj
 * @description 工具栏按钮
 * @create:2025-06-0614:39:50
 */
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.Nullable;

public class PackageParserActionGroup extends DefaultActionGroup {

    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent e) {
        return new AnAction[]{
                new MethodDesignAction(),
                new UnusedMethodCleanupAction()
        };
    }
}