package com.maven.spring.mvc.model;

import java.util.ArrayList;
import java.util.List;
 
import org.springframework.stereotype.Service;
//The classes annotated by @Controller or @Service be regarded as the Bean, 
//and Spring will automatically inject dependencies in the Field annotated 
//by @Autowired annotation. -->MainController
@Service
public class DepartmentService {
 
    public List<Department> listDepartment() {
        List<Department> list = new ArrayList<Department>();
 
        list.add(new Department(1, "Operations", "Chicago"));
        list.add(new Department(2, "HR", "Hanoi"));
        return list;
    }
}