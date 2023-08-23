package com.softserve.itacademy.security;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

     public boolean isOwner(long todoId){
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          ToDo toDo = toDoService.readById(todoId);
          User user = userService.findByEmail(authentication.getName());
          long userId = user.getId();
          log.info("USER ID " + userId);
          log.info("TODO OWNER " + toDo.getOwner().getId());
          return toDo.getOwner().getId()==userId;
     }

     public List<ToDo> userToDos(long userId){
          User user = userService.readById(userId);
          return user.getMyTodos();
     }

     public boolean isCollaborator(long todoId, long userId){
          ToDo toDo = toDoService.readById(todoId);
          List<User> collabList = toDo.getCollaborators();
          return collabList.stream().anyMatch(c->c.getId()==userId);
     }

}
