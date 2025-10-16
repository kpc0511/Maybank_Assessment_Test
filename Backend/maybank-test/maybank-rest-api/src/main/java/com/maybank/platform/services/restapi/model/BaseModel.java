package com.maybank.platform.services.restapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1216494278265970514L;

    @Column(name = "version")
    private Integer version;

    @Column(name = "status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "createDate")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updateDate")
    private Date updateDate;

    @Column(name = "createBy")
    private String createBy;

    @Column(name = "updateBy")
    private String updateBy;
}
