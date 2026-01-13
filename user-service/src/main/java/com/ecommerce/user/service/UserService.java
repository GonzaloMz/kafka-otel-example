package com.ecommerce.user.service;

import com.ecommerce.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private static final String TOPIC = "user-events";
    
    private final ConcurrentHashMap<String, User> userDatabase = new ConcurrentHashMap<>();
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        userDatabase.put(user.getId(), user);
        
        // Publish event to Kafka
        kafkaTemplate.send(TOPIC, "user-created", user);
        
        return user;
    }

    public User getUser(String id) {
        return userDatabase.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }

    public User updateUser(String id, User user) {
        if (userDatabase.containsKey(id)) {
            user.setId(id);
            userDatabase.put(id, user);
            
            // Publish event to Kafka
            kafkaTemplate.send(TOPIC, "user-updated", user);
            
            return user;
        }
        return null;
    }

    public boolean deleteUser(String id) {
        User user = userDatabase.remove(id);
        if (user != null) {
            // Publish event to Kafka
            kafkaTemplate.send(TOPIC, "user-deleted", user);
            return true;
        }
        return false;
    }
}
