package com.salespage.salespageservice.domains.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class Test {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Module{
    Long moduleId;

    String moduleName;

    Long parentModuleId;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ModuleResponse{
    Long moduleId;

    String moduleName;

    Long parentModuleId;

    List<ModuleResponse> responseList = new ArrayList<>();
  }

  public static List<ModuleResponse> convertToModuleResponse(List<Module> modules) {
    List<ModuleResponse> moduleResponses = new ArrayList<>();

    for (Module module : modules) {
      if (module.parentModuleId == null) {
        // Tạo một đối tượng ModuleResponse tương ứng và đệ quy chuyển đổi các mô-đun con
        ModuleResponse moduleResponse = new ModuleResponse();
        moduleResponse.moduleId = module.moduleId;
        moduleResponse.moduleName = module.moduleName;
        moduleResponse.responseList = convertToModuleResponseRecursive(modules, module.moduleId);

        moduleResponses.add(moduleResponse);
      }
    }

    return moduleResponses;
  }
  private static List<ModuleResponse> convertToModuleResponseRecursive(List<Module> modules, Long moduleId) {
    List<ModuleResponse> moduleResponses = new ArrayList<>();

    for (Module module : modules) {
      if (module.parentModuleId != null && module.parentModuleId.equals(moduleId)) {
        // Tìm thấy mô-đun con của mô-đun hiện tại, tìm mô-đun cha và đệ quy chuyển đổi các mô-đun con
        ModuleResponse moduleResponse = new ModuleResponse();
        moduleResponse.moduleId = module.moduleId;
        moduleResponse.moduleName = module.moduleName;
        moduleResponse.parentModuleId = module.parentModuleId;
        moduleResponse.responseList = convertToModuleResponseRecursive(modules, module.moduleId);

        // Tìm mô-đun cha của mô-đun hiện tại
        boolean foundParent = false;
        for (ModuleResponse parentModuleResponse : moduleResponses) {
          if (parentModuleResponse.moduleId.equals(module.parentModuleId)) {
            parentModuleResponse.responseList.add(moduleResponse);
            foundParent = true;
            break;
          }
        }

        // Nếu không tìm thấy mô-đun cha, xem mô-đun hiện tại là một mô-đun cấp độ cao nhất
        if (!foundParent) {
          moduleResponses.add(moduleResponse);
        }
      }
    }

    return moduleResponses;
  }

  public static void main(String[] args) {
    List<Module> modules = new ArrayList<>();
    modules.add(new Module(2L, "Module 1.1", 1L));
    modules.add(new Module(3L, "Module 1.2", 1L));
    modules.add(new Module(8L, "Module 2.2", 5L));
    modules.add(new Module(4L, "Module 1.1.1", 2L));
    modules.add(new Module(1L, "Module 1", null));
    modules.add(new Module(6L, "Module 2.1", 5L));
    modules.add(new Module(7L, "Module 2.1.1", 6L));
    modules.add(new Module(9L, "Module 1.2.1", 3L));
    modules.add(new Module(5L, "Module 2", null));
    modules.add(new Module(10L, "Module 1.1.2", 11L)); // module con có id lớn hơn module cha
    modules.add(new Module(12L, "Module 1.3", 13L)); // module con có parentModuleId không hợp lệ

    List<ModuleResponse> moduleResponses = convertToModuleResponse(modules);

    System.out.println(JsonParser.toJson(moduleResponses));

  }
}
