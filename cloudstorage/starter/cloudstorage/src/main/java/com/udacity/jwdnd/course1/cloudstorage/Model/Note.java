package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private Integer noteId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;
}
