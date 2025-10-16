package com.maybank.platform.services.restapi.model;

import com.maybank.platform.services.util.enums.EFileStatus;
import com.maybank.platform.services.util.enums.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fileInfo")
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo extends BaseModel implements Serializable {
    @Id
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String fileName;

    @Column(length = 10)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private EFileStatus fileStatus;

    @Size(max = 300)
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private FileType fileType;
}
