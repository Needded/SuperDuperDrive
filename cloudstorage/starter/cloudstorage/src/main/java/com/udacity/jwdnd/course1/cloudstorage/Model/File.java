package com.udacity.jwdnd.course1.cloudstorage.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File {

    private Integer fileId;
    private String fileName;
    private String contentType;
    private long fileSize;
    private Integer userId;
    private byte[] fileData;

}
