package com.scm.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Message{
    private String content;
    private MessageType type=MessageType.blue;

    
}

