package com.softserve.itacademy.security;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticatedUserService {

     @Autowired
     private UserService userService;

     @Autowired
     private ToDoService toDoService;

     public boolean hasId(long id){
          String username =  SecurityContextHolder.getContext().getAuthentication().getName();
          User user = userService.findByEmail(username);
          return user.getId()==id;
     }

     public boolean isOwner(long id, ToDo toDo){
          return toDo.getOwner().getId()==id;
     }

}
