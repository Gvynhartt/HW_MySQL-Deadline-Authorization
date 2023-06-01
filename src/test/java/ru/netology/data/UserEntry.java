package ru.netology.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
    public class UserEntry {
        private String defUserLogin;
        private String defUserPassword;
    }
