package com.yanda.school.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "chat")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String  botContent;
    Integer  recordId;
    Integer  titleId;
    String  userContent;
    Integer  userId;
}
