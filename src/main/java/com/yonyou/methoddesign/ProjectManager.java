package com.yonyou.methoddesign;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompileStatusNotification;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManager {

    /**
     * 编译项目并获取所有类和方法
     * @param project IDEA项目实例
     * @return 返回类和方法的映射，key为类名，value为该类中的方法列表
     */
    public static void compileAndGetAllClassesAndMethods(Project project, String classMethodStr, JButton parseButton, String action) throws Exception {
        // 创建结果Map
        List<Map<String, String>> result = new ArrayList<>();

        // 使用同步方式处理
        ApplicationManager.getApplication().invokeAndWait(() -> {
            // 获取项目编译管理器
            CompilerManager compilerManager = CompilerManager.getInstance(project);

            // 获取所有模块
            Module[] modules = ModuleManager.getInstance(project).getModules();

            // 使用同步编译，传入模块数组和回调
            compilerManager.make(project, modules, new CompileStatusNotification() {
                @Override
                public void finished(boolean aborted, int errors, int warnings, CompileContext compileContext) {
                    if (!aborted && errors == 0) {
                        // 编译完成后处理PSI
                        ApplicationManager.getApplication().runReadAction(() -> {
                            PsiManager psiManager = PsiManager.getInstance(project);
                            GlobalSearchScope scope = GlobalSearchScope.projectScope(project);

                            JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
                            PsiPackage rootPackage = javaPsiFacade.findPackage("");

                            if (rootPackage != null) {
                                processPackage(rootPackage, scope, result, classMethodStr);
                                if (action.equals("开始解析并导出Excel")) {
                                    outExcel(result);
                                }
                                JOptionPane.showMessageDialog(null, "成功！");
                                // 恢复按钮状态
                                parseButton.setEnabled(true);
                                parseButton.setText(action);
                            }
                        });
                    }
                }
            });
        });
    }

    /**
     * 处理包中的类和方法
     * @param psiPackage 包对象
     * @param scope 搜索范围
     * @param result 结果
     */
    private static void processPackage(PsiPackage psiPackage, GlobalSearchScope scope, List<Map<String, String>> result, String classMethodStr) {
        // 处理当前包中的类
        for (PsiClass psiClass : psiPackage.getClasses(scope)) {

            String qualifiedName = psiClass.getQualifiedName();
            String pathName = /*"src/manufacturing-service/src/main/java/".concat()*/ qualifiedName.replace(".", "/").concat(".java");
            String className = psiClass.getName();

            // 获取所有方法
            List<String> methods = new ArrayList<>();
            for (PsiMethod method : psiClass.getMethods()) {
                methods.add(method.getName());

                // 将类名和方法列表添加到结果Map中
                Map<String, String> object = new HashMap<>();
                object.put("pathName", pathName);
                object.put("className", className);
                object.put("methodName", method.getName());
                String classMethodName = className + "." + method.getName();
                object.put("classMethodName", classMethodName);
                if (StringUtils.isNotEmpty(classMethodStr) && !classMethodStr.contains(classMethodName)) {
                    continue;
                }
                result.add(object);
            }
        }
        
        // 递归处理子包
        for (PsiPackage subPackage : psiPackage.getSubPackages(scope)) {
            processPackage(subPackage, scope, result, classMethodStr);
        }
    }

    /**
     * 输出excel
     */
    private static void outExcel(List<Map<String, String>> result) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("白名单");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("文件路径");
        headerRow.createCell(1).setCellValue("类名");
        headerRow.createCell(2).setCellValue("方法名");
        headerRow.createCell(3).setCellValue("类名.方法名");

        // 先完成所有数据的写入
        for (int i = 0; i < result.size(); i++) {
            Map<String, String> item = result.get(i);
            int rowNum = i + 1;
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(item.get("pathName"));
            row.createCell(1).setCellValue(item.get("className"));
            row.createCell(2).setCellValue(item.get("methodName"));
            row.createCell(3).setCellValue(item.get("classMethodName"));
        }

        // 获取系统下载目录
        String downloadPath = System.getProperty("user.home") + 
            (System.getProperty("os.name").toLowerCase().contains("win") ? 
            "\\Downloads\\" : "/Downloads/");
        String outputPath = downloadPath + "output.xlsx";

        // 最后一次性写入文件
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog("文件写入失败：" + e.getMessage(), "错误");
        }
    }


}