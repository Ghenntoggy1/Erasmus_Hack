package org.example.User.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.User.User;
import java.util.Set;

public enum Role {

    user,
    admin
}

